package nicolaecaralicea.todolist;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import nicolaecaralicea.todolist.controller.ToDoListController;
import nicolaecaralicea.todolist.dao.ToDoDao;
import nicolaecaralicea.todolist.datamodel.ToDoItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTests {

    @Autowired
    private ToDoDao toDoDao;

    @Autowired
    private ToDoListController toDoListController;

    @Autowired
    private ObjectMapper objectMapper;

    // TODO: Break this integration test into smaller cases.
    //  It currently combines several REST operations and validates the resulting DB state.
    @Test
    void shouldHaveDatabaseInConsistentStateAfterMultipleRestCalls() {
        // 1 - test creating todo items
        List<ToDoItem> expectedToDos =
                List.of(
                        ToDoItem.builder().id(0).description("Buy milk").completionStatus(0).build(),
                        ToDoItem.builder().id(1).description("Buy bread").completionStatus(0).build());

        expectedToDos.forEach(toDoItem -> toDoDao.insert(toDoItem));

        List<ToDoItem> actualToDos = toDoListController.getItems().getBody();

        assertIterableEquals(expectedToDos, actualToDos);

        // 2 - test patching and looking up todo items
        String expectedDescription = "Buy car";
        JsonPatch patch = createReplacementPatch("/description", expectedDescription);
        toDoListController.patchItem(patch, 1);
        ToDoItem toDoItem = toDoListController.findItem(1).getBody().get();
        assertEquals(expectedDescription, toDoItem.getDescription());

        // 3 - test deleting todo items
        toDoListController.deleteItem(1);
        Optional<ToDoItem> optToDoItem = toDoListController.findItem(1).getBody();
        assertTrue(optToDoItem.isEmpty());

        // 4 - trying to delete a non-existent item should throw exception
        assertThrows(RuntimeException.class, () -> toDoListController.deleteItem(1));

    }

    private JsonPatch createReplacementPatch(String path, String value) {
        try {
            ObjectNode node = objectMapper.createObjectNode();
            node.put("op", "replace");
            node.put("path", path);
            node.put("value", value);
            String patchString = node.toString();
            // sample of patch for replacing the values of the description field:
            //  [{"op":"replace","path":"/description","value":"updated description.."}]
            JsonNode patch = objectMapper.readTree("[" + patchString + "]");
            return JsonPatch.fromJson(patch);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
