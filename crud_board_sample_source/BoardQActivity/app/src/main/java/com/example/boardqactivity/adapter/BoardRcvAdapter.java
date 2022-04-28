package com.example.boardqactivity.adapter;

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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardqactivity.R;
import com.example.boardqactivity.activity.ReaderActivity;

import static com.example.boardqactivity.GlobalVar.*;

public class BoardRcvAdapter extends RecyclerView.Adapter<BoardRcvAdapter.ViewHolder> {
    Context context;

    public BoardRcvAdapter(Context context) {
        this.context = context;

        boardHelper.rcyBoardSelect();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcv_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;

        holder.intentBoardTitle.setText(rcyBoardList.get(position).getBoardDataTitle());
        holder.intentBoardName.setText(rcyBoardList.get(position).getBoardDataName());
        holder.intentBoardDate.setText(rcyBoardList.get(position).getBoardDataDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIntent = true;
                Intent intent = new Intent(context, ReaderActivity.class);
                intent.putExtra("intentBoardId", rcyBoardList.get(position).getBoardDataId());
                Log.e("TAG",rcyBoardList.get(position).getBoardDataId());
                ContextCompat.startActivity(holder.itemView.getContext(), intent, null);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase deleteDB = boardHelper.getWritableDatabase();
                        String DelQuery = String.format("DELETE FROM %s WHERE %s = '%s'",BOARD_TABLE_NAME,BOARD_KEY_ID,rcyBoardList.get(position).getBoardDataId());
                        Log.e("TAG", "onClick: " + DelQuery);
                        deleteDB.execSQL( DelQuery );
                        boardHelper.rcyBoardSelect();
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
        return rcyBoardList.size();
    }

    // 아이템뷰 지정
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView intentBoardTitle;
        TextView intentBoardName;
        TextView intentBoardDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            intentBoardTitle = (TextView) itemView.findViewById( R.id.txt_rcy_title );
            intentBoardName = (TextView) itemView.findViewById( R.id.txt_rcy_name );
            intentBoardDate = (TextView) itemView.findViewById( R.id.txt_rcy_date );
        }
    }
}