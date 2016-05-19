package io.util;

import java.io.*;

/**
 * Created by huang.tudou on 5/19/16.
 */
public class FileWriter {
    private String filePath = null;
    private StringBuffer stringBuff = null;
    private String content = null;

    public FileWriter(String filePath) {
        this.filePath = filePath;
        this.stringBuff = new StringBuffer();
    }

    public FileWriter(String filePath, StringBuffer sb) {
        this.filePath = filePath;
        this.stringBuff = sb;
    }

    public FileWriter(String filePath, String content) {
        this.filePath = filePath;
        this.content = content;
    }

    /********** write the entire file once **********/
    public void WriteEntireFile() {
        try {
            File file = new File(this.filePath);
            FileUlitity.makeFileDirectory(this.filePath);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

            if(this.content == null) {
                writer.write(this.stringBuff.toString());
            } else {
                writer.write(this.content);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void append(String inline) {
        this.stringBuff.append(inline);
    }

    public void appendLine(String line) {
        this.stringBuff.append(line + "\n");
    }
}
