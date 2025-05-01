package nicolaecaralicea.todolist.controller;

import nicolaecaralicea.todolist.datamodel.ToDoItem;
import nicolaecaralicea.todolist.service.ToDoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class ToDoListControllerTest {

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoListController toDoListController;

    @Test
    void shouldReturnAllTodos() throws Exception {
        List<ToDoItem> expectedToDos =
                List.of(
                        ToDoItem.builder().id(1).description("Buy milk").completionStatus(1).build(),
                        ToDoItem.builder().id(1).description("Buy bread").completionStatus(2).build());
        Mockito.when(toDoService.getItems()).thenReturn(expectedToDos);

        List<ToDoItem> actualToDos = toDoListController.getItems().getBody();

        Assertions.assertIterableEquals(expectedToDos, actualToDos);
    }
}