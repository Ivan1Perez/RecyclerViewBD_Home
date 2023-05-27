package com.example.myrecyclerviewexample;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.base.ImageDownloader;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Usuario> list;
    private List<Oficio> oficios;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Handler handler = new Handler(Looper.getMainLooper());

    public MyRecyclerViewAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new ArrayList<>();
        oficios = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simple_element,parent,false);
        view.setOnClickListener(onClickListener);
        return new ViewHolder(view);
    }

    protected void executeCall(CallInterface callInterface){
        executor.execute(() -> {
            callInterface.doInBackground();
            handler.post(() -> {
                callInterface.doInUI();
            });
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Si los oficios están nulos contectamos con la bbdd en segundo plano para obtenerlos.
        if (oficios.isEmpty()) {
            executeCall(new CallInterface() {

                public void doInBackground() {
                    oficios = Connector.getConector().getAsList(Oficio.class, "oficios");
                }

                @Override
                public void doInUI() {
                    updateData(holder, position);
                }
            });
        }  else updateData(holder,position);
    }

    private void updateData(ViewHolder holder, int position) {
        Usuario u = list.get(position);

        Optional<Oficio> optionalOficio = oficios.stream()
                .filter(o -> o.getIdOficio() == u.getIdOficio())
                .findFirst();

        if (optionalOficio.isPresent()) {
            Oficio oficio = optionalOficio.get();
            holder.title.setText(u.getApellidos().concat(", ").concat(u.getNombre()));
            holder.subtitle.setText(
                    oficio.getDescripcion()
            );

            //Concatenamos el atributo 'imageurl' a la url de ImageDownloader
            ImageDownloader.downloadImage("http://192.168.1.36/images/" + oficio.getImageurl(), holder.image);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setUsuarios(List<Usuario> usuarioList) {
        this.list = usuarioList;
    }

    public List<Oficio> getOficios() {
        return oficios;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView subtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
        }

    }
}
