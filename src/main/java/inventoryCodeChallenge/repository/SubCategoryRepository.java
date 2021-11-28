package inventoryCodeChallenge.repository;

import inventoryCodeChallenge.dao.SubCategoryDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SubCategoryRepository extends JpaRepository<SubCategoryDao, Integer> {
}
