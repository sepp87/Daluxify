package dlx.client.model;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Joost
 */
public class File  implements Nameable, Identifiable, Serializable {
    public String fileId;
    public String fileRevisionId;
    public String fileName;
    public String fileAreaId;
    public String folderId;
    public String uploadedByUserId;
    public String uploaded;
    public String lastModifiedByUserId;
    public String lastModified;
    public String version;
    public boolean deleted;
    public String fileType;
    public long fileSize;
    public String contentHash;
    public String downloadLink;
    public List<Property> properties;
    
    public class Property {
        public String name;
        public String value;
    }
    
            
}
