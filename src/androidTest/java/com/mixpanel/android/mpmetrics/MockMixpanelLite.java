package com.mixpanel.android.mpmetrics;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.concurrent.Future;


@SuppressWarnings("deprecation")
class MockMixpanelLite extends MixpanelLiteAPI {
    public MockMixpanelLite(Context context, Future<SharedPreferences> prefsFuture, String
            testToken) {
        super(context, prefsFuture, testToken);
    }

    // Not complete- you may need to override track(), registerSuperProperties, etc
    // as you use this class more.

}
