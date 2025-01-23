package dlx.client.model;

import java.io.Serializable;

/**
 * Actually called FormAttachmentRelation
 * 
 * @author Joost
 */
public class FormAttachment implements Serializable{
    public String attachmentId;
    public String modified;
    public String created;
    public String formId;
    public UserDefinedField userDefinedField;
    public TaskAttachment attachmentData; // Actually a FormAttachment object, but it has the same schema as TaskAttachment
    public boolean deleted;
            
}
