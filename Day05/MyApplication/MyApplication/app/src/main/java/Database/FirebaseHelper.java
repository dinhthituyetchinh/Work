package Database;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.List;
import java.util.Map;

import Model.AnimationEntity;
import Model.CategoryEntity;

public class FirebaseHelper {

    public interface OnDataFetchedListener {
        void onDataFetched(Map<String, CategoryEntity> data);
        void onError(Exception e);
    }
    public static Map<String, CategoryEntity> parseJsonToCategoryMap(String jsonString) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, CategoryEntity>>() {}.getType();
        return gson.fromJson(jsonString, type);
    }

    public static void fetchAllParents(OnDataFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("raw").document("json3")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Map<String, Object> rawMap = documentSnapshot.getData(); // Raw Firestore map
                    Map<String, CategoryEntity> categoryMap = new java.util.HashMap<>();

                    if (rawMap != null) {
                        Gson gson = new Gson();
                        for (Map.Entry<String, Object> entry : rawMap.entrySet()) {
                            // Convert each object to JSON string
                            String json = gson.toJson(entry.getValue());

                            // Parse JSON to CategoryEntity
                            CategoryEntity entity = gson.fromJson(json, CategoryEntity.class);

                            // Put into result map
                            categoryMap.put(entry.getKey(), entity);
                        }

                        // Callback with result
                        listener.onDataFetched(categoryMap);
                    }
                })
                .addOnFailureListener(listener::onError);
    }



}