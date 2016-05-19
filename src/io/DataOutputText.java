package io;

import io.util.*;
import io.util.FileWriter;

import java.io.*;

/**
 * Created by huang.tudou
 */
public class DataOutputText {
    String fileName = null;

    public DataOutputText(String fileName) {
        this.fileName = fileName;
    }

    public void OutPut2txt(String tree) {
        FileWriter fw = new FileWriter(this.fileName, tree);
        fw.WriteEntireFile();
    }
}
