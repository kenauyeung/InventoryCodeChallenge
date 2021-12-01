package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.config.Constants;
import inventoryCodeChallenge.model.CategoryModel;
import inventoryCodeChallenge.model.RequestResponse;
import inventoryCodeChallenge.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "category", produces = Constants.MEDIA_TYPE_JSON, consumes = Constants.MEDIA_TYPE_JSON)
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping(consumes = "*/*")
    public RequestResponse<List<CategoryModel>> getAll() {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.getCategories());
    }

    @PostMapping
    public RequestResponse<CategoryModel> insert(@Valid @RequestBody CategoryModel model) {
        return new RequestResponse(RequestResponse.State.SUCCESS, null, service.insertCategory(model));
    }
}
