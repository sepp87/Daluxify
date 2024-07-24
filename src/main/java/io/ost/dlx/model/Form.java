package io.ost.dlx.model;

import io.ost.dlx.ObjectResponse;

/**
 *
 * @author Joost
 */
public class Form {
    
    public String formId;
    public String type;
    public String number;
    public TemplateReference template;
    public String status;
    public String workpackageId;
    public String created;
    public UserRelation createdBy;
    public String modified;
    public UserRelation modifiedBy;
    public boolean deleted;
    public Location location;
    public ObjectResponse<UserDefinedField> userDefinedFields;
    
}
