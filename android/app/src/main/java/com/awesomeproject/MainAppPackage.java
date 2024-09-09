package com.awesomeproject;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;
import com.awesomeproject.newarchitecture.modules.MobiCareModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAppPackage implements ReactPackage {
    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();

        // modules.add(new NativeAlertModule(reactContext));
        // modules.add(new ForegroundModule(reactContext));
        // modules.add(new SendMessageModule(reactContext));
        // modules.add(new LocationModule(reactContext));
        // modules.add(new BackgroundTimerModule(reactContext));
        // modules.add(new SNSPermissionModule(reactContext));
        modules.add(new MobiCareModule(reactContext));
        return modules;
    }
}
