package com.yi.tools;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件读取
 * 2017年9月9日16:41:42
 */
public class FileUtil {
    public static String read(String path) throws IOException {
        return read(path, "utf-8");
    }

    public static String read(String path, String charSetName) throws IOException {
        BufferedReader reader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File(path);
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charSetName));
            String line = null;
            while ((line = reader.readLine()) != null)
                stringBuilder.append(line + "\r\n");
        } catch (IOException e) {
            throw e;

        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void write(String path, String content) throws IOException {
        write(path, content, "utf-8", false);
    }

    public static void write(String path, String content, String charSetName, boolean append) throws IOException {
        BufferedWriter writer = null;
        try {
            File file = new File(path);
            File parent = file.getParentFile();
            if (parent != null && parent.exists() == false) {
                if (parent.mkdirs() == false) {
                    throw new IOException("File '" + file + "' could not be created");
                }
            }
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append), charSetName));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            throw e;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @SuppressWarnings("resource")
    public static List<String> getProxyFromFile(String path) {
        List<String> proxylists = new ArrayList<String>();
        BufferedReader bf;
        String line = null;
        try {
            bf = new BufferedReader(new FileReader(path));
            while ((line = bf.readLine()) != null) {
                proxylists.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxylists;
    }

    public static List<String> readFileFromInputStream(InputStream in) {
        List<String> proxylists = new ArrayList<String>();
        BufferedReader bf;
        String line = null;
        try {
            bf = new BufferedReader(new InputStreamReader(in, "utf-8"));
            while ((line = bf.readLine()) != null) {
                proxylists.add(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return proxylists;
    }
}
