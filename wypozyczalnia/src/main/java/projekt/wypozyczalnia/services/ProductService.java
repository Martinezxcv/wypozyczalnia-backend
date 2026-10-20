package projekt.wypozyczalnia.services;

import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.entities.Category;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ProductService {

    List<ProductDTO> getAllProducts();
    ProductDTO getProductByName(String name);
    void addProduct(ProductDTO productDTO, String categoryName, String skillLevelName, String equipmentTypeName);
    Map<String, Long> getProductRentalCounts(LocalDate startOfMonth, LocalDate endOfMonth);
    void updateProduct(ProductDTO productDTO, String categoryName, String skillLevelName, String equipmentTypeName);
    void deleteProduct(int id);
}
