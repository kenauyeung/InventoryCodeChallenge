package inventoryCodeChallenge.service;

import inventoryCodeChallenge.BaseTest;
import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.CategoryModel;
import inventoryCodeChallenge.model.SubCategoryModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SubCategoryServiceTest extends BaseTest {

    @Autowired
    private SubCategoryService service;


    @Test
    public void getSubCategories_containNoSubCategoryRecordInDB_returnEmptyList() {
        List<SubCategoryModel> subCategories = service.getSubCategories();
        List<SubCategoryDao> dbSubCategories = subCategoryRepo.findAll();

        assertTrue(subCategories.isEmpty(), "Category must return non null empty list");
        assertTrue(dbSubCategories.isEmpty(), "Sub-category from DB must return non null empty list");
    }

    @Test
    public void getSubCategories_containsSubCategoryRecordInDB_returnNonEmptyList() {

        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = new SubCategoryDao(null, "subCategory1", category);
        SubCategoryDao subCategory2 = new SubCategoryDao(null, "subCategory2", category);
        List<SubCategoryDao> initSubCategoryDataList = Arrays.asList(subCategory1, subCategory2);

        initSubCategoryDataList = subCategoryRepo.saveAll(initSubCategoryDataList);

        List<SubCategoryModel> testSubCategories = service.getSubCategories();

        assertFalse(testSubCategories.isEmpty(), "Sub-category cannot be an empty list");
        assertEquals(2, testSubCategories.size(), "Sub-category list must contains 2 element");

        for (int i = 0; i < initSubCategoryDataList.size(); i++) {
            SubCategoryDao initSubCategory = initSubCategoryDataList.get(i);
            SubCategoryModel testCategory = testSubCategories.get(i);

            assertNotNull(testCategory.getCategory(), "Category cannot be null");
            assertEquals(initSubCategory.getId(), testCategory.getId(), "Sub-category id of element " + i + " must equal to " + initSubCategory.getId());
            assertEquals(initSubCategory.getName(), testCategory.getName(), "Sub-category name of element " + i + " must equal to " + initSubCategory.getName());
            assertEquals(category.getId(), testCategory.getCategory().getId(), "category id of element must equal to " + category.getId());
            assertEquals(category.getName(), testCategory.getCategory().getName(), "Category name of element must equal to " + category.getName());
        }
    }

    @Test
    public void insertSubCategory_withValidSubCategoryName_returnNewlyCreatedSubCategoryRecord() {

        CategoryDao categoryDao = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryModel category = new CategoryModel(categoryDao.getId(), categoryDao.getName());

        SubCategoryModel subCategory = new SubCategoryModel(null, "sub-categoryNew", category);
        SubCategoryModel newSubCategory = service.insertSubCategory(subCategory);
        SubCategoryDao subCategoryFromDB = subCategoryRepo.findById(newSubCategory.getId()).orElse(null);

        assertNotNull(subCategoryFromDB, "the new sub-category must exist in db");
        assertEquals(subCategory.getName(), newSubCategory.getName(), "new sub-category name must equal to " + subCategory.getName());
        assertEquals(subCategory.getName(), subCategoryFromDB.getName(), "sub-category from db name must equal to " + subCategory.getName());
        assertEquals(newSubCategory.getId(), subCategoryFromDB.getId(), "sub-category ID from db must equal to " + subCategoryFromDB.getId());
        assertEquals(categoryDao.getId(), newSubCategory.getCategory().getId(), "Category ID in new sub-category object must equal to " + categoryDao.getId());
        assertEquals(categoryDao.getName(), newSubCategory.getCategory().getName(), "Category name in new sub-category object must equal to " + categoryDao.getName());
    }

    @Test
    public void insertSubCategory_withBlankSubCategoryName_throwsInvalidValueException() {
        SubCategoryModel testCategory1 = new SubCategoryModel(null, null);
        SubCategoryModel testCategory2 = new SubCategoryModel(null, "");
        SubCategoryModel testCategory3 = new SubCategoryModel(null, "   ");

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testCategory1),
                "must throw exception when provided incorrect category name");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testCategory2),
                "must throw exception when provided incorrect category name");
        InvalidValueException exception3 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testCategory3),
                "must throw exception when provided incorrect category name");

        assertEquals("Sub-category name cannot be blank", exception1.getMessage());
        assertEquals("Sub-category name cannot be blank", exception2.getMessage());
        assertEquals("Sub-category name cannot be blank", exception3.getMessage());
    }

    @Test
    public void insertSubCategory_withBlankCategoryId_throwsInvalidValueException() {
        SubCategoryModel testSubCategory1 = new SubCategoryModel(null, "sub-category1");
        SubCategoryModel testSubCategory2 = new SubCategoryModel(null, "sub-category1", new CategoryModel(null, null));
        SubCategoryModel testSubCategory3 = new SubCategoryModel(null, "sub-category1", new CategoryModel(-1, null));

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testSubCategory1),
                "must throw exception when provided incorrect category id");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testSubCategory2),
                "must throw exception when provided incorrect category id");
        InvalidValueException exception3 = assertThrows(InvalidValueException.class, () -> service.insertSubCategory(testSubCategory3),
                "must throw exception when provided incorrect category id");

        assertEquals("category id cannot be blank", exception1.getMessage());
        assertEquals("category id cannot be blank", exception2.getMessage());
        assertEquals("category id must be a positive integer", exception3.getMessage());
    }

    @Test
    public void insertSubCategory_withNotExistCategoryId_throwsMissingRecordException() {
        Integer categoryId = 999;
        SubCategoryModel testSubCategory1 = new SubCategoryModel(null, "sub-category1", new CategoryModel(categoryId, null));

        MissingRecordException exception1 = assertThrows(MissingRecordException.class, () -> service.insertSubCategory(testSubCategory1),
                "must throw exception when provided not exist category id");

        assertEquals("Unable to find category with id " + categoryId, exception1.getMessage());
    }

    @Test
    public void insertSubCategory_withSelfDefineSubCategoryId_returnRecordWithDBAutoGenerateSubCategoryId() {
        CategoryDao categoryDao = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryModel category = new CategoryModel(categoryDao.getId(), categoryDao.getName());

        SubCategoryModel subCategory1 = new SubCategoryModel(5000, "categoryNew1", category);
        SubCategoryModel subCategory2 = new SubCategoryModel(10000, "categoryNew2", category);

        SubCategoryModel newSubCategory1 = service.insertSubCategory(subCategory1);
        SubCategoryModel newSubCategory2 = service.insertSubCategory(subCategory2);
        SubCategoryDao subCategoryFromDB1 = subCategoryRepo.findById(newSubCategory1.getId()).orElse(null);
        SubCategoryDao subCategoryFromDB2 = subCategoryRepo.findById(newSubCategory2.getId()).orElse(null);

        assertNotNull(subCategoryFromDB1, "the new sub-category 1 must exist in db");
        assertNotNull(subCategoryFromDB2, "the new sub-category 2 must exist in db");
        assertEquals(categoryDao.getId(), subCategoryFromDB1.getCategory().getId(), "New sub category 1 must contain category id " + categoryDao.getId());
        assertEquals(categoryDao.getId(), subCategoryFromDB2.getCategory().getId(), "New sub category 2 must contain category id " + categoryDao.getId());
        assertEquals(categoryDao.getName(), subCategoryFromDB1.getCategory().getName(), "New sub category 1 must contain category name " + categoryDao.getName());
        assertEquals(categoryDao.getName(), subCategoryFromDB2.getCategory().getName(), "New sub category 2 must contain category name " + categoryDao.getName());

        assertEquals(newSubCategory1.getId(), subCategoryFromDB1.getId(), "id from both return and db must equal to " + newSubCategory1.getId());
        assertEquals(newSubCategory2.getId(), subCategoryFromDB2.getId(), "id from both return and db must equal to " + newSubCategory2.getId());
        assertNotEquals(subCategory1.getId(), newSubCategory1.getId(), "sub-category 1 ID must not equal to " + subCategory1.getId());
        assertNotEquals(subCategory2.getId(), newSubCategory2.getId(), "sub-category 2 ID must not equal to " + subCategory2.getId());
        assertEquals(newSubCategory1.getId(), newSubCategory2.getId() - 1, "ID from sub-category 2 must be an increment of 1 from sub-category 1");
    }
}