package dlx.client;

import dlx.client.api.Companies;
import dlx.client.api.FileAreas;
import dlx.client.api.Files;
import dlx.client.api.Folders;
import dlx.client.api.Forms;
import dlx.client.api.Projects;
import dlx.client.api.Tasks;
import dlx.client.api.Users;
import dlx.client.api.VersionSets;
import dlx.client.api.WorkPackages;
import dlx.client.model.TaskAttachment;
import dlx.client.model.Company;
import dlx.client.model.File;
import dlx.client.model.FileArea;
import dlx.client.model.Folder;
import dlx.client.model.Form;
import dlx.client.model.Nameable;
import dlx.client.model.ObjectReference;
import dlx.client.model.Project;
import dlx.client.model.ProjectMetadata;
import dlx.client.model.ProjectMetadataValue;
import dlx.client.model.ProjectMetadataMapping;
import dlx.client.model.Task;
import dlx.client.model.TaskChange;
import dlx.client.model.User;
import dlx.client.model.VersionSet;
import dlx.client.model.WorkPackage;
import java.io.IOException;
import java.util.ArrayList;
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

        }

//        getAll(client);
    }

    public static void getAll(ApiClient client) {

        // Dalux Project Metadata
        List<ProjectMetadataMapping> mappingsToCreate = Projects.getProjectMetadataToCreate(client);
        for (ProjectMetadataMapping mapping : mappingsToCreate) {
            Projects.getProjectMetadataValuesToCreate(mapping, client);
        }

        // Dalux Projects
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());

        for (Project p : projects) {
            // Dalux Project Metadata
            List<ProjectMetadata> metadata = Projects.getProjectMetadata(p, client);
            List<ProjectMetadataMapping> mappingsToUpdate = Projects.getProjectMetadataToUpdate(p, client);
            for (ProjectMetadataMapping mapping : mappingsToUpdate) {
                Projects.getProjectMetadataValuesToUpdate(p, mapping, client);
            }

            // Dalux Project User Management
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

    public static void testPatchProject(ApiClient client, Project p) {
        Project body = new Project();
        body.projectId = p.projectId;
        body.number = p.number;
        body.address = p.address;
        body.projectName = p.projectName.equals("JME Rosenheimer 139") ? "JME Rosenheimer 139-2" : "JME Rosenheimer 139";

        body.metadata = new ArrayList<>();

        ProjectMetadata d1 = new ProjectMetadata();
        d1.key = "162303029384";
        d1.values = new ArrayList<>();
        ProjectMetadataValue businessArea = new ProjectMetadataValue();
        businessArea.reference = new ObjectReference();
//            value.reference.key = "0_1";
//            value.reference.value = "Schlüsselfertigbau";
        businessArea.reference.key = "0_2";
        businessArea.reference.value = "Rohbau";
        d1.values.add(businessArea);
        body.metadata.add(d1);

        ProjectMetadata d2 = new ProjectMetadata();
        d2.key = "160121722559";
        d2.values = new ArrayList<>();
        ProjectMetadataValue projectVolume = new ProjectMetadataValue();
        projectVolume.reference = new ObjectReference();
//            value.reference.key = "0_1";
//            value.reference.value = "0-1";
        projectVolume.reference.key = "0_2";
        projectVolume.reference.value = "1-10";
        d2.values.add(projectVolume);
        body.metadata.add(d2);
        Projects.updateProject(body, client);
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
