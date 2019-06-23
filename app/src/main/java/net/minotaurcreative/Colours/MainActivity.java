package net.minotaurcreative.Colours;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import net.minotaurcreative.Colours.enums.AlgorithmType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> algorithmNames = new ArrayList<>();
        for (int i = 0; i < AlgorithmType.values().length; i++) {
            algorithmNames.add(AlgorithmType.name(AlgorithmType.values()[i]));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, algorithmNames);

        ListView algorithmListView = findViewById(R.id.algorithmListView);

        algorithmListView.setAdapter(arrayAdapter);

        algorithmListView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
                intent.putExtra("algorithmType", i);
                startActivity(intent);
            }
        });
    }
}
