package cheap.steel.www.hshsteel.model;

public class ItemObjects {
    private String name, photo, file;

    public ItemObjects() {
    }

    public ItemObjects(String name, String photo, String file) {
        this.name = name;
        this.photo = photo;
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }
}