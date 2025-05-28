package Model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Child {
    @SerializedName("static")
    private List<String> staticImages;

    @SerializedName("gif")
    private List<String> gifImages;

    public List<String> getStaticImages() {
        return staticImages;
    }

    public List<String> getGifImages() {
        return gifImages;
    }

    public void setStaticImages(List<String> staticImages) {
        this.staticImages = staticImages;
    }

    public void setGifImages(List<String> gifImages) {
        this.gifImages = gifImages;
    }
}

