package net.minotaurcreative.Colours.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import net.minotaurcreative.Colours.objects.Colour;
import net.minotaurcreative.Colours.objects.ColourScreen;

public class CanvasView extends View {
    ColourScreen colourScreen = new ColourScreen();

    public CanvasView(Context context) {
        super(context);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CanvasView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.RED);

        /// I'd like this to actually work pls
//        Bitmap bitmap = colourScreen.generateBitmap();
//
//       canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
    }

}
