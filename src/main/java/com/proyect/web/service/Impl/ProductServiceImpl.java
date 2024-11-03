package com.proyect.web.service.Impl;

import com.proyect.web.dtos.product.ProductCreateDTO;
import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductUpdateDTO;
import com.proyect.web.entitys.*;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.repository.ProductCategoryRepository;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
        Product product = new Product();

        ProductCategory category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));

        User user = userRepository.findById(productDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + productDTO.getUserId()));

        product.setProductName(productDTO.getProductName());
        product.setCategory(category);
        product.setProductDescription(productDTO.getProductDescription());
        product.setUser(user);
        product.setOriginalPrice(productDTO.getOriginalPrice());
        product.setSalePrice(productDTO.getSalePrice());
        product.setIsNew(true);
        product.setIsSold(false);

        ProductStock stock = new ProductStock();
        stock.setQuantity(productDTO.getStockQuantity());
        stock.setProduct(product);
        product.setStock(stock);

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Override
    public ProductDTO getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductUpdateDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (productDTO.getCategoryId() != null) {
            ProductCategory category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + productDTO.getCategoryId()));
            product.setCategory(category);
        }

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

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new ResourceNotFoundException("Category not found with id: " + categoryId);
        }

        List<Product> products = productRepository.findByCategoryProductCategoryId(categoryId);

        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> getProductsByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        List<Product> products = productRepository.findByUserId(userId);

        return products.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO markProductAsSold(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if (product.getIsSold()) {
            throw new InvalidOperationException("Product is already marked as sold");
        }

        product.setIsSold(true);

        if (product.getStock() != null) {
            product.getStock().setQuantity(0);
        }

        Product updatedProduct = productRepository.save(product);
        return convertToDTO(updatedProduct);
    }

    private ProductDTO convertToDTO(Product product) {
        if (product == null) {
            return null;
        }

        ProductDTO dto = new ProductDTO();

        dto.setProductId(product.getProductId());
        dto.setProductName(product.getProductName());

        if (product.getCategory() != null) {
            dto.setCategoryId(product.getCategory().getProductCategoryId());
        }

        dto.setProductDescription(product.getProductDescription());
        dto.setIsNew(product.getIsNew());

        if (product.getUser() != null) {
            dto.setUserId(product.getUser().getId());
        }

        dto.setOriginalPrice(product.getOriginalPrice());
        dto.setSalePrice(product.getSalePrice());
        dto.setIsSold(product.getIsSold());

        if (product.getImages() != null) {
            List<String> imageUrls = product.getImages()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(ProductImage::getUrl)
                    .collect(Collectors.toList());
            dto.setImageUrls(imageUrls);
        } else {
            dto.setImageUrls(new ArrayList<>());
        }

        if (product.getStock() != null) {
            dto.setStockQuantity(product.getStock().getQuantity());
        }

        return dto;
    }
}