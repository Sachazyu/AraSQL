package com.example.arasql;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    Button tambah_data;

    Custome_adapter adapter;
    Cursor cursor;
    BiodataTbl biodataTbl;
    ArrayList<Objek> list;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView =findViewById(R.id.listview);
        tambah_data  =findViewById(R.id.tambah_data);

        biodataTbl = new BiodataTbl(getApplicationContext());
        tambah_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),TambahData.class));
            }
        });
        ambil_data();
    }
    void ambil_data(){
        list = new ArrayList<Objek>();
        cursor = biodataTbl.tampil_data();
        if (cursor!= null&& cursor.getCount()>0){
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String nama = cursor.getString(1);
                String alamat = cursor.getString(2);
                list.add(new Objek(
                        id,
                        nama,
                        alamat
                ));
            }
            adapter = new Custome_adapter(getApplicationContext(),list,MainActivity.this);
            listView.setAdapter(adapter);
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        ambil_data();
    }
}
class Custome_adapter extends BaseAdapter {

    Activity activity;
    BiodataTbl biodataTbl;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Objek> model;
    Custome_adapter(Context context ,ArrayList<Objek> list ,Activity activity){
        this.context = context;
        this.model = list;
        this.activity = activity;
        biodataTbl = new BiodataTbl(context);
        layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return model.size();
    }

    @Override
    public Object getItem(int position) {
        return model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView id,nama,alamat;
    Button edit,hapus;
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view1 = layoutInflater.inflate(R.layout.list_data,viewGroup,false);

        id = view1.findViewById(R.id.id);
        nama = view1.findViewById(R.id.nama);
        alamat = view1.findViewById(R.id.alamat);

        id.setText(model.get(position).getId());
        nama.setText(model.get(position).getNama());
        alamat.setText(model.get(position).getAlamat());

        edit=view1.findViewById(R.id.edit);
        hapus=view1.findViewById(R.id.hapus);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,EditData.class);
                intent.putExtra("nama", model.get(position).getNama());
                intent.putExtra("alamat", model.get(position).getAlamat());
                intent.putExtra("id", model.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, "Hapus", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Tanya");
                builder.setMessage("Apakah Anda Ingin Mnghapus Data Ini?");
                builder.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        biodataTbl.delete(model.get(position).getId());

                        Intent intent = new Intent( context,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        return view1;
    }
}