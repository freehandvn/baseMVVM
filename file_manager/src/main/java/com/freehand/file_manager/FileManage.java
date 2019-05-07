package com.freehand.file_manager;

import android.os.Environment;
import android.text.TextUtils;

import com.freehand.file_manager.file_model.FileUtils;
import com.freehand.file_manager.filter.AFilter;
import com.freehand.file_manager.scheduler.IScheduler;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by minhpham on 2/23/17.
 * Purpose: implement of IFileManage
 */
public class FileManage implements IFileManage {
    private static final String DEFAULT_HOME = Environment.getExternalStorageDirectory().getPath();
    private static Comparator<File> DEFAULT_SORT;
    private static IFileManage ourInstance = new FileManage();
    private String homePath;
    private boolean showHidden;
    private FileStackSupport fileSupport;

    private FileManage() {
    }

    public static IFileManage getInstance() {
        return ourInstance;
    }

    @Override
    public void init(FileConfig config) {
        this.homePath = config.homePath;
        if (TextUtils.isEmpty(homePath)) homePath = DEFAULT_HOME;
        fileSupport = new FileStackSupport(homePath);
        this.showHidden = config.showHiddenFile;
        ApplicationProvider.getInstance().init(config.mApp);
        DEFAULT_SORT = config.getDefaultSort();
    }

    @Override
    public String getHomePath() {
        return homePath;
    }

    @Override
    public boolean createDir(String name) {
        String path = fileSupport.getCurrentDir();
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        if (path.charAt(len - 1) != '/')
            path += "/";

        if (new File(path + name).mkdir())
            return true;

        return false;
    }

    /**
     * support copy file and folder
     *
     * @param src
     * @param des
     *
     * @return true on success
     */
    @Override
    public Observable<Boolean> copy(final String src, final String des) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observer) throws Exception {
                observer.onNext(FileUtils.buildFile(src).copyTo(des));
                observer.onComplete();
            }
        });
    }

    /**
     * support rename file or folder
     *
     * @param path
     * @param newName
     *
     * @return true on success
     */
    @Override
    public Observable<Boolean> rename(final String path, final String newName, final boolean forceRename) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observer) throws Exception {
                observer.onNext(FileUtils.buildFile(path).rename(newName, forceRename));
                observer.onComplete();
            }
        });
    }

    /**
     * support delete file or folder (if folder, will delete all children in site and itself)
     *
     * @param filePath
     *
     * @return true on success
     */
    @Override
    public Observable<Boolean> delete(final String filePath) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observer) throws Exception {
                observer.onNext(FileUtils.buildFile(filePath).delete());
                observer.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> deletes(List<File> files) {
        if (files == null || files.size() == 0) return Observable.just(false);
        return Observable.just(files).flatMap(new Function<List<File>, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> apply(@NonNull List<File> files) throws Exception {
                boolean result = true;
                for (File file : files) {
                    result = result && FileUtils.buildFile(file.getAbsoluteFile()).delete();
                }
                return Observable.just(result);
            }
        });
    }

    /**
     * support move file or folder from srcPath to desPath
     *
     * @param srcPath
     * @param desPath
     *
     * @return true on success
     */
    @Override
    public Observable<String> move(final String srcPath, final String desPath) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observer) throws Exception {
                boolean success = FileUtils.buildFile(srcPath).moveTo(desPath);
                if (!success) {
                    observer.onNext(srcPath);
                } else {
                    String path = desPath;
                    if (desPath.charAt(desPath.length() - 1) != '/') {
                        path = desPath + "/";
                    }
                    observer.onNext(path + new File(srcPath).getName());
                }
                observer.onComplete();
            }
        });
    }

    /**
     * support get dir size
     *
     * @param dirPath
     *
     * @return size of dir
     */
    @Override
    public Observable<Long> getDirSize(final String dirPath) {
        return Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(ObservableEmitter<Long> observer) throws Exception {
                observer.onNext(FileUtils.buildFile(dirPath).getFileSize());
                observer.onComplete();
            }
        });
    }

    /**
     * get all children of dir
     *
     * @param path
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public List<File> getAllChild(@NonNull String path, Comparator<File> compare) {
        if (TextUtils.isEmpty(path)) return null;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory() || !dir.canRead()) return null;

        List<File> arr = Arrays.asList(dir.listFiles(new FileFilter() {
            // filter hidden file
            @Override
            public boolean accept(File file) {
                return showHidden || (!showHidden && !isHiddenFile(file));
            }
        }));
        if (compare == null) {
            compare = DEFAULT_SORT;
        }
        Collections.sort(arr, compare);
        return arr;
    }

    /**
     * reset stack and set @path become root
     *
     * @param path
     */
    @Override
    public void setHome(String path) {
        homePath = path;
        fileSupport.setHome(path);
    }

    /**
     * @param path
     * @param name
     *
     * @return
     */
    @Override
    public boolean createDir(String path, String name) {
        int len = path.length();

        if (len < 1 || len < 1)
            return false;

        if (path.charAt(len - 1) != '/')
            path += "/";

        if (new File(path + name).mkdir())
            return true;

        return false;
    }

    @Override
    public Observable<List<File>> filter(final String dir, final AFilter filter) {
        return Observable.create(new ObservableOnSubscribe<List<File>>() {
            @Override
            public void subscribe(ObservableEmitter<List<File>> observer) throws Exception {
                ArrayList<File> files = new ArrayList<File>();
                doFilter(dir, filter, files);
                observer.onNext(files);
                observer.onComplete();
            }
        });
    }

    /**
     * filter file synchronise
     *
     * @param dir
     * @param filter
     *
     * @return
     */
    @Override
    public List<File> filterSync(String dir, AFilter filter) {
        ArrayList<File> files = new ArrayList<File>();
        doFilter(dir, filter, files);
        return files;
    }

    @Override
    public Observable<String> zipFile(final String filePath, final String desPath) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> observer) throws Exception {
                observer.onNext(FileUtils.createZipFile(filePath, desPath));
                observer.onComplete();
            }
        });
    }

    @Override
    public Observable<Boolean> extractFile(final String zipPath, final String desDir) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> observer) throws Exception {
                FileUtils.extractZipFiles(zipPath, desDir);
                observer.onNext(true);
                observer.onComplete();
            }
        });
    }

    /**
     * return scheduler . via: delete all file with last modified over duration
     *
     * @param scheduler
     *
     * @return
     */
    @Override
    public Observable<Boolean> schedulerOn(final IScheduler scheduler, final String dirPath) {
        if (scheduler == null) return null;
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> e) throws Exception {
                ArrayList<File> files = new ArrayList<File>();
                doFilter(TextUtils.isEmpty(dirPath) ? homePath : dirPath, scheduler.getFilter(), files);
                scheduler.onScheduler(files);
                e.onNext(true);
                e.onComplete();
            }
        });
    }

    @Override
    public FileStackSupport getFileStackSupport() {
        return fileSupport;
    }

    private void doFilter(String dir, AFilter filter, ArrayList<File> n) {
        File root_dir = new File(dir);
        File[] files = root_dir.listFiles();

        if (files == null || files.length == 0) return;

        for (File file : files) {
            if (filter.accept(file) && (filter.showHiddenFile || (!filter.showHiddenFile && !isHiddenFile(file)))) {
                if ((filter.filterFileOnly && file.isFile()) || !filter.filterFileOnly)
                    n.add(file);
            }
            if (file.isDirectory() && (filter.showHiddenFile || (!filter.showHiddenFile && !isHiddenFile(file)))) {
                doFilter(file.getPath(), filter, n);
            }
        }
    }

    private boolean isHiddenFile(File file) {
        if (!file.exists()) return false;
        return file.getName().charAt(0) == '.';
    }

}
