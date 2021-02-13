package com.example.berfinkeleshomework3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentsViewHolder> {

    List<CommentItem> comments;
    Context context;

    public CommentAdapter(List<CommentItem> comments, Context context) {
        this.comments = comments;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment_rows_layout,parent, false);
        return new CommentsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsViewHolder holder, int position) {

        holder.txtname.setText(comments.get(position).getName());
        holder.txtcomment.setText(comments.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentsViewHolder extends RecyclerView.ViewHolder{

        TextView txtname;
        TextView txtcomment;
        ConstraintLayout root;

        public CommentsViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            txtcomment = itemView.findViewById(R.id.txtcomment);
            root = itemView.findViewById(R.id.cont);
        }
    }


}
