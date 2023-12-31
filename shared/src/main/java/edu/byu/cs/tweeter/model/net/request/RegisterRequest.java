package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {

    private String alias;
    private String password;
    private String firstName;
    private String lastName;
    private String image;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private RegisterRequest() {}

    /**
     * Creates an instance.
     *
     * @param username the username of the user to be registered.
     * @param password the password of the user to be registered.
     */
    public RegisterRequest(String username, String password, String firstName, String lastName, String image) {
        this.alias = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String username) {
        this.alias = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
