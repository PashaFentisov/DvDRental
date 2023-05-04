package service;

import entity.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.CategotyRepository;

@Service
public class CategoryService {
    private final CategotyRepository categotyRepository;

    public CategoryService(CategotyRepository categotyRepository) {
        this.categotyRepository = categotyRepository;
    }

    @Transactional(readOnly = true)
    public Category getCategory(Long id) {
        return categotyRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public Iterable<Category> getAllCategories() {
        return categotyRepository.findAll();
    }

    @Transactional
    public void deleteCategory(Long id) {
        categotyRepository.deleteById(id);
    }

    @Transactional
    public void addCategory(Category category) {
        categotyRepository.save(category);
    }

}
