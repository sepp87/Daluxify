package io.ost.dlx.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Joost
 */
public interface Nameable {

    public default String getName() {
        Field[] fields = this.getClass().getFields();
        Field field = null;
        for (Field candidate : fields) {
            if (candidate.getName().toLowerCase().endsWith("name")) {
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

    public static <T extends Nameable> List<T> filterListByNames(List<T> list, List<String> names) {
        if (names.isEmpty()) {
            return List.copyOf(list);
        }

        List<T> result = new ArrayList<>();
        for (T candidate : list) {
            if (names.contains(candidate.getName())) {
                result.add(candidate);
            }
        }
        return result;
    }
}
