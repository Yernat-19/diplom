package kz.mussin.oiynshyq.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.ar.sceneform.samples.gltf.R;

import java.util.ArrayList;

import kz.mussin.oiynshyq.main.MainActivity;
import kz.mussin.oiynshyq.main.adapters.ToyRecyclerAdapter;
import kz.mussin.oiynshyq.main.objects.Toy;
import kz.mussin.oiynshyq.main.repositories.ToyRepository;

public class MainFragment extends Fragment {

    private ArrayList<Toy> toys;
    private ToyRepository repository;
    private RecyclerView rv;
    private ToyRecyclerAdapter adapter;
    private Context context;
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
        toys = repository.fetchToysFromLocal();
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment = inflater.inflate(R.layout.fragment_main, container, false);
        rv = fragment.findViewById(R.id.main_list);
        adapter = new ToyRecyclerAdapter(context,toys, listener);
        return fragment;
    }
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ((MainActivity)getActivity()).setCurrentToy(toys.get(Integer.parseInt(view.getTag().toString())));
            ((MainActivity)getActivity()).goToInfo();
        }
    };
}