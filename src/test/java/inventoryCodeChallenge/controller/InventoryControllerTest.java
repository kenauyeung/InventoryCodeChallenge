package inventoryCodeChallenge.controller;

import inventoryCodeChallenge.BaseTest;
import inventoryCodeChallenge.dao.CategoryDao;
import inventoryCodeChallenge.dao.InventoryDao;
import inventoryCodeChallenge.dao.SubCategoryDao;
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

import java.util.Arrays;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@SpringBootTest
class InventoryControllerTest extends BaseTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private final String URL_PATH = "/inventory/";

    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getAll_containNoInventoryRecord_returnJsonWithSuccessAndEmptyInventory() throws Exception {
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
    public void getAll_containsInventoryRecord_returnJsonWithSuccessAndInventory() throws Exception {
        int quantity = 99;
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));
        InventoryDao inventory = inventoryRepo.save(new InventoryDao(null, "inventory1", quantity, Arrays.asList(subCategory1)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(inventory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(inventory.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantity").value(quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].categories[0].id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].categories[0].name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].categories[0].subCategories[0].name").value(subCategory1.getName()));
    }

    @Test
    public void getInventory_withExistInventoryId_returnJsonWithSuccessAndInventory() throws Exception {
        int quantity = 99;
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));
        InventoryDao inventory = inventoryRepo.save(new InventoryDao(null, "inventory1", quantity, Arrays.asList(subCategory1)));

        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + inventory.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(inventory.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(inventory.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].name").value(subCategory1.getName()));
    }

    @Test
    public void getInventory_withNotExistInventoryId_returnJsonWithSuccessAndEmptyData() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(URL_PATH + "9999"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty());
    }

    @Test
    public void insert_validInventoryInfo_returnJsonWithNewlyCreatedInventory() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[" + subCategory1.getId() + "]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(inventoryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].name").value(subCategory1.getName()));
    }

    @Test
    public void insert_validMultipleSubCategoryWithSameCategory_returnJsonWithNewlyCreatedInventory() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));
        SubCategoryDao subCategory2 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory2", category));

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[" + subCategory1.getId() + "," + subCategory2.getId() + "]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(inventoryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].name").value(subCategory1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[1].id").value(subCategory2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[1].name").value(subCategory2.getName()));
    }

    @Test
    public void insert_validMultipleSubCategoryWithDifferentCategory_returnJsonWithNewlyCreatedInventory() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        CategoryDao category2 = categoryRepo.save(new CategoryDao(null, "Category2"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category1));
        SubCategoryDao subCategory2 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory2", category2));

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[" + subCategory1.getId() + "," + subCategory2.getId() + "]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(inventoryName))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(quantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].id").value(category1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].name").value(category1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].name").value(subCategory1.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[1].id").value(category2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[1].name").value(category2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[1].subCategories[0].id").value(subCategory2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[1].subCategories[0].name").value(subCategory2.getName()));
    }

    @Test
    public void insert_withNotExistSubCategories_returnJsonWithErrorState() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[1]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unable to retrieve sub-category with id 1"));
    }

    @Test
    public void insert_withOneOfSubCategoryNotExist_returnJsonWithErrorState() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        CategoryDao category1 = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category1));
        int notExistSubCategory = subCategory1.getId() + 1;

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[" + subCategory1.getId() + "," + notExistSubCategory + "]}";
        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unable to retrieve sub-category with id " + notExistSubCategory));
    }

    @Test
    public void insert_withEmptyJson_returnBadRequest() throws Exception {
        String json = "";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void insert_withNoInventoryData_returnBadRequest() throws Exception {
        String json = "";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    public void insert_withNegativeQuantity_returnJsonWithErrorState() throws Exception {

        String inventoryName = "inventory1";
        int quantity = -999;

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[1]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[inventoryInsertModel::quantity::Min]"));
    }

    @Test
    public void insert_withEmptySubCategories_returnJsonWithErrorState() throws Exception {
        String inventoryName = "inventory1";
        int quantity = 999;

        String json = "{\"name\":\"" + inventoryName + "\",\"quantity\":" + quantity + ",\"subCategories\":[]}";

        this.mockMvc.perform(MockMvcRequestBuilders.post(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[inventoryInsertModel::subCategories::NotEmpty]"));
    }


    @Test
    public void update_validInventoryWithInfo_returnJsonWithUpdatedInventory() throws Exception {

        int initialQuantity = 999;
        int updateQuantity = 5000;
        CategoryDao category = categoryRepo.save(new CategoryDao(null, "Category1"));
        SubCategoryDao subCategory1 = subCategoryRepo.save(new SubCategoryDao(null, "subCategory1", category));
        InventoryDao inventory = inventoryRepo.save(new InventoryDao(null, "inventory1", initialQuantity, Arrays.asList(subCategory1)));

        String json = "{\"id\":" + inventory.getId() + ",\"quantity\":" + updateQuantity + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("SUCCESS"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(""))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value(inventory.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.quantity").value(updateQuantity))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].id").value(category.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].name").value(category.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].id").value(subCategory1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.categories[0].subCategories[0].name").value(subCategory1.getName()));
    }

    @Test
    public void update_notExistInventoryId_returnJsonWithErrorState() throws Exception {
        int updateQuantity = 5000;
        int id = 9999;
        String json = "{\"id\":" + id + ",\"quantity\":" + updateQuantity + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Unable to find inventory with id " + id));
    }

    @Test
    public void update_inventoryWithNegativeQuantity_returnJsonWithErrorState() throws Exception {
        int updateQuantity = -5000;
        int id = 9999;
        String json = "{\"id\":" + id + ",\"quantity\":" + updateQuantity + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[inventoryUpdateModel::quantity::Min]"));
    }

    @Test
    public void update_inventoryWithBlankInventoryId_returnJsonWithErrorState() throws Exception {
        int updateQuantity = 5000;
        String json = "{\"quantity\":" + updateQuantity + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[inventoryUpdateModel::id::NotNull]"));
    }

    @Test
    public void update_inventoryWithBlankQuantity_returnJsonWithErrorState() throws Exception {
        int id = 5000;
        String json = "{\"id\":" + id + "}";

        this.mockMvc.perform(MockMvcRequestBuilders.put(URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.state").value("ERROR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("[inventoryUpdateModel::quantity::Min]"));
    }
}