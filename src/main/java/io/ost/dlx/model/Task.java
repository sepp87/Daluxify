package io.ost.dlx.model;

import io.ost.dlx.ListResponse;

/**
 *
 * @author Joost
 */
public class Task implements Identifiable {

    public String taskId;
    public String subject;
    public String usage;
    public Type type;
    public String number;
    public String created;
    public UserRelation createdBy;
    public Workflow workflow;
    public Location location;
    public ListResponse<UserDefinedField> userDefinedFields;

    public class Type {

        public String typeId;
        public String name;
    }

    public class Workflow {

        public String name;
    }
}
