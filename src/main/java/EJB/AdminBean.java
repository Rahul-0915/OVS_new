/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Candidates;
import Entity.Elections;
import Entity.Party;
import jakarta.annotation.security.DeclareRoles;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Rahul
 */
@Stateless
@DeclareRoles({"admin", "user"})
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "OVS")
    EntityManager em;

    @Override
    // candidates...ejb ................................................................................
    public void addCandidates(int cId, String candidateName, Date nominationDate, int eId, int pId) {
        try {
            Elections e = em.find(Elections.class, eId);
            Party p = em.find(Party.class, pId);

            // 🛑 Validation checks
            if (e == null) {
                throw new IllegalArgumentException("Invalid Election ID: " + eId);
            }
            if (p == null) {
                throw new IllegalArgumentException("Invalid Party ID: " + pId);
            }

            Candidates existing = em.find(Candidates.class, cId);
            if (existing != null) {
                throw new IllegalArgumentException("Candidate ID already exists: " + cId);
            }

            Candidates candidate = new Candidates();
            candidate.setCandidateId(cId);
            candidate.setCandidateName(candidateName);
            candidate.setNominationDate(nominationDate);
            candidate.setElections(e);
            candidate.setParty(p);

            if (e.getCandidateCollection() != null) {
                e.getCandidateCollection().add(candidate);
            }

            if (p.getCandidatesCollection() != null) {
                p.getCandidatesCollection().add(candidate);
            }

            em.persist(candidate);
            em.flush(); // forces write

            System.out.println("Candidate added successfully: " + candidateName);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error while adding candidate: " + ex.getMessage());
        }
    }

    @Override
    public void updateCandidate(int cId, String candidateName, Date nominationDate, int eId, int pId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Candidates candidate = em.find(Candidates.class, cId);
            if (candidate == null) {
                throw new IllegalArgumentException("Candidate not found with ID: " + cId);
            }

            Elections e = em.find(Elections.class, eId);
            Party p = em.find(Party.class, pId);

            if (e != null) {
                candidate.setElections(e);
            }
            if (p != null) {
                candidate.setParty(p);
            }
            candidate.setCandidateName(candidateName);
            candidate.setNominationDate(nominationDate);

            em.merge(candidate);
            em.flush();

            System.out.println("Candidate updated successfully: " + cId);

        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error updating candidate: " + ex.getMessage());
        }
    }

    @Override
    public void deleteCandidate(int cId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Candidates c = em.find(Candidates.class, cId);
        if (c != null) {
            em.remove(c); // delete
            System.out.println("Candidate deleted successfully: " + cId);
        } else {
            System.out.println("Candidate not found: " + cId);
        }

    }

    @Override
    public Collection<Candidates> findByElectionId(int eId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT  c FROM Candidates c WHERE c.elections.electionId=:eid", Candidates.class)
                .setParameter("eid", eId)
                .getResultList();
    }

    @Override
    public Collection<Candidates> findByPartyId(int pId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT c FROM Candidates c WHERE c.party.partyId=:pid", Candidates.class)
                .setParameter("pid", pId)
                .getResultList();
    }

    @Override
    public Collection<Candidates> getAllCandidates() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT c FROM Candidates c", Candidates.class).getResultList();
    }

    @Override
    public Candidates findByCandidateId(int cId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.find(Candidates.class, cId);
    }

    @Override
    public Collection<Candidates> findByCandidateName(String candidateName) {
        return em.createQuery(
                "SELECT c FROM Candidates c WHERE LOWER(c.candidateName) LIKE LOWER(:name)",
                Candidates.class
        )
                .setParameter("name", "%" + candidateName + "%")
                .getResultList();
    }

    // elections.. ejb.................................................................................
    @Override
    public void addElections(Integer eId, java.sql.Date startDate, java.sql.Date endDate, String electionName, String status, String desc) {

        try {
            Elections election = new Elections();
            election.setElectionId(eId);
            election.setStartDate(startDate);
            election.setEndDate(endDate);
            election.setElectionName(electionName);
            election.setStatus(status);
            election.setDescription(desc);

            em.persist(election);
            em.flush();

            System.out.println("Election added successfully: " + electionName);

        } catch (Exception e) {
            System.err.println("Error adding election: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void updateElection(Integer eId, java.sql.Date startDate, java.sql.Date endDate, String electionName, String status, String desc) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Elections e = em.find(Elections.class, eId);
        if (e != null) {
            e.setStartDate(startDate);
            e.setEndDate(endDate);
            e.setElectionName(electionName);
            e.setStatus(status);
            e.setDescription(desc);
            em.merge(e);

        }

    }

    @Override
    public Collection<Elections> getAllElections() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        return em.createQuery("SELECT e FROM Elections e", Elections.class)
                .getResultList();
    }

    @Override
    public Elections getElectionById(int eId) {
        return em.find(Elections.class, eId);
    }

    @Override
    public void deleteElection(int eId) {
        Elections e = em.find(Elections.class, eId); // DB se fetch
        if (e != null) {
            em.remove(e); // delete
            System.out.println("Election deleted successfully: " + eId);
        } else {
            System.out.println("Election not found: " + eId);
        }
    }

    @Override
    public Collection<Elections> findByName(String electionName) {
        // JPQL query to find elections by name (partial match)
        return em.createQuery("SELECT e FROM Elections e WHERE e.electionName LIKE :name", Elections.class)
                .setParameter("name", "%" + electionName + "%") // partial match
                .getResultList();
    }

    @Override
    public Collection<Elections> findByStartDate(java.sql.Date startDate) {
        return em.createQuery("SELECT e FROM Elections e WHERE e.startDate = :startDate", Elections.class)
                .setParameter("startDate", startDate)
                .getResultList();
    }

    @Override
    public Collection<Elections> findByEndDate(java.sql.Date endDate) {
        return em.createQuery("SELECT e FROM Elections e WHERE e.endDate = :endDate", Elections.class)
                .setParameter("endDate", endDate)
                .getResultList();
    }
//party ejb.......................................

    @Override
    public void addParty(Integer partyId, String partyName, String partySymbol, String leaderName, Integer foundedYear) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        Party p = new Party();
        p.setPartyId(partyId);
        p.setPartyName(partyName);
        p.setPartySymbol(partySymbol);
        p.setLeaderName(leaderName);
        p.setFoundedYear(foundedYear);
        if (p != null) {
            em.persist(p);
        }
    }

    @Override
    public Collection<Party> getAllParties() {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
         return em.createQuery("SELECT p FROM Party p", Party.class).getResultList();
    }

    @Override
    public Party findByPartyId(Integer partyId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    return em.find(Party.class, partyId);
    }

    @Override
    public Collection<Party> findByPartyName(String partyName) {
        return em.createQuery("SELECT p FROM Party p WHERE LOWER(p.partyName) LIKE LOWER(:name)", Party.class)
                 .setParameter("name", "%" + partyName + "%")
                 .getResultList();
    }

    @Override
    public Collection<Party> findByLeaderName(String leaderName) {
        return em.createQuery("SELECT p FROM Party p WHERE LOWER(p.leaderName) LIKE LOWER(:leader)", Party.class)
                 .setParameter("leader", "%" + leaderName + "%")
                 .getResultList();
    }

    @Override
    public Collection<Party> findByFoundedYear(Integer foundedYear) {
        return em.createQuery("SELECT p FROM Party p WHERE p.foundedYear = :year", Party.class)
                 .setParameter("year", foundedYear)
                 .getResultList();
    }

    @Override
    public void updateParty(Integer partyId, String partyName, String partySymbol, String leaderName, Integer foundedYear) {
        Party p = em.find(Party.class, partyId);
        if (p != null) {
            p.setPartyName(partyName);
            p.setLeaderName(leaderName);
            p.setFoundedYear(foundedYear);
            p.setPartySymbol(partySymbol);
            em.merge(p);
        }
    }

    @Override
    public void deleteParty(Integer partyId) {
        Party p = em.find(Party.class, partyId);
        if (p != null) {
            em.remove(p);
        }   
    }
    @RolesAllowed("Admin")
//@PermitAll  
    //@DenyAll   
    public String saySecureHello() {
        return "Secure Hello from Secure Bean";
    }
}
