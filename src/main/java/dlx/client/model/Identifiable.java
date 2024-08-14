package dlx.client.model;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public interface Identifiable {

    default String getId() {
        Field[] fields = this.getClass().getFields();
        Field field = null;
        for (Field candidate : fields) {
            String fieldName = (this.getClass().getSimpleName() + "id").toLowerCase();
            if (candidate.getName().toLowerCase().equals(fieldName)) {
                field = candidate;
                break;
            }
        }
        if (field == null) {
            return "";
        }
        try {
            return (String) field.get(this);
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Nameable.class.getName()).log(Level.SEVERE, null, ex);
        }
        return "";
    }

    public static <T extends Identifiable> T filterListById(List<T> list, String id) {

        for (T candidate : list) {
            if (candidate.getId().equals(id)) {
                return candidate;
            }
        }
        return null;
    }

    public static <T extends Identifiable> Map<String, T> getAsMap(List<T> list) {
        Map<String, T> map = new TreeMap<>();
        for (T t : list) {
            map.put(t.getId(), t);
        }
        return map;
    }
}
