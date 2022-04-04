package kz.mussin.ar.main.objects;

public class Toy {
    public String id;
    public String name;
    public String price;
    public String image;
    public String description;
    public String gltf;

    public Toy() {
    }

    public Toy(String id, String name, String price, String image, String description, String gltf) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.description = description;
        this.gltf = gltf;
    }
}
