package inventoryCodeChallenge.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryInsertModel {

    @NotBlank(message = "Category name cannot be blank")
    private String name;

    @Min(value = 0, message = "Quantity must be a positive value")
    private int quantity;

    @NotEmpty(message = "sub category cannot be empty")
    private List<Integer> subCategories;

    public InventoryInsertModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<Integer> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Integer> subCategories) {
        this.subCategories = subCategories;
    }
}
