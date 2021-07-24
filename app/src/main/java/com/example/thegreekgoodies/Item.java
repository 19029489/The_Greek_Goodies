package com.example.thegreekgoodies;
import java.io.Serializable;


//=====================HandleProducts======================
public class Item implements Serializable {
    private int _id;
    String itemName;
    String itemDetails;
    String itemPhoto;
    int category;
    double itemPrice;

    public Item(int _id, String itemName, String itemDetails, String itemPhoto, int category, double itemPrice) {
        this._id=_id;
        this.itemName = itemName;
        this.itemDetails = itemDetails;
        this.itemPhoto = itemPhoto;
        this.category = category;
        this.itemPrice = itemPrice;
    }

    public int get_id() {
        return _id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getItemDetails() {
        return itemDetails;
    }

    public String getItemPhoto() {
        return itemPhoto;
    }

    public int getCategory() {
        return category;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
//=====================HandleProducts======================