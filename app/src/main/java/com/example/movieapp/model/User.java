package com.example.movieapp.model;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User implements Serializable {
    private int id;
    private String email;
    private List<Movie> wishlist;
    private List<Movie> cart;
    private String name;
    private String address;
    private String gender;
    private List<OrderHistory> orderHistory;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public User(String email, List<Movie> wishlist, List<Movie> cart, String name, String address, String gender, List<OrderHistory> orderHistory) {
        this.email = email;
        this.wishlist = wishlist;
        this.cart = cart;
        this.name = name;
        this.address = address;
        this.gender = gender;
        this.orderHistory = orderHistory;
    }

    public User(String email, List<Movie> wishlist, List<Movie> cart, String name, String address, String gender) {
        this.email = email;
        this.wishlist = wishlist;
        this.cart = cart;
        this.name = name;
        this.address = address;
        this.gender = gender;
    }

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

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }
}
