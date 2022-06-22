package com.example.arasql;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class EditData extends AppCompatActivity {

    BiodataTbl biodataTbl;
    EditText nama, alamat;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data);

        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        update = findViewById(R.id.update_data);
        getSupportActionBar().setTitle("Edit Data " + getIntent().getStringExtra("nama"));

        biodataTbl = new BiodataTbl(getApplicationContext());

        nama.setText(getIntent().getStringExtra("nama"));
        alamat.setText(getIntent().getStringExtra("alamat"));

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                biodataTbl.update_data(getIntent().getStringExtra("id"),
                        nama.getText().toString(),
                        alamat.getText().toString()
                );
                finish();
            }
        });

    }
}
