package com.tpt.tpt_ecom.service.impl;

import com.tpt.tpt_ecom.category.CategoryRepository;
import com.tpt.tpt_ecom.dto.CategoryDTO;
import com.tpt.tpt_ecom.dto.CategoryResponse;
import com.tpt.tpt_ecom.dto.CategoryUpdateDTO;
import com.tpt.tpt_ecom.exceptions.APIException;
import com.tpt.tpt_ecom.model.Category;
import com.tpt.tpt_ecom.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    private List<Category> categories;
    @Override
    public CategoryResponse getAllCategories() {
        List<Category> categories = this.categoryRepository.findAll();
        CategoryResponse categoryResponse = new CategoryResponse();
        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTOS =  categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        categoryResponse.setCategories(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO getCategoryById(long id) {
        Category categoryReturn = null;
        for (Category category : categories) {
            if(category.getCategoryId() == id) {
                categoryReturn = category;
                break;
            }
        }
        return modelMapper.map(categoryReturn, CategoryDTO.class);
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Optional<Category> savedCategory = this.categoryRepository.findByName(categoryDTO.getCategoryName());
        if(savedCategory.isPresent()) {
            throw new APIException("Category already exists");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        this.categoryRepository.save(category);
        return categoryDTO;
    }

    @Override
    public CategoryDTO updateCategory(long id, CategoryUpdateDTO categoryUpdateDTO) {
        // handle null with optional
        Category category = (Category) categories.stream()
                .filter(c -> c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new APIException("Category not found"));

        category.setCategoryName(categoryUpdateDTO.getCategoryName());
        this.categoryRepository.save(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public String deleteCategory(long id) {
        Category category = (Category) categories.stream()
                .filter(c -> c.getCategoryId().equals(id))
                .findFirst()
                .orElseThrow(() -> new APIException("Category not found"));
        this.categories.remove(category);
        return "Category deleted successfully";
    }
}
