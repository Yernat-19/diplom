package kz.mussin.ar.main.fragments;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import com.google.ar.sceneform.samples.gltf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import kz.mussin.ar.main.MainActivity;
import kz.mussin.ar.main.adapters.ToyRecyclerAdapter;
import kz.mussin.ar.main.objects.Toy;
import kz.mussin.ar.main.repositories.ToyRepository;


public class MainFragment extends Fragment {

    private ArrayList<Toy> toys = new ArrayList<>();
    private ToyRepository repository;
    private RecyclerView rv;
    private ToyRecyclerAdapter adapter;
    private Context context;
    private DatabaseReference reference;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    public MainFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(ArrayList<Toy> toys) {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repository = new ToyRepository();

        context = getContext();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);
        rv = fragment.findViewById(R.id.main_list);
        adapter = new ToyRecyclerAdapter(context,toys,((MainActivity)getActivity()).getFavouriteToyIds(), listener);
        fetchToys(getContext(),adapter);
        rv.setLayoutManager( new LinearLayoutManager(this.context));
        rv.setAdapter(adapter);
        return fragment;
    }
    public void toggleFavourite(String id){
        ((MainActivity)getActivity()).addToyToFavourite(id);
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity)getActivity()).setCurrentToy(toys.get(Integer.parseInt(view.getTag().toString())));
            ((MainActivity)getActivity()).goToAR();
        }
    };
    public void fetchToys(Context context, ToyRecyclerAdapter notify){
        this.context = context;
        reference = FirebaseDatabase.getInstance("https://oyinshyq-default-rtdb.firebaseio.com/").getReference("toys");

        gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    //Toy toy = new Toy();



                    String json = gson.toJson(ds.getValue());
                    //Log.d("JSON", json.toString());
                    Toy toy = gson.fromJson(json, Toy.class);
                    toys.add(toy);
                    adapter.notifyDataSetChanged();
                }

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Bailanys zhoq", Toast.LENGTH_SHORT).show();
            }
        });
    }
}