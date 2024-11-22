/* package com.proyect.web.controllers;

import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.service.ProductService;
import com.proyect.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminDashboardController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/users/search")
    @ResponseBody
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
            @RequestParam(required = false) String query) {
        List<UserResponseDTO> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/products/search")
    @ResponseBody
    public ResponseEntity<ProductPageResponseDTO> searchProducts(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "productId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {
        ProductPageResponseDTO products = productService.searchProducts(
                query, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(products);
    }
}
 */