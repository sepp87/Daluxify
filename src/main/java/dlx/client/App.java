package dlx.client;

import dlx.client.api.Companies;
import dlx.client.api.FileAreas;
import dlx.client.api.FileUpload;
import dlx.client.api.Files;
import dlx.client.api.Folders;
import dlx.client.api.Forms;
import dlx.client.api.Projects;
import dlx.client.api.Tasks;
import dlx.client.api.Users;
import dlx.client.api.VersionSets;
import dlx.client.api.Workpackages;
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
import dlx.client.model.Workpackage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;

/**
 *
 * @author Joost
 */
public class App {

    static final CountDownLatch latch = new CountDownLatch(1);
    static final PendingCalls pendingCalls = new PendingCalls();

    public static void main(String[] args) throws IOException, InterruptedException {
        ApiClient client = new ApiClient(Config.get().getApiKey(), Config.get().getBaseUrl());

        System.out.println("PENDING CALLS " + pendingCalls.get());
        if (pendingCalls.get() > 0) {
            System.out.println("AWAITING RESPONSES");
            latch.await();
        }
        System.out.println("DONE");
    }

    public static PendingCalls pendingCallsProperty() {
        return pendingCalls;
    }

    public static void awaitData() throws InterruptedException {
        if (dlx.client.App.pendingCalls.get() > 0) {
            dlx.client.App.latch.await();
        }
    }

    public static void testGetAll(ApiClient client) {

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
            List<Workpackage> workPackages = Workpackages.getWorkpackages(p, client);
            List<Form> forms = Forms.getForms(p, client);

            // Dalux Box
            List<FileArea> fileAreas = FileAreas.getFileAreas(p, client);
            List<File> files = Files.getFiles(p, fileAreas.get(0), client);
            List<Folder> folders = Folders.getFolders(p, fileAreas.get(0), client);
            List<VersionSet> versionSets = VersionSets.getVersionSets(p, client);
            List<VersionSet> fileAreaVersionSets = VersionSets.getVersionSets(p, fileAreas.get(0), client);
        }
    }

    public static void testUploadFile(ApiClient client) throws IOException {
        List<Project> candidates = Projects.getProjects(client);
        Project project = Nameable.filterListByNames(candidates, Config.get().getProjectNames()).get(0);

        // Dalux Box
        List<FileArea> fileAreas = FileAreas.getFileAreas(project, client);
        FileArea files = null;
        for (FileArea fileArea : fileAreas) {
            if (fileArea.fileAreaType.equals("files")) {
                files = fileArea;
            }
        }
        if (files == null) {
            return;
        }

        Folder folder = Folders.getFolders(project, files, client).get(0);
        String uploadGuid = FileUpload.createUpload(project, files, client);
        java.io.File pdf = new java.io.File("example.pdf");

        boolean isSuccessfull = FileUpload.uploadFile(project, files, uploadGuid, pdf, client);

        if (isSuccessfull) {
            // Only the properties fileId, folderId, properties, fileType and fileName are considered in the metadata.
            dlx.client.model.File file = new dlx.client.model.File();
            file.fileName = pdf.getName();
            file.folderId = folder.folderId;
            file.fileType = "document";
            file.fileSize = java.nio.file.Files.size(pdf.toPath());

            FileUpload.finishUpload(project, files, uploadGuid, file, client);
        }
    }

    public static void testGetFormsAsync(ApiClient client) throws InterruptedException {
        List<AsyncResponse<Form>> result = new ArrayList<>();

        List<Project> projects = Projects.getProjects(client);
        for (Project p : projects) {
//            Tasks.getTaskChanges(p, client);
//            Tasks.getTaskChangesAsync(p, client);
            result.add(Forms.getFormsAsync(p, client));
        }

        System.out.println("PENDING CALLS " + pendingCalls.get());
        if (pendingCalls.get() > 0) {
            System.out.println("AWAITING RESPONSES");
            latch.await();
        }

        Map<String, Integer> stats = new TreeMap<>();
        for (AsyncResponse<Form> r : result) {
            for (Form f : r.getResult()) {
                if (f.status == null) {
                    System.out.println(f.number);
                    continue;
                }
                if (stats.containsKey(f.status)) {
                    int count = stats.get(f.status) + 1;
                    stats.put(f.status, count);
                } else {
                    stats.put(f.status, 1);
                }
            }
        }
        System.out.println(stats);
    }

    public static void testFilterProjects(ApiClient client) {
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());
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
