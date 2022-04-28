package com.example.boardqactivity.adapter;

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
import com.example.boardqactivity.dataclass.QAData;

import static com.example.boardqactivity.GlobalVar.BOARD_KEY_ID;
import static com.example.boardqactivity.GlobalVar.BOARD_TABLE_NAME;
import static com.example.boardqactivity.GlobalVar.boardHelper;
import static com.example.boardqactivity.GlobalVar.insertIntent;
import static com.example.boardqactivity.GlobalVar.rcyBoardList;
import static com.example.boardqactivity.GlobalVar.rcyQAList;

public class QARcvAdapter extends RecyclerView.Adapter<QARcvAdapter.ViewHolder> {
    Context context;

    public QARcvAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcy_qa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;

        holder.QATitle.setText(rcyQAList.get(position).getQaDataTitle());
        holder.QADesc.setText(rcyQAList.get(position).getQaDataDesc());

        boolean getExpanded = rcyQAList.get(position).getExpanded();
        holder.QADesc.setVisibility(getExpanded ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return rcyQAList.size();
    }

    // 아이템뷰 지정
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView QATitle;
        TextView QADesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            QATitle = (TextView) itemView.findViewById( R.id.txt_rcy_qa_title );
            QADesc = (TextView) itemView.findViewById( R.id.txt_rcy_qa_desc );

            QATitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QAData qa = rcyQAList.get(getAdapterPosition());
                    qa.setExpanded(!qa.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}