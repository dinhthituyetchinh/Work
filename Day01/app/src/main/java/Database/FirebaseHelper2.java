package Database;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import Model.Child;
import Model.Child2;
import Model.Parent;
import Model.Parent2;

public class FirebaseHelper2 {
    public interface OnDataFetchedListener {
        void onDataFetched(List<Parent2> data);
        void onError(Exception e);
    }

    public static void fetchAllParents(OnDataFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("raw2").document("json2")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<ParentWrapper> wrapperList = new ArrayList<>();

                    if (documentSnapshot.exists()) {
                        Map<String, Object> dataMap = documentSnapshot.getData();

                        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                            String fieldName = entry.getKey();
                            Object value = entry.getValue();

                            if (value instanceof Map) {
                                Map<String, Object> innerMap = (Map<String, Object>) value;

                                Long indexLong = innerMap.get("Index") instanceof Long
                                        ? (Long) innerMap.get("Index")
                                        : 0L;

                                int index = indexLong.intValue();

                                // Get "List" field as List<Map<String, Object>>
                                List<Map<String, Object>> rawList = (List<Map<String, Object>>) innerMap.get("List");
                                List<Child2> childList = new ArrayList<>();

                                for (Map<String, Object> item : rawList) {
                                    String img = (String) item.get("imgChild");
                                    String name = (String) item.get("name");
                                    childList.add(new Child2(img, name));
                                }

                                Parent2 parent = new Parent2(fieldName, childList);
                                wrapperList.add(new ParentWrapper(parent, index));
                            }
                        }
                    }

                    Collections.sort(wrapperList, Comparator.comparingInt(p -> p.index));

                    List<Parent2> sortedParents = new ArrayList<>();
                    for (ParentWrapper wrapper : wrapperList) {
                        sortedParents.add(wrapper.parent);
                    }

                    listener.onDataFetched(sortedParents);
                })
                .addOnFailureListener(listener::onError);
    }

    private static class ParentWrapper {
        Parent2 parent;
        int index;

        ParentWrapper(Parent2 parent, int index) {
            this.parent = parent;
            this.index = index;
        }
    }
}
