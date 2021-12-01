package inventoryCodeChallenge.model;

import java.util.ArrayList;
import java.util.List;

public class InventoryModel {
    private Integer id;

    private String name;

    private int quantity;

    private List<InventoryCategoryModel> categories = new ArrayList<>();

    public InventoryModel() {
    }

    public InventoryModel(Integer id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
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

    public List<InventoryCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<InventoryCategoryModel> categories) {
        this.categories = categories;
    }

    public void addCategory(Integer categoryId, String categoryName, Integer subCategoryId, String subCategoryName) {
        addCategory(categoryId, categoryName);
        addSubCategory(categoryId, subCategoryId, subCategoryName);
    }

    public void addCategory(Integer id, String name) {
        InventoryCategoryModel c = getCategory(id);
        if (c == null) {
            this.categories.add(new InventoryCategoryModel(id, name));
        }
    }

    private InventoryCategoryModel getCategory(Integer id) {
        return this.getCategories().stream().filter(category -> category.getId().compareTo(id) == 0).findFirst().orElse(null);
    }

    public void addSubCategory(Integer categoryId, Integer subCategoryId, String subCategoryName) {
        InventoryCategoryModel tmp = getCategory(categoryId);
        if (tmp != null) {
            tmp.addSubCategory(subCategoryId, subCategoryName);
        } else {
            throw new RuntimeException("Missing category " + categoryId);
        }
    }

    public static class InventoryCategoryModel {
        private Integer id;
        private String name;
        private List<InventorySubCategoryModel> subCategories = new ArrayList<>();

        public InventoryCategoryModel() {

        }

        public InventoryCategoryModel(Integer id, String name) {
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

        public List<InventorySubCategoryModel> getSubCategories() {
            return subCategories;
        }

        public void addSubCategory(Integer id, String name) {
            subCategories.add(new InventorySubCategoryModel(id, name));
        }
    }

    public static class InventorySubCategoryModel {
        private Integer id;
        private String name;

        public InventorySubCategoryModel() {
        }

        public InventorySubCategoryModel(Integer id, String name) {
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
}
