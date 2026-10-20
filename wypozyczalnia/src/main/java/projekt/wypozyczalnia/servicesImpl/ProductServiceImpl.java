package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.entities.Category;
import projekt.wypozyczalnia.data.entities.EquipmentType;
import projekt.wypozyczalnia.data.entities.Product;
import projekt.wypozyczalnia.data.entities.SkillLevel;
import projekt.wypozyczalnia.mappers.*;
import projekt.wypozyczalnia.repositories.OrderProductRepository;
import projekt.wypozyczalnia.repositories.ProductRepository;
import projekt.wypozyczalnia.services.CategoryService;
import projekt.wypozyczalnia.services.EquipmentTypeService;
import projekt.wypozyczalnia.services.ProductService;
import projekt.wypozyczalnia.services.SkillLevelService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final SkillLevelMapper skillLevelMapper;
    private final EquipmentTypeMapper equipmentTypeMapper;
    private final ProductRepository productRepository;
    private final SkillLevelService skillLevelService;
    private final CategoryService categoryService;
    private final EquipmentTypeService equipmentTypeService;
    private final OrderProductRepository orderProductRepository;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductByName(String name) {
        return productMapper.toDto(productRepository.findByName(name).orElse(null));
    }

    @Override
    public void addProduct(ProductDTO productDTO, String categoryName, String skillLevelName, String equipmentTypeName) {
        manageProduct(productDTO, categoryName, skillLevelName, equipmentTypeName);
    }

    @Override
    public Map<String, Long> getProductRentalCounts(LocalDate startOfMonth, LocalDate endOfMonth) {
        List<Product> products = productRepository.findAll();
        Map<String, Long> rentalCounts = new HashMap<>();

        for (Product product : products) {
            long count = orderProductRepository.countByProductAndOrder_OrderDateBetween(product, startOfMonth, endOfMonth);
            if (count > 0) {
                rentalCounts.put(product.getName(), count);
            }
        }

        return rentalCounts;
    }

    @Override
    public void updateProduct(ProductDTO productDTO, String categoryName, String skillLevelName, String equipmentTypeName) {
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found. Cannot update."));

        ProductDTO existingProductDTO = productMapper.toDto(existingProduct);

        existingProductDTO.setModel(productDTO.getModel());
        existingProductDTO.setBrand(productDTO.getBrand());
        existingProductDTO.setName(productDTO.getName());
        existingProductDTO.setWeight(productDTO.getWeight());
        existingProductDTO.setPrice(productDTO.getPrice());
        existingProductDTO.setSize(productDTO.getSize());
        existingProductDTO.setAvailable(productDTO.isAvailable());
        existingProductDTO.setDescription(productDTO.getDescription());

        manageProduct(existingProductDTO, categoryName, skillLevelName, equipmentTypeName);
    }

    @Override
    public void deleteProduct(int id) {
        ProductDTO unneededProduct = productMapper.toDto(productRepository.findById(id).orElse(null));
        unneededProduct.setAvailable(false);
        productRepository.save(productMapper.toEntity(unneededProduct));
    }

    private void manageProduct(ProductDTO productDTO, String categoryName, String skillLevelName, String equipmentTypeName) {
        Category category = categoryService.getCategory(categoryName);
        SkillLevel skillLevel = skillLevelService.getSkillLevel(skillLevelName);
        EquipmentType equipmentType = equipmentTypeService.getEquipmentType(equipmentTypeName);

        productDTO.setCategory(categoryMapper.toDto(category));
        productDTO.setSkillLevel(skillLevelMapper.toDto(skillLevel));
        productDTO.setEquipmentType(equipmentTypeMapper.toDto(equipmentType));
        productDTO.setAvailable(productDTO.isAvailable());

        Product product = productMapper.toEntity(productDTO);
        productRepository.save(product);
    }
}
