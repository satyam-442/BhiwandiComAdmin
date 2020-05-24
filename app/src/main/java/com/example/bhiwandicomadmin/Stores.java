package com.example.bhiwandicomadmin;

public class Stores {
public String ShopName,OwnerName,fromTime,toTime,ShopAddress,image, OwnerPhone;

    public Stores() {
    }

    public Stores(String shopName, String ownerName, String fromTime, String toTime, String shopAddress, String image, String ownerPhone) {
        ShopName = shopName;
        OwnerName = ownerName;
        this.fromTime = fromTime;
        this.toTime = toTime;
        ShopAddress = shopAddress;
        this.image = image;
        OwnerPhone = ownerPhone;
    }

    public String getShopNamee() {
        return ShopName;
    }

    public void setShopNamee(String shopName) {
        ShopName = shopName;
    }

    public String getOwnerNamee() {
        return OwnerName;
    }

    public void setOwnerNamee(String ownerName) {
        OwnerName = ownerName;
    }

    public String getFromTimee() {
        return fromTime;
    }

    public void setFromTimee(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTimee() {
        return toTime;
    }

    public void setToTimee(String toTime) {
        this.toTime = toTime;
    }

    public String getShopAddresss() {
        return ShopAddress;
    }

    public void setShopAddresss(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public String getImagee() {
        return image;
    }

    public void setImagee(String image) {
        this.image = image;
    }

    public String getOwnerPhonee() {
        return OwnerPhone;
    }

    public void setOwnerPhonee(String ownerPhone) {
        OwnerPhone = ownerPhone;
    }
}

