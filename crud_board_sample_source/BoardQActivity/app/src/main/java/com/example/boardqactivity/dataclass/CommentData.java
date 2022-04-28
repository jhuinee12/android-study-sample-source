package com.example.boardqactivity.dataclass;

import android.graphics.Bitmap;

public class CommentData {
    private String CommentDataId;
    private Bitmap CommentDataImage;
    private String CommentDataDesc;
    private String CommentDataDate;
    private String CommentDataBoardId;
    private String CommentDataCustId;

    public CommentData(String CommentDataId, Bitmap CommentDataImage, String CommentDataDesc, String CommentDataDate, String CommentDataBoardId, String CommentDataCustId) {
        this.CommentDataId = CommentDataId;
        this.CommentDataImage = CommentDataImage;
        this.CommentDataDesc = CommentDataDesc;
        this.CommentDataDate = CommentDataDate;
        this.CommentDataBoardId = CommentDataBoardId;
        this.CommentDataCustId = CommentDataCustId;
    }

    public String getCommentDataId() {
        return CommentDataId;
    }
    public void setCommentDataId(String CommentDataId) {
        this.CommentDataId = CommentDataId;
    }

    public Bitmap getCommentDataImage() {
        return CommentDataImage;
    }
    public void setCommentDataImage(Bitmap CommentDataImage) {
        this.CommentDataImage = CommentDataImage;
    }

    public String getCommentDataDesc() {
        return CommentDataDesc;
    }
    public void setCommentDataDesc(String CommentDataDesc) {
        this.CommentDataDesc = CommentDataDesc;
    }

    public String getCommentDataDate() {
        return CommentDataDate;
    }
    public void setCommentDataDate(String CommentDataDate) {
        this.CommentDataDate = CommentDataDate;
    }

    public String getCommentDataBoardId() {
        return CommentDataBoardId;
    }
    public void setCommentDataBoardId(String CommentDataBoardId) {
        this.CommentDataBoardId = CommentDataBoardId;
    }

    public String getCommentDataCustId() {
        return CommentDataCustId;
    }
    public void setCommentDataCustId(String CommentDataCustId) {
        this.CommentDataCustId = CommentDataCustId;
    }
}