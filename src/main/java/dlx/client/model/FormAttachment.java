package dlx.client.model;

/**
 * Actually called FormAttachmentRelation
 * 
 * @author Joost
 */
public class FormAttachment {
    public String attachmentId;
    public String modified;
    public String formId;
    public UserDefinedField userDefinedField;
    public TaskAttachment attachmentData; // Actually a FormAttachment object, but it has the same schema as TaskAttachment
    public boolean deleted;
            
}
