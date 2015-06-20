package org.intermine.app.view; 
/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.intermine.app.util.Collections;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daria Komkova <Daria_Komkova @ hotmail.com>
 */
public class InterMineListItem extends View {
    private List<String> mAttributes;
    private List<String> mValues;

    private Paint mAttributePaint;
    private Paint mValuePaint;

    public InterMineListItem(Context context) {
        super(context);
    }

    public InterMineListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InterMineListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void updateView(List<String> attributes, List<String> values) {
        mAttributes = new ArrayList<>(attributes);
        mValues = new ArrayList<>(values);

        requestLayout();
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!Collections.isNullOrEmpty(mAttributes) && !Collections.isNullOrEmpty(mValues)
                && mAttributes.size() == mValues.size()) {
            int attributesSize = mAttributes.size();

            float defaultPadding = 5;

            float posX = defaultPadding;
            float posY = getPaddingTop();

            int fromLegendItem = 0, toLegendItem = 0;
            float lineWidth = 0;
            float screenWidth = getWidth();

            for (int i = 0; i < attributesSize; i++) {
                canvas.drawText(mAttributes.get(i), posX, posY, mAttributePaint);
                posY += 40;
                canvas.drawText(mValues.get(i), posX + screenWidth / 2, posY, mValuePaint);
            }

//            while (attributesSize > toLegendItem) {
//                float legendItemWidth = getLegendItemWidth(toLegendItem);
//
//                if (toLegendItem != attributesSize - 1) {
//                    legendItemWidth += mLegend.getStackSpace();
//                }
//
//                if (screenWidth > lineWidth + legendItemWidth) {
//                    lineWidth += legendItemWidth;
//                } else {
//                    posX = screenWidth / 2f - lineWidth / 2f;
//                    posY += Math.max(mLegend.getMaximumEntryHeight(mAttributePaint), mLegend.getFormSize());
//                    drawLine(canvas, posX, posY, fromLegendItem, toLegendItem);
//                    posY += mLegend.getYEntrySpace();
//
//                    fromLegendItem = toLegendItem;
//                    lineWidth = legendItemWidth;
//                }
//
//                toLegendItem++;
//            }
//
//            if (fromLegendItem != toLegendItem) {
//                posX = getWidth() / 2f - lineWidth / 2f;
//                posY += Math.max(mLegend.getMaximumEntryHeight(mAttributePaint), mLegend.getFormSize());
//                drawLine(canvas, posX, posY, fromLegendItem, toLegendItem);
//            }
        }
    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//        if (null != mLegend) {
//            int height = (int) (Math.ceil(getMeasuredWidth() / mLegend.mNeededWidth) * mLegend.getMaximumEntryHeight(mAttributePaint));
//            height += getPaddingBottom() + getPaddingTop();
//            setMeasuredDimension(getMeasuredWidth(), height);
//        } else {
//            setMeasuredDimension(getMeasuredWidth(), 0);
//        }
//    }
    //private void drawLine(Canvas canvas, float posX, float posY, int from, int to) {
//        for (int i = from; i < to; i++) {
//            int initialPosX = (int) posX;
//            int color = mLegend.getColors()[i];
//            mAttributePaint.setColor(mLegend.getTextColor());
//
//            if (mSelectedItems.contains(i)) {
//                color = Colors.colorWithBrightness(color, 0.7);
//                mAttributePaint.setColor(Color.GRAY);
//            }
//            drawForm(canvas, posX, posY - mLegend.mTextHeightMax / 2f, color);
//            posX += mLegend.getFormSize() + mLegend.getFormToTextSpace();
//
//            String text = mLegend.getLabel(i);
//            drawLabel(canvas, posX, posY, text);
//
//            posX += Utils.calcTextWidth(mAttributePaint, text) + mLegend.getStackSpace();
//
//            Rect rect = new Rect(initialPosX, (int) (posY - mLegend.mTextHeightMax), (int) posX, (int) (posY));
//            rect.inset(-20, -20);
//            mLegendItemToRect.put(i, rect);
//        }
//    }
//
//    private void drawForm(Canvas c, float x, float y, int color) {
//        mValuePaint.setColor(color);
//        float half = mLegend.getFormSize() / 2f;
//        c.drawCircle(x + half, y, half, mValuePaint);
//    }
//
//    private void drawLabel(Canvas c, float x, float y, String label) {
//        c.drawText(label, x, y, mAttributePaint);
//    }
//
//    private float getLegendItemWidth(int legendItem) {
//        float width = mLegend.getFormSize() + mLegend.getFormToTextSpace();
//        width += Utils.calcTextWidth(mAttributePaint, mLegend.getLabel(legendItem));
//        return width;
//    }
//
//    private void updateLegend() {
//        mLegend.setFormSize(15f);
//        mLegend.setTextSize(14f);
//        mLegend.setXEntrySpace(50f);
//        //mLegend.calculateDimensions(mAttributePaint);
//
//        mLegendItemToRect = Collections.newHashMap();
//        mSelectedItems = new HashSet<>();
//
//        mAttributePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mAttributePaint.setTextSize(Utils.convertDpToPixel(12f));
//        mAttributePaint.setTextAlign(Paint.Align.LEFT);
//
//        mValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        mValuePaint.setStyle(Paint.Style.FILL);
//        mValuePaint.setStrokeWidth(3f);
//    }
}