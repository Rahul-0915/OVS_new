package controller;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class PageController implements Serializable {

    private String currentPage = "/pages/Dashboard.xhtml";

    public String getCurrentPage() {
        return currentPage;
    }

    public void setPage(String page) {
        this.currentPage = page;
    }
}
