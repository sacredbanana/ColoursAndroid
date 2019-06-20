package net.minotaurcreative.Colours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Intent intent = getIntent();

        final String algorithmName = intent.getStringExtra("algorithmName");

        TextView algorithmNameTextView = findViewById(R.id.algorithmNameTextView);

        algorithmNameTextView.setText(algorithmName);
    }
}
