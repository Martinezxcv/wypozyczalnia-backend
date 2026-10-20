package projekt.wypozyczalnia.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import projekt.wypozyczalnia.requests.AddProductRequest;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.services.ProductService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/{name}")
    public ResponseEntity<ProductDTO> getProductByName(@PathVariable String name) {
        return ResponseEntity.ok(productService.getProductByName(name));
    }

    @GetMapping
    public List<ProductDTO> getProducts() {
        return productService.getAllProducts();
    }

    @PreAuthorize("hasAnyAuthority('Manager','Warehouseman')")
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody AddProductRequest request) {
        productService.addProduct(request.getProductDTO(), request.getCategoryName(), request.getSkillLevelName(), request.getEquipmentTypeName());
        return ResponseEntity.status(HttpStatus.CREATED).body(request.getProductDTO());
    }

    @PreAuthorize("hasAnyAuthority('Manager','Online Sales Representative')")
    @PutMapping
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody AddProductRequest request) {
        productService.updateProduct(request.getProductDTO(), request.getCategoryName(), request.getSkillLevelName(), request.getEquipmentTypeName());
        return ResponseEntity.ok(request.getProductDTO());
    }

    @PreAuthorize("hasAnyAuthority('Manager','Warehouseman')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

}
