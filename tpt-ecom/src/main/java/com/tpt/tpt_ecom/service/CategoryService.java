package com.tpt.tpt_ecom.service;

import com.tpt.tpt_ecom.dto.CategoryDTO;
import com.tpt.tpt_ecom.dto.CategoryResponse;
import com.tpt.tpt_ecom.dto.CategoryUpdateDTO;
import com.tpt.tpt_ecom.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryResponse getAllCategories(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection
    );
    CategoryDTO getCategoryById(long id);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO updateCategory(long id, CategoryUpdateDTO categoryUpdateDTO);
    String deleteCategory(long id);
}
