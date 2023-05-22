package com.example.myrecyclerviewexample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myrecyclerviewexample.API.Connector;
import com.example.myrecyclerviewexample.base.CallInterface;
import com.example.myrecyclerviewexample.model.Oficio;
import com.example.myrecyclerviewexample.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private List<Usuario> list;
    private final LayoutInflater inflater;
    private View.OnClickListener onClickListener;
    protected ExecutorService executor = Executors.newSingleThreadExecutor();
    protected Handler handler = new Handler(Looper.getMainLooper());

    public MyRecyclerViewAdapter(Context context){
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        list = new ArrayList<>();
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
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        executeCall(new CallInterface() {

            List<Oficio> oficios = Connector.getConector().getAsList(Oficio.class, "/oficios");
            @Override
            public void doInBackground() {
                oficios = Connector.getConector().getAsList(Oficio.class, "/oficios");
            }

            @Override
            public void doInUI() {
                Usuario u = list.get(position);
                holder.title.setText(u.getApellidos().concat(", ").concat(u.getNombre()));
                holder.subtitle.setText(
                        oficios.stream()
                                .filter(o->o.getIdOficio()==u.getOficio())
                                .findFirst()
                                .get()
                                .getDescripcion()
                );
                switch (u.getOficio()){
                    case 1 : holder.image.setImageResource(R.mipmap.ic_1_foreground);
                        break;
                    case 2 : holder.image.setImageResource(R.mipmap.ic_2_foreground);
                        break;
                    case 3 : holder.image.setImageResource(R.mipmap.ic_3_foreground);
                        break;
                    case 4 : holder.image.setImageResource(R.mipmap.ic_4_foreground);
                        break;
                    case 5 : holder.image.setImageResource(R.mipmap.ic_5_foreground);
                        break;
                    case 6 : holder.image.setImageResource(R.mipmap.ic_6_foreground);
                        break;
                    case 7 : holder.image.setImageResource(R.mipmap.ic_7_foreground);
                        break;
                    case 8 : holder.image.setImageResource(R.mipmap.ic_8_foreground);
                        break;
                    case 9 : holder.image.setImageResource(R.mipmap.ic_9_foreground);
                        break;
                    case 10 : holder.image.setImageResource(R.mipmap.ic_10_foreground);
                        break;
                    case 11 : holder.image.setImageResource(R.mipmap.ic_11_foreground);
                        break;
                    case 12 : holder.image.setImageResource(R.mipmap.ic_12_foreground);
                        break;
                }
            }
        });

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
