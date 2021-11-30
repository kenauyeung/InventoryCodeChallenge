package inventoryCodeChallenge.dao;


import javax.persistence.*;

@Entity
@Table(name = "inventory_sub_category")
@IdClass(InventorySubCategoryCompositeKey.class)
public class InventorySubCategoryDao {
    @Id
    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Id
    @Column(name = "sub_category_id")
    private Integer subCategoryId;

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
