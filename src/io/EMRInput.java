package io;

import java.io.*;

/**
 * Created by huang.tudou on 5/9/16.
 */
public class EMRInput {
    private String fileName = null;

    public EMRInput(String fileName) {
        this.fileName = fileName;
    }

    public String EMRDataReader() {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer strBuffer = new StringBuffer();

        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(read);
            String line = null;

            while((line = reader.readLine()) != null) {
                strBuffer.append(line + "\n");
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {}
            }
        }

        return strBuffer.toString();
    }
}
