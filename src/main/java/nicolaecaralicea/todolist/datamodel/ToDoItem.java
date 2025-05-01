package nicolaecaralicea.todolist.datamodel;

import lombok.Builder;
import lombok.Data;

/**
 * ToDoItem class is used for both transferring to/from rest endpoints and by the data access layer (not recommended)
 * This is a anti-pattern and I used it for simplicity only.
 *
 * TODO:
 *      - Create 2 classes instead of this. These classes could be ToDoItemDto and ToDoItemEntity.
 *        Mapping between those 2 classes could be done be using MapStruct, etc
 *      - the id property should not be exposed outside, and its values should be handled by the Data Access Layer
 *        (with autoincrement).
 *
 */
@Builder
@Data
public class ToDoItem {

    private int id;

    private String description;

    private int completionStatus;

}
