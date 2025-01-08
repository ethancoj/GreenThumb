package com.plants;

import androidx.annotation.Keep;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

@Keep
public class Growth {
    @Exclude
    private String id;
    private String plantName;
    private double height;
    private int leafCount;
    private String imagePath;
    private Timestamp timestamp;

    public Growth() {
        // Required empty constructor for Firestore
        this.timestamp = Timestamp.now();
    }

    public Growth(String plantName, double height, int leafCount, String imagePath) {
        this.plantName = plantName;
        this.height = height;
        this.leafCount = leafCount;
        this.imagePath = imagePath;
        this.timestamp = Timestamp.now();
    }

    @Exclude
    public String getId() {
        return id;
    }

    @Exclude
    public void setId(String id) {
        this.id = id;
    }

    public String getPlantName() {
        return plantName;
    }

    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getLeafCount() {
        return leafCount;
    }

    public void setLeafCount(int leafCount) {
        this.leafCount = leafCount;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}