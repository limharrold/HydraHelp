package com.limtechlabs.hydrahelp;

import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import androidx.annotation.NonNull;

public class CustomClickableSpan extends ClickableSpan {

    @Override
    public void onClick(@NonNull View widget) {
        // Your click handling logic here
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setColor(Color.WHITE); // Text color
        ds.setUnderlineText(true); // Underline (default color)
    }
}