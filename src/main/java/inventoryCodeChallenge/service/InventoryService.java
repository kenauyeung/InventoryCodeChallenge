package inventoryCodeChallenge.service;

import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.exception.InvalidValueException;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.mapper.InventoryDataMapper;
import inventoryCodeChallenge.model.InventoryInsertModel;
import inventoryCodeChallenge.model.InventoryModel;
import inventoryCodeChallenge.model.InventoryUpdateModel;
import inventoryCodeChallenge.repository.InventoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    public InventoryModel getInventory(int inventoryId) {
        InventoryDao dao = repo.findById(inventoryId).orElse(null);
        return dao == null ? null : InventoryDataMapper.dataConversion(dao);
    }

    public InventoryModel insertInventory(InventoryInsertModel model) {
        String errMsg = "name cannot be blank";
        if (StringUtils.isNotBlank(model.getName())) {
            if (model.getQuantity() >= 0) {
                if (!CollectionUtils.isEmpty(model.getSubCategories())) {
                    Set<Integer> subIdSet = subCategoryRepo.findAllById(model.getSubCategories()).stream().map(SubCategoryDao::getId).collect(Collectors.toSet());
                    String unFoundSubCategoryId = model.getSubCategories().stream().filter(id -> !subIdSet.contains(id)).map(String::valueOf).collect(Collectors.joining(","));
                    if (StringUtils.isBlank(unFoundSubCategoryId)) {
                        InventoryDao dao = repo.save(InventoryDataMapper.dataConversion(model));

                        // workaround - above save does not load the category info, it must load it manually
                        dao.setSubCategories(subCategoryRepo.findAllById(dao.getSubCategories().stream().map(SubCategoryDao::getId).collect(Collectors.toList())));
                        return InventoryDataMapper.dataConversion(dao);
                    } else {
                        throw new MissingRecordException("Unable to retrieve sub-category with id " + unFoundSubCategoryId);
                    }
                } else {
                    errMsg = "sub-category cannot be empty";
                }
            } else {
                errMsg = "quantity must be positive integer";
            }
        }
        throw new InvalidValueException("Inventory " + errMsg);
    }

    public InventoryModel updateInventory(InventoryUpdateModel inventory) {
        if (inventory.getId() != null && inventory.getId() >= 0) {
            if (inventory.getQuantity() >= 0) {
                InventoryDao dao = repo.findById(inventory.getId()).orElse(null);
                if (dao != null) {
                    if (dao.getQuantity() != inventory.getQuantity()) {
                        dao.setQuantity(inventory.getQuantity());
                        dao = repo.save(dao);
                    }
                    return InventoryDataMapper.dataConversion(dao);
                }
                throw new MissingRecordException("Unable to find inventory with id " + inventory.getId());
            }
            throw new InvalidValueException("Inventory quantity must be positive integer");
        }
        throw new InvalidValueException("Inventory id must be positive integer");
    }
}
