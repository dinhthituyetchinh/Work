package Database;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Model.Parent;

public class FirebaseHelper {
    // Giao diện callback kết quả
    public interface OnDataFetchedListener {
        void onDataFetched(List<Parent> data);
        void onError(Exception e);
    }

    // Hàm chính để lấy dữ liệu từ Firestore
    public static void fetchAllParents(OnDataFetchedListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("raw").document("json")
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    List<Parent> parentList = new ArrayList<>();

                    if (documentSnapshot.exists()) {
                        Map<String, Object> dataMap = documentSnapshot.getData();

                        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                            String fieldName = entry.getKey(); // ví dụ: "Animal"
                            Object value = entry.getValue();

                            if (value instanceof Map) {
                                Map<String, Object> innerMap = (Map<String, Object>) value;

                                // Lấy chuỗi list JSON từ trường "List"
                                String jsonList = (String) innerMap.get("List");

                                // Convert thành List<String>
                                List<String> childList = parseJsonList(jsonList);

                                // Tạo Parent object
                                Parent parent = new Parent(fieldName, childList);
                                parentList.add(parent);
                            }
                        }
                    }

                    // Gọi callback khi thành công
                    listener.onDataFetched(parentList);
                })
                .addOnFailureListener(listener::onError);
    }

    // Hàm parse chuỗi JSON đơn giản như ["a1", "a2"] => List<String>
    private static List<String> parseJsonList(String json) {
        List<String> result = new ArrayList<>();

        if (json == null || json.isEmpty()) return result;

        json = json.replace("[", "").replace("]", "").replace("\"", "");
        String[] items = json.split(",");

        for (String item : items) {
            if (!item.trim().isEmpty()) {
                result.add(item.trim());
            }
        }

        return result;
    }
}