package com.example.boardqactivity.dataclass;

public class QAData {
    private String qaDataTitle;
    private String qaDataDesc;
    private Boolean expanded;


    public QAData(String qaDataTitle, String qaDataDesc) {
        this.qaDataTitle = qaDataTitle;
        this.qaDataDesc = qaDataDesc;
        this.expanded = false;
    }

    public String getQaDataTitle() {
        return qaDataTitle;
    }
    public void setQaDataTitle(String qaDataTitle) {
        this.qaDataTitle = qaDataTitle;
    }

    public String getQaDataDesc() {
        return qaDataDesc;
    }
    public void setQaDataDesc(String qaDataDesc) {
        this.qaDataDesc = qaDataDesc;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
}