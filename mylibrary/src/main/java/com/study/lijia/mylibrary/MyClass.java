package com.study.lijia.mylibrary;

import com.study.lijia.internallibrary.InternalClass;

/**
 * Created by lijia on 17-11-14.
 */

public class MyClass {
    public String getMyClass() {
        return "MyClass";
    }

    public String getContentFromInternal() {
        return new InternalClass().getInternalClass();
    }
}
