package io;

import java.io.*;
import java.util.*;

/**
 * Created by huang.tudou
 */
public class DataOutputTXT {
    String fileName = null;

    public DataOutputTXT(String fileName) {
        this.fileName = fileName;
    }

    public void OutPut2txt(String tree) {
        try {
            File file = new File(fileName);
            if(!file.exists())
                file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
            writer.write(tree);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
