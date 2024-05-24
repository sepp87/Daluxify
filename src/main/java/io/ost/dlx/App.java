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
    }


}
