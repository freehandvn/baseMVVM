package com.freehand.file_manager;

import java.util.Stack;

/**
 * Created by minhpham on 3/9/17.
 * Purpose: support save file in back stack when travel file in browser
 */

public class AFileStackSupport<T> {

    private Stack<T> mPathStack;
    private T homePath;

    public AFileStackSupport(T homePath) {
        this.homePath = homePath;
        mPathStack = new Stack<>();
        stayAtHome();
    }



    /**
     * clear back stack make home path is top
     */
    public void stayAtHome() {
        mPathStack.clear();
        mPathStack.push(homePath);
    }


    /**
     * return current directory
     *
     * @return
     */
    public T getCurrentDir() {
        if(mPathStack.size() == 0) return homePath;
        return mPathStack.peek();
    }

    public void updateCurrent(T value){
        mPathStack.pop();
        mPathStack.push(value);
    }


    /**
     * remove current dir to go back
     */
    public T goPrevDir() {
        int size = mPathStack.size();
        // can go back
        if (size > 1)
            mPathStack.pop();
        else if (size == 0) {
            stayAtHome();
        }
        return getCurrentDir();
    }

    /**
     * set this path become current dir
     *
     * @param path
     */
    public void goToDir(T path) {
        int size = mPathStack.size();

        if (!path.equals(mPathStack.peek()) || size == 0) {
            mPathStack.push(path);
        }
    }

    /**
     * reset stack and set @path become root
     *
     * @param path
     */
    public void setHome(T path) {
        homePath = path;
        stayAtHome();
    }


    public boolean isEmptyStack() {
        return mPathStack.size() == 1;
    }

}
