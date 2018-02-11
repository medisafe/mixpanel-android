package com.mixpanel.android.mpmetrics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import com.mixpanel.android.util.MPLog;

/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This file has been modified from its original version by Mixpanel, Inc. The original
 * contents were part of GCMRegistrar, retrieved from
 * https://code.google.com/p/gcm/source/browse/gcm-client/src/com/google/android/gcm/GCMRegistrar.java
 * on Jan 3, 2013
 */


/* package */ class ConfigurationChecker {

    public static String LOGTAG = "MixpanelAPI.ConfigurationChecker";

    private static Boolean mTakeoverActivityAvailable;

    public static boolean checkBasicConfiguration(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();

        if (packageManager == null || packageName == null) {
            MPLog.w(LOGTAG, "Can't check configuration when using a Context with null packageManager or packageName");
            return false;
        }
        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("android.permission.INTERNET", packageName)) {
            MPLog.w(LOGTAG, "Package does not have permission android.permission.INTERNET - Mixpanel will not work at all!");
            MPLog.i(LOGTAG, "You can fix this by adding the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"android.permission.INTERNET\" />");
            return false;
        }

        return true;
    }

    public static boolean checkPushConfiguration(Context context) {

        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();

        if (packageManager == null || packageName == null) {
            MPLog.w(LOGTAG, "Can't check configuration when using a Context with null packageManager or packageName");
            return false;
        }

        final String permissionName = packageName + ".permission.C2D_MESSAGE";

        // check special permission
        try {
            packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA);
        } catch (final NameNotFoundException e) {
            MPLog.w(LOGTAG, "Application does not define permission " + permissionName);
            MPLog.i(LOGTAG, "You will need to add the following lines to your application manifest:\n" +
                    "<permission android:name=\"" + packageName + ".permission.C2D_MESSAGE\" android:protectionLevel=\"signature\" />\n" +
                    "<uses-permission android:name=\"" + packageName + ".permission.C2D_MESSAGE\" />");
            return false;
        }
        // check regular permissions

        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("com.google.android.c2dm.permission.RECEIVE", packageName)) {
            MPLog.w(LOGTAG, "Package does not have permission com.google.android.c2dm.permission.RECEIVE");
            MPLog.i(LOGTAG, "You can fix this by adding the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"com.google.android.c2dm.permission.RECEIVE\" />");
            return false;
        }

        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("android.permission.INTERNET", packageName)) {
            MPLog.w(LOGTAG, "Package does not have permission android.permission.INTERNET");
            MPLog.i(LOGTAG, "You can fix this by adding the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"android.permission.INTERNET\" />");
            return false;
        }

        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("android.permission.WAKE_LOCK", packageName)) {
            MPLog.w(LOGTAG, "Package does not have permission android.permission.WAKE_LOCK");
            MPLog.i(LOGTAG, "You can fix this by adding the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"android.permission.WAKE_LOCK\" />");
            return false;
        }

        // This permission is only required on older devices
        if (PackageManager.PERMISSION_GRANTED != packageManager.checkPermission("android.permission.GET_ACCOUNTS", packageName)) {
            MPLog.i(LOGTAG, "Package does not have permission android.permission.GET_ACCOUNTS");
            MPLog.i(LOGTAG, "Android versions below 4.1 require GET_ACCOUNTS to receive Mixpanel push notifications.\n" +
                    "Devices with later OS versions will still be able to receive messages, but if you'd like to support " +
                    "older devices, you'll need to add the following to your AndroidManifest.xml file:\n" +
                    "<uses-permission android:name=\"android.permission.GET_ACCOUNTS\" />");

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                return false;
            }
        }

        // check receivers
        final PackageInfo receiversInfo;
        try {
            receiversInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_RECEIVERS);
        } catch (final NameNotFoundException e) {
            MPLog.w(LOGTAG, "Could not get receivers for package " + packageName);
            return false;
        }

        boolean canRegisterWithPlayServices = false;
        try {
            Class.forName("com.google.android.gms.common.GooglePlayServicesUtil");
            canRegisterWithPlayServices = true;
        } catch(final ClassNotFoundException e) {
            MPLog.w(LOGTAG, "Google Play Services aren't included in your build- push notifications won't work on Lollipop/API 21 or greater");
            MPLog.i(LOGTAG, "You can fix this by adding com.google.android.gms:play-services as a dependency of your gradle or maven project");
        }

        return canRegisterWithPlayServices;
    }

}
