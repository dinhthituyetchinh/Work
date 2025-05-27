package Model;

import java.util.List;

public class Parent {
    private String title;
    private int index;
    private List<Child> listItemChild;

    public Parent(String title, int index, List<Child> listItemChild) {
        this.title = title;
        this.index = index;
        this.listItemChild = listItemChild;
    }

    public String getTitle() {
        return title;
    }

    public int getIndex() {
        return index;
    }

    public List<Child> getListItemChild() {
        return listItemChild;
    }
}
