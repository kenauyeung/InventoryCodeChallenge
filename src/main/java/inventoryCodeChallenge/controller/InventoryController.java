package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.config.Constants;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.*;
import inventoryCodeChallenge.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "inventory", produces = Constants.MEDIA_TYPE_JSON, consumes = Constants.MEDIA_TYPE_JSON)
public class InventoryController {

    @Autowired
    private InventoryService service;

    @GetMapping(path = "/", consumes = "*/*")
    public List<InventoryModel> getAll() {
        return service.getInventories();
    }

    @GetMapping(path = "/{id}", consumes = "*/*")
    public InventoryModel getInventory(@PathVariable @Min(0) @NotNull Integer id) {
        return service.getInventories(id);
    }

    @PostMapping
    public RequestResponse<InventoryModel> insert(@Valid @RequestBody InventoryInsertModel model) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.insert(model));
    }

    @PutMapping(path = "/{id}/{quantity}", consumes = "*/*")
    public RequestResponse<InventoryModel> update(@PathVariable @Min(0) @NotNull Integer id, @PathVariable @Min(0) @NotNull Integer quantity) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.update(id, quantity));
    }

    @ExceptionHandler({MissingRecordException.class})
    public RequestResponse validationError(Exception e) {
        return new RequestResponse(RequestResponse.State.ERROR, e.getMessage(), null);
    }
}
