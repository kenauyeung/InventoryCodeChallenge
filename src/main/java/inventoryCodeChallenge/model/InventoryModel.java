package inventoryCodeChallenge.model;

import java.util.HashMap;
import java.util.Map;

public class InventoryModel {
    private Integer id;

    private String name;

    private int quantity;

    private Map<Integer, InventoryCategoryModel> categories = new HashMap<>();

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

    public Map<Integer, InventoryCategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(Map<Integer, InventoryCategoryModel> categories) {
        this.categories = categories;
    }

    public void addCategory(Integer categoryId, String categoryName, Integer subCategoryId, String subCategoryName) {
        addCategory(categoryId, categoryName);
        addSubCategory(categoryId, subCategoryId, subCategoryName);
    }

    public void addCategory(Integer id, String name) {
        if (!this.categories.containsKey(id)) {
            this.categories.put(id, new InventoryCategoryModel(id, name));
        }
    }

    public void addSubCategory(Integer categoryId, Integer subCategoryId, String subCategoryName) {
        InventoryCategoryModel tmp = this.categories.get(categoryId);
        if (tmp != null) {
            tmp.addSubCategory(subCategoryId, subCategoryName);
        } else {
            throw new RuntimeException("Missing category " + categoryId);
        }
    }

    public static class InventoryCategoryModel {
        private Integer id;
        private String name;
        private Map<Integer, String> subCategory = new HashMap<>();

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

        public Map<Integer, String> getSubCategory() {
            return subCategory;
        }

        public void setSubCategory(Map<Integer, String> subCategory) {
            this.subCategory = subCategory;
        }

        public void addSubCategory(Integer id, String name) {
            subCategory.put(id, name);
        }
    }
}
