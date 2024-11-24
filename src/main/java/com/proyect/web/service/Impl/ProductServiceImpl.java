package com.proyect.web.service.Impl;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.entitys.*;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.exceptions.WebException;
import com.proyect.web.repository.ProductCategoryRepository;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductCategoryRepository categoryRepository,
                              UserRepository userRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ProductDTO createProduct(ProductCreateDTO productDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidOperationException("Usuario no autenticado");
        }

        String currentUserEmail = authentication.getName();

        User currentUser = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", 0));

        if (!currentUser.isSeller()) {
            throw new InvalidOperationException("Solo los vendedores pueden crear productos");
        }

        if (productDTO.getSalePrice() != null &&
                productDTO.getSalePrice().compareTo(productDTO.getOriginalPrice()) > 0) {
            throw new InvalidOperationException("El precio de venta no puede ser mayor al precio original");
        }

        ProductCategory category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", productDTO.getCategoryId()));

        Product product = new Product();
        product.setProductName(productDTO.getProductName());
        product.setCategory(category);
        product.setProductDescription(productDTO.getProductDescription());
        product.setUser(currentUser);
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setSalePrice(productDTO.getSalePrice());
        product.setIsNew(productDTO.getIsNew() != null ? productDTO.getIsNew() : true);
        product.setIsSold(false);

        Product savedProduct = productRepository.save(product);

        if (productDTO.getImageUrls() != null && !productDTO.getImageUrls().isEmpty()) {
            List<ProductImage> images = new ArrayList<>();
            for (String url : productDTO.getImageUrls()) {
                if (url == null || url.trim().isEmpty()) {
                    throw new InvalidOperationException("Las URLs de las imágenes no pueden estar vacías");
                }
                ProductImage image = new ProductImage();
                image.setProduct(savedProduct);
                image.setUrl(url.trim());
                images.add(image);
            }
            savedProduct.setImages(images);
            savedProduct = productRepository.save(savedProduct);
        }

        if (productDTO.getStockQuantity() == null || productDTO.getStockQuantity() < 1) {
            throw new InvalidOperationException("La cantidad en stock debe ser al menos 1");
        }

        ProductStock stock = new ProductStock();
        stock.setQuantity(productDTO.getStockQuantity());
        stock.setProduct(savedProduct);
        savedProduct.setStock(stock);

        savedProduct = productRepository.save(savedProduct);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", id));
        return convertToDTO(product);
    }

    @Override
    public ProductPageResponseDTO getAllProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> pageProducts = productRepository.findAll(pageable);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(pageProducts.getNumber());
        productPageResponseDTO.setPageSize(pageProducts.getSize());
        productPageResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productPageResponseDTO.setLast(pageProducts.isLast());

        return productPageResponseDTO;
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new InvalidOperationException("Usuario no autenticado");
        }

        String currentUserEmail = authentication.getName();

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", id));

        User currentUser = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", 0));

        if (!product.getUser().getId().equals(currentUser.getId())) {
            throw new InvalidOperationException("No está autorizado para actualizar este producto");
        }

        if (productDTO.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category", "ID", productDTO.getCategoryId()));
            product.setCategory(category);
        }

        if (productDTO.getProductName() != null) {
            product.setProductName(productDTO.getProductName());
        }

        if (productDTO.getProductDescription() != null) {
            product.setProductDescription(productDTO.getProductDescription());
        }

        if (productDTO.getIsNew() != null) {
            product.setIsNew(productDTO.getIsNew());
        }

        if (productDTO.getOriginalPrice() != null) {
            product.setOriginalPrice(productDTO.getOriginalPrice());
        }

        if (productDTO.getSalePrice() != null) {
            if (productDTO.getSalePrice().compareTo(product.getOriginalPrice()) > 0) {
                throw new InvalidOperationException("El precio de venta no puede ser mayor al precio original");
            }
            product.setSalePrice(productDTO.getSalePrice());
        }

        if (productDTO.getImageUrls() != null) {
            List<ProductImage> newImages = new ArrayList<>();
            List<ProductImage> existingImages = product.getImages();

            for (String url : productDTO.getImageUrls()) {
                if (url == null || url.trim().isEmpty()) {
                    throw new InvalidOperationException("Las URLs de las imágenes no pueden estar vacías");
                }

                boolean imageExists = false;
                for (ProductImage image : existingImages) {
                    if (image.getUrl().equals(url.trim())) {
                        imageExists = true;
                        break;
                    }
                }

                if (!imageExists) {
                    ProductImage image = new ProductImage();
                    image.setProduct(product);
                    image.setUrl(url.trim());
                    newImages.add(image);
                }
            }

            product.getImages().addAll(newImages);
        }

        if (productDTO.getStockQuantity() != null) {
            if (productDTO.getStockQuantity() < 0) {
                throw new InvalidOperationException("La cantidad en stock no puede ser negativa");
            }

            if (product.getStock() == null) {
                ProductStock stock = new ProductStock();
                stock.setProduct(product);
                stock.setQuantity(productDTO.getStockQuantity());
                product.setStock(stock);
            } else {
                product.getStock().setQuantity(productDTO.getStockQuantity());
            }
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id, String currentUserEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", id));

        User currentUser = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email", 0));

        boolean isAdmin = currentUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
        boolean isOwner = product.getUser().getId().equals(currentUser.getId());

        // Lanza excepción solo si NO es admin Y NO es el dueño
        if (!isAdmin && !isOwner) {
            throw new InvalidOperationException("No está autorizado para eliminar este producto");
        }


        try {
            if (product.getTransactions() != null) {
                product.getTransactions().clear();
            }
            if (product.getImages() != null) {
                product.getImages().clear();
            }
            if (product.getStock() != null) {
                product.setStock(null);
            }

            productRepository.delete(product);
        } catch (InvalidOperationException e) {
            throw e;
        } catch (Exception e) {
            throw new InvalidOperationException("Error al eliminar el producto: " + e.getMessage());
        }
    }

    @Override
    public ProductPageResponseDTO getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Verificar si existe la categoría
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Categoría", "ID", categoryId);
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> pageProducts = productRepository.findByCategoryProductCategoryId(categoryId, pageable);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(pageProducts.getNumber());
        productPageResponseDTO.setPageSize(pageProducts.getSize());
        productPageResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productPageResponseDTO.setLast(pageProducts.isLast());

        return productPageResponseDTO;
    }

    @Override
    public ProductPageResponseDTO getProductsByUser(Long userId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "ID", userId));

        if (!user.isSeller()) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El usuario no es un vendedor");
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> pageProducts = productRepository.findByUserId(userId, pageable);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(pageProducts.getNumber());
        productPageResponseDTO.setPageSize(pageProducts.getSize());
        productPageResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productPageResponseDTO.setLast(pageProducts.isLast());

        return productPageResponseDTO;
    }

    @Override
    public ProductPageResponseDTO getProductsByUserEmail(String email, int pageNumber, int pageSize, String sortBy, String sortDir) {
        User user = userRepository.findByUserNameOrEmail(email, email)  // Usamos el mismo valor para ambos parámetros
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email o username", 0));

        if (!user.isSeller()) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El usuario no es un vendedor");
        }

        return getProductsByUser(user.getId(), pageNumber, pageSize, sortBy, sortDir);
    }

    @Override
    public ProductDTO markProductAsSold(Long id, String currentUserEmail) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", id));

        if (product.getIsSold()) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El producto ya está marcado como vendido");
        }

        try {
            product.setIsSold(true);
            if (product.getStock() != null) {
                product.getStock().setQuantity(0);
            }
            Product updatedProduct = productRepository.save(product);
            return convertToDTO(updatedProduct);
        } catch (Exception e) {
            throw new WebException(HttpStatus.BAD_REQUEST, "Error al marcar el producto como vendido");
        }
    }

    private void updateProductFields(Product product, ProductUpdateDTO productDTO) {
        if (productDTO.getProductName() != null) {
            product.setProductName(productDTO.getProductName());
        }
        if (productDTO.getProductDescription() != null) {
            product.setProductDescription(productDTO.getProductDescription());
        }
        if (productDTO.getOriginalPrice() != null) {
            product.setOriginalPrice(productDTO.getOriginalPrice());
        }
        if (productDTO.getSalePrice() != null) {
            product.setSalePrice(productDTO.getSalePrice());
        }
        if (productDTO.getStockQuantity() != null && product.getStock() != null) {
            product.getStock().setQuantity(productDTO.getStockQuantity());
        }
    }

    @Override
    public ProductPageResponseDTO searchProducts(String query, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> pageProducts = productRepository.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(
                query, query, pageable);

        List<Product> products = pageProducts.getContent();

        if(products.isEmpty()) {
            throw new ResourceNotFoundException("Productos", "búsqueda", 0);
        }

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(pageProducts.getNumber());
        productPageResponseDTO.setPageSize(pageProducts.getSize());
        productPageResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productPageResponseDTO.setLast(pageProducts.isLast());

        return productPageResponseDTO;
    }

    @Override
    public ProductPageResponseDTO getRelatedProducts(Long productId, int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Obtener el producto original
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", productId));

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        // Obtener productos relacionados
        Page<Product> relatedProducts = productRepository.findRelatedProducts(
                productId,
                product.getCategory().getProductCategoryId(),
                product.getProductName(),
                product.getProductDescription(),
                pageable
        );

        List<Product> products = relatedProducts.getContent();

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(relatedProducts.getNumber());
        productPageResponseDTO.setPageSize(relatedProducts.getSize());
        productPageResponseDTO.setTotalElements(relatedProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(relatedProducts.getTotalPages());
        productPageResponseDTO.setLast(relatedProducts.isLast());

        return productPageResponseDTO;
    }

    @Override
    public ProductPageResponseDTO getMyProducts(int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Obtener usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();

        User user = userRepository.findByUserNameOrEmail(currentUserEmail, currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "email o username", 0));

        if (!user.isSeller()) {
            throw new WebException(HttpStatus.BAD_REQUEST, "El usuario no es un vendedor");
        }

        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<Product> pageProducts = productRepository.findByUserId(user.getId(), pageable);
        List<Product> products = pageProducts.getContent();

        List<ProductDTO> content = products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        ProductPageResponseDTO productPageResponseDTO = new ProductPageResponseDTO();
        productPageResponseDTO.setContent(content);
        productPageResponseDTO.setPageNo(pageProducts.getNumber());
        productPageResponseDTO.setPageSize(pageProducts.getSize());
        productPageResponseDTO.setTotalElements(pageProducts.getTotalElements());
        productPageResponseDTO.setTotalPages(pageProducts.getTotalPages());
        productPageResponseDTO.setLast(pageProducts.isLast());

        return productPageResponseDTO;
    }

    private ProductDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();
        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        // Obtener categoryId y categoryName
        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getProductCategoryId());
            dto.setCategoryName(product.getCategory().getCategoryName()); // Asumiendo que el método getName() existe en la clase Category
        }

        dto.setProductDescription(product.getProductDescription());
        dto.setIsNew(product.getIsNew());

        // Obtener userId y userName
        if (product.getUser () != null) {
            dto.setUserId(product.getUser ().getId());
            dto.setUserName(product.getUser ().getUserName()); // Asumiendo que el método getUser Name() existe en la clase User
        }

        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setIsSold(product.getIsSold());

        // Obtener imágenes
        if (product.getImages() != null) {
            List<String> imageUrls = product.getImages().stream()
                    .filter(Objects::nonNull)
                    .map(ProductImage::getUrl)
                    .collect(Collectors.toList());
            dto.setImageUrls(imageUrls);
        } else {
            dto.setImageUrls(new ArrayList<>());
        }

        // Obtener stockQuantity
        if (product.getStock() != null) {
            dto.setStockQuantity(product.getStock().getQuantity());
        } else {
            dto.setStockQuantity(1);
        }

        return dto;
    }

    private UserResponseDTO convertToResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setUserName(user.getUserName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserImage(user.getUserImage());
        dto.setSeller(user.isSeller());
        return dto;
    }
}