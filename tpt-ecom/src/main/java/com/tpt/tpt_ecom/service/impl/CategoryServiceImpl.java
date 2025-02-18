package com.tpt.tpt_ecom.service.impl;

import com.tpt.tpt_ecom.model.Category;
import com.tpt.tpt_ecom.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {
    private List<Category> categories;
    @Override
    public List<Category> getAllCategories() {
        return categories;
    }

    @Override
    public Category getCategoryById(long id) {
        Category categoryReturn = null;
        for (Category category : categories) {
            if(category.getCategoryId() == id) {
                categoryReturn = category;
                break;
            }
        }
        return categoryReturn;
    }

    @Override
    public String createCategory(Category category) {
        this.categories.add(category);
        return "Category created successfully";
    }

    @Override
    public String updateCategory(long id, Category categoryUpdate) {
        // handle null with optional
        Category category = (Category) categories.stream()
                .filter(c -> c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "category not found"
                ));
        category.setCategoryName(categoryUpdate.getCategoryName());

//        Optional<Category> categoryOptional = categories.stream()
//                .filter(c -> c.getCategoryId() == id)
//                .findFirst();
//        if(categoryOptional.isPresent()) {
//            categoryOptional.get().setCategoryName(categoryUpdate.getCategoryName());
//        } else {
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND,
//                    "category not found"
//            );
//        }

        return "Category updated successfully";
    }

    @Override
    public String deleteCategory(long id) {
        Category category = (Category) categories.stream()
                .filter(c -> c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "category not found"
                ));
        this.categories.remove(category);
        return "Category deleted successfully";
    }
}
