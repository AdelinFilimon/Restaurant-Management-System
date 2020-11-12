package data;

import business.Restaurant;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public final class RestaurantSerializator {

    private static final RestaurantSerializator singleObject = new RestaurantSerializator();

    private RestaurantSerializator() {}


    public static RestaurantSerializator getInstance() {
        return singleObject;
    }

    public void serialize(String filename, Restaurant restaurant) {

        try {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(restaurant);
            out.close();
            file.close();
        } catch (Exception e) {
            System.out.println("Serialization error: " + e);
        }
    }

    public Restaurant deserialize(String filename) {
        try {
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            Restaurant restaurant = (Restaurant)in.readObject();
            in.close();
            file.close();
            return restaurant;
        } catch (Exception e) {
            System.out.println("Deserialization error: " + e);
            return null;
        }
    }
}
