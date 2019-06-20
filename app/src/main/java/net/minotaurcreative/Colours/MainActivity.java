package net.minotaurcreative.Colours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ArrayList<String> algorithms = new ArrayList<>(asList(
                "Colour Cube Slice",
                "Colour Cube Slice with Smoothing",
                "Random Spread",
                "Clump Colours",
                "Nearest to Previous Colour",
                "Colour Cube Slice with Red Accumulation",
                "Nearest Colour to Previous Block",
                "Nearest Colour to Block Above",
                "Nearest Colour to Both Above and Previous Blocks",
                "Nearest Colour to All Three Blocks Above",
                "Nearest Colour to All Three Pixels Above Plus Previous"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, algorithms);

        ListView algorithmListView = findViewById(R.id.algorithmListView);

        algorithmListView.setAdapter(arrayAdapter);

        algorithmListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra("algorithmName", algorithms.get(i));
                startActivity(intent);
            }
        });
    }
}
