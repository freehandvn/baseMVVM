package com.freehand.file_manager.file_attrs;

/**
 * Created by minhpham on 3/2/17.
 * Purpose: provider for DI
 */
public class AttributeProvider {
    private static IAttributeControl ourInstance = AttrControllBySHR.getInstance();

    public static IAttributeControl getInstance() {
        return ourInstance;
    }

    private AttributeProvider() {
    }

    public static void init(IAttributeControl control){
        ourInstance = control;
    }
}
