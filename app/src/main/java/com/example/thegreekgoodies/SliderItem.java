package com.example.thegreekgoodies;

public class SliderItem {

    //To load images from the internet,
    // use string variable to store url

    private int image;

    SliderItem(int image){
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
