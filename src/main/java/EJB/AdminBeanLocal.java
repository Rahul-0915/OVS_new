/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Local
public interface AdminBeanLocal {

    // elections 
    public void addElections(Integer eId, java.sql.Date startDate, java.sql.Date endDate, String electionName, String status, String desc);

    public void updateElection(Integer eId, java.sql.Date startDate, java.sql.Date endDate, String electionName, String status, String desc);

    public Collection<Elections> getAllElections();

    public Elections getElectionById(int eId);

    public void deleteElection(int eId);

    public Collection<Elections> findByName(String electionName);

    public Collection<Elections> findByStartDate(java.sql.Date startDate);

    public Collection<Elections> findByEndDate(java.sql.Date endDate);

    //candidates
    public void addCandidates(int cId, String CandidateName, Date NomininationDate, int eId, int pId);

    void updateCandidate(int cId, String candidateName, Date nominationDate, int eId, int pId);

    void deleteCandidate(int cId);

    Candidates findByCandidateId(int cId);

    Collection<Candidates> findByCandidateName(String candidateName);

    Collection<Candidates> findByElectionId(int eId);

    Collection<Candidates> findByPartyId(int pId);

    Collection<Candidates> getAllCandidates();

//   Party ..........
    void addParty(Integer partyId, String partyName, String partySymbol, String leaderName, Integer foundedYear);

    Collection<Party> getAllParties();

    Party findByPartyId(Integer partyId);

    Collection<Party> findByPartyName(String partyName);

    Collection<Party> findByLeaderName(String leaderName);

    Collection<Party> findByFoundedYear(Integer foundedYear);

    void updateParty(Integer partyId, String partyName, String leaderName, String partySymbolPath, Integer foundedYear);

    void deleteParty(Integer partyId);
    
    public String saySecureHello();

}
