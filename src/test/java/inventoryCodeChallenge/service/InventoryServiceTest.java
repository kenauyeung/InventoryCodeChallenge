package inventoryCodeChallenge.service;

import inventoryCodeChallenge.BaseTest;
import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.InventoryInsertModel;
import inventoryCodeChallenge.model.InventoryModel;
import inventoryCodeChallenge.model.InventoryUpdateModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InventoryServiceTest extends BaseTest {
    @Autowired
    private InventoryService service;


    @Test
    public void getInventories_containNoInventoryRecordInDB_returnEmptyList() {
        List<InventoryModel> inventories = service.getInventories();
        List<InventoryDao> dbInventories = inventoryRepo.findAll();

        assertTrue(inventories.isEmpty(), "Inventory must return non null empty list");
        assertTrue(dbInventories.isEmpty(), "Inventory from DB must return non null empty list");
    }

    @Test
    public void getInventories_containsInventoryRecordInDB_returnNonEmptyList() {
        int quantity1 = 10, quantity2 = 20, quantity3 = 30;
        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryDao category2 = categoryRepo.save(new CategoryDao(null, "Category2"));

        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 1", category1));
        SubCategoryDao subCategory2 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 2", category1));
        SubCategoryDao subCategory3 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 3", category2));

        InventoryDao inventoryDao1 = new InventoryDao(null, "inventory1", quantity1, Arrays.asList(subCategory1));
        InventoryDao inventoryDao2 = new InventoryDao(null, "inventory2", quantity2, Arrays.asList(subCategory1, subCategory2));
        InventoryDao inventoryDao3 = new InventoryDao(null, "inventory3", quantity3, Arrays.asList(subCategory1, subCategory2, subCategory3));

        List<InventoryDao> insertInventoryList = Arrays.asList(inventoryDao1, inventoryDao2, inventoryDao3);
        List<InventoryDao> initInventories = inventoryRepo.saveAll(insertInventoryList);
        List<InventoryModel> testInventories = service.getInventories();

        assertEquals(insertInventoryList.size(), testInventories.size(), "Retrieved inventory list must be the size of " + insertInventoryList.size());
        for (int i = 0; i < testInventories.size(); i++) {
            assertInventoryDao(insertInventoryList.get(i), initInventories.get(i)); // test the data in db match with data in the initial object
            assertInventoryDao(convertModelToDao(testInventories.get(i)), initInventories.get(i)); // test the data against the returned modal
        }
    }

    @Test
    public void getInventory_withInventoryIdExist_returnInventoryInfo() {
        int quantity1 = 10, quantity2 = 20, quantity3 = 30;
        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 1", category1));
        InventoryDao inventoryDao1 = new InventoryDao(null, "inventory1", quantity1, Arrays.asList(subCategory1));

        InventoryDao initInventory = inventoryRepo.save(inventoryDao1);
        InventoryModel testInventory = service.getInventory(inventoryDao1.getId());

        assertInventoryDao(inventoryDao1, initInventory); // test the data in db match with data in the initial object
        assertInventoryDao(convertModelToDao(testInventory), initInventory); // test the data against the returned modal
    }

    @Test
    public void getInventory_withInventoryIdNotExist_returnNull() {
        InventoryModel testInventory = service.getInventory(9999);
        assertNull(testInventory);
    }

    @Test
    public void insertInventory_withValidInventoryInfo_returnNewlyCreatedInventoryRecord() {
        int quantity1 = 10, quantity2 = 20, quantity3 = 30;
        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryDao category2 = categoryRepo.save(new CategoryDao(null, "Category2"));

        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 1", category1));
        SubCategoryDao subCategory2 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 2", category1));
        SubCategoryDao subCategory3 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 3", category2));

        InventoryInsertModel inventory1 = new InventoryInsertModel("invnetory1", quantity1, Arrays.asList(subCategory1.getId()));
        InventoryInsertModel inventory2 = new InventoryInsertModel("invnetory2", quantity2, Arrays.asList(subCategory1.getId(), subCategory2.getId()));
        InventoryInsertModel inventory3 = new InventoryInsertModel("invnetory3", quantity3, Arrays.asList(subCategory1.getId(), subCategory2.getId(), subCategory3.getId()));

        InventoryModel newInventory1 = this.service.insertInventory(inventory1);
        InventoryModel newInventory2 = this.service.insertInventory(inventory2);
        InventoryModel newInventory3 = this.service.insertInventory(inventory3);

        assertNotNull(newInventory1, "new inventory object 1 cannot be null");
        assertNotNull(newInventory2, "new inventory object 2 cannot be null");
        assertNotNull(newInventory3, "new inventory object 3 cannot be null");

        assertInventoryDao(inventoryRepo.findById(newInventory1.getId()).orElse(null), convertModelToDao(newInventory1));
        assertInventoryDao(inventoryRepo.findById(newInventory2.getId()).orElse(null), convertModelToDao(newInventory2));
        assertInventoryDao(inventoryRepo.findById(newInventory3.getId()).orElse(null), convertModelToDao(newInventory3));
    }

    @Test
    public void insertInventory_withInvalidInventoryName_throwsInvalidValueException() {
        List<Integer> subCategories = Arrays.asList(1, 2, 3);

        InventoryInsertModel testInventory1 = new InventoryInsertModel(null, 100, subCategories);
        InventoryInsertModel testInventory2 = new InventoryInsertModel("", 100, subCategories);
        InventoryInsertModel testInventory3 = new InventoryInsertModel("   ", 100, subCategories);

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory1),
                "must throw exception when provided incorrect inventory name");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory2),
                "must throw exception when provided incorrect inventory name");
        InvalidValueException exception3 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory3),
                "must throw exception when provided incorrect inventory name");

        assertEquals("Inventory name cannot be blank", exception1.getMessage());
        assertEquals("Inventory name cannot be blank", exception2.getMessage());
        assertEquals("Inventory name cannot be blank", exception3.getMessage());
    }

    @Test
    public void insertInventory_withNegativeQuantity_throwsInvalidValueException() {
        List<Integer> subCategories = Arrays.asList(1, 2, 3);

        InventoryInsertModel testInventory1 = new InventoryInsertModel("inventory1", -100, subCategories);

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory1),
                "must throw exception when provided incorrect inventory quantity");

        assertEquals("Inventory quantity must be positive integer", exception1.getMessage());
    }

    @Test
    public void insertInventory_withInvalidSubCategoryId_throwsInvalidValueException() {
        InventoryInsertModel testInventory1 = new InventoryInsertModel("inventory1", 100, null);
        InventoryInsertModel testInventory2 = new InventoryInsertModel("inventory1", 100, Arrays.asList());

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory1),
                "must throw exception when provided incorrect inventory sub-category id");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.insertInventory(testInventory2),
                "must throw exception when provided incorrect inventory sub-category id");

        assertEquals("Inventory sub-category cannot be empty", exception1.getMessage());
        assertEquals("Inventory sub-category cannot be empty", exception2.getMessage());
    }

    @Test
    public void insertInventory_withNotExistSubCategoryId_throwsMissingRecordException() {
        InventoryInsertModel testInventory1 = new InventoryInsertModel("inventory1", 100, Arrays.asList(1));

        MissingRecordException exception1 = assertThrows(MissingRecordException.class, () -> service.insertInventory(testInventory1),
                "must throw exception when provided not exist inventory sub-category id");

        assertEquals("Unable to retrieve sub-category with id " + 1, exception1.getMessage());
    }

    @Test
    public void updateInventory_withValidInventoryInfo_returnUpdatedInventoryRecord() {
        int quantity1 = 10, quantity2 = 20, quantity3 = 30;
        int updateQuantity = 100;

        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryDao category2 = categoryRepo.save(new CategoryDao(null, "Category2"));

        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 1", category1));
        SubCategoryDao subCategory2 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 2", category1));
        SubCategoryDao subCategory3 = subCategoryRepo.save(new SubCategoryDao(null, "sub-category 3", category2));

        InventoryInsertModel inventory1 = new InventoryInsertModel("invnetory1", quantity1, Arrays.asList(subCategory1.getId()));
        InventoryInsertModel inventory2 = new InventoryInsertModel("invnetory2", quantity2, Arrays.asList(subCategory1.getId(), subCategory2.getId()));
        InventoryInsertModel inventory3 = new InventoryInsertModel("invnetory3", quantity3, Arrays.asList(subCategory1.getId(), subCategory2.getId(), subCategory3.getId()));

        InventoryModel newInventory1 = this.service.insertInventory(inventory1);
        InventoryModel newInventory2 = this.service.insertInventory(inventory2);
        InventoryModel newInventory3 = this.service.insertInventory(inventory3);

        InventoryModel updateInventory1 = this.service.updateInventory(new InventoryUpdateModel(newInventory1.getId(), updateQuantity));
        InventoryModel updateInventory2 = this.service.updateInventory(new InventoryUpdateModel(newInventory2.getId(), updateQuantity));
        InventoryModel updateInventory3 = this.service.updateInventory(new InventoryUpdateModel(newInventory3.getId(), updateQuantity));

        assertEquals(quantity1, newInventory1.getQuantity());
        assertEquals(quantity2, newInventory2.getQuantity());
        assertEquals(quantity3, newInventory3.getQuantity());

        assertEquals(updateQuantity, updateInventory1.getQuantity());
        assertEquals(updateQuantity, updateInventory2.getQuantity());
        assertEquals(updateQuantity, updateInventory3.getQuantity());

        assertInventoryDao(inventoryRepo.findById(newInventory1.getId()).orElse(null), convertModelToDao(updateInventory1));
        assertInventoryDao(inventoryRepo.findById(newInventory2.getId()).orElse(null), convertModelToDao(updateInventory2));
        assertInventoryDao(inventoryRepo.findById(newInventory3.getId()).orElse(null), convertModelToDao(updateInventory3));
    }

    @Test
    public void updateInventory_withNotExistInventoryId_throwsMissingRecordException() {
        int id = 999;
        InventoryUpdateModel testInventory1 = new InventoryUpdateModel(id, 999);

        MissingRecordException exception1 = assertThrows(MissingRecordException.class, () -> service.updateInventory(testInventory1),
                "must throw exception when provided not exist inventory id");

        assertEquals("Unable to find inventory with id " + id, exception1.getMessage());
    }


    @Test
    public void updateInventory_withInvalidInventoryId_throwsInvalidValueException() {
        InventoryUpdateModel testInventory1 = new InventoryUpdateModel(null, 999);
        InventoryUpdateModel testInventory2 = new InventoryUpdateModel(-1, 999);

        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.updateInventory(testInventory1),
                "must throw exception when provided invalid inventory id");
        InvalidValueException exception2 = assertThrows(InvalidValueException.class, () -> service.updateInventory(testInventory2),
                "must throw exception when provided invalid inventory id");

        assertEquals("Inventory id must be positive integer", exception1.getMessage());
        assertEquals("Inventory id must be positive integer", exception2.getMessage());
    }

    @Test
    public void updateInventory_withNegativeQuantity_throwsInvalidValueException() {
        InventoryUpdateModel testInventory1 = new InventoryUpdateModel(1, -999);
        InvalidValueException exception1 = assertThrows(InvalidValueException.class, () -> service.updateInventory(testInventory1),
                "must throw exception when provided negative quantity");
        assertEquals("Inventory quantity must be positive integer", exception1.getMessage());
    }

    private void assertInventoryDao(InventoryDao source, InventoryDao testDao) {
        assertEquals(source.getId(), testDao.getId(), "inventory id from both dao must match");
        assertEquals(source.getName(), testDao.getName(), "inventory name from both dao must match");
        assertEquals(source.getQuantity(), testDao.getQuantity(), "inventory quantity from both dao must match");
        assertEquals(source.getSubCategories().size(), testDao.getSubCategories().size(), "inventory category size from both dao must match");

        for (int i = 0; i < source.getSubCategories().size(); i++) {
            SubCategoryDao subCategorySourceDao = source.getSubCategories().get(i);
            SubCategoryDao subCategoryTestDao = testDao.getSubCategories().get(i);
            CategoryDao categorySourceDao = subCategorySourceDao.getCategory();
            CategoryDao categoryTestDao = subCategoryTestDao.getCategory();

            assertEquals(categorySourceDao.getId(), categoryTestDao.getId(), "category id must match for element " + i);
            assertEquals(categorySourceDao.getName(), categoryTestDao.getName(), "category name must match for element " + i);
            assertEquals(subCategorySourceDao.getId(), subCategoryTestDao.getId(), "sub category id must match for element " + i);
            assertEquals(subCategorySourceDao.getName(), subCategoryTestDao.getName(), "sub category name must match for element " + i);
        }
    }

    private InventoryDao convertModelToDao(InventoryModel model) {

        final List<SubCategoryDao> subCategoryDaoList = new ArrayList<>();
        model.getCategories().forEach(category -> {
            category.getSubCategories().forEach(subCategory -> {
                subCategoryDaoList.add(new SubCategoryDao(subCategory.getId(), subCategory.getName(), new CategoryDao(category.getId(), category.getName())));
            });
        });

        return new InventoryDao(model.getId(), model.getName(), model.getQuantity(), subCategoryDaoList);
    }
}
