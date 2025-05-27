package Database;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import Model.Child;
import Model.Parent;

public class FirebaseHelper {

    public interface OnDataFetchedListener {
        void onDataFetched(List<Parent> data);
        void onError(Exception e);
    }

    public static void fetchAllParents(OnDataFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("raw").document("json2")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<Parent> parents = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Child>>() {}.getType();

                    if (documentSnapshot.exists()) {
                        Map<String, Object> dataMap = documentSnapshot.getData();

                        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                            String title = entry.getKey();
                            Map<String, Object> value = (Map<String, Object>) entry.getValue();

                            String jsonList = (String) value.get("List");
                            int index = ((Long) value.get("Index")).intValue();

                            // Parse list of children
                            List<Child> children = gson.fromJson(jsonList, listType);

                            // ✅ Log dữ liệu
                            Log.d("FirebaseHelper", "Title: " + title);
                            Log.d("FirebaseHelper", "Index: " + index);
                            Log.d("FirebaseHelper", "Children size: " + (children != null ? children.size() : 0));

                            if (children != null) {
                                for (Child child : children) {
                                    Log.d("FirebaseHelper", "  Static: " + child.getStaticImages() + ", Gif: " + child.getGifImages());
                                }
                            }

                            // Add to parent list
                            parents.add(new Parent(title, index, children));
                        }
                    }

                    // Sort by index
                    Collections.sort(parents, Comparator.comparingInt(Parent::getIndex));
                    listener.onDataFetched(parents);
                })
                .addOnFailureListener(listener::onError);
    }
}
