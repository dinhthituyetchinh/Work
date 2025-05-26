package Model;

public class Child2 {
    private String imgChild;
    private String name; // Optional

    public Child2(String imgChild, String name) {
        this.imgChild = imgChild;
        this.name = name;
    }

    public String getImgChild() {
        return imgChild;
    }

    public void setImgChild(String imgChild) {
        this.imgChild = imgChild;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

// file json nhu sau
//    "Animal": {
//        "Index": 0,
//                "List": [
//        {
//            "imgChild": "https://link-to-image1.jpg",
//                "name": "Animal 1"
//        },
//        {
//            "imgChild": "https://link-to-image2.jpg",
//                "name": "Animal 2"
//        }
//  ]
//    }

}
