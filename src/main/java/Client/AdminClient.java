/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;

/**
 * Jersey REST client generated for REST resource:AdminRest [Admin]<br>
 * USAGE:
 * <pre>
 *        AdminClient client = new AdminClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author Rahul
 */
public class AdminClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "https://localhost:1509/OVS/resources";

    public AdminClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("Admin");
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
    public String getElectionById(String eId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("election/{0}", new Object[]{eId}));
       return resource.request().get(String.class);
    }

    public String findByElection(String eId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("candidatebyelection/{0}", new Object[]{eId}));
       return resource.request().get(String.class);
    }

    public String getAllElections() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("electionsall");
       return resource.request().get(String.class);
    }

    public String findByParty(String pId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("candidatebyparty/{0}", new Object[]{pId}));
       return resource.request().get(String.class);
    }

    public String findByCandidateName(String name) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("candidatebyname/{0}", new Object[]{name}));
       return resource.request().get(String.class);
    }

    public String getElectionsByName(String electionName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("electionsbyname/{0}", new Object[]{electionName}));
       return resource.request().get(String.class);
    }

    public String getElectionsByEndDate(String endDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("electionsbyend/{0}", new Object[]{endDate}));
       return resource.request().get(String.class);
    }

    public String updateCandidate(String cId, String candidateName, String nominationDate, String eId, String pId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("candidateupdate/{0}/{1}/{2}/{3}/{4}", new Object[]{cId, candidateName, nominationDate, eId, pId})).request().put(null, String.class);
    }

    public String updateElection(String eId, String startDate, String endDate, String electionName, String status, String desc) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("electionsupdate/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{eId, startDate, endDate, electionName, status, desc})).request().put(null, String.class);
    }

    public String findByPartyId(String partyId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("partybyid/{0}", new Object[]{partyId}));
       return resource.request().get(String.class);
    }

    public String sayHello() throws ClientErrorException {
        WebTarget resource = webTarget;
       return resource.request().get(String.class);
    }

    public String findByFoundedYear(String foundedYear) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("partybyyear/{0}", new Object[]{foundedYear}));
       return resource.request().get(String.class);
    }

    public String deleteCandidate(String cId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("candidatedelete/{0}", new Object[]{cId})).request().delete(String.class);
    }

    public String updateParty(String partyId, String partyName, String partySymbol, String leaderName, String foundedYear) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("partyupdate/{0}/{1}/{2}/{3}/{4}", new Object[]{partyId, partyName, partySymbol, leaderName, foundedYear})).request().put(null, String.class);
    }

    public String addElection(String eId, String startDate, String endDate, String electionName, String status, String desc) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("electionsadd/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{eId, startDate, endDate, electionName, status, desc})).request().post(null, String.class);
    }

    public String findByLeader(String leaderName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("partybyleader/{0}", new Object[]{leaderName}));
       return resource.request().get(String.class);
    }

    public String deleteElection(String eId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("electiondelete/{0}", new Object[]{eId})).request().delete(String.class);
    }

    public String findByCandidateId(String cId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("candidatebyid/{0}", new Object[]{cId}));
       return resource.request().get(String.class);
    }

    public String getElectionsByStartDate(String startDate) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("electionsbystart/{0}", new Object[]{startDate}));
       return resource.request().get(String.class);
    }

    public String addCandidate(String candidateId, String candidateName, String nominationDate, String eId, String pId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("candidateadd/{0}/{1}/{2}/{3}/{4}", new Object[]{candidateId, candidateName, nominationDate, eId, pId})).request().post(null, String.class);
    }

    public String findByPartyName(String partyName) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("partybyname/{0}", new Object[]{partyName}));
       return resource.request().get(String.class);
    }

    public String deleteParty(String partyId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("partydelete/{0}", new Object[]{partyId})).request().delete(String.class);
    }

    public String getAllCandidates() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("candidatesall");
       return resource.request().get(String.class);
    }

    public String getAllParties() throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getallparty");
       return resource.request().get(String.class);
    }

    public void addParty(String partyid, String partyname, String partysymbol, String leadername, String foundedyear) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("addparty/{0}/{1}/{2}/{3}/{4}", new Object[]{partyid, partyname, partysymbol, leadername, foundedyear})).request().post(null);
    }

    public void close() {
        client.close();
    }
    
}
