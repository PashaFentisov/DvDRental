package controller;

import entity.Category;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import service.CategoryService;


@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public Category getCategory(@PathVariable("id") Long id){
        return categoryService.getCategory(id);
    }

    @GetMapping
    public Iterable<Category> getCategories(){
        return categoryService.getAllCategories();
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteCategory(@PathVariable("id") Long id){
         categoryService.deleteCategory(id);
        return new RedirectView("categories");
    }

    @PostMapping
    public RedirectView addCategory(@RequestBody Category category){
        categoryService.addCategory(category);
        return new RedirectView("categories");
//        return "redirect:/getAll";
    }

    @PutMapping("/{id}")
    public RedirectView updateCategory(@PathVariable("id") Long id, @RequestBody Category category){
        category.setId(id);
        categoryService.addCategory(category);
        return new RedirectView("categories");
    }

}
