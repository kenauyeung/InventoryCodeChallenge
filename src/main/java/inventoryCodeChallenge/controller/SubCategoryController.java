package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.config.Constants;
import inventoryCodeChallenge.exception.MissingRecordException;
import inventoryCodeChallenge.model.CategoryModel;
import inventoryCodeChallenge.model.RequestResponse;
import inventoryCodeChallenge.model.SubCategoryModel;
import inventoryCodeChallenge.service.SubCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "subcategory", produces = Constants.MEDIA_TYPE_JSON, consumes = Constants.MEDIA_TYPE_JSON)
class SubCategoryController {

    @Autowired
    private SubCategoryService service;

    @GetMapping(path = "/", consumes = "*/*")
    public RequestResponse<List<SubCategoryModel>> getAll() {
        return new RequestResponse(RequestResponse.State.SUCCESS, null,service.getSubCategories());
    }

    @PostMapping(path = "/{categoryId}")
    public RequestResponse<SubCategoryModel> insert(@Valid @RequestBody SubCategoryModel model, @Min(0) @PathVariable Integer categoryId) {
        model.setCategory(new CategoryModel(categoryId, null));
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.insertSubCategory(model));
    }

    @ExceptionHandler({MissingRecordException.class})
    public RequestResponse validationError(Exception e) {
        return new RequestResponse(RequestResponse.State.ERROR, e.getMessage(), null);
    }
}
