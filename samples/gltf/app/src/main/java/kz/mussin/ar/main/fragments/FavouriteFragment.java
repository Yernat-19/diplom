package kz.mussin.ar.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.ar.sceneform.samples.gltf.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import kz.mussin.ar.main.MainActivity;
import kz.mussin.ar.main.adapters.ToyRecyclerAdapter;
import kz.mussin.ar.main.objects.Toy;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView recyclerView;
    private ToyRecyclerAdapter recyclerAdapter;
    private ArrayList<Toy> favouriteToys = new ArrayList<>();
    private ArrayList<Toy> toys = new ArrayList<>();


    private Context context;
    private DatabaseReference reference;
    private Gson gson;
    private GsonBuilder gsonBuilder;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String LOCAL_STORAGE_NAME = "LocalStorage";
    private String FAVOURITE_TOYS = "favouriteToys";
    public FavouriteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_account, container, false);
        TextView tv = fragment.findViewById(R.id.fav_title);
        tv.setText("Tandauly");
        fetchToys(getContext());
        //getFavourites();
        recyclerView = fragment.findViewById(R.id.fav_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAdapter = new ToyRecyclerAdapter(getContext(),favouriteToys, ((MainActivity)getActivity()).getFavouriteToyIds(),listener);
        recyclerView.setAdapter(recyclerAdapter);
        return  fragment;
    }
    public void getFavourites(){
         for (String id :((MainActivity)getActivity()).getFavouriteToyIds()) {
             //Log.d("Fav id", id);

             for (Toy t : toys) {
                 //Log.d("Fav id---", t.id);

                 if (t.id.equals(id)) {
                     Log.d("Fav id ++++", id);
                     favouriteToys.add(t);
                     recyclerAdapter.notifyDataSetChanged();
                 }
             }

         }
         recyclerAdapter.notifyDataSetChanged();
    }
    public void fetchToys(Context context){
        this.context = context;
        toys = new ArrayList<>();
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
                    recyclerAdapter.notifyDataSetChanged();
                }
                getFavourites();

            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(context, "Bailanys zhoq", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity)getActivity()).setCurrentToy(toys.get(Integer.parseInt(view.getTag().toString())));
            ((MainActivity)getActivity()).goToAR();
        }
    };
}