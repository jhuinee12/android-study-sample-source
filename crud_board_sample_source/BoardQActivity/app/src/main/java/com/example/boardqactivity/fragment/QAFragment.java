package com.example.boardqactivity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.boardqactivity.R;
import com.example.boardqactivity.activity.InsertActivity;
import com.example.boardqactivity.activity.MyQuestionActivity;
import com.example.boardqactivity.adapter.BoardRcvAdapter;
import com.example.boardqactivity.adapter.QARcvAdapter;
import com.example.boardqactivity.dataclass.QAData;

import static com.example.boardqactivity.GlobalVar.rcyBoardAdapter;
import static com.example.boardqactivity.GlobalVar.rcyQAList;

public class QAFragment extends Fragment {
    QARcvAdapter rcyQAAdapter;
    RecyclerView rcyQa;
    Button btnGotoMyQa;

    public QAFragment() {
    }

    public static QAFragment newInstance(String param1, String param2) {
        QAFragment fragment = new QAFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qa, container, false);

        initData();

        rcyQa = view.findViewById(R.id.rcy_qa);
        btnGotoMyQa = view.findViewById(R.id.btn_goto_myqa);

        rcyQAAdapter = new QARcvAdapter(getContext());
        rcyQa.setLayoutManager(new LinearLayoutManager(getContext()));
        rcyQa.setAdapter(rcyQAAdapter);
        rcyQa.setHasFixedSize(true);

        btnGotoMyQa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyQuestionActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    public void initData(){
        rcyQAList.clear();

        rcyQAList.add(new QAData("글쓰기는 어떻게 하는 건가요?"
                , "애플리케이션 실행 후 '게시판' 탭으로 이동합니다. 오른쪽 하단 + 버튼을 선택하면 글쓰기 화면으로 이동하게 되고, 모든 칸을 채운 후 오른쪽 하단 버튼 클릭 시 글쓰기가 완료됩니다."));

        rcyQAList.add(new QAData("댓글은 어떻게 쓰나요?"
                , "애플리케이션 실행 후 '게시판' 탭으로 이동합니다. 원하는 게시글을 선택하면 게시글의 상세 화면 페이지로 이동하게 되는데, 해당 화면의 하단 '댓글 작성' 부분에서 원하는 댓글을 입력 후 오른쪽 버튼을 클릭하면 댓글 입력이 완료됩니다."));
    }
}