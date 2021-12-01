package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.config.Constants;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.InventoryInsertModel;
import inventoryCodeChallenge.model.InventoryModel;
import inventoryCodeChallenge.model.InventoryUpdateModel;
import inventoryCodeChallenge.model.RequestResponse;
import inventoryCodeChallenge.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "inventory", produces = Constants.MEDIA_TYPE_JSON, consumes = Constants.MEDIA_TYPE_JSON)
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping(consumes = "*/*")
    public RequestResponse<List<InventoryModel>> getAll() {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.getInventories());
    }

    @GetMapping(path = "/{inventoryId}", consumes = "*/*")
    public RequestResponse<InventoryModel> getInventory(@PathVariable int inventoryId) {
        InventoryModel result = inventoryId >= 0 ? service.getInventory(inventoryId) : null;
        return new RequestResponse(RequestResponse.State.SUCCESS, null, result);
    }

    @PostMapping
    public RequestResponse<InventoryModel> insert(@Valid @RequestBody InventoryInsertModel model) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.insertInventory(model));
    }

    @PutMapping
    public RequestResponse<InventoryModel> update(@Valid @RequestBody InventoryUpdateModel model) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.updateInventory(model));
    }

    @ExceptionHandler({MissingRecordException.class})
    public RequestResponse validationError(Exception e) {
        return new RequestResponse(RequestResponse.State.ERROR, e.getMessage(), null);
    }
}
