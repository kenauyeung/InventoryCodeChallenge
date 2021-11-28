package inventoryCodeChallenge.mapper;

import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.model.InventoryModel;
import inventoryCodeChallenge.model.InventoryInsertModel;
import org.springframework.util.CollectionUtils;

import java.util.stream.Collectors;


public class InventoryDataMapper {

    public static InventoryModel dataConversion(InventoryDao dao) {
        InventoryModel model = new InventoryModel(dao.getId(), dao.getName(), dao.getQuantity());

        if (!CollectionUtils.isEmpty(dao.getSubCategories())) {
            dao.getSubCategories().stream().forEach(sub -> {
                if(sub.getCategory() != null) {
                    model.addCategory(sub.getCategory().getId(), sub.getCategory().getName(), sub.getId(), sub.getName());
                }
            });
        }
        return model;
    }

    public static InventoryDao dataConversion(InventoryInsertModel model) {
        InventoryDao dao = new InventoryDao(null, model.getName(), model.getQuantity());

        if (!CollectionUtils.isEmpty(model.getSubCategories())) {
            dao.setSubCategories(model.getSubCategories().stream().map(id -> {
                return new SubCategoryDao(id, null);
            }).collect(Collectors.toList()));
        }

        return dao;
    }
}
