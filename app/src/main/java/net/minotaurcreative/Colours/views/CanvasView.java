package net.minotaurcreative.Colours.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Process;
import android.util.AttributeSet;
import android.view.View;
import net.minotaurcreative.Colours.R;
import net.minotaurcreative.Colours.enums.AlgorithmType;
import net.minotaurcreative.Colours.objects.ColourScreen;

import java.util.Timer;
import java.util.TimerTask;

public class CanvasView extends View {
    private ColourScreen colourScreen;

    private Matrix scalingMatrix;

    private boolean isReadyToRender = false;

    private AlgorithmType algorithmType;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        scalingMatrix = new Matrix();
    }

    public void run() {
        colourScreen = new ColourScreen(algorithmType);
        colourScreen.canvasView = this;
        colourScreen.processImage();
        isReadyToRender = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isReadyToRender) {
            float scaleWidth = ((float) getWidth()) / 256;
            float scaleHeight = ((float) getHeight()) / 128;
            scalingMatrix.postScale(scaleWidth, scaleHeight);

            Bitmap bitmap = colourScreen.getBitmap(scalingMatrix);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        }
    }

    public void setAlgorithmType(AlgorithmType algorithmType) {
        this.algorithmType = algorithmType;
    }
}
