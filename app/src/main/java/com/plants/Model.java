package com.plants;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Model {
    public static CollectionReference getGrowth() {
        return FirebaseFirestore.getInstance().collection("growth");
    }

    public static CollectionReference getInformation() {
        return FirebaseFirestore.getInstance().collection("information");
    }
}