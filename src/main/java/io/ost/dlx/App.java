package io.ost.dlx;

import io.ost.dlx.api.Companies;
import io.ost.dlx.api.FileAreas;
import io.ost.dlx.api.Files;
import io.ost.dlx.api.Folders;
import io.ost.dlx.api.Forms;
import io.ost.dlx.api.Projects;
import io.ost.dlx.api.Tasks;
import io.ost.dlx.api.Users;
import io.ost.dlx.api.VersionSets;
import io.ost.dlx.api.WorkPackages;
import io.ost.dlx.model.TaskAttachment;
import io.ost.dlx.model.Company;
import io.ost.dlx.model.File;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Folder;
import io.ost.dlx.model.Form;
import io.ost.dlx.model.Nameable;
import io.ost.dlx.model.Project;
import io.ost.dlx.model.Task;
import io.ost.dlx.model.TaskChange;
import io.ost.dlx.model.User;
import io.ost.dlx.model.VersionSet;
import io.ost.dlx.model.WorkPackage;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Joost
 */
public class App {

    public static final boolean LOG = true;

    public static void main(String[] args) throws IOException {
        ApiClient client = new ApiClient(Config.get().getApiKey(), Config.get().getBaseUrl());
        
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());
        
        for (Project p : projects) {
            List<FileArea> fileAreas = FileAreas.getFileAreas(p, client);
            List<File> files = Files.getFiles(p, fileAreas.get(0), client);
            List<TaskChange> changes = Tasks.getTaskChanges(p, client);
        }

//        getAll(client);
//        FileUploadActivity.getActivity(client);
    }

    public static void getAll(ApiClient client) {
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());
        
        for (Project p : projects) {
            // Dalux Common
            List<User> users = Users.getUsers(p, client);
            List<Company> companies = Companies.getCompanies(p, client);

            // Dalux Field
            List<Task> tasks = Tasks.getTasks(p, client);
            List<TaskChange> changes = Tasks.getTaskChanges(p, client);
            List<TaskAttachment> attachments = Tasks.getTaskAttachments(p, client);
            List<WorkPackage> workPackages = WorkPackages.getWorkPackages(p, client);
            List<Form> forms = Forms.getForms(p, client);

            // Dalux Box
            List<FileArea> fileAreas = FileAreas.getFileAreas(p, client);
            List<File> files = Files.getFiles(p, fileAreas.get(0), client);
            List<Folder> folders = Folders.getFolders(p, fileAreas.get(0), client);
            List<VersionSet> versionSets = VersionSets.getVersionSets(p, client);
            List<VersionSet> fileAreaVersionSets = VersionSets.getVersionSets(p, fileAreas.get(0), client);
        }
    }



}

// Projects                 ListResponse > ObjectResponse 
// Users                    ListResponse 
// Companies                ListResponse 
// Work Packages            ListResponse 
// File Areas               ListResponse > ObjectResponse 
// Folders                  ListResponse > ObjectResponse 
// Version Sets             ListResponse > ObjectResponse 
// Files                    ListResponse > ObjectResponse 
// File Revisions 
// Inspection Plans         ListResponse 
// Test Plans               ListResponse 
// Forms                    ListResponse > ObjectResponse 
// Project Templates        ListResponse 
// Company Catalog          ListResponse > ObjectResponse 
// Tasks                    ListResponse > ObjectResponse 
// Attachments              ListResponse
