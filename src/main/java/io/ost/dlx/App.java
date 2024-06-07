package io.ost.dlx;

import io.ost.dlx.api.Task;
import io.ost.dlx.solutions.TaskAttachmentsDownloader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        List<Task> list = getList(Task.class);
    }


    // https://stackoverflow.com/questions/17995776/writing-a-function-that-needs-class-class-as-a-parameter-in-java?rq=3
    public static <T> List<T> getList(Class<T> type) {
        List<T> result = new ArrayList<>();
        return result;

    }
}
