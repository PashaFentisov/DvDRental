package controller;

import entity.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import service.CategoryService;

import java.time.LocalDate;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/get")
    public Category getCategory(@RequestParam("id") Long id){
        return categoryService.getCategory(id);
    }

    @GetMapping("/getAll")
    public Iterable<Category> getCategories(){
        return categoryService.getAllCategories();
    }

    @GetMapping("/delete")
    public RedirectView deleteCategory(@RequestParam("id") Long id){
         categoryService.deleteCategory(id);
        return new RedirectView("getAll");
    }

    @GetMapping("/add")
    public RedirectView addCategory(@RequestParam("name") String name){
        Category category = new Category(name, LocalDate.now());
        categoryService.addCategory(category);
        return new RedirectView("getAll");
//        return "redirect:/getAll";
    }

}
