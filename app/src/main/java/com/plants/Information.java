package com.plants;

import androidx.annotation.Keep;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;
import java.util.List;
import java.util.Map;

@Keep
public class Information {
    @Exclude
    private String id;
    private String scientificName;
    private String name;
    private String description;
    private String longDescription;
    private List<String> specialFeatures;
    private Map<String, String> careTips;
    private Timestamp timestamp;

    public Information() {
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

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public List<String> getSpecialFeatures() {
        return specialFeatures;
    }

    public void setSpecialFeatures(List<String> specialFeatures) {
        this.specialFeatures = specialFeatures;
    }

    public Map<String, String> getCareTips() {
        return careTips;
    }

    public void setCareTips(Map<String, String> careTips) {
        this.careTips = careTips;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}