package com.mixpanel.android.mpmetrics;

import android.os.Bundle;
import android.test.AndroidTestCase;

public class MPLConfigTest extends AndroidTestCase {

    public static final String TOKEN = "TOKEN";
    public static final String DISABLE_VIEW_CRAWLER_METADATA_KEY = "com.mixpanel.android.MPConfig.DisableViewCrawler";

    private MPLConfig mpConfig(final Bundle metaData) {
        return new MPLConfig(metaData, getContext());
    }

    private MixpanelLiteAPI mixpanelApi(final MPLConfig config) {
        return new MixpanelLiteAPI(getContext(), new TestUtils.EmptyPreferences(getContext()),
                TOKEN, config);
    }
}
