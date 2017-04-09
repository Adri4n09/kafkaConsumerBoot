package com.example.model;

public class Book {

    private String isbn;
    private String title;
    private String author;
    private String genre;
    private int quantity;
    private double price;

    public Book() {

    }

    public Book(String isbn, int quantity, String genre, String author, String title, double price) {
        this.isbn = isbn;
        this.quantity = quantity;
        this.genre = genre;
        this.author = author;
        this.title = title;
        this.price = price;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
