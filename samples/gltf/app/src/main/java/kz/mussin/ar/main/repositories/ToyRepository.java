package kz.mussin.ar.main.repositories;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.ArrayList;

import kz.mussin.ar.main.adapters.ToyRecyclerAdapter;
import kz.mussin.ar.main.objects.Toy;


public class ToyRepository {
    private Context context;
    private DatabaseReference reference;
    private GsonBuilder gsonBuilder;
    private Gson gson;
    public ArrayList<Toy> fetchToysFromLocal(){
        ArrayList<Toy> toys = new ArrayList<>();
        toys.add(new Toy("1","жолбарыс","2400","https://ae04.alicdn.com/kf/H1bab09e08f014080a5ee4e6193e8a10cZ/Schleich.jpg","жолбарыс",""));
        toys.add(new Toy("2","Көлік","4500","https://www.toyway.ru/upload/resize_cache/iblock/f77/440_440_1/Welly%2024081.jpg","Көлік",""));
        toys.add(new Toy("3","Ұшақ","4000","https://3dd-modeli.com/uploads/posts/2018-03/1522132828_igrushka-samolet.jpg","Ұшақ",""));
        toys.add(new Toy("4","Тікұшақ","6300","https://simg.marwin.kz/media/catalog/product/cache/8d1771fdd19ec2393e47701ba45e606d/i/m/syma_vertolet_ru_s5_belyy.jpg","Тікұшақ",""));
        toys.add(new Toy("5","Қуыршақ","3500","https://www.i-igrushki.ru/upload/iblock/999/999f98bc4fd32fe0a415f8e57610440e.jpg","Қуыршақ",""));
        return toys;
    }
    public ArrayList<Toy> fetchToys(Context context, ToyRecyclerAdapter notify){
        this.context = context;
        ArrayList<Toy> toys = new ArrayList<>();
        reference = FirebaseDatabase.getInstance("https://oyinshyq-default-rtdb.firebaseio.com/").getReference("toys");

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Toy toy = new Toy();



                    String json = gson.toJson(ds.getValue());
                    Log.d("JSON", json.toString());
                    Toy toy = gson.fromJson(json, Toy.class);
                    toys.add(toy);
                    notify.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Bailanys zhoq", Toast.LENGTH_SHORT).show();
            }
        });
        return toys;
    }
}
