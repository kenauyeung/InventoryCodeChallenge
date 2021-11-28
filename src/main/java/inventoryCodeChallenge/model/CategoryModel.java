package inventoryCodeChallenge.model;

import javax.validation.constraints.NotBlank;

public class CategoryModel {
    private Integer id;

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    public CategoryModel() {
    }

    public CategoryModel(Integer id, String name) {
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
}
