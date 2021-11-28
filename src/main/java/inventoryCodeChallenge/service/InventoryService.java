package inventoryCodeChallenge.service;

import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.mapper.InventoryDataMapper;
import inventoryCodeChallenge.model.InventoryInsertModel;
import inventoryCodeChallenge.model.InventoryModel;
import inventoryCodeChallenge.repository.InventoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository repo;

    @Autowired
    private SubCategoryRepository subCategoryRepo;

    public List<InventoryModel> getInventories() {
        return repo.findAll().stream().filter(Objects::nonNull).map(InventoryDataMapper::dataConversion).collect(Collectors.toList());
    }

    public InventoryModel getInventories(int id) {
        InventoryDao dao = repo.findById(id).orElse(null);
        if (dao != null) {
            return InventoryDataMapper.dataConversion(dao);
        }
        throw new MissingRecordException("Unable to find inventory with id " + id);
    }

    public InventoryModel insert(InventoryInsertModel model) {
        String errMsg = "name cannot be blank";
        if (StringUtils.isNotBlank(model.getName())) {
            if (model.getQuantity() >= 0) {
                if (!CollectionUtils.isEmpty(model.getSubCategories())) {
                    Set<Integer> subIdSet = subCategoryRepo.findAllById(model.getSubCategories()).stream().map(SubCategoryDao::getId).collect(Collectors.toSet());
                    String unFoundSubCategoryId = model.getSubCategories().stream().filter(id -> !subIdSet.contains(id)).map(String::valueOf).collect(Collectors.joining(","));
                    if (StringUtils.isBlank(unFoundSubCategoryId)) {
                        InventoryDao dao = repo.save(InventoryDataMapper.dataConversion(model));
                        return InventoryDataMapper.dataConversion(dao);
                    } else {
                        errMsg = "sub category not found : " + unFoundSubCategoryId;
                    }
                } else {
                    errMsg = "sub category cannot be empty";
                }
            } else {
                errMsg = "quantity must be positive integer";
            }
        }
        throw new InvalidValueException("Inventory " + errMsg);
    }

    public InventoryModel update(int id, int quantity) {
        InventoryDao dao = repo.findById(id).orElse(null);
        if (dao != null) {
            if (dao.getQuantity() != quantity) {
                dao.setQuantity(Math.max(0, quantity));
                dao = repo.save(dao);
            }
            return InventoryDataMapper.dataConversion(dao);
        }

        throw new MissingRecordException("Unable to find inventory with id " + id);
    }
}
