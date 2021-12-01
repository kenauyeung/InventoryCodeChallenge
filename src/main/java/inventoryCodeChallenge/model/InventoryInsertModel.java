package inventoryCodeChallenge.model;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

public class InventoryInsertModel {

    @NotBlank(message = "Inventory name cannot be blank")
    @Length()
    private String name;

    @Min(value = 0, message = "Quantity must be a positive value")
    private int quantity = -1;

    @NotEmpty(message = "Sub-category cannot be empty")
    private List<Integer> subCategories;

    public InventoryInsertModel() {
    }

    public InventoryInsertModel(String name, int quantity, List<Integer> subCategories) {
        this.name = name;
        this.quantity = quantity;
        this.subCategories = subCategories;
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
