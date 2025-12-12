package CdiBean;

import EJB.UserBeanLocal;
import Entity.Voters;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Named
@SessionScoped
public class VoterRegistrationBean implements Serializable {

    @Inject
    private UserBeanLocal userBean;

    private String voterName, email, city, address;
    private long mobileNumber, adharNumber;
    private int pincode;
    private Date dob;

    private Part adharFile;
    private Part voterImage;

    // Folder path relative to deployment
    private String UPLOAD_DIR = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/Uploads");

    public String registerVoter() {
        FacesContext context = FacesContext.getCurrentInstance();

        try {
            // 1️⃣ Validation
            if (voterName == null || voterName.isEmpty() ||
                email == null || email.isEmpty() ||
                city == null || city.isEmpty() ||
                address == null || address.isEmpty() ||
                dob == null ||
                adharFile == null ||
                voterImage == null ||
                mobileNumber == 0 ||
                adharNumber == 0 ||
                pincode == 0) {

                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "All fields are required!", ""));
                return null;
            }

            // 2️⃣ Age validation
            long ageInMillis = new Date().getTime() - dob.getTime();
            long age = TimeUnit.MILLISECONDS.toDays(ageInMillis) / 365;
            if (age < 18) {
                context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Voter must be 18+ years old!", ""));
                return null;
            }

            // 3️⃣ File upload
            String adharFileName = extractFileName(adharFile);
            String voterImgName = extractFileName(voterImage);

            File adharDest = new File(UPLOAD_DIR, adharFileName);
            File voterImgDest = new File(UPLOAD_DIR, voterImgName);

            saveFile(adharFile, adharDest);
            saveFile(voterImage, voterImgDest);

            // 4️⃣ Call EJB to persist
            userBean.addVoter(
                voterName, mobileNumber, adharNumber, email, dob, city, pincode,
                address, "Uploads/" + adharFileName, "Uploads/" + voterImgName
            );

            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Your Voter Registration Form has been submitted successfully.Please visit the website after 7 days Using Aadhaar Number download your Voter Card ,Thank you!!", ""));
            
            
            // Clear form
            clearForm();
            return null;

        } catch (Exception e) {
            e.printStackTrace();
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error: " + e.getMessage(), ""));
            return null;
        }
    }

    private void saveFile(Part part, File dest) throws Exception {
        try (InputStream is = part.getInputStream(); FileOutputStream fos = new FileOutputStream(dest)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String s : contentDisp.split(";")) {
            if (s.trim().startsWith("filename")) {
                return new File(s.split("=")[1].replace("\"", "")).getName();
            }
        }
        return null;
    }

    private void clearForm() {
        voterName = email = city = address = null;
        dob = null;
        mobileNumber = adharNumber = pincode = 0;
        adharFile = voterImage = null;
    }

    // ✅ Getters & Setters

    public String getVoterName() { return voterName; }
    public void setVoterName(String voterName) { this.voterName = voterName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public long getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(long mobileNumber) { this.mobileNumber = mobileNumber; }

    public long getAdharNumber() { return adharNumber; }
    public void setAdharNumber(long adharNumber) { this.adharNumber = adharNumber; }

    public int getPincode() { return pincode; }
    public void setPincode(int pincode) { this.pincode = pincode; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public Part getAdharFile() { return adharFile; }
    public void setAdharFile(Part adharFile) { this.adharFile = adharFile; }

    public Part getVoterImage() { return voterImage; }
    public void setVoterImage(Part voterImage) { this.voterImage = voterImage; }
}
