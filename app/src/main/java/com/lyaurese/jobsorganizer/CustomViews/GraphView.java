package com.lyaurese.jobsorganizer.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.lyaurese.jobsorganizer.Objects.GraphEntry;
import com.lyaurese.jobsorganizer.R;

public class GraphView extends View {
    private Paint pWhite, pRED, pBottom, pBlack, pBars;
    private int canvasHeight, canvasWidth;
    private int screenWidth, screenHeight;
    private GraphEntry[] entries;
    private int width, height, graphHeight, barWidth, space;
    private int max;
    private Context context;
    private int[] barsColors;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        barWidth = 50;
        space = 250;

        pRED = new Paint();
        pRED.setColor(Color.RED);
        pRED.setTextAlign(Paint.Align.CENTER);

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
//        canvas.drawColor(this.context.getResources().getColor(R.color.lightGray));

        pWhite.setTextAlign(Paint.Align.CENTER);

        graphHeight = canvasHeight - ((canvasHeight / 10) * 3);

        int index = 0;

        if (entries != null) {

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
            canvas.drawText("No data yet", (float)(canvasWidth / 2), (float)(canvasHeight / 2), pBlack);
        }
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasWidth = w;
        canvasHeight = h;
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
        pRED.setTextSize(size);
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

            canvas.drawRect(entry.getPoint().x - barWidth, (canvasHeight/10) +(graphHeight - entry.getPoint().y), entry.getPoint().x + barWidth, canvasHeight - (canvasHeight / 10) * 2, pBars);
            canvas.drawText(entry.getLabel(), entry.getPoint().x, canvasHeight - (canvasHeight / 30), pWhite);

            if (entry.getData() != 0) {
                canvas.drawText(Integer.toString((int)entry.getData()), entry.getPoint().x, (canvasHeight/10) + (graphHeight - entry.getPoint().y) - 20, pBlack);
            }
        }
    }

    public void setBarsColors(int[] barsColors) {
        this.barsColors = barsColors;
    }
}
