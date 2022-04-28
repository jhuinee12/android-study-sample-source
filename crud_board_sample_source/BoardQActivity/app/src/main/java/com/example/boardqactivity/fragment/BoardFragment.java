package com.example.boardqactivity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.boardqactivity.R;
import com.example.boardqactivity.activity.InsertActivity;
import com.example.boardqactivity.adapter.BoardRcvAdapter;
import com.example.boardqactivity.helper.BoardDBHelper;

import static com.example.boardqactivity.GlobalVar.*;

public class BoardFragment extends Fragment {

    public static SwipeRefreshLayout SwipeRefresh;
    public static Button btnInsert;
    public static ImageButton ibSearch;
    public static RecyclerView rcyView;
    public static EditText edtSearch;

    public BoardFragment() {
        // Required empty public constructor
    }

    public static BoardFragment newInstance(String param1, String param2) {
        BoardFragment fragment = new BoardFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_board, container, false);

        SwipeRefresh = view.findViewById(R.id.swipe_refresh);
        btnInsert = view.findViewById(R.id.btn_insert);
        ibSearch = view.findViewById(R.id.ib_search);
        rcyView = view.findViewById(R.id.rcy_view);
        edtSearch = view.findViewById(R.id.edt_search);

        boardHelper = new BoardDBHelper(getContext());

        rcyBoardAdapter = new BoardRcvAdapter(getContext());
        rcyView.setLayoutManager(new LinearLayoutManager(getContext()));
        rcyView.setAdapter(rcyBoardAdapter);
        rcyView.setHasFixedSize(true);

        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rcyBoardAdapter.notifyDataSetChanged();
                SwipeRefresh.setRefreshing(false);
            }
        });

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), InsertActivity.class);
                startActivity(intent);
            }
        });

        ibSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardHelper.rcyBoardSearch(edtSearch.getText().toString());
                rcyBoardAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}