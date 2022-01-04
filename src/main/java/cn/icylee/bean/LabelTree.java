package cn.icylee.bean;

import java.util.List;

public class LabelTree {
    private int id;

    private int value;

    private String label;

    private String is;

    private String icon;

    private List<LabelTree> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getIs() {
        return is;
    }

    public void setIs(String is) {
        this.is = is;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<LabelTree> getChildren() {
        return children;
    }

    public void setChildren(List<LabelTree> children) {
        this.children = children;
    }
}
