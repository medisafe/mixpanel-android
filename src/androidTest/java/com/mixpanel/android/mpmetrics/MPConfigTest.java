package com.mixpanel.android.mpmetrics;

import android.os.Bundle;
import android.test.AndroidTestCase;

public class MPConfigTest extends AndroidTestCase {

    public static final String TOKEN = "TOKEN";
    public static final String DISABLE_VIEW_CRAWLER_METADATA_KEY = "com.mixpanel.android.MPConfig.DisableViewCrawler";

    private MPConfig mpConfig(final Bundle metaData) {
        return new MPConfig(metaData, getContext());
    }

    private MixpanelAPI mixpanelApi(final MPConfig config) {
        return new MixpanelAPI(getContext(), new TestUtils.EmptyPreferences(getContext()), TOKEN, config);
    }
}
