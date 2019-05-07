package com.freehand.file_manager.file_attrs;

import java.io.File;

/**
 * Created by minhpham on 2/17/17.
 * Purpose: manage file attributes set, get,delete
 */

public interface IAttributeControl {

    void addAttribute(File file, String attr, String value);

    String getAttribute(File file, String attr);

    void removeAttribute(File file, String attr);

}
