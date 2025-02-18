package com.tpt.tpt_ecom.service;

import com.tpt.tpt_ecom.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(long id);
    String createCategory(Category category);
    String updateCategory(long id, Category category);
    String deleteCategory(long id);
}
