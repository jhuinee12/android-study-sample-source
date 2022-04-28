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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.boardqactivity.R;
import com.example.boardqactivity.activity.MyQuestionActivity;
import com.example.boardqactivity.activity.ReaderActivity;
import com.example.boardqactivity.dataclass.MyQAData;
import com.example.boardqactivity.dataclass.QAData;
import com.example.boardqactivity.popup.QACommentPopup;

import static com.example.boardqactivity.GlobalVar.*;

public class MyQARcvAdapter extends RecyclerView.Adapter<MyQARcvAdapter.ViewHolder> {
    Context context;

    public MyQARcvAdapter(Context context) {
        this.context = context;

        myQAHelper.rcyMyQASelect();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcy_myqa, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;

        holder.MyQuestionTitle.setText(rcyMyQAList.get(position).getMyQADataTitle());
        holder.MyQuestionDesc.setText(rcyMyQAList.get(position).getMyQADataDesc());
        holder.MyQuestionComment.setText(rcyMyQAList.get(position).getMyQADataComment());

        boolean getExpanded = rcyMyQAList.get(position).getExpanded();
        holder.MyQuestionExpandLayout.setVisibility(getExpanded ? View.VISIBLE : View.GONE);

        holder.MyQuetionDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyQuestionActivity.myqaActivity);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase deleteDB = myQAHelper.getWritableDatabase();
                        String DelQuery = String.format("DELETE FROM %s WHERE %s = '%s'",MYQA_TABLE_NAME,MYQA_KEY_ID,rcyMyQAList.get(position).getMyQADataId());
                        deleteDB.execSQL( DelQuery );
                        myQAHelper.rcyMyQASelect();
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("취소",null);
                builder.show();
            }
        });

        holder.MyQuetionComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 답변 등록 intent
                Intent intent = new Intent(MyQuestionActivity.myqaActivity, QACommentPopup.class);
                intent.putExtra("myqaId", rcyMyQAList.get(position).getMyQADataId());
                ContextCompat.startActivity(MyQuestionActivity.myqaActivity, intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return rcyMyQAList.size();
    }

    // 아이템뷰 지정
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView MyQuestionTitle;
        TextView MyQuestionDesc;
        TextView MyQuestionComment;
        Button MyQuetionDel;
        Button MyQuetionComment;
        LinearLayout MyQuestionTitleLayout;
        LinearLayout MyQuestionExpandLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            MyQuestionTitle = (TextView) itemView.findViewById( R.id.txt_rcy_myqa_title );
            MyQuestionDesc = (TextView) itemView.findViewById( R.id.txt_rcy_myqa_desc );
            MyQuestionComment = (TextView) itemView.findViewById( R.id.txt_rcy_myqa_comment );
            MyQuetionDel = (Button) itemView.findViewById( R.id.btn_myqa_del );
            MyQuetionComment = (Button) itemView.findViewById( R.id.btn_myqa_comment );
            MyQuestionTitleLayout = (LinearLayout) itemView.findViewById( R.id.li_myqa_title );
            MyQuestionExpandLayout = (LinearLayout) itemView.findViewById( R.id.li_myqa_expand );

            MyQuestionTitleLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyQAData qa = rcyMyQAList.get(getAdapterPosition());
                    qa.setExpanded(!qa.getExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}