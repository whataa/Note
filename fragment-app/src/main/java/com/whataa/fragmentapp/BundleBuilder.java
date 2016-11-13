package com.whataa.fragmentapp;

import android.os.Bundle;

/**
 * Created by Summer on 2016/11/13.
 */

public class BundleBuilder {
    Bundle bundle;

    public BundleBuilder() {
        bundle = new Bundle();
    }
    public BundleBuilder putInt(String s, int i) {
        bundle.putInt(s,i);
        return this;
    }
    public Bundle build() {
        return bundle;
    }
}
