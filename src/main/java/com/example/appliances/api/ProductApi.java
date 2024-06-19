package com.example.appliances.api;

import com.example.appliances.model.request.ImageRequest;
import com.example.appliances.model.request.ProductRequest;
import com.example.appliances.model.response.ProductResponse;
import com.example.appliances.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductApi {

    private final ProductService productService;

    @Autowired
    public ProductApi( ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/api/statistics/products/count")
    public Long countAllProducts() {
        return productService.countAllProducts();
    }

    //find all by page
    @GetMapping("/page")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN')")
    public Page<ProductResponse> findAllBySpecification(@RequestParam(required = false, defaultValue = "0") int page,
                                                                @RequestParam(required = false, defaultValue = "25") int size,
                                                                @RequestParam(required = false) Optional<Boolean> sortOrder,
                                                                @RequestParam(required = false) String sortBy) {
        return productService.getAllProduct(page, size, sortOrder, sortBy);
    }
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
//    @ApiOperation(value = "Создание", notes = "Создание нового продукта")
    @PostMapping(value = "/create", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> save(@RequestPart(required = false) List<MultipartFile> files, @Valid ProductRequest model) {
        try {
            productService.create(model, files);
            return ResponseEntity.ok("Запись успешно сохранена!");
        } catch (Exception e) {
            return new ResponseEntity<>("Произошла ошибка при сохранении", HttpStatus.BAD_REQUEST);
        }
    }




    @GetMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<ProductResponse> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable UUID id) {
        ProductResponse updatedProduct = productService.update(productRequest, id);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

        //filters
    @GetMapping("/byId/category")
    public ResponseEntity<Page<ProductResponse>> getProductsByCategoryId(
            @RequestParam(required = false)Long categoryId,
            @RequestParam(required = false) List<Long> brandId,
            @RequestParam(required = false) List<Long> producingCountryId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ProductResponse> products = productService.getProductsByCategoryIdAndFilters(categoryId, brandId, producingCountryId, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(products);
    }

//    @GetMapping("/findAl")
//    public List<ProductResponse> findAll(){
//        return productService.findAllProduct();
//    }
    @GetMapping("findAll")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ROLE_SALEMAN', 'ROLE_ADMIN') ")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/image/{photoName}")
    public ResponseEntity<Resource> getImageByName(@PathVariable String photoName) throws IOException {
        Resource imageResource = productService.getImageByName(photoName);

        // Set Content-Disposition header to inline to display the image in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResource);
    }
    @GetMapping("/products/{productId}/image")
    public ResponseEntity<Resource> getProductImage(@PathVariable UUID productId) throws IOException {
        Resource imageResource = productService.getProductImage(productId);

        // Set Content-Disposition header to inline to display the image in the browser
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.IMAGE_JPEG)
                .body(imageResource);
    }
}