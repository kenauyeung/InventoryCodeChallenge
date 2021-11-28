package inventoryCodeChallenge.model;

import inventoryCodeChallenge.service.CategoryService;

import javax.validation.constraints.NotBlank;

public class SubCategoryModel {
    private Integer id;

    @NotBlank(message = "Sub category name cannot be blank")
    private String name;
    private CategoryModel category;

    public SubCategoryModel() {

    }

    public SubCategoryModel(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }
}
