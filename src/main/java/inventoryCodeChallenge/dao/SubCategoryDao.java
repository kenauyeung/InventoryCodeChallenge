package inventoryCodeChallenge.dao;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "sub_category")
public class SubCategoryDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private CategoryDao category;

    public SubCategoryDao() {
    }

    public SubCategoryDao(Integer id, String name) {
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

    public CategoryDao getCategory() {
        return category;
    }

    public void setCategory(CategoryDao category) {
        this.category = category;
    }
}
