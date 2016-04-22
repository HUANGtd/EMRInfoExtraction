package io;

import java.io.*;
import java.util.*;

/**
 * Created by hhw on 4/22/16.
 */
public class DataOutput {
    String fileName = null;

    public DataOutput(String fileName) {
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
