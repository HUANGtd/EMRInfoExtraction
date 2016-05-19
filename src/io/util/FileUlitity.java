package io.util;

import java.io.File;
import java.io.IOException;

/**
 * Created by huang.tudou on 5/18/16.
 */
public class FileUlitity {

    /********** make file/folder if not exist **********/
    public static void makeFileDirectory(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            File folder = new File(filePath.substring(0, filePath.lastIndexOf(File.separator)));
            if(!folder.exists()) {
                folder.mkdirs();
            }
            file.createNewFile();
        }
    }

    /********** delete folder **********/
    public static boolean DeleteFolder(String sPath) {
        Boolean flag = false;
        File file = new File(sPath);

        if (!file.exists()) {
            return flag;
        } else {
            if (file.isFile()) {
                return deleteFile(sPath);
            } else {
                return deleteDirectory(sPath);
            }
        }
    }

    public static boolean deleteDirectory(String sPath) {
        if(!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }

        File dirFile = new File(sPath);
        if(!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }

        Boolean flag = true;
        File[] files = dirFile.listFiles();
        for(int i = 0; i < files.length; i++) {
            if(files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if(!flag) {
                    break;
                }
            } else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if(!flag) {
                    break;
                }
            }
        }
        if(!flag) {
            return false;
        }

        if(dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteFile(String sPath) {
        Boolean flag = false;
        File file = new File(sPath);

        if(file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }
}
