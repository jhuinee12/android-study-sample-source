package com.testlogin.snslogin;

import java.sql.Blob;

public class BoardData {
    private String dataId;
    private Blob dataImage;
    private String dataTitle;
    private String dataDesc;
    private String dataName;

    public BoardData() {}

    public BoardData(String dataId, Blob dataImage, String dataTitle, String dataDesc, String dataName) {
        this.dataId = dataId;
        this.dataImage = dataImage;
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataName = dataName;
    }

    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public Blob getDataImage() {
        return dataImage;
    }
    public void setDataImage(Blob dataImage) {
        this.dataImage = dataImage;
    }

    public String getDataTitle() {
        return dataTitle;
    }
    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }
    public void setDataDesc(String dataDesc) {
        this.dataDesc = dataDesc;
    }

    public String getDataName() {
        return dataName;
    }
    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
