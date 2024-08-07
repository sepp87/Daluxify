package io.ost.dlx.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class ProjectMetadata implements Nameable {

    public String key;
    public String name;
    public List<ProjectMetadataValue> values;

    public class ProjectMetadataValue {

        public String text;
        public ObjectReference reference;

        public class ObjectReference {

            public String key;
            public String value;
        }
    }
}
