package inventoryCodeChallenge.repository;

import inventoryCodeChallenge.dao.InventorySubCategoryCompositeKey;
import inventoryCodeChallenge.dao.InventorySubCategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventorySubCategoryRepository extends JpaRepository<InventorySubCategoryDao, InventorySubCategoryCompositeKey> {
}
