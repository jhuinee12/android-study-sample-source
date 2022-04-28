package com.example.boardqactivity.dataclass;

import android.graphics.Bitmap;

public class BoardData {
    private String boardDataId;
    private Bitmap boardDataImage;
    private String boardDataTitle;
    private String boardDataDesc;
    private String boardDataName;
    private String boardDataDate;

    public BoardData(String boardDataId, Bitmap boardDataImage, String boardDataTitle, String boardDataDesc, String boardDataName, String boardDataDate) {
        this.boardDataId = boardDataId;
        this.boardDataImage = boardDataImage;
        this.boardDataTitle = boardDataTitle;
        this.boardDataDesc = boardDataDesc;
        this.boardDataName = boardDataName;
        this.boardDataDate = boardDataDate;
    }

    public String getBoardDataId() {
        return boardDataId;
    }
    public void setBoardDataId(String boardDataId) {
        this.boardDataId = boardDataId;
    }

    public Bitmap getBoardDataImage() {
        return boardDataImage;
    }
    public void setBoardDataImage(Bitmap boardDataImage) {
        this.boardDataImage = boardDataImage;
    }

    public String getBoardDataTitle() {
        return boardDataTitle;
    }
    public void setBoardDataTitle(String boardDataTitle) {
        this.boardDataTitle = boardDataTitle;
    }

    public String getBoardDataDesc() {
        return boardDataDesc;
    }
    public void setBoardDataDesc(String boardDataDesc) {
        this.boardDataDesc = boardDataDesc;
    }

    public String getBoardDataName() {
        return boardDataName;
    }
    public void setBoardDataName(String boardDataName) {
        this.boardDataName = boardDataName;
    }

    public String getBoardDataDate() {
        return boardDataDate;
    }
    public void setBoardDataDate(String boardDataDate) {
        this.boardDataDate = boardDataDate;
    }
}