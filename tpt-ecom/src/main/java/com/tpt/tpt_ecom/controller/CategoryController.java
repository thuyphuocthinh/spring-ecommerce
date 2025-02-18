package com.tpt.tpt_ecom.controller;

import com.tpt.tpt_ecom.dto.CategoryDTO;
import com.tpt.tpt_ecom.dto.CategoryResponse;
import com.tpt.tpt_ecom.dto.CategoryUpdateDTO;
import com.tpt.tpt_ecom.model.Category;
import com.tpt.tpt_ecom.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/")
// mapping request header https to java spring boot
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories() {
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

    // validation in request controller => validation in repo => validation in db
    @PostMapping("/admin/categories")
    public ResponseEntity<CategoryDTO> addCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        return new ResponseEntity<>(this.categoryService.createCategory(categoryDTO), HttpStatus.CREATED);
    }

    @GetMapping("/public/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> getCategory(@PathVariable long categoryId) {
        return new ResponseEntity<>(this.categoryService.getCategoryById(categoryId), HttpStatus.OK);
    }

    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable long categoryId) {
        String status = this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable long categoryId, @RequestBody CategoryUpdateDTO category) {
        return ResponseEntity.ok(this.categoryService.updateCategory(categoryId, category));
    }
}

/*
* Request
* {
*   ...
*   statusCode:..
*   data: {
*       status: "",
*       data: []..
*   }
* }
* */