package inventoryCodeChallenge.mapper;

import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.model.CategoryModel;


public class CategoryDataMapper {
    public static CategoryModel dataConversion(CategoryDao dao) {
        return new CategoryModel(dao.getId(), dao.getName());
    }

    public static CategoryDao dataConversion(CategoryModel model) {
        return new CategoryDao(model.getId(), model.getName());
    }
}
