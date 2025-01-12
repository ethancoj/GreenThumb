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
    private double growthRate; // cm per day
    private double leafGrowthRate; // leaves per day

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

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getLeafGrowthRate() {
        return leafGrowthRate;
    }

    public void setLeafGrowthRate(double leafGrowthRate) {
        this.leafGrowthRate = leafGrowthRate;
    }

    // Calculate growth rates based on previous record
    public void calculateGrowthRates(Growth previousGrowth) {
        if (previousGrowth != null && this.timestamp != null && previousGrowth.timestamp != null) {
            long timeDifferenceMillis = this.timestamp.toDate().getTime() - previousGrowth.timestamp.toDate().getTime();
            double daysDifference = timeDifferenceMillis / (1000.0 * 60 * 60 * 24); // Convert to days

            if (daysDifference > 0) {
                this.growthRate = (this.height - previousGrowth.height) / daysDifference;
                this.leafGrowthRate = (this.leafCount - previousGrowth.leafCount) / daysDifference;
            }
        }
    }
}