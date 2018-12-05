package com.freehand.dynamicfunction;

/**
 * Created by minhpham on 4/25/17.
 * Purpose: group controll all function
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public interface IGroupFunction {

    /**
     * @return name of group
     */
    String getName();

    /**
     * @param name
     * @return function by it's name
     */
    IFunction getFunctionByName(String name);

    <T extends IFunction>T getFunctionByClass(Class<T> clazz);

    /**
     * add functions to group
     *
     * @param function
     */
    void addFunction(IFunction... function);

    /**
     * release all resource
     */
    void release();

    void removeFunction(IFunction... inputFunctions);
}
