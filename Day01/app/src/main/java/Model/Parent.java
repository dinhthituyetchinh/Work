package Model;

import java.util.List;

public class Parent {
    private String title;
    private List<String> listItemChild;

    public Parent(String title, List<String> listItemChild) {
        this.title = title;
        this.listItemChild = listItemChild;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getListItemChild() {
        return listItemChild;
    }

    public void setListItemChild(List<String> listItemChild) {
        this.listItemChild = listItemChild;
    }
}
