package com.proyect.web.controllers;

import com.proyect.web.dtos.product.ProductDTO;
import com.proyect.web.dtos.product.ProductPageResponseDTO;
import com.proyect.web.dtos.user.UserDTO;
import com.proyect.web.dtos.user.UserPageResponseDTO;
import com.proyect.web.dtos.user.UserResponseDTO;
import com.proyect.web.repository.UserRepository;
import com.proyect.web.responses.ApiResponse;
import com.proyect.web.service.ProductService;
import com.proyect.web.service.UserService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/dashboard")
@PreAuthorize("hasRole('ADMIN')")
public class AdminViewController {

    @Autowired
    private final UserService userService;
    @Autowired
    private final ProductService productService;

    public AdminViewController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping
    public String showDashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<ApiResponse<UserPageResponseDTO>> searchUsers(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        UserPageResponseDTO pageResponse = userService.searchUsers(query, pageNumber, pageSize, sortBy, sortDir);

        ApiResponse<UserPageResponseDTO> response = new ApiResponse<>(
                "Usuarios recuperados exitosamente",
                pageResponse,
                true
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    @GetMapping("/report")
    public ResponseEntity<byte[]> generateReport() throws JRException, SQLException, IOException {
        // Load the JasperReport template from the classpath
        ClassPathResource reportResource = new ClassPathResource("reporteUsuarios.jrxml");
        InputStream reportStream = reportResource.getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        // Create a datasource connection (adjust according to your database configuration)
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_web", "postgres", "12345");

        // Fill the report with data
        Map<String, Object> parameters = new HashMap<>();
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

        // Export the report to PDF format
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        // Close the database connection and input stream
        connection.close();
        reportStream.close();

        // Set the response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "reporteUsuarios.pdf");

        // Return the PDF as a byte array
        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}
