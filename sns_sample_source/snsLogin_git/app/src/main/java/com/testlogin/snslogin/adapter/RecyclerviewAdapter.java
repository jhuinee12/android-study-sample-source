package com.testlogin.snslogin.adapter;

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

import com.testlogin.snslogin.activity.InsertActivity;
import com.testlogin.snslogin.R;

import org.jetbrains.annotations.NotNull;

import static com.testlogin.snslogin.GlobalVar.*;

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.ViewHolder> {
    //private List<String> itemList=new ArrayList<>();
    Context context;
    Activity activity;

    public RecyclerviewAdapter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;

        mHelper.mSelect();
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder)viewHolder;

        holder.itemTitle.setText(data_list.get(position).getDataTitle());
        holder.itemDesc.setText(data_list.get(position).getDataDesc());
        holder.itemName.setText("작성자 : " +data_list.get(position).getDataName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertIntent = true;
                Intent intent = new Intent(activity, InsertActivity.class);
                intent.putExtra("itemId", data_list.get(position).getDataId());
                intent.putExtra("itemTitle", data_list.get(position).getDataTitle());
                intent.putExtra("itemDesc", data_list.get(position).getDataDesc());
                Log.e("TAG",data_list.get(position).getDataId());
                activity.startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("삭제");
                builder.setMessage("삭제하시겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SQLiteDatabase deleteDB = mHelper.getWritableDatabase();
                        String DelQuery = String.format("DELETE FROM %s WHERE %s=%s",TABLE_NAME,KEY_ID,data_list.get(position).getDataId());
                        deleteDB.execSQL( DelQuery );
                        mHelper.mSelect();
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
        return data_list.size();
    }

    // 아이템뷰 지정
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemTitle;
        TextView itemDesc;
        TextView itemName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemTitle = (TextView) itemView.findViewById( R.id.txt_title );
            itemDesc = (TextView) itemView.findViewById( R.id.txt_desc );
            itemName = (TextView) itemView.findViewById( R.id.txt_name );
        }
    }
}
