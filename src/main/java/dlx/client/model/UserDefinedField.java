package dlx.client.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class UserDefinedField  implements Nameable {

    public String key;
    public String set;
    public String name;
    public String description;
    public List<UserDefinedFieldValue> values;

    public class UserDefinedFieldValue {

        public String text;
        public String date;
        public String datetime;
        public double number;
        public int integer;

        public class Reference {

            public String key;
            public String value;
        }

        public class Relation {

            public String userId;
            public String companyId;
        }
    }
}
