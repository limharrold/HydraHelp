package com.limtechlabs.hydrahelp; // Replace with your package name

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatTextView;

public class UnderlinedTextView extends AppCompatTextView {

    public UnderlinedTextView(Context context) {
        super(context);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setPaintFlags(getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
}