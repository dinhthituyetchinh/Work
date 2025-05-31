package Database;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;


import Model.CategoryEntity;

public class FirebaseHelper {

    public interface OnListDataFetchedListener {
        void onDataFetched(List<CategoryEntity> categoryList);
        void onError(Exception e);
    }

    public static void fetchAllParents(OnListDataFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("raw").document("json4")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Object rawJson = documentSnapshot.get("Category");
                        if (rawJson != null) {
                            Gson gson = new Gson();
                            String jsonArrayString = gson.toJson(rawJson);

                            Type listType = new TypeToken<List<CategoryEntity>>() {}.getType();
                            List<CategoryEntity> categoryList = gson.fromJson(jsonArrayString, listType);

                            listener.onDataFetched(categoryList);
                        } else {
                            listener.onDataFetched(new ArrayList<>());
                        }
                    }
                })
                .addOnFailureListener(listener::onError);
    }


}