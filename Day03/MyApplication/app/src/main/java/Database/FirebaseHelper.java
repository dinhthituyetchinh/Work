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
//    public static void fetchAllParents(OnDataFetchedListener listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("raw").document("json3")
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    String json=documentSnapshot.getData().toString();
//                    Map<String, CategoryEntity> categoryMap = parseJsonToCategoryMap(json);
//                    Log.d("FirebaseHelper", "JSON: " + json);
//                    listener.onDataFetched(categoryMap);
//                })
//                .addOnFailureListener(listener::onError);
//    }

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


//    public static void fetchAllParents(OnDataFetchedListener listener) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("raw").document("json2")
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    List<Parent> parents = new ArrayList<>();
//                    Gson gson = new Gson();
//                    Type listType = new TypeToken<List<Child>>() {}.getType();
//
//                    if (documentSnapshot.exists()) {
//                        Map<String, Object> dataMap = documentSnapshot.getData();
//
//                        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
//                            String title = entry.getKey();
//                            Map<String, Object> value = (Map<String, Object>) entry.getValue();
//
//                            // Get the JSON string from "List" field
//                            String jsonList = (String) value.get("List");
//
//                            // Convert to List<Child>
//                            List<Child> children = gson.fromJson(jsonList, listType);
//
//                            // Get index safely
//                            int index = ((Long) value.get("Index")).intValue();
//
//                            Log.d("FirebaseHelper", "Title: " + title);
//                            Log.d("FirebaseHelper", "Index: " + index);
//                            Log.d("FirebaseHelper", "Children size: " + (children != null ? children.size() : 0));
//
//                            if (children != null) {
//                                for (Child child : children) {
//                                    Log.d("FirebaseHelper", "  Static: " + child.getStaticImages());
//                                    Log.d("FirebaseHelper", "  Gif: " + child.getGifImages());
//                                }
//                            }
//
//                            // Add Parent object
//                            parents.add(new Parent(title, index, children));
//                        }
//
//                        // Sort by index
//                        Collections.sort(parents, Comparator.comparingInt(Parent::getIndex));
//                        listener.onDataFetched(parents);
//
//                    } else {
//                        listener.onDataFetched(parents); // Return empty list
//                    }
//                })
//                .addOnFailureListener(listener::onError);
//    }
}