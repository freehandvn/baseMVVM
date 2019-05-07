package com.freehand.file_manager.file_model;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by minhpham on 2/22/17.
 * Purpose: wrap on directory
 */

class WFolder implements IFile {
    private static final int BUFFER = 2048;
    private File mFile;
    private long mDirSize;

    public WFolder(String path) {
        this.mFile = new File(path);
    }

    // Inspired by org.apache.commons.io.FileUtils.isSymlink()
    private static boolean isSymlink(File file) throws IOException {
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }
        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    @Override
    public long getFileSize() throws IOException {
        if (!mFile.exists()) return 0;
        mDirSize = 0;
        get_dir_size(mFile);
        return mDirSize;
    }

    @Override
    public File getFile() {
        return mFile;
    }

    @Override
    public boolean delete() {
        if (!mFile.exists() || !mFile.canRead()) return false;
        return deleteTarget(mFile.getPath()) == 0;
    }

    @Override
    public boolean moveTo(String desPath) {
        File des = new File(desPath);
        if (mFile.getParentFile().equals(des)) return true;
        if (!des.exists() || !des.isDirectory() || !des.canWrite()) return false;
        if (desPath.contains(mFile.getAbsolutePath())) return false;
        // first copy file
        if (!copyTo(desPath)) return false;
        //second delete old file
        if (!delete()) return false;
        return true;
    }

    @Override
    public boolean rename(String name, boolean forceRename) {
        if (!mFile.exists()) return false;
        File des = new File(mFile.getParent() + "/" + name);
        if (!forceRename && des.exists()) return false;
        if (mFile.renameTo(des)) {
            mFile = des;
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
    public boolean copyTo(String desPath) {
        boolean result = copyToDirectory(mFile.getPath(), desPath) == 0;
//        mFile = new File(desPath);
        return result;
    }

    @Override
    public List<File> getAllChild() {
        if (!mFile.exists() || !mFile.canRead()) return null;
        return Arrays.asList(mFile.listFiles());
    }

    @Override
    public boolean isFile() {
        return false;
    }

    /**
     * The full path name of the file to delete.
     *
     * @param path
     *         name
     *
     * @return
     */
    private int deleteTarget(String path) {
        File target = new File(path);

        if (target.exists() && target.isFile() && target.canWrite()) {
            target.delete();
            return 0;
        } else if (target.exists() && target.isDirectory() && target.canRead()) {
            String[] file_list = target.list();

            if (file_list != null && file_list.length == 0) {
                target.delete();
                return 0;

            } else if (file_list != null && file_list.length > 0) {

                for (int i = 0; i < file_list.length; i++) {
                    File temp_f = new File(target.getAbsolutePath() + "/" + file_list[i]);

                    if (temp_f.isDirectory())
                        deleteTarget(temp_f.getAbsolutePath());
                    else if (temp_f.isFile())
                        temp_f.delete();
                }
            }
            if (target.exists())
                if (target.delete())
                    return 0;
        }
        return -1;
    }


    /*
     *
     * @param path
     */
    private void get_dir_size(File path) throws IOException {
        File[] list = path.listFiles();
        int len;

        if (list != null) {
            len = list.length;

            for (int i = 0; i < len; i++) {
                if (list[i].isFile() && list[i].canRead()) {
                    mDirSize += list[i].length();

                } else if (list[i].isDirectory() && list[i].canRead() && !isSymlink(list[i])) {
                    get_dir_size(list[i]);
                }
            }
        }
    }

    /**
     * @param old
     *         the file to be copied
     * @param newDir
     *         the directory to move the file to
     *
     * @return
     */
    private int copyToDirectory(String old, String newDir) {
        File old_file = new File(old);
        File temp_dir = new File(newDir);
        byte[] data = new byte[BUFFER];
        int read = 0;

        if (old_file.isFile() && temp_dir.canWrite()) {
            if (!temp_dir.isDirectory()) {
                newDir = temp_dir.getParent() + "/";
            }
            String file_name = old.substring(old.lastIndexOf("/"), old.length());
            File cp_file = new File(newDir + file_name);

            try {
                BufferedOutputStream o_stream = new BufferedOutputStream(
                        new FileOutputStream(cp_file));
                BufferedInputStream i_stream = new BufferedInputStream(
                        new FileInputStream(old_file));

                while ((read = i_stream.read(data, 0, BUFFER)) != -1)
                    o_stream.write(data, 0, read);

                o_stream.flush();
                i_stream.close();
                o_stream.close();

            } catch (FileNotFoundException e) {
                Log.e("FileNotFoundException", e.getMessage());
                return -1;

            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
                return -1;
            }

        } else if (old_file.isDirectory() && temp_dir.canWrite()) {
            if (!temp_dir.isDirectory()) {
                newDir = temp_dir.getParent();
            }
            String files[] = old_file.list();
            String dir = newDir + old.substring(old.lastIndexOf("/"), old.length());
            int len = files.length;

            if (!new File(dir).mkdir())
                return -1;

            for (int i = 0; i < len; i++)
                copyToDirectory(old + "/" + files[i], dir);

        } else if (!temp_dir.canWrite())
            return -1;

        return 0;
    }
}
