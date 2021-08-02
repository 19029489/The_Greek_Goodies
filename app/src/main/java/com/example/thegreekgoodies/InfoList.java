package com.example.thegreekgoodies;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class InfoList extends Fragment {

    ImageView imageView;
    ImageButton plusQuantity, minusQuantity;
    TextView quantityNumber, itemName, itemPrice, itemDetails, pricePerPack, priceDesc;
    Button addToCart;
    int quantity = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_info_list, container, false);

        //===================Casting=====================
        itemDetails = v.findViewById(R.id.descriptioninfo);
        imageView = v.findViewById(R.id.imageViewInfo);
        plusQuantity = v.findViewById(R.id.addquantity);
        minusQuantity  = v.findViewById(R.id.subquantity);
        quantityNumber = v.findViewById(R.id.quantity);
        addToCart = v.findViewById(R.id.addtocart);
        itemName = v.findViewById(R.id.ItemNameinInfo);
        itemPrice = v.findViewById(R.id.ItemPrice);
        pricePerPack = v.findViewById(R.id.PricePerPack);
        priceDesc = v.findViewById(R.id.ItemPriceDescription);
        //===================Casting=====================

        //------------------GetIntentData---------------------
        Item itemData;
        Intent i = getActivity().getIntent();
        itemData = (Item) i.getSerializableExtra("positionData");
        //------------------GetIntentData---------------------

        //------------------UISetup-------------
        quantityNumber.setText(String.valueOf(quantity));
        Glide.with(this).load(itemData.getItemPhoto()).into(imageView);
        itemName.setText(itemData.getItemName());
        itemDetails.setText(itemData.getItemDetails());
        itemPrice.setText(String.format("%.2f", itemData.getItemPrice()));
        pricePerPack.setText("Price per pack: " + String.format("$%.2f",itemData.getItemPrice()));
        //------------------UISetup-------------

        //==========================PlusButton============================
        plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double basePrice = itemData.itemPrice;
                quantity++;
                displayQuantity();
                double ItemPrice = basePrice * quantity;
                String setNewPrice = String.format("%.2f",ItemPrice);
                itemPrice.setText(setNewPrice);
            }
        });
        //==========================PlusButton============================
        //==========================MinusButton============================
        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double basePrice = itemData.itemPrice;
                if (quantity < 1) {
                    Toast.makeText(getActivity(), "Cant decrease quantity < 0", Toast.LENGTH_SHORT).show();
                } else {
                    quantity--;
                    displayQuantity();
                    double ItemPrice = basePrice * quantity;
                    String setNewPrice = String.format("%.2f",ItemPrice);
                    itemPrice.setText(setNewPrice);
                }
            }
        });
        //==========================MinusButton============================

        //===========================AddToCartTempDataBase=============================
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(itemPrice);
                //------------------AddCusDataBase-------------------
                //------------------DataToBeAdded--------------------
                String pname = itemName.getText().toString();
                int quantity = Integer.parseInt(quantityNumber.getText().toString());
                double price = Double.parseDouble(itemPrice.getText().toString());
                //------------------DataToBeAdded--------------------
                if (quantity >= 1) {
                    DBCusOrderTemp dbc = new DBCusOrderTemp(getActivity());
                    long inserted_id = dbc.insertCustomer(pname, quantity, price);
                    dbc.close();
                    //------------------AddCusDataBase-------------------
                    Intent intent = new Intent(getActivity(), Summarylist.class);
                    startActivity(intent);
                    if (inserted_id != -1) {
                        Toast.makeText(getActivity(), "Success adding to Cart",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed to add to Cart",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                //===========================CatchPreventAdd0===================
                else {
                    Toast.makeText(getActivity(), "Quantity cannot be 0",
                            Toast.LENGTH_SHORT).show();
                }
                //===========================CatchPreventAdd0===================
            }
        });
        //===========================AddToCartTempDataBase=============================


        return v;
    }

    //==================FunctionZone========================
    private void displayQuantity() {
        quantityNumber.setText(String.valueOf(quantity));
    }
    //==================FunctionZone========================


}