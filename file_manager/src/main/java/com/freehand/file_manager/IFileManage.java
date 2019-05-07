package com.freehand.file_manager;

import java.io.File;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.scheduler.IScheduler;

/**
 * Created by minhpham on 2/25/17.
 * Purpose: define interface and all functions support of file manage
 */
public interface IFileManage {
    void init(FileConfig config);

    /**
     * support copy file or directory
     *
     * @param src
     * @param des
     * @return
     */
    Observable<Boolean> copy(@NonNull String src, @NonNull String des);

    /**
     * rename file or directory
     *
     * @param path
     * @param newName
     * @return
     */
    Observable<Boolean> rename(String path, String newName,boolean forceRename);

    /**
     * delete file or directory
     *
     * @param filePath
     * @return
     */
    Observable<Boolean> delete(String filePath);
    /**
     * delete multiple file or directory
     *
     * @param
     * @return
     */
    Observable<Boolean> deletes(List<File> files);

    /**
     * move file or directory
     *
     * @param srcPath
     * @param desPath
     * @return
     */
    Observable<String> move(String srcPath, String desPath);

    /**
     * get directory size (byte)
     *
     * @param dirPath
     * @return
     */
    Observable<Long> getDirSize(String dirPath);


    /**
     * get all children of directory and sort base on compare
     *
     * @param path
     * @param compare
     * @return
     */
    List<File> getAllChild(String path, Comparator<File> compare);

    /**
     * set root of browser
     *
     * @param path
     */
    void setHome(String path);

    /**
     * create new directory to path directry
     *
     * @param path
     * @param name
     * @return
     */
    boolean createDir(String path, String name);

    /**
     * support filter file in directory
     *
     * @param dir      : search in dir
     * @return
     */
    Observable<List<File>> filter(String dir, AFilter filter);

    /**
     * filter file synchronise
     * @param dir
     * @param filter
     * @return
     */
    List<File> filterSync(String dir, AFilter filter);
    /**
     * zip file function
     *
     * @param filePath file address need to zip
     * @return zipped file path
     */
    Observable<String> zipFile(String filePath,String desPath);

    /**
     * extract all file
     *
     * @param zipPath
     * @param desDir
     * @return
     */
    Observable<Boolean> extractFile(String zipPath, String desDir);

    /**
     * return scheduler . via: delete all file with last modified over duration
     *
     * @param scheduler
     * @return
     */
    Observable<Boolean> schedulerOn(IScheduler scheduler, String rootPath);

    FileStackSupport getFileStackSupport();

    String getHomePath();

    boolean createDir(String name);

}
