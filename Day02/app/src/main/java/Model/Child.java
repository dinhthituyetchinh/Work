package Model;


import java.util.List;

public class Child {
    private List<String> staticImages;
    private List<String> gifImages;

    public List<String> getStaticImages() {
        return staticImages;
    }

    public void setStaticImages(List<String> staticImages) {
        this.staticImages = staticImages;
    }

    public List<String> getGifImages() {
        return gifImages;
    }

    public void setGifImages(List<String> gifImages) {
        this.gifImages = gifImages;
    }
}

