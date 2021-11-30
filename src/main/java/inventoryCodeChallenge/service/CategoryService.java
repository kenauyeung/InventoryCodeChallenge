package inventoryCodeChallenge.service;

import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.mapper.CategoryDataMapper;
import inventoryCodeChallenge.model.CategoryModel;
import inventoryCodeChallenge.repository.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<CategoryModel> getCategories() {
        return repo.findAll().stream().filter(Objects::nonNull).map(CategoryDataMapper::dataConversion).collect(Collectors.toList());
    }

    public CategoryModel insertCategory(CategoryModel model) {
        if (StringUtils.isBlank(model.getName())) {
            throw new InvalidValueException("Category name cannot be blank");
        }

        model.setId(null);
        return CategoryDataMapper.dataConversion(repo.save(CategoryDataMapper.dataConversion(model)));
    }
}
