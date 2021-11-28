package inventoryCodeChallenge.repository;

import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InventoryRepository extends JpaRepository<InventoryDao, Integer> {
}
