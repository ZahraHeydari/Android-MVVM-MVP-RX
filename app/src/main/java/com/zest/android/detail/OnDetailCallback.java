package com.zest.android.detail;

import android.graphics.drawable.Drawable;

public interface OnDetailCallback {

    void setFavoriteIcon(int drawableRes);

    void showMessage(int stringRes);
}
