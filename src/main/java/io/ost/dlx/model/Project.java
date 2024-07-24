package io.ost.dlx.model;

import java.util.List;

/**
 *
 * @author Joost
 */
public class Project {

    public String projectId;
    public String projectName;
    public String type;
    public String projectTemplateId;
    public String address;
    public String number;
    public String created;
    public String closed;
    public List<Module> modules;

    public class Module {

        public String type;
        public String tier;
        public String restriction;
    }
}
