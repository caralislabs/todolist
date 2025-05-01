package nicolaecaralicea.todolist.service;

import com.github.fge.jsonpatch.JsonPatch;
import nicolaecaralicea.todolist.datamodel.ToDoItem;
import nicolaecaralicea.todolist.datapatching.DataPatcher;
import nicolaecaralicea.todolist.dao.ToDoDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * ToDoService class provided service based methods for todo items
 * For simplicity the exception handling is minimal and is relying on throwing runtime exceptions.
 * TODO:
 *  Defining custom exceptions and using a Global Exception handler (by using ControllerAdvice) might be considered.
 */
@Service
public class ToDoService {

    private final ToDoDao toDoDao;

    private final DataPatcher dataPatcher;

    public ToDoService(ToDoDao toDoDao, DataPatcher dataPatcher) {
        this.toDoDao = toDoDao;
        this.dataPatcher = dataPatcher;
    }

    public List<ToDoItem> getItems() {
        return toDoDao.findAll();
    }

    public Optional<ToDoItem> getItemById(int id) {
        return toDoDao.find(id);
    }

    public ToDoItem applyPatchToItemById(int id, JsonPatch patch) {
        ToDoItem toUpdate = toDoDao.find(id).orElseThrow(
                () -> new RuntimeException("Invalid resource identifier"));
        ToDoItem updated = dataPatcher.applyPatch(patch, toUpdate, ToDoItem.class);
        int affectedEntries = toDoDao.update(updated);
        if (affectedEntries == 1) {
            return updated;
        } else {
            throw new RuntimeException("Unexpected number of affected DB entries by DB update operation");
        }
    }

    public ToDoItem createItem(ToDoItem toDoItem) {
        int affectedEntries = toDoDao.insert(toDoItem);
        if (affectedEntries == 1) {
            return toDoItem;
        } else {
            throw new RuntimeException("Unexpected number of affected DB entries by DB insert operation");
        }
    }

    public void deleteItemById(int id) {
        int affectedEntries = toDoDao.delete(id);
        if (affectedEntries != 1)
            throw new RuntimeException("Unexpected number of affected DB entries by DB delete operation");
    }
}


