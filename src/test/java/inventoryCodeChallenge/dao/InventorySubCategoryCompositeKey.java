package inventoryCodeChallenge.dao;

import java.io.Serializable;

public class InventorySubCategoryCompositeKey implements Serializable {
    private Integer inventoryId;
    private Integer subCategoryId;

    public InventorySubCategoryCompositeKey() {

    }

    public InventorySubCategoryCompositeKey(Integer inventoryId, Integer subCategoryId) {
        this.inventoryId = inventoryId;
        this.subCategoryId = subCategoryId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Integer getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(Integer subCategoryId) {
        this.subCategoryId = subCategoryId;
    }
}
