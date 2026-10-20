package projekt.wypozyczalnia.servicesImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projekt.wypozyczalnia.data.entities.Category;
import projekt.wypozyczalnia.repositories.CategoryRepository;
import projekt.wypozyczalnia.services.CategoryService;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategory(String name) {
        return categoryRepository.findByName(name).orElse(null);
    }
}
