package com.freehand.file_manager.file_model;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by minhpham on 2/22/17.
 * Purpose: wrap File of Android os, it can be file or directory
 */

public interface IFile {
    /**
     * get file size on disk
     * @return file size by byte
     * @throws IOException
     */
    long getFileSize() throws IOException;

    /**
     *
     * @return origin file
     */
    File getFile();

    /**
     * delete current file
     * @return
     */
    boolean delete();

    /**
     * move current file to parent directory
     * @param desPath
     * @return
     * @throws IOException
     */
    boolean moveTo(String desPath) throws IOException;

    /**
     * rename current file
     * @param name
     * @return
     */
    boolean rename(String name,boolean forceRename);

    /**
     *
     * @return name of file/dir
     */
    String getName();

    /**
     * @return last modify of file/dir
     */
    long getLastModify();

    /**
     * copy current file to another directory
     * @param desPath
     * @return
     * @throws IOException
     */
    boolean copyTo(String desPath) throws IOException;

    /**
     * get all file children of current file
     * @return
     */
    List<File> getAllChild();

    /**
     * detect file is dir or file
     * @return
     */
    boolean isFile();
}
