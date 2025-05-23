package eu.isygoit.controller;

import eu.isygoit.annotation.CtrlDef;
import eu.isygoit.com.rest.controller.impl.MappedCrudController;
import eu.isygoit.dto.data.CategoryDto;
import eu.isygoit.dto.data.TagDto;
import eu.isygoit.exception.handler.SmeKitExceptionHandler;
import eu.isygoit.mapper.CategoryMapper;
import eu.isygoit.model.Category;
import eu.isygoit.service.ICategoryService;
import eu.isygoit.service.impl.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@CtrlDef(handler = SmeKitExceptionHandler.class, mapper = CategoryMapper.class, minMapper = CategoryMapper.class, service = CategoryService.class)
@RequestMapping(value = "/api/v1/private/category")
public class CategoryController extends MappedCrudController<Long, Category, CategoryDto, CategoryDto, CategoryService> {
    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/{categoryId}/tags")
    public ResponseEntity<CategoryDto> addTagsToCategory(
            @PathVariable Long categoryId,
            @RequestBody List<TagDto> tagDtos) {
        Category updatedCategory = crudService().addTagsToCategory(categoryId, tagDtos);
        CategoryDto responseDto = mapper().entityToDto(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{categoryId}/tags")
    public ResponseEntity<CategoryDto> setTagsForCategory(
            @PathVariable Long categoryId,
            @RequestBody List<TagDto> tagDtos) {
        Category updatedCategory = crudService().setTagsForCategory(categoryId, tagDtos);
        CategoryDto responseDto = mapper().entityToDto(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{categoryId}/tags/{tagId}")
    public ResponseEntity<CategoryDto> removeTagFromCategory(
            @PathVariable Long categoryId,
            @PathVariable Long tagId) {
        Category updatedCategory = crudService().removeTagFromCategory(categoryId, tagId);
        CategoryDto responseDto = mapper().entityToDto(updatedCategory);
        return ResponseEntity.ok(responseDto);
    }


}