package kz.mussin.ar.main.adapters;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.ar.sceneform.samples.gltf.R;

import java.util.List;

import kz.mussin.ar.main.MainActivity;
import kz.mussin.ar.main.fragments.MainFragment;
import kz.mussin.ar.main.objects.Toy;


public class ToyRecyclerAdapter  extends RecyclerView.Adapter<ToyRecyclerAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Toy> toys;
    private final List<String> favouriteToys;
    private Context context = null;
    private View.OnClickListener listener;
    private String LOCAL_STORAGE_NAME = "LocalStorage";
    private String FAVOURITE_TOYS = "favouriteToys";
    public ToyRecyclerAdapter(Context context, List<Toy> toys,List<String> favouriteToys,
                              View.OnClickListener listener) {
        this.toys = toys;
        this.favouriteToys = favouriteToys;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
        this.listener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.toy_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Toy toy = toys.get(position);
        holder.nameView.setText(toy.name);
        holder.priceView.setText(String.valueOf(toy.price) + " теңге");
        Glide.with(this.inflater.getContext()).load(toy.image).into(holder.imageView);
        holder.showAR.setOnClickListener(listener);
        holder.showAR.setTag(position);
        holder.showDescription.setTag(toy);
        holder.showDescription.setOnClickListener(dListener);
        holder.favouriteButton.setImageDrawable(context.getResources().getDrawable(
                favouriteToys.contains(toy.id) ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24));
        holder.favouriteButton.setOnClickListener(fListener);
        holder.favouriteButton.setTag(toy.id);
    }
    View.OnClickListener fListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String id = (String) view.getTag();
            Log.d("before",String.valueOf(favouriteToys.size()));
            boolean isFavourite = favouriteToys.contains(id);
            if (isFavourite) {
                int i = favouriteToys.indexOf(id);
                favouriteToys.remove(i);

            } else {
                favouriteToys.add(id);
            }
            Log.d("after",String.valueOf(favouriteToys.size()));
            isFavourite = !isFavourite;
            //favouriteToys.remove(id);
            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(context.getResources().getDrawable(
                    isFavourite ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24));
            toggleFavourite();
        }
    };
    private void toggleFavourite(){
        SharedPreferences localStorage = context.getSharedPreferences(LOCAL_STORAGE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor localStorageEditor = localStorage.edit();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            localStorageEditor.putString(FAVOURITE_TOYS, String.join(",", favouriteToys));
            localStorageEditor.commit();
        }
    }
    View.OnClickListener dListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toy t = (Toy) view.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(t.description);
            builder.setMessage("");
            builder.setPositiveButton("Түсінікті", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
// 3. Get the <code><a href="/reference/android/app/AlertDialog.html">AlertDialog</a></code> from <code><a href="/reference/android/app/AlertDialog.Builder.html#create()">create()</a></code>
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };
    public class StartGameDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("desc")
                    .setPositiveButton("Tusinikty", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            // Create the AlertDialog object and return it
            return builder.create();
        }
    }
    @Override
    public int getItemCount() {
        return toys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nameView,priceView, showDescription, showAR, description;
        final ImageView imageView, favouriteButton;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.toy_name);
            priceView = view.findViewById(R.id.toy_price);
            imageView = view.findViewById(R.id.toy_image);
            showDescription = view.findViewById(R.id.toy_show_description);
            showAR = view.findViewById(R.id.toy_show_ar);
            description = view.findViewById(R.id.toy_description);
            favouriteButton = view.findViewById(R.id.favouriteButton);
        }
    }
}
