package com.marksarchi.countylocationservice.geom;

import java.util.ArrayList;

public class Root {
    public ArrayList<Feature1> features;
    public String fileName;
    public String type;

    public Root() {
    }



    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Feature1> getFeatures() {
        return features;
    }

    public void setFeatures(ArrayList<Feature1> features) {
        this.features = features;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


}
