package dlx.client.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class Project implements Nameable, Identifiable {

    public String projectId;
    public String projectName;
    public String type;
    public String projectTemplateId;
    public String address;
    public String number;
    public String created;
    public String closing;
    public List<Module> modules;
    public List<ProjectMetadata> metadata;

    public class Module {

        public String type;
        public String tier;
        public String restriction;
    }
}
