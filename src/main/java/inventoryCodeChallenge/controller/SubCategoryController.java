package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.config.Constants;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.RequestResponse;
import inventoryCodeChallenge.model.SubCategoryModel;
import inventoryCodeChallenge.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "subcategory", produces = Constants.MEDIA_TYPE_JSON, consumes = Constants.MEDIA_TYPE_JSON)
public class SubCategoryController {

    @Autowired
    private SubCategoryService service;

    @GetMapping(path = "/", consumes = "*/*")
    public List<SubCategoryModel> getAll() {
        return service.getSubCategories();
    }

    @PostMapping
    public RequestResponse<SubCategoryModel> insert(@Valid @RequestBody SubCategoryModel model) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.insert(model));
    }

    @ExceptionHandler({MissingRecordException.class})
    public RequestResponse validationError(Exception e) {
        return new RequestResponse(RequestResponse.State.ERROR, e.getMessage(), null);
    }
}
