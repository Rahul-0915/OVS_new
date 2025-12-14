package CdiBean;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import java.util.ArrayList;
import java.util.List;

@Named("newsCdiBean")
@ApplicationScoped
public class NewsCdiBean {

    private List<String> newsList = new ArrayList<>();
    private String newMessage;

    public List<String> getNewsList() {
        return newsList;
    }

    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }

    // naya news add karega (top pe)
    public void addNews() {
        if (newMessage != null && !newMessage.trim().isEmpty()) {
            newsList.add(0, newMessage.trim());
            newMessage = "";
        }
    }

    // saari news clear
    public void clearAll() {
        newsList.clear();
    }
}
