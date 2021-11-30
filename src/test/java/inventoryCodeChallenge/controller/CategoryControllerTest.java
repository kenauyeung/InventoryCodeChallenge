package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.dao.CategoryDao;
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
class CategoryControllerTest {
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

    private final String URL_PATH = "/category/";
    
    @BeforeEach
    public void beforeEach() {
        invSubCategoryRepo.deleteAll();
        inventoryRepo.deleteAll();
        subCategoryRepo.deleteAll();
        categoryRepo.deleteAll();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getAll_containNoCategoryRecord_returnJsonWithSuccessAndEmptyCategory() throws Exception {
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
    public void getAll_containsCategoryRecord_returnJsonWithSuccessAndCategory() throws Exception {
        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "category1"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(category1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(category1.getName()));
    }

    @Test
    public void insert_validCategoryInfoWithoutId_returnNewlyCreatedCategory() throws Exception {
        String categoryName = "category1";
        String json = "{\"name\":\"" + categoryName + "\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(categoryName));
    }

    @Test
    public void insert_validCategoryInfoWithId_returnNewlyCreatedCategory() throws Exception {
        int id = 99999;
        String categoryName = "category1";
        String json = "{\"id\":" + id + ", \"name\":\"" + categoryName + "\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.not(id)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(categoryName));
    }

    @Test
    public void insert_missingCategoryName_returnErrorState() throws Exception {
        String json = "{}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[categoryModel::name::NotBlank]"));
    }

    @Test
    public void insert_withEmptyCategoryName_returnErrorState() throws Exception {
        String json = "{\"name\":\"\"}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[categoryModel::name::NotBlank]"));
    }
}