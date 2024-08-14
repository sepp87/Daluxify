package dlx.client.solutions;

import dlx.client.ApiClient;
import dlx.client.Config;
import dlx.client.api.FileAreas;
import dlx.client.api.Files;
import dlx.client.api.Projects;
import dlx.client.model.File;
import dlx.client.model.FileArea;
import dlx.client.model.Nameable;
import dlx.client.model.Project;
import java.time.LocalDate;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static java.time.temporal.ChronoUnit.MONTHS;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Joost
 */
public class FileUploadActivity {

    public static void getActivity(ApiClient client) {
        List<Project> candidates = Projects.getProjects(client);
        List<Project> projects = Nameable.filterListByNames(candidates, Config.get().getProjectNames());
        
        for (Project p : projects) {
            LocalDate projectStart = LocalDate.parse(p.created.split("T")[0]);
            List<FileArea> fileAreas = FileAreas.getFileAreas(p, client);
            List<File> files = Files.getFiles(p, fileAreas.get(0), client);
            System.out.println(files.size() + "\t files contained in " + p.projectName);
            Map<Long, Integer> daysDeltaToCount = new TreeMap<>();
            Map<Long, Integer> weeksDeltaToCount = new TreeMap<>();
            Map<Long, Integer> monthsDeltaToCount = new TreeMap<>();

            for (File f : files) {
                LocalDate fileUploaded = LocalDate.parse(f.uploaded.split("T")[0]);
                long d = DAYS.between(projectStart, fileUploaded);
                long w = WEEKS.between(projectStart, fileUploaded);
                long m = MONTHS.between(projectStart, fileUploaded);

                countDelta(d, daysDeltaToCount);
                countDelta(w, weeksDeltaToCount);
                countDelta(m, monthsDeltaToCount);
            }
            System.out.println(daysDeltaToCount);
            System.out.println(weeksDeltaToCount);
            System.out.println(monthsDeltaToCount);

        }
    }

    private static void countDelta(Long delta, Map<Long, Integer> deltaToCount) {
        if (deltaToCount.containsKey(delta)) {
            int count = deltaToCount.get(delta) + 1;
            deltaToCount.put(delta, count);
        } else {
            deltaToCount.put(delta, 1);
        }
    }
}
