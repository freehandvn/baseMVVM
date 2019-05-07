package com.freehand.file_manager.file_model;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Deque;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by minhpham on 2/22/17.
 * Purpose: Utility work with IFile, support functions work on file.
 */
public class FileUtils {

    private static final int BUFFER = 2048;

    public static IFile buildFile(String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) throw new Exception("file not exist");
        if (file.isDirectory()) {
            return new WFolder(path);
        }
        return new WFile(path);
    }


    public static IFile buildFile(File file) throws Exception {
        return buildFile(file.getPath());
    }


    /**
     * @param zip_file
     * @param directory
     */
    public static void extractZipFiles(String zip_file, String directory) throws IOException {
        byte[] data = new byte[BUFFER];
        String name, path, zipDir;
        ZipEntry entry;
        ZipInputStream zipstream;

        if (!(directory.charAt(directory.length() - 1) == '/'))
            directory += "/";

        if (zip_file.contains("/")) {
            path = zip_file;
            name = path.substring(path.lastIndexOf("/") + 1,
                    path.length() - 4);
            zipDir = directory + name + "/";

        } else {
            path = directory + zip_file;
            name = path.substring(path.lastIndexOf("/") + 1,
                    path.length() - 4);
            zipDir = directory + name + "/";
        }

        new File(zipDir).mkdir();

        zipstream = new ZipInputStream(new FileInputStream(path));

        while ((entry = zipstream.getNextEntry()) != null) {
            String buildDir = zipDir;
            String[] dirs = entry.getName().split("/");

            if (dirs != null && dirs.length > 0) {
                for (int i = 0; i < dirs.length - 1; i++) {
                    buildDir += dirs[i] + "/";
                    new File(buildDir).mkdir();
                }
            }

            int read = 0;
            FileOutputStream out = new FileOutputStream(
                    zipDir + entry.getName());
            while ((read = zipstream.read(data, 0, BUFFER)) != -1)
                out.write(data, 0, read);

            zipstream.closeEntry();
            out.close();
        }


    }

    /**
     * @param path
     */
    public static String createZipFile(String path,String desPath) throws IOException {
        File dir = new File(path);
        String[] list = dir.list();
        String name = dir.getName();
        String _path;
        if (!dir.canRead() || !dir.canWrite())
            return null;


        if (dir.isFile()) {
            ZipOutputStream zip_out = new ZipOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(desPath + "/" + name + ".zip"), BUFFER));

            zipFile(dir, zip_out);
            return desPath + "/" + name + ".zip";
        } else {
            zip(dir, new File(desPath + "/" + name + ".zip"));
            return desPath + "/" + name + ".zip";
        }
//        int len = list.length;
//
//        if (path.charAt(path.length() - 1) != '/')
//            _path = path + "/";
//        else
//            _path = path;
//
//
//        for (int i = 0; i < len; i++)
//            zipFile(new File(_path + list[i]), zip_out);
//        try {
//            zip_out.close();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        return dir.getParent()+"/" + name + ".zip";

    }

    /*
     *
     * @param file
     * @param zout
     * @throws IOException
     */
    private static void zipFile(File file, ZipOutputStream zout) throws IOException {
        byte[] data = new byte[BUFFER];
        int read;

        if (file.isFile()) {
            ZipEntry entry = new ZipEntry(file.getName());
            zout.putNextEntry(entry);
            BufferedInputStream instream = new BufferedInputStream(
                    new FileInputStream(file));

            while ((read = instream.read(data, 0, BUFFER)) != -1)
                zout.write(data, 0, read);

            zout.closeEntry();
            instream.close();

        } else if (file.isDirectory()) {
            String[] list = file.list();
            int len = list.length;

            for (int i = 0; i < len; i++)
                zipFile(new File(file.getPath() + "/" + list[i]), zout);
        }
    }

    static void addDir(File dirObj, ZipOutputStream out) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDir(files[i], out);
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            Log.d("zip file", " Adding: " + files[i].getAbsolutePath());
            out.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
            int len;
            while ((len = in.read(tmpBuf)) > 0) {
                out.write(tmpBuf, 0, len);
            }
            out.closeEntry();
            in.close();
        }
    }

    public static void zip(File directory, File zipfile) throws IOException {
        URI base = directory.toURI();
        Deque<File> queue = new LinkedList<>();
        queue.push(directory);
        OutputStream out = new FileOutputStream(zipfile);
        Closeable res = out;
        try {
            ZipOutputStream zout = new ZipOutputStream(out);
            res = zout;
            while (!queue.isEmpty()) {
                directory = queue.pop();
                for (File kid : directory.listFiles()) {
                    String name = base.relativize(kid.toURI()).getPath();
                    if (kid.isDirectory()) {
                        queue.push(kid);
                        name = name.endsWith("/") ? name : name + "/";
                        zout.putNextEntry(new ZipEntry(name));
                    } else {
                        zout.putNextEntry(new ZipEntry(name));
                        copy(kid, zout);
                        zout.closeEntry();
                    }
                }
            }
        } finally {
            res.close();
        }
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        while (true) {
            int readCount = in.read(buffer);
            if (readCount < 0) {
                break;
            }
            out.write(buffer, 0, readCount);
        }
    }

    private static void copy(File file, OutputStream out) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            copy(in, out);
        }
    }

    private static void copy(InputStream in, File file) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            copy(in, out);
        } finally {
            out.close();
        }
    }

    public static void unzip(File zipfile, File directory) throws IOException {
        ZipFile zfile = new ZipFile(zipfile);
        Enumeration<? extends ZipEntry> entries = zfile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File file = new File(directory, entry.getName());
            if (entry.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                try (InputStream in = zfile.getInputStream(entry)) {
                    copy(in, file);
                }
            }
        }
    }

}
