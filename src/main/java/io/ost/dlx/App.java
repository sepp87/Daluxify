package io.ost.dlx;

import io.ost.dlx.solutions.TaskAttachmentsDownloader;
import java.io.IOException;

/**
 *
 * @author Joost
 */
public class App {

    public static void main(String[] args) throws IOException {

        TaskAttachmentsDownloader.download();

//        List<Project> projects = getSelectedProjects();
//        for (Project p: projects) {
//            List<Task> tasks = Task.getTasks(p);
//        }
    }


}
