package com.proyect.web.service.Impl;

import com.proyect.web.dtos.productImage.ProductImageDTO;
import com.proyect.web.dtos.productImage.ProductImageCreateDTO;
import com.proyect.web.dtos.productImage.ProductImageUpdateDTO;
import com.proyect.web.entitys.Product;
import com.proyect.web.entitys.ProductImage;
import com.proyect.web.exceptions.ResourceNotFoundException;
import com.proyect.web.exceptions.InvalidOperationException;
import com.proyect.web.repository.ProductImageRepository;
import com.proyect.web.repository.ProductRepository;
import com.proyect.web.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductImageRepository productImageRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ProductImageServiceImpl(ProductImageRepository productImageRepository,
                                   ProductRepository productRepository) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductImageDTO createProductImage(ProductImageCreateDTO productImageDTO) {
        Product product = productRepository.findById(productImageDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", productImageDTO.getProductId()));

        // Verificar si la URL ya existe
        if (productImageRepository.existsByUrl(productImageDTO.getUrl())) {
            throw new InvalidOperationException("Ya existe una imagen con esta URL");
        }

        ProductImage productImage = new ProductImage();
        productImage.setProduct(product);
        productImage.setUrl(productImageDTO.getUrl());

        ProductImage savedImage = productImageRepository.save(productImage);
        return convertToDTO(savedImage);
    }

    @Override
    public ProductImageDTO getProductImage(Long id) {
        ProductImage productImage = productImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Imagen", "ID", id));
        return convertToDTO(productImage);
    }

    @Override
    public List<ProductImageDTO> getAllProductImages() {
        return productImageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductImageDTO> getImagesByProduct(Long productId) {
        return productImageRepository.findByProductProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductImageDTO> updateProductImages(Long productId, List<String> newUrls) {
        List<ProductImage> existingImages = productImageRepository.findByProductProductId(productId);
        List<String> existingUrls = existingImages.stream()
                .map(ProductImage::getUrl)
                .collect(Collectors.toList());

        List<String> urlsToAdd = newUrls.stream()
                .filter(url -> !existingUrls.contains(url))
                .collect(Collectors.toList());

        List<String> urlsToRemove = existingUrls.stream()
                .filter(url -> !newUrls.contains(url))
                .collect(Collectors.toList());

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "ID", productId));

        existingImages.stream()
                .filter(img -> urlsToRemove.contains(img.getUrl()))
                .forEach(productImageRepository::delete);

        List<ProductImage> newImages = urlsToAdd.stream()
                .map(url -> {
                    ProductImage image = new ProductImage();
                    image.setProduct(product);
                    image.setUrl(url);
                    return productImageRepository.save(image);
                })
                .collect(Collectors.toList());

        List<ProductImage> updatedImages = productImageRepository.findByProductProductId(productId);
        return updatedImages.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProductImage(Long id) {
        if (!productImageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Imagen", "ID", id);
        }
        productImageRepository.deleteById(id);
    }

    @Override
    public void deleteAllProductImages(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Producto", "ID", productId);
        }
        productImageRepository.deleteByProductProductId(productId);
    }

    private ProductImageDTO convertToDTO(ProductImage productImage) {
        return new ProductImageDTO(
                productImage.getImageId(),
                productImage.getProduct().getProductId(),
                productImage.getUrl()
        );
    }
}
