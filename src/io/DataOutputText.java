package io;

import util.FileUlitity;

import java.io.*;
import java.util.*;

/**
 * Created by huang.tudou
 */
public class DataOutputText {
    String fileName = null;

    public DataOutputText(String fileName) {
        this.fileName = fileName;
    }

    public void OutPut2txt(String tree) {
        try {
            File file = new File(fileName);
            FileUlitity.makeFileDirectory(fileName);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(tree);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
