package com.marksarchi.countylocationservice.geom;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Properties {
    @JsonProperty("OBJECTID")
    public int oBJECTID;
    @JsonProperty("AREA")
    public double aREA;
    @JsonProperty("PERIMETER")
    public double pERIMETER;
    @JsonProperty("COUNTY3_")
    public int cOUNTY3_;
    @JsonProperty("COUNTY3_ID")
    public int cOUNTY3_ID;

    public int getoBJECTID() {
        return oBJECTID;
    }

    public void setoBJECTID(int oBJECTID) {
        this.oBJECTID = oBJECTID;
    }

    public double getaREA() {
        return aREA;
    }

    public void setaREA(double aREA) {
        this.aREA = aREA;
    }

    public double getpERIMETER() {
        return pERIMETER;
    }

    public void setpERIMETER(double pERIMETER) {
        this.pERIMETER = pERIMETER;
    }

    public int getcOUNTY3_() {
        return cOUNTY3_;
    }

    public void setcOUNTY3_(int cOUNTY3_) {
        this.cOUNTY3_ = cOUNTY3_;
    }

    public int getcOUNTY3_ID() {
        return cOUNTY3_ID;
    }

    public void setcOUNTY3_ID(int cOUNTY3_ID) {
        this.cOUNTY3_ID = cOUNTY3_ID;
    }

    public String getcOUNTY() {
        return cOUNTY;
    }

    public void setcOUNTY(String cOUNTY) {
        this.cOUNTY = cOUNTY;
    }

    public double getShape_Leng() {
        return shape_Leng;
    }

    public void setShape_Leng(double shape_Leng) {
        this.shape_Leng = shape_Leng;
    }

    public double getShape_Area() {
        return shape_Area;
    }

    public void setShape_Area(double shape_Area) {
        this.shape_Area = shape_Area;
    }

    @JsonProperty("COUNTY")
    public String cOUNTY;
    @JsonProperty("Shape_Leng")
    public double shape_Leng;
    @JsonProperty("Shape_Area")
    public double shape_Area;
}
