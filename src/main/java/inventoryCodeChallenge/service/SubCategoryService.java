package inventoryCodeChallenge.service;

import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.SubCategoryModel;
import inventoryCodeChallenge.repository.CategoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import inventoryCodeChallenge.mapper.SubCategoryDataMapper;
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

    public SubCategoryModel insert(SubCategoryModel model) {
        if (StringUtils.isBlank(model.getName())) {
            throw new InvalidValueException("Category name cannot be blank");
        } else if (model.getCategory() == null || model.getCategory().getId() == null) {
            throw new InvalidValueException("Category id cannot be blank");
        }

        model.setId(null);
        if (categoryRepo.findById(model.getCategory().getId()).isPresent()) {
            return SubCategoryDataMapper.dataConversion(repo.save(SubCategoryDataMapper.dataConversion(model)));
        }
        throw new MissingRecordException("Unable to find category with id " + model.getCategory().getId());
    }
}
