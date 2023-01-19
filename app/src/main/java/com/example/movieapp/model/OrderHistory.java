package com.example.movieapp.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderHistory implements Serializable {
    private List<Movie> order;
    private String totalAmount;
    private String date;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public OrderHistory(List<Movie> order, String totalAmount, String date, String status) {
        this.order = order;
        this.totalAmount = totalAmount;
        this.date = date;
        this.status = status;
    }

    public OrderHistory(){
        order = new ArrayList<>();
        totalAmount = "";
        date = "";
    }
    public List<Movie> getOrder() {
        return order;
    }

    public void setOrder(List<Movie> order) {
        this.order = order;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public OrderHistory(List<Movie> order, String totalAmount, String date) {
        this.order = order;
        this.totalAmount = totalAmount;
        this.date = date;
    }
}
