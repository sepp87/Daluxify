package io.ost.dlx.solutions;

import io.ost.dlx.api.FileAreas;
import io.ost.dlx.api.Files;
import io.ost.dlx.api.Projects;
import io.ost.dlx.model.File;
import io.ost.dlx.model.FileArea;
import io.ost.dlx.model.Project;
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

    public static void getActivity() {
        List<Project> projects = Projects.getSelectedProjects();
        for (Project p : projects) {
            LocalDate projectStart = LocalDate.parse(p.created.split("T")[0]);
            List<FileArea> fileAreas = FileAreas.getFileAreas(p);
            List<File> files = Files.getFiles(p, fileAreas.get(0));
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
