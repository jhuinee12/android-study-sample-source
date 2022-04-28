package com.example.boardqactivity.dataclass;

public class MyQAData {
    private String MyQADataId;
    private String MyQADataTitle;
    private String MyQADataDesc;
    private String MyQADataName;
    private String MyQADataComment;
    private String MyQADataDate;
    private Boolean expanded;

    public MyQAData(String MyQADataId, String MyQADataTitle, String MyQADataDesc, String MyQADataName, String MyQADataComment, String MyQADataDate) {
        this.MyQADataId = MyQADataId;
        this.MyQADataTitle = MyQADataTitle;
        this.MyQADataDesc = MyQADataDesc;
        this.MyQADataName = MyQADataName;
        this.MyQADataComment = MyQADataComment;
        this.MyQADataDate = MyQADataDate;
        this.expanded = false;
    }

    public String getMyQADataId() {
        return MyQADataId;
    }

    public void setMyQADataId(String myQADataId) {
        MyQADataId = myQADataId;
    }

    public String getMyQADataTitle() {
        return MyQADataTitle;
    }

    public void setMyQADataTitle(String myQADataTitle) {
        MyQADataTitle = myQADataTitle;
    }

    public String getMyQADataDesc() {
        return MyQADataDesc;
    }

    public void setMyQADataDesc(String myQADataDesc) {
        MyQADataDesc = myQADataDesc;
    }

    public String getMyQADataName() {
        return MyQADataName;
    }

    public void setMyQADataName(String myQADataName) {
        MyQADataName = myQADataName;
    }

    public String getMyQADataComment() {
        return MyQADataComment;
    }

    public void setMyQADataComment(String myQADataComment) {
        MyQADataComment = myQADataComment;
    }

    public String getMyQADataDate() {
        return MyQADataDate;
    }

    public void setMyQADataDate(String myQADataDate) {
        MyQADataDate = myQADataDate;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
}