package io.util;

import java.io.*;

/**
 * Created by huang.tudou on 5/19/16.
 */
public class LogWriter{
    private String filePath = null;
    private File logFile = null;

    public LogWriter(String filePath) {
        this.filePath = filePath;
        this.logFile = new File(filePath);
        try {
            FileUlitity.makeFileDirectory(this.filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void WriteLog(String content) {
        BufferedWriter log = null;

        try {
            log = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.logFile, true), "UTF-8"));
            log.write(content);
            log.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                log.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
