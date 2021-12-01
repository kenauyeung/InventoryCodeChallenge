package inventoryCodeChallenge;

import inventoryCodeChallenge.repository.CategoryRepository;
import inventoryCodeChallenge.repository.InventoryRepository;
import inventoryCodeChallenge.repository.InventorySubCategoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTest {
    @Autowired
    protected InventoryRepository inventoryRepo;

    @Autowired
    protected SubCategoryRepository subCategoryRepo;

    @Autowired
    protected CategoryRepository categoryRepo;

    @Autowired
    protected InventorySubCategoryRepository invSubCategoryRepo;

    @BeforeEach
    public void beforeEach() {
        invSubCategoryRepo.deleteAll();
        inventoryRepo.deleteAll();
        subCategoryRepo.deleteAll();
        categoryRepo.deleteAll();
    }
}
