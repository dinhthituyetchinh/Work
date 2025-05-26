package Model;


import java.util.List;

public class Parent2 {
    private String title;
    private List<Child2> listItemChild;

    public Parent2(String title, List<Child2> listItemChild) {
        this.title = title;
        this.listItemChild = listItemChild;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Child2> getListItemChild() {
        return listItemChild;
    }

    public void setListItemChild(List<Child2> listItemChild) {
        this.listItemChild = listItemChild;
    }
}
