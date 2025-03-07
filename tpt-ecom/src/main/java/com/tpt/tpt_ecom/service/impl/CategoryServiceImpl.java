package com.tpt.tpt_ecom.service.impl;

import com.tpt.tpt_ecom.repository.CategoryRepository;
import com.tpt.tpt_ecom.dto.CategoryDTO;
import com.tpt.tpt_ecom.dto.CategoryResponse;
import com.tpt.tpt_ecom.dto.CategoryUpdateDTO;
import com.tpt.tpt_ecom.dto.PaginationMetadata;
import com.tpt.tpt_ecom.exceptions.APIException;
import com.tpt.tpt_ecom.model.Category;
import com.tpt.tpt_ecom.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {
        Sort sortByAndDirection = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable page = PageRequest.of(pageNumber, pageSize, sortByAndDirection);
        Page<Category> categoriesPage = this.categoryRepository.findAll(page);
        List<Category> categories = categoriesPage.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No categories found");
        }
        List<CategoryDTO> categoryDTOS =  categories.stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .collect(Collectors.toList());

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOS);
        PaginationMetadata paginationMetadata = PaginationMetadata
                .builder().pageNumber(categoriesPage.getNumber())
                .pageSize(categoriesPage.getSize())
                .totalPages(categoriesPage.getTotalPages())
                .lastPage(categoriesPage.isLast())
                .build();
        categoryResponse.setPaginationMetadata(paginationMetadata);
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
