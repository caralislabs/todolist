package nicolaecaralicea.todolist.datapatching;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.stereotype.Component;

/**
 *
 * DataPatcher is a helper class used to apply patches on objects.
 *
 */
@Component
public class DataPatcher {

    private final ObjectMapper objectMapper;

    public DataPatcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public <T> T applyPatch(JsonPatch patch, T target, Class<T> targetClass) {
        try {
            JsonNode patched = patch.apply(objectMapper.convertValue(target, JsonNode.class));
            return objectMapper.treeToValue(patched, targetClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
