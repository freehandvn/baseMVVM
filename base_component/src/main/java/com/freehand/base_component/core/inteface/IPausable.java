package com.freehand.base_component.core.inteface;

/**
 * Created by minhpham on 8/21/18.
 * Purpose:
 * Copyright © 2018 Pham Duy Minh. All rights reserved.
 */
public interface IPausable {
    /**
     * occur pause action
     */
    void pause();

    /**
     * occur unpause action
     */
    void unpause();

    /**
     *
     * @return true if pausing
     */
    boolean isPause();
}
