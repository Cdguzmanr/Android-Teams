package edu.fvtc.teams;

import android.graphics.Bitmap;

public class Team {
    private int id;
    private String name;
    private String city;
    private String cellPhone;
    private float  rating;
    private int imgId;
    private boolean isFavorite;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    private Bitmap photo;

    public Team()
    {
        this.id = -1;
        this.name = "";
        this.city = "";
        this.cellPhone = "";
        this.rating = 0.0f;
        this.imgId = 0;
        this.isFavorite = false;
    }

    public Team(int id,
                String name,
                String city,
                String cellPhone,
                float rating,
                boolean isFavorite,
                int imgId)
    {
        this.id = id;
        this.name = name;
        this.city = city;
        this.cellPhone = cellPhone;
        this.rating = rating;
        this.imgId = imgId;
        this.isFavorite = isFavorite;
    }

    public void setControlText(int controlId, String value)
    {
        if(controlId == R.id.etName)
        {
            this.setName(value);
        } else if (controlId == R.id.etCity) {
            this.setCity(value);
        }
        else
        {
            this.setCellPhone(value);
        }

    }

    @Override
    public String toString()
    {
        return String.valueOf(id) + '|' +
                name + '|' +
                city + '|' +
                cellPhone + '|' +
                rating + '|' +
                isFavorite + '|' +
                imgId;
    }
}
