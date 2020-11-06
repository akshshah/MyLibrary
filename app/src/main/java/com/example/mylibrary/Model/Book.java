package com.example.mylibrary.Model;

public class Book {
    private int id;
    private String name;
    private String author;
    private String language;
    private int pages;
    private String description;
    private String imageURL;
    private boolean isFavourite;
    private String status;

    public Book() {
    }

    public Book(int id, String name, String author, String language,
                int pages, String description, String imageURL, boolean isFavourite, String status) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.language = language;
        this.pages = pages;
        this.description = description;
        this.imageURL = imageURL;
        this.isFavourite = isFavourite;
        this.status = status;
    }

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
