package dlx.client.model;

import java.io.Serializable;

/**
 *
 * @author Joost
 */
public class Folder  implements Nameable, Identifiable, Serializable {

    public String folderId;
    public String folderName;
    public String parentFolderId;
}
