package net.minotaurcreative.Colours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import net.minotaurcreative.Colours.enums.AlgorithmType;
import net.minotaurcreative.Colours.views.CanvasView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        final AlgorithmType algorithmType = AlgorithmType.values()[intent.getIntExtra("algorithmType",0)];

        CanvasView canvasView = findViewById(R.id.colourScreenView);
        canvasView.init(algorithmType);
    }
}
