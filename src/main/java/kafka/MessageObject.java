package kafka;

import lombok.Data;

@Data
public class MessageObject {

    private String nameMethod;
    private String userEmail;

    public MessageObject() {
    }

    public MessageObject(String nameMethod, String userEmail) {
        this.nameMethod = nameMethod;
        this.userEmail = userEmail;
    }

    public String getNameMethod() {
        return nameMethod;
    }

    public void setNameMethod(String nameMethod) {
        this.nameMethod = nameMethod;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}