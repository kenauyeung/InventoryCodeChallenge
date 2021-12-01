package inventoryCodeChallenge.service;

import inventoryCodeChallenge.BaseTest;
import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.model.CategoryModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryService service;

    @Test
    public void getCategories_containNoCategoryRecordInDB_returnEmptyList() {
        List<CategoryModel> categories = service.getCategories();
        List<CategoryDao> dbCategories = categoryRepo.findAll();

        assertTrue(categories.isEmpty(), "Category must return non null empty list");
        assertTrue(dbCategories.isEmpty(), "Category from DB must return non null empty list");
    }

    @Test
    public void getCategories_containsCategoryRecordInDB_returnNonEmptyList() {
        CategoryDao category1 = new CategoryDao(null, "category1");
        CategoryDao category2 = new CategoryDao(null, "category2");
        List<CategoryDao> initCategoryDataList = Arrays.asList(category1, category2);

        initCategoryDataList = categoryRepo.saveAll(initCategoryDataList);

        List<CategoryModel> testCategories = service.getCategories();

        assertFalse(testCategories.isEmpty(), "Category cannot be an empty list");
        assertEquals(2, testCategories.size(), "Category list must contains 2 element");

        for (int i = 0; i < initCategoryDataList.size(); i++) {
            CategoryDao initCategory = initCategoryDataList.get(i);
            CategoryModel testCategory = testCategories.get(i);

            assertEquals(initCategory.getId(), testCategory.getId(), "Category id of element " + i + " must equal to " + initCategory.getId());
            assertEquals(initCategory.getName(), testCategory.getName(), "Category name of element " + i + " must equal to " + initCategory.getName());
        }
    }

    @Test
    public void insertCategory_withValidCategoryName_returnNewlyCreatedCategoryRecord() {
        CategoryModel category = new CategoryModel(null, "categoryNew");
        CategoryModel newCategory = service.insertCategory(category);
        CategoryDao categoryFromDB = categoryRepo.findById(newCategory.getId()).orElse(null);

        assertNotNull(categoryFromDB, "the new category must exist in db");
        assertEquals(category.getName(), newCategory.getName(), "new category name must equal to " + category.getName());
        assertEquals(category.getName(), categoryFromDB.getName(), "category from db name must equal to " + category.getName());
        assertEquals(newCategory.getId(), categoryFromDB.getId(), "category ID from db must equal to " + newCategory.getId());
    }

    @Test
    public void insertCategory_withBlankCategoryName_throwsInvalidValueException() {
        CategoryModel testCategory1 = new CategoryModel(null, null);
        CategoryModel testCategory2 = new CategoryModel(null, "");
        CategoryModel testCategory3 = new CategoryModel(null, "   ");

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertCategory(testCategory1),
                "must throw exception when provided incorrect category name");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.insertCategory(testCategory2),
                "must throw exception when provided incorrect category name");
        InvalidValueException exception3 = assertThrows(InvalidValueException.class, () -> service.insertCategory(testCategory3),
                "must throw exception when provided incorrect category name");

        assertEquals("Category name cannot be blank", exception1.getMessage());
        assertEquals("Category name cannot be blank", exception2.getMessage());
        assertEquals("Category name cannot be blank", exception3.getMessage());
    }

    @Test
    public void insertCategory_withSelfDefineCategoryId_returnRecordWithDBAutoGenerateCategoryId() {
        CategoryModel category1 = new CategoryModel(5000, "categoryNew1");
        CategoryModel category2 = new CategoryModel(10000, "categoryNew2");

        CategoryModel newCategory1 = service.insertCategory(category1);
        CategoryModel newCategory2 = service.insertCategory(category2);
        CategoryDao categoryFromDB1 = categoryRepo.findById(newCategory1.getId()).orElse(null);
        CategoryDao categoryFromDB2 = categoryRepo.findById(newCategory2.getId()).orElse(null);

        assertNotNull(categoryFromDB1, "the new category 1 must exist in db");
        assertNotNull(categoryFromDB2, "the new category 2 must exist in db");
        assertEquals(newCategory1.getId(), categoryFromDB1.getId(), "id from both return and db must equal to " + newCategory1.getId());
        assertEquals(newCategory2.getId(), categoryFromDB2.getId(), "id from both return and db must equal to " + newCategory2.getId());
        assertNotEquals(category1.getId(), newCategory1.getId(), "category 1 ID must not equal to " + category1.getId());
        assertNotEquals(category2.getId(), newCategory2.getId(), "category 2 ID must not equal to " + category2.getId());
        assertEquals(newCategory1.getId(), newCategory2.getId() - 1, "ID from category 2 must be an increment of 1 from category 1");
    }

}