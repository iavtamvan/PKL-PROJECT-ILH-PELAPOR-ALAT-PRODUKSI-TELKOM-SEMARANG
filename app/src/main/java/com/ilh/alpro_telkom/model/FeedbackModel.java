package com.ilh.alpro_telkom.model;

import com.google.gson.annotations.SerializedName;

public class FeedbackModel {

    @SerializedName("total_pelapor")
    private String totalPelapor;

    @SerializedName("total_fedback_teknisi")
    private String totalFedbackTeknisi;

    @SerializedName("error")
    private boolean error;

    @SerializedName("total_point")
    private String totalPoint;

    public void setTotalPelapor(String totalPelapor) {
        this.totalPelapor = totalPelapor;
    }

    public String getTotalPelapor() {
        return totalPelapor;
    }

    public void setTotalFedbackTeknisi(String totalFedbackTeknisi) {
        this.totalFedbackTeknisi = totalFedbackTeknisi;
    }

    public String getTotalFedbackTeknisi() {
        return totalFedbackTeknisi;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isError() {
        return error;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getTotalPoint() {
        return totalPoint;
    }
}