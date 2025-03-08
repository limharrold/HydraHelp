package com.limtechlabs.hydrahelp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.core.content.ContextCompat;

public class ActiveAwareFAB extends FrameLayout {

    private boolean isActive = false;
    private int activeBackgroundColor;

    public ActiveAwareFAB(Context context) {
        super(context);
        init(context, null);
    }

    public ActiveAwareFAB(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ActiveAwareFAB(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        activeBackgroundColor = ContextCompat.getColor(context, R.color.bottomnav);
    }

    public void setActive(boolean active) {
        isActive = active;
        updateBackground();
    }

    private void updateBackground() {
        if (isActive) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setShape(GradientDrawable.OVAL); // Set the shape to oval
            drawable.setColor(activeBackgroundColor);

            setBackground(drawable); // Set the drawable as the background
        } else {
            setBackgroundColor(Color.TRANSPARENT);
        }
    }
}