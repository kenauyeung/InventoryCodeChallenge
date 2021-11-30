package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
import inventoryCodeChallenge.repository.CategoryRepository;
import inventoryCodeChallenge.repository.InventoryRepository;
import inventoryCodeChallenge.repository.InventorySubCategoryRepository;
import inventoryCodeChallenge.repository.SubCategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class SubCategoryControllerTest {
    @Autowired
    private InventoryRepository inventoryRepo;

    @Autowired
    private SubCategoryRepository subCategoryRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @Autowired
    private InventorySubCategoryRepository invSubCategoryRepo;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final String URL_PATH = "/subcategory/";

    @BeforeEach
    public void beforeEach() {
        invSubCategoryRepo.deleteAll();
        inventoryRepo.deleteAll();
        subCategoryRepo.deleteAll();
        categoryRepo.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    public void getAll_containNoSubCategoryRecord_returnJsonWithSuccessAndEmptyCategory() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());

    }

    @Test
    public void getAll_containsSubCategoryRecord_returnJsonWithSuccessAndCategory() throws Exception {
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(subCategory1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category.name").value(category.getName()));
    }

    @Test
    public void insert_validSubCategoryInfoWithoutId_returnNewlyCreatedCategory() throws Exception {
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "category1"));
        String path = URL_PATH + category.getId();
        String subCategoryName = "sub-category1";
        String json = "{\"name\":\"" + subCategoryName + "\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(subCategoryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category.name").value(category.getName()));
    }

    @Test
    public void insert_validCategoryInfoWithId_returnNewlyCreatedCategory() throws Exception {
        int id = 99999;
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "category1"));
        String path = URL_PATH + category.getId();
        String subCategoryName = "sub-category1";
        String json = "{\"id\":" + id + ", \"name\":\"" + subCategoryName + "\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(subCategoryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category.id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.category.name").value(category.getName()));
    }

    @Test
    public void insert_missingSubCategoryName_returnErrorState() throws Exception {
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "category1"));
        String path = URL_PATH + category.getId();

        String json = "{}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[subCategoryModel::name::NotBlank]"));
    }

    @Test
    public void insert_withEmptySubCategoryName_returnErrorState() throws Exception {
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "category1"));
        String path = URL_PATH + category.getId();

        String json = "{\"name\":\"\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[subCategoryModel::name::NotBlank]"));
    }

    @Test
    public void insert_withEmptyCategoryId_returnErrorState() throws Exception {
        String path = URL_PATH;

        String json = "{\"name\":\"\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void insert_withNegativeCategoryId_returnErrorState() throws Exception {
        String path = URL_PATH + "-1";

        String json = "{\"name\":\"\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[subCategoryModel::name::NotBlank]"));
    }

    @Test
    public void insert_withNotExistCategoryId_returnErrorState() throws Exception {
        int categoryId = 999;
        String path = URL_PATH + categoryId;
        String subCategoryName = "sub-category1";
        String json = "{\"name\":\"" + subCategoryName + "\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unable to find category with id " + categoryId));
    }
}