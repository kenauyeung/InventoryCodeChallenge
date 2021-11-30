package inventoryCodeChallenge.dao;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "inventory")
public class InventoryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private int quantity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "inventory_sub_category", joinColumns = @JoinColumn(name = "inventory_id"), inverseJoinColumns = @JoinColumn(name = "sub_category_id"))
    private List<SubCategoryDao> subCategories;

    public InventoryDao() {

    }

    public InventoryDao(Integer id, String name, int quantity) {
        this(id, name, quantity, null);
    }

    public InventoryDao(Integer id, String name, int quantity, List<SubCategoryDao> subCategories) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.subCategories = subCategories;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<SubCategoryDao> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategoryDao> subCategories) {
        this.subCategories = subCategories;
    }
}
