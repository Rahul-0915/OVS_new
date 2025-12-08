package CdiBean;

import EJB.UserBeanLocal;
import Entity.Voters;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named("downloadCard")
@SessionScoped
public class DownloadCard implements Serializable {

    @Inject
    private UserBeanLocal userBean;

    private boolean openPopup = false;
    private String inputLabel;     // "Voter ID" OR "Aadhar Number"
    private String inputValue;

    private Voters voter;

    private boolean notFound = false;
    private boolean pending = false;
    private boolean approved = false;

    // OPEN POPUP
    public void openVoterPopup() {
        inputLabel = "Voter ID";
        inputValue = "";
        openPopup = true;
    }

    public void openAdharPopup() {
        inputLabel = "Aadhar Number";
        inputValue = "";
        openPopup = true;
    }

    public void closePopup() {
        openPopup = false;
    }

    // SEARCH
    public void searchVoter() {

        notFound = pending = approved = false;

        try {
            if (inputLabel.equals("Voter ID")) {
                int id = Integer.parseInt(inputValue.trim());
                voter = userBean.findByVoterId(id);

            } else {
                long adhar = Long.parseLong(inputValue.trim());
                voter = userBean.findByAdharNumber(adhar);
            }

            openPopup = false;

            if (voter == null) {
                notFound = true;
                return;
            }

            if (voter.getStatus() == 0) {
                pending = true;
            } else {
                approved = true;
            }

        } catch (Exception e) {
            notFound = true;
        }
    }
 public String forceRefresh() {
    jakarta.faces.context.FacesContext.getCurrentInstance()
        .getExternalContext()
        .invalidateSession();

    return "downloadCard.jsf?faces-redirect=true";
}


    // GETTERS
    public boolean isOpenPopup() { return openPopup; }
    public String getInputLabel() { return inputLabel; }
    public String getInputValue() { return inputValue; }
    public void setInputValue(String inputValue) { this.inputValue = inputValue; }

    public boolean isNotFound() { return notFound; }
    public boolean isPending() { return pending; }
    public boolean isApproved() { return approved; }

    public Voters getVoter() { return voter; }
}
