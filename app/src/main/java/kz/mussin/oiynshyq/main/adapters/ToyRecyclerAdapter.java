package kz.mussin.oiynshyq.main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.ar.sceneform.samples.gltf.R;

import java.util.List;

import kz.mussin.oiynshyq.main.objects.Toy;


public class ToyRecyclerAdapter  extends RecyclerView.Adapter<ToyRecyclerAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Toy> toys;
    private Context context = null;
    private View.OnClickListener listener;
    public ToyRecyclerAdapter(Context context, List<Toy> toys, View.OnClickListener listener) {
        this.toys = toys;
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
        holder.imageView.setOnClickListener(listener);
        holder.imageView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return toys.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nameView,priceView;
        final ImageView imageView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.toy_name);
            priceView = view.findViewById(R.id.toy_price);
            imageView = view.findViewById(R.id.toy_image);

        }
    }
}
