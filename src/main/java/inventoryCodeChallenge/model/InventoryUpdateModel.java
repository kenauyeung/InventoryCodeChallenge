package inventoryCodeChallenge.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InventoryUpdateModel {

    @NotNull(message = "Id cannot be null")
    @Min(value = 0, message = "Id must be a positive value")
    private Integer id;

    @Min(value = 0, message = "Quantity must be a positive value")
    private int quantity;

    public InventoryUpdateModel() {
    }

    public InventoryUpdateModel(Integer id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
