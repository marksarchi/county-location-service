package com.marksarchi.countylocationservice.geom;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;

public class Geometry {
    @JsonIgnore
    public ArrayList<Double> bbox;
    public String type;
    public ArrayList<ArrayList<ArrayList<Object>>> coordinates;

    public ArrayList<Double> getBbox() {
        return bbox;
    }

    public void setBbox(ArrayList<Double> bbox) {
        this.bbox = bbox;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ArrayList<ArrayList<Object>>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<ArrayList<ArrayList<Object>>> coordinates) {
        this.coordinates = coordinates;
    }


}
