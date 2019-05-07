package com.freehand.file_manager.file_model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by minhpham on 2/22/17.
 * Purpose: wrap on single file.
 */

class WFile implements IFile {
    private static final int BUFFER = 2048;
    private File mFile;

    public WFile(String path) {
        this.mFile = new File(path);
    }

    @Override
    public long getFileSize() {
        return mFile.length();
    }

    @Override
    public File getFile() {
        return mFile;
    }

    @Override
    public boolean delete() {
        if (mFile.exists() && mFile.canWrite()) {
            mFile.delete();
            return true;
        }
        return false;
    }

    @Override
    public boolean moveTo(String desPath) throws IOException {
        File des = new File(desPath);
        if (des.equals(mFile.getParentFile())) return true;
        if (!des.exists() || !des.canWrite()) return false;
        // first copy file
        if (!copyTo(desPath)) return false;
        //second delete old file
        if (!delete()) return false;
        return true;
    }

    @Override
    public boolean rename(String name,boolean forceRename) {
        if (name.length() < 1 || !mFile.exists())
            return false;

        /*get file extension*/
        String filename = mFile.getName();
        String ext = filename.substring(filename.lastIndexOf("."), filename.length());

        File dest = new File(mFile.getParent() + "/" + name + ext);
        if(!forceRename && dest.exists()) return false;
        if (mFile.renameTo(dest)) {
            mFile = dest;
            return true;
        } else
            return false;
    }

    @Override
    public String getName() {
        return mFile.getName();
    }

    @Override
    public long getLastModify() {
        return mFile.lastModified();
    }

    @Override
    public boolean copyTo(String desPath) throws IOException {
        File des = new File(desPath);
        if (!des.exists() || !des.canWrite()) return false;
        if (!des.isDirectory()) {
            desPath = des.getParent() + "/";
        }
        if (desPath.charAt(desPath.length() - 1) != '/') {
            desPath = desPath + "/";
        }
        // first copy file
        File copyFile = new File(desPath + mFile.getName());

        byte[] data = new byte[BUFFER];
        int read = 0;

        BufferedOutputStream o_stream = new BufferedOutputStream(
                new FileOutputStream(copyFile));
        BufferedInputStream i_stream = new BufferedInputStream(
                new FileInputStream(mFile));

        while ((read = i_stream.read(data, 0, BUFFER)) != -1)
            o_stream.write(data, 0, read);

        o_stream.flush();
        i_stream.close();
        o_stream.close();

        return true;
    }

    @Override
    public List<File> getAllChild() {
        return null;
    }

    @Override
    public boolean isFile() {
        return true;
    }

    /**
     * @param zip_file
     * @param directory
     */
    public void extractZipFiles(String zip_file, String directory) {
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

        try {
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

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
