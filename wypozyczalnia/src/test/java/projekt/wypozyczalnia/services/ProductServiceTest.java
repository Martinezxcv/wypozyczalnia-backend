package projekt.wypozyczalnia.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import projekt.wypozyczalnia.data.dtos.ProductDTO;
import projekt.wypozyczalnia.data.entities.Category;
import projekt.wypozyczalnia.data.entities.EquipmentType;
import projekt.wypozyczalnia.data.entities.Product;
import projekt.wypozyczalnia.data.entities.SkillLevel;
import projekt.wypozyczalnia.mappers.ProductMapper;
import projekt.wypozyczalnia.repositories.OrderProductRepository;
import projekt.wypozyczalnia.repositories.ProductRepository;
import projekt.wypozyczalnia.servicesImpl.ProductServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductMapper productMapper;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SkillLevelService skillLevelService;
    @Mock
    private CategoryService categoryService;
    @Mock
    private EquipmentTypeService equipmentTypeService;
    @Mock
    private OrderProductRepository orderProductRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllProducts_ShouldReturnAllProducts() {
        // Arrange
        List<Product> products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toDto(any(Product.class))).thenReturn(new ProductDTO());

        // Act
        List<ProductDTO> result = productService.getAllProducts();

        // Assert
        assertEquals(products.size(), result.size());
        verify(productRepository, times(1)).findAll();
        verify(productMapper, times(products.size())).toDto(any(Product.class));
    }

    @Test
    void getProductByName_ShouldReturnProductDTO_WhenProductExists() {
        // Arrange
        String productName = "Test Product";
        Product product = new Product();
        when(productRepository.findByName(productName)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(new ProductDTO());

        // Act
        ProductDTO result = productService.getProductByName(productName);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findByName(productName);
        verify(productMapper, times(1)).toDto(product);
    }

    @Test
    void addProduct_ShouldSaveProduct() {
        // Arrange
        ProductDTO productDTO = new ProductDTO();
        String categoryName = "Category1";
        String skillLevelName = "Skill1";
        String equipmentTypeName = "Type1";

        when(categoryService.getCategory(categoryName)).thenReturn(new Category());
        when(skillLevelService.getSkillLevel(skillLevelName)).thenReturn(new SkillLevel());
        when(equipmentTypeService.getEquipmentType(equipmentTypeName)).thenReturn(new EquipmentType());
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(new Product());

        // Act
        productService.addProduct(productDTO, categoryName, skillLevelName, equipmentTypeName);

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductRentalCounts_ShouldReturnRentalCounts() {
        // Arrange
        LocalDate startOfMonth = LocalDate.of(2023, 1, 1);
        LocalDate endOfMonth = LocalDate.of(2023, 1, 31);
        List<Product> products = List.of(new Product());
        when(productRepository.findAll()).thenReturn(products);
        when(orderProductRepository.countByProductAndOrder_OrderDateBetween(any(Product.class), eq(startOfMonth), eq(endOfMonth))).thenReturn(5L);

        // Act
        Map<String, Long> result = productService.getProductRentalCounts(startOfMonth, endOfMonth);

        // Assert
        assertFalse(result.isEmpty());
        verify(productRepository, times(1)).findAll();
        verify(orderProductRepository, times(products.size())).countByProductAndOrder_OrderDateBetween(any(Product.class), eq(startOfMonth), eq(endOfMonth));
    }

    @Test
    void updateProduct_ShouldUpdateExistingProduct() {
        // Arrange
        int productId = 1;
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productId);
        Product existingProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productMapper.toDto(existingProduct)).thenReturn(productDTO);
        when(categoryService.getCategory(anyString())).thenReturn(new Category());
        when(skillLevelService.getSkillLevel(anyString())).thenReturn(new SkillLevel());
        when(equipmentTypeService.getEquipmentType(anyString())).thenReturn(new EquipmentType());
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(new Product());

        // Act
        productService.updateProduct(productDTO, "Category1", "Skill1", "Type1");

        // Assert
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void deleteProduct_ShouldMarkProductAsUnavailable() {
        // Arrange
        int productId = 1;
        Product product = new Product();
        ProductDTO productDTO = new ProductDTO();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDTO);
        when(productMapper.toEntity(any(ProductDTO.class))).thenReturn(new Product());

        // Act
        productService.deleteProduct(productId);

        // Assert
        assertFalse(productDTO.isAvailable());
        verify(productRepository, times(1)).save(any(Product.class));
    }
}
