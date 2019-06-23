package net.minotaurcreative.Colours.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import net.minotaurcreative.Colours.enums.AlgorithmType;
import net.minotaurcreative.Colours.objects.ColourScreen;

public class CanvasView extends View {
    private ColourScreen colourScreen;

    private Matrix scalingMatrix;

    private boolean isReadyToRender = false;

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);

        scalingMatrix = new Matrix();
    }

    public void init(AlgorithmType algorithmType) {
        colourScreen = new ColourScreen(algorithmType);
        isReadyToRender = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isReadyToRender) {
            float scaleWidth = ((float) getWidth()) / 256;
            float scaleHeight = ((float) getHeight()) / 128;
            scalingMatrix.postScale(scaleWidth, scaleHeight);

            Bitmap bitmap = colourScreen.generateBitmap(scalingMatrix);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        }
    }
}
