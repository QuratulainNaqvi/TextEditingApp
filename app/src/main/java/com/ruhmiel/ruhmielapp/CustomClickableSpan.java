package com.ruhmiel.ruhmielapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import android.os.Handler;
import android.os.Looper;

public class CustomClickableSpan extends ClickableSpan {
    private static final int TOOLTIP_DELAY_MILLIS = 3000;

    private final Handler handler = new Handler(Looper.getMainLooper());

    private final String tooltipText;
    private final TextView anchorView;
    private final Context context;
    private boolean clicked = false;


    private PopupWindow tooltipPopup;
    public CustomClickableSpan(String text, String tooltipText, TextView anchorView, Context context) {
        this.tooltipText = tooltipText;
        this.anchorView = anchorView;
        this.context =context;
    }

    @Override
    public void onClick(View widget) {
        if (!clicked) {
            showTooltip(widget);
            clicked = true;

            // Schedule a task to hide the tooltip after a delay
            handler.postDelayed(this::hideTooltip, TOOLTIP_DELAY_MILLIS);
        } else {
            hideTooltip();
        }
    }
    private void hideTooltip() {
        if (tooltipPopup != null && tooltipPopup.isShowing()) {
            tooltipPopup.dismiss();
        }

        // Reset the click state
        clicked = false;

        // Disabling the ripple effect by setting the background to null
        Drawable background = anchorView.getBackground();
        if (background != null) {
            background.setCallback(null);
        }
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

//        ds.setColor(clicked ? Color.BLUE : Color.RED); // Customize the text color based on the click state
    }


    private void showTooltip(View widget) {
        View tooltipView = LayoutInflater.from(context).inflate(R.layout.tooltip_layout, null);

        TextView tooltipTextView = tooltipView.findViewById(R.id.tooltip_text);
        tooltipTextView.setText(tooltipText);

        int[] location = new int[2];
        widget.getLocationOnScreen(location);

        int x = location[0] + widget.getWidth() / 10 - tooltipView.getMeasuredWidth() / 10;
        int y = location[1] + widget.getHeight()/2;

        tooltipPopup = new PopupWindow(tooltipView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tooltipPopup.setBackgroundDrawable(null);
        tooltipPopup.setOutsideTouchable(true);
        tooltipPopup.setFocusable(false); // Allow interactions with underlying views

        tooltipPopup.showAtLocation(widget.getRootView(), Gravity.NO_GRAVITY, x, y);
    }
}
