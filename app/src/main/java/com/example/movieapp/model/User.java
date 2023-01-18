package com.example.movieapp.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User {
    private int id;
    private String email;
    private List<Movie> wishlist;
    private List<Movie> cart;

    public User(String email, List<Movie> wishlist, List<Movie> cart) {
        this.email = email;
        this.wishlist = wishlist;
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Movie> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<Movie> wishlist) {
        this.wishlist = wishlist;
    }

    public List<Movie> getCart() {
        return cart;
    }

    public void setCart(List<Movie> cart) {
        this.cart = cart;
    }
}
