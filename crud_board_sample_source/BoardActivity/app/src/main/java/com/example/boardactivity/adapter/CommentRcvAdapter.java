package com.example.boardactivity.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardactivity.R;

import static com.example.boardactivity.GlobalVar.*;

public class CommentRcvAdapter extends RecyclerView.Adapter<CommentRcvAdapter.ViewHolder> {
    Context context;
    Activity activity;
    String commentBaordId;

    public CommentRcvAdapter(Context context, Activity activity, String commentBaordId) {
        this.context = context;
        this.activity = activity;
        this.commentBaordId = commentBaordId;

        commentHelper.rcyCommentSelect(commentBaordId);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcy_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;

        holder.itemCommentDesc.setText(rcyCommentList.get(position).getCommentDataDesc());
        holder.itemCommentName.setText(rcyCommentList.get(position).getCommentDataCustId());
        holder.itemCommentDate.setText(rcyCommentList.get(position).getCommentDataDate());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase deleteDB = commentHelper.getWritableDatabase();
                        String DelQuery = String.format("DELETE FROM %s WHERE %s=%s",COMMENT_TABLE_NAME,COMMENT_KEY_ID,rcyCommentList.get(position).getCommentDataId());
                        deleteDB.execSQL( DelQuery );
                        commentHelper.rcyCommentSelect(rcyCommentList.get(position).getCommentDataId());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return rcyCommentList.size();
    }

    // 아이템뷰 지정
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemCommentDesc;
        TextView itemCommentName;
        TextView itemCommentDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemCommentDesc = (TextView) itemView.findViewById( R.id.txt_comment_desc );
            itemCommentName = (TextView) itemView.findViewById( R.id.txt_comment_name );
            itemCommentDate = (TextView) itemView.findViewById( R.id.txt_comment_date );
        }
    }
}