package io.github.whataa.picer.picer;


public class Picture {
    private String name;
    private long addTime;
    private String type;
    private String size;
    private String path;
    private String folderPath;
    private boolean isChosen;
    private boolean isPriview;

    public boolean isPriview() {
        return isPriview;
    }

    public void setPriview(boolean priview) {
        isPriview = priview;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getName() {

        return name;
    }

    public long getAddTime() {
        return addTime;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
