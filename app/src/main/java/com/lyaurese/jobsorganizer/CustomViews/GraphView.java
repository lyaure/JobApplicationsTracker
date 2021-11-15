package com.lyaurese.jobsorganizer.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.lyaurese.jobsorganizer.Objects.GraphEntry;
import com.lyaurese.jobsorganizer.R;

public class GraphView extends View {
    private final Paint pWhite, pRED, pPrimary, pBlack;
    private int canvasHeight, canvasWidth;
    private int screenWidth, screenHeight;
    private GraphEntry[] entries;
    private int width, height, graphHeight, barWidth, space;
    private int max;
    private Context context;

    public GraphView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        barWidth = 50;
        space = 250;

        pRED = new Paint();
        pRED.setColor(Color.RED);
        pRED.setTextAlign(Paint.Align.CENTER);

        pWhite = new Paint();
        pWhite.setColor(Color.WHITE);
        pWhite.setTextAlign(Paint.Align.CENTER);
        pWhite.setStrokeWidth(5);

        pBlack = new Paint();
        pBlack.setColor(Color.BLACK);
        pBlack.setTextAlign(Paint.Align.CENTER);

        pPrimary = new Paint();
        pPrimary.setColor(getResources().getColor(R.color.primary));
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        canvas.drawColor(this.context.getResources().getColor(R.color.lightGray));

        pWhite.setTextAlign(Paint.Align.CENTER);

        graphHeight = canvasHeight - ((canvasHeight / 10) * 3);

        int index = 0;

        // calculates the coordinates of the object to draw according to data
        for (GraphEntry entry : entries) {
            int tmp = Math.round((((float) entry.getData()) / ((float)max)) * ((float)graphHeight));
            if (tmp > graphHeight) {
                tmp = graphHeight;
            }
            entry.setPoint((screenWidth / 8) + (space * index), tmp);
            index++;
        }

        canvas.drawRect(0, (float)(canvasHeight - (canvasHeight / 10)), (float)canvasWidth, (float)canvasHeight, pPrimary);

        if (entries != null) {
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
        for (GraphEntry entry : entries) {
            canvas.drawRect(entry.getPoint().x - barWidth, (canvasHeight/10) +(graphHeight - entry.getPoint().y), entry.getPoint().x + barWidth, canvasHeight - (canvasHeight / 10) * 2, pWhite);
            canvas.drawText(entry.getLabel(), entry.getPoint().x, canvasHeight - (canvasHeight / 30), pWhite);

            if (entry.getData() != 0) {
                canvas.drawText(Integer.toString((int)entry.getData()), entry.getPoint().x, (canvasHeight/10) + (graphHeight - entry.getPoint().y) - 20, pBlack);
            }
        }
    }
}
