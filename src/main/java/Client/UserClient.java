/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:UserRest [User]<br>
 * USAGE:
 * <pre>
 *        UserClient client = new UserClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Rahul
 */
public class UserClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:1509/Online_Voting_System/webresources";

    public UserClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("User");
    }
    
    static {
        //for localhost testing only
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                new javax.net.ssl.HostnameVerifier() {

            public boolean verify(String hostname,
                    javax.net.ssl.SSLSession sslSession) {
                if (hostname.equals("localhost")) {
                    return true;
                }
                return false;
            }
        });
    }

    public String addVote(String voterId, String candidateId, String partyId, String electionId, String verifyStatus, String electionName) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addvote/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{voterId, candidateId, partyId, electionId, verifyStatus, electionName})).request().post(null, String.class);
    }

    public String test() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("test");
        return resource.request().get(String.class);
    }

    public String findByVoteId(String voteId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byvoteid/{0}", new Object[]{voteId}));
        return resource.request().get(String.class);
    }

    public String addUser(String userName, String email, String mobile, String password, String gId, String verify) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("adduser/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{userName, email, mobile, password, gId, verify})).request().post(null, String.class);
    }

    public String updateVoter(String voterId, String voterName, String mobileNumber, String adharNumber, String emailId, String dob, String city, String pincode, String address, String adharFile, String voterImage) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updatevoter/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}/{8}/{9}/{10}", new Object[]{voterId, voterName, mobileNumber, adharNumber, emailId, dob, city, pincode, address, adharFile, voterImage})).request().put(null, String.class);
    }

    public String findByElectionId(String eid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byelection/{0}", new Object[]{eid}));
        return resource.request().get(String.class);
    }

    public String addVoter(String voterName, String mobileNumber, String adharNumber, String emailId, String dob, String city, String pincode, String address, String adharFile, String voterImage) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("addvoter/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}/{8}/{9}", new Object[]{voterName, mobileNumber, adharNumber, emailId, dob, city, pincode, address, adharFile, voterImage})).request().post(null, String.class);
    }

    public String findByStatus(String status) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("voterbystatus/{0}", new Object[]{status}));
        return resource.request().get(String.class);
    }

    public String findByCity(String city) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("voterbycity/{0}", new Object[]{city}));
        return resource.request().get(String.class);
    }

    public String getAllVotes() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getall");
        return resource.request().get(String.class);
    }

    public String getAllVoters() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("allvoters");
        return resource.request().get(String.class);
    }

    public String findByElectionName(String ename) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byelectionname/{0}", new Object[]{ename}));
        return resource.request().get(String.class);
    }

    public String findByCandidateId(String cid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("bycandidate/{0}", new Object[]{cid}));
        return resource.request().get(String.class);
    }

    public String deleteVoter(String voterId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("deletevoter/{0}", new Object[]{voterId})).request().delete(String.class);
    }

    public String user() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("user");
        return resource.request().get(String.class);
    }

    public String findByVoterId(String voterId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("votersbyid/{0}", new Object[]{voterId}));
        return resource.request().get(String.class);
    }

    public String findByPartyId(String pid) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("byparty/{0}", new Object[]{pid}));
        return resource.request().get(String.class);
    }

    public void close() {
        client.close();
    }

}
