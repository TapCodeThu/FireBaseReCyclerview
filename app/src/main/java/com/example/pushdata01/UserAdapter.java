package com.example.pushdata01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{
    Context context;
    private IClickListener mClickListener;
    List<User> listUser;
    public interface IClickListener{
        void onClickUpdate(User user);
        void onClickDelete(User user);
    }

    public UserAdapter(Context context, IClickListener listener, List<User> listUser) {
        this.context = context;
        this.mClickListener = listener;
        this.listUser = listUser;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_user,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = listUser.get(position);
        holder.TenUser.setText(user.getName());
        holder.EmailUser.setText(user.getEmail());
        holder.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickUpdate(user);
            }
        });
        holder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onClickDelete(user);
            }
        });

    }

    @Override
    public int getItemCount() {
        return listUser.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView TenUser, EmailUser;
        Button Update;
        ImageButton Delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            TenUser = itemView.findViewById(R.id.id_name_tv);
            EmailUser = itemView.findViewById(R.id.id_email_tv);
            Update = itemView.findViewById(R.id.id_btnUpdate);
            Delete =itemView.findViewById(R.id.id_imag_xoa);
        }
    }
}
