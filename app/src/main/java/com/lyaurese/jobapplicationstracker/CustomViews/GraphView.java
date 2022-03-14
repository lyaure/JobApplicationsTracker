package com.lyaurese.jobapplicationstracker.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lyaurese.jobapplicationstracker.Objects.GraphEntry;
import com.lyaurese.jobapplicationstracker.R;

public class GraphView extends View {
    private Context context;
    private Paint pWhite, pBlack, pBars;
    private int height, width;
    private int screenWidth, screenHeight;
    private GraphEntry[] entries;
    private int graphHeight, barWidth, space;
    private int max;
    private int[] barsColors;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        barWidth = 50;
        space = 250;

        pWhite = new Paint();
        pWhite.setColor(getResources().getColor(R.color.white));
        pWhite.setTextAlign(Paint.Align.CENTER);
        pWhite.setStrokeWidth(5);

        pBlack = new Paint();
        pBlack.setColor(getResources().getColor(R.color.lightGray));
        pBlack.setTextAlign(Paint.Align.CENTER);


        pBars = new Paint();
        pBars.setStrokeWidth(5);
        pBars.setColor(getResources().getColor(R.color.dark_grey));


    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        pWhite.setTextAlign(Paint.Align.CENTER);
        
        graphHeight = height - ((height / 10) * 3);

        int index = 0;

        if (entries.length != 0) {

            // calculates the coordinates of the object to draw according to data
            for (GraphEntry entry : entries) {
                int tmp = Math.round((((float) entry.getData()) / ((float)max)) * ((float)graphHeight));
                if (tmp > graphHeight) {
                    tmp = graphHeight;
                }
                entry.setPoint((screenWidth / 8) + (space * index), tmp);
                index++;
            }
            
            drawBars(canvas);
        } else {
            canvas.drawText("No data yet", (float)(width / 2), (float)(height / 2), pBlack);
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        height = (screenHeight - 350) / 3;

        if (entries == null || entries.length < 2)
            width = screenWidth;
        else
            width = (screenWidth / 4) + ((entries.length - 1) / 2) + ((entries.length - 1) * space);

        if (width < screenWidth)
            this.width = screenWidth;

        setMeasuredDimension(width, height);
        setSize(screenWidth / 30);
    }

    public void setScreenDimensions(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    private void setSize(int size) {
        pBlack.setTextSize(size);
        pWhite.setTextSize(size);
    }

    public void setEntries(GraphEntry[] entries) {
        this.entries = entries;
    }

    public void setMax(int max) {
        float num = (float)max/10;
        this.max = (int)Math.ceil(num) * 10;
    }

    private void drawBars(Canvas canvas) {
        int colorIdx = 0;
        for (GraphEntry entry : entries) {
            if(barsColors != null && entry.getData() != 0){
                pBars.setColor(barsColors[colorIdx]);

                colorIdx++;

                if(colorIdx == barsColors.length)
                    colorIdx = 0;
            }

            canvas.drawRect(entry.getPoint().x - barWidth, (height /10) +(graphHeight - entry.getPoint().y), entry.getPoint().x + barWidth, height - (height / 10) * 2, pBars);
            // ----------- TODO ----------------
            canvas.drawText(entry.getLabelParts()[0], entry.getPoint().x, height - (height / 30) *3, pWhite);
            if(entry.getLabelParts().length > 1)
                canvas.drawText(entry.getLabelParts()[1], entry.getPoint().x, height - (height / 30), pWhite);

            if (entry.getData() != 0) {
                canvas.drawText(Integer.toString((int)entry.getData()), entry.getPoint().x, (height /10) + (graphHeight - entry.getPoint().y) - 20, pBlack);
            }
        }
    }

    public void setBarsColors(int[] barsColors) {
        this.barsColors = barsColors;
    }

    public String getObjectLabelAt(float x, float y){
        for(GraphEntry entry: entries){
            if(x > entry.getPoint().x - barWidth && x < entry.getPoint().x + barWidth && y > (height /10) + (graphHeight - entry.getPoint().y) - 20 && y < height - (height / 10) * 2){
                return entry.getLabel();
            }
        }
        return null;
    }
}
