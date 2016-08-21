package io.github.whataa.picer.picer;


public class Folder {

    public static final String PATH_OF_ALL = "path_of_all";
    public static final String NAME_OF_ALL = "全部";

    private String name;
    private String path;
    private boolean isCurrent;

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {

        return path;
    }

    public String getName() {
        return name;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }
}
