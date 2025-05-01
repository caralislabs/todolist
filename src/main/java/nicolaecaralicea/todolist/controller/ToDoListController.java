package nicolaecaralicea.todolist.controller;

import com.github.fge.jsonpatch.JsonPatch;
import nicolaecaralicea.todolist.datamodel.ToDoItem;
import nicolaecaralicea.todolist.service.ToDoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The ToDoListController class provides endpoints for managing ToDo resources.
 */
@RestController
@RequestMapping("/todos")
public class ToDoListController {

    private final ToDoService toDoService;

    public ToDoListController(ToDoService toDoService) {
        this.toDoService = toDoService;
    }

    @GetMapping
    public ResponseEntity<List<ToDoItem>> getItems() {
        return ResponseEntity.ok(toDoService.getItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ToDoItem>> findItem(@PathVariable("id") int id) {
        return ResponseEntity.ok(toDoService.getItemById(id));
    }

    @PatchMapping(value = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<ToDoItem> patchItem(@RequestBody JsonPatch patch, @PathVariable("id") int id) {
        ToDoItem patched = toDoService.applyPatchToItemById(id, patch);
        return ResponseEntity.ok(patched);
    }

    @PostMapping
    public ResponseEntity<ToDoItem> createItem(@RequestBody ToDoItem toDoItem) {
        ToDoItem createdItem = toDoService.createItem(toDoItem);
        return ResponseEntity.ok(createdItem);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable("id") int id) {
        toDoService.deleteItemById(id);
        // No redo operation is expected to be done by the endpoint caller so we return HTTP 204 and no content.
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
