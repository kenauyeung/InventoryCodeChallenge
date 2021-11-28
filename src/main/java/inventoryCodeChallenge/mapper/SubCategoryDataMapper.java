package inventoryCodeChallenge.mapper;

import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.model.CategoryModel;
import inventoryCodeChallenge.model.SubCategoryModel;


public class SubCategoryDataMapper {
    public static SubCategoryDao dataConversion(SubCategoryModel model) {
        SubCategoryDao dao = new SubCategoryDao(model.getId(), model.getName());

        if (model.getCategory() != null) {
            dao.setCategory(new CategoryDao(model.getCategory().getId(), model.getCategory().getName()));
        }
        return dao;
    }

    public static SubCategoryModel dataConversion(SubCategoryDao dao) {
        SubCategoryModel model = new SubCategoryModel(dao.getId(), dao.getName());

        if (dao.getCategory() != null) {
            model.setCategory(new CategoryModel(dao.getCategory().getId(), dao.getCategory().getName()));
        }
        return model;
    }
}
