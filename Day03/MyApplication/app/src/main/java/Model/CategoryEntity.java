package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryEntity {
    private int index;
    @SerializedName("List")
    private List<AnimationEntity> list;

    public int getIndex() {
        return index;
    }

    public List<AnimationEntity> getList() {
        return list;
    }
}
