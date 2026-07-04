package Sathish292004.service;

import Sathish292004.exception.CategoryNotFoundException;
import Sathish292004.model.Category;
import Sathish292004.repository.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategoryById(int id) {
        return categoryRepo.findById(id).orElse(new Category());
    }

    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category updateCategory(int id, Category category) {

        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found"));

        existing.setName(category.getName());
        existing.setSlug(category.getSlug());
        existing.setIcon(category.getIcon());

        return categoryRepo.save(existing);
    }

    public void deleteCategory(int id) {
        categoryRepo.deleteById(id);
    }

    public List<Category> searchCategory(String keyword) {
        return categoryRepo.findByNameContainingIgnoreCase(keyword);
    }
}