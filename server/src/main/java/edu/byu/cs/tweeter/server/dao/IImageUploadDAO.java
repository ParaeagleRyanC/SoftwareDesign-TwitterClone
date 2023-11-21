package edu.byu.cs.tweeter.server.dao;

public interface IImageUploadDAO {
    // return the url string to where to image is stored
    String uploadImage(String image, String alias);
}
