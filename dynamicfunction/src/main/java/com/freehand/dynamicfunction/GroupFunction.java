package com.freehand.dynamicfunction;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by minhpham on 4/25/17.
 * Purpose: .
 * Copyright © 2017 Pham Duy Minh. All rights reserved.
 */

public class GroupFunction implements IGroupFunction {
    private HashMap<String, IFunction> functions;


    public GroupFunction(IFunction... functions) {
        this.functions = new HashMap<>();
        if (functions == null || functions.length == 0) return;
        for (IFunction function : functions) {
            this.functions.put(function.getName(), function);
        }
    }

    public Iterable<IFunction> getfuncs() {
        return functions.values();
    }

    /**
     * @return name of group
     */
    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    /**
     * @param name
     * @return function by it's name
     */
    @Override
    public IFunction getFunctionByName(String name) {
        return functions.get(name);
    }

    @Override
    public <T extends IFunction> T getFunctionByClass(Class<T> clazz) {
        return (T) getFunctionByName(clazz.getSimpleName());
    }

    /**
     * add functions to group
     *
     * @param functions
     */
    @Override
    public void addFunction(IFunction... functions) {
        if (functions == null || functions.length == 0) return;
        for (IFunction function : functions) {
            this.functions.put(function.getName(), function);
        }
    }

    /**
     * release all resource
     */
    @Override
    public void release() {
        if (functions == null || functions.size() == 0) return;
        for (IFunction function : functions.values()) {
            Log.d("GroupFunction", "release() func: " + function.getName());
            function.release();
        }
        functions.clear();
    }

    public List<Observable<?>> getOutput() {
        if (functions == null || functions.size() == 0) return null;
        List<Observable<?>> out = new ArrayList<>();
        Collection<IFunction> items = functions.values();
        for (IFunction func : items) {
            out.add(func.getOutput());
        }
        return out;
    }

    public int size() {
        return functions.size();
    }

    @Override
    public void removeFunction(IFunction... inputFunctions) {
        if (inputFunctions == null || inputFunctions.length == 0) return;
        for (IFunction function : inputFunctions) {
            this.functions.remove(function.getName());
        }
    }
}
