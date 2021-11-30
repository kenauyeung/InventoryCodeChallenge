package inventoryCodeChallenge.service;

import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.mapper.SubCategoryDataMapper;
import inventoryCodeChallenge.model.SubCategoryModel;
import inventoryCodeChallenge.repository.CategoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class SubCategoryService {
    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private SubCategoryRepository repo;

    public List<SubCategoryModel> getSubCategories() {
        return repo.findAll().stream().filter(Objects::nonNull).map(SubCategoryDataMapper::dataConversion).collect(Collectors.toList());
    }

    public SubCategoryModel insertSubCategory(SubCategoryModel model) {
        if (StringUtils.isBlank(model.getName())) {
            throw new InvalidValueException("Sub-category name cannot be blank");
        } else if (model.getCategory() == null || model.getCategory().getId() == null) {
            throw new InvalidValueException("category id cannot be blank");
        } else if (model.getCategory().getId() < 0) {
            throw new InvalidValueException("category id must be a positive integer");
        }

        model.setId(null);
        if (categoryRepo.findById(model.getCategory().getId()).isPresent()) {
            SubCategoryDao tmpDao = repo.save(SubCategoryDataMapper.dataConversion(model));
            tmpDao.setCategory(categoryRepo.findById(model.getCategory().getId()).orElse(null));
            return SubCategoryDataMapper.dataConversion(tmpDao);
        }
        throw new MissingRecordException("Unable to find category with id " + model.getCategory().getId());
    }
}
