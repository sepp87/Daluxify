package dlx.client.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class TaskChange {

    public String taskId;
    public String description;
    public String timestamp;
    public String action;
    public TaskChangeFields fields;

    public class TaskChangeFields {

        public String title;
        public String status;
        public AssignedTo assignedTo;
        public UserRelation currentResponsible;
        public UserRelation modifiedBy;
        public Location location;
        public String workpackageId;
        public Deadline deadline;
        public ListResponse<UserDefinedField> userDefinedFields;

        public class AssignedTo {

            public String roleId;
            public String roleName;
        }

        public class Deadline {

            public String value;
            public String empty;
        }

    }
}
