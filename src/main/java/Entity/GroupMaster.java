/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author Rahul
 */
@Entity
@Table(name = "group_master")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupMaster.findAll", query = "SELECT g FROM GroupMaster g"),
    @NamedQuery(name = "GroupMaster.findByGId", query = "SELECT g FROM GroupMaster g WHERE g.gId = :gId"),
    @NamedQuery(name = "GroupMaster.findByGName", query = "SELECT g FROM GroupMaster g WHERE g.gName = :gName")})
public class GroupMaster implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "g_id")
    private Integer gId;
    @Size(max = 20)
    @Column(name = "g_name")
    private String gName;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupmaster")
    private Collection<Users> usersCollection;

    public GroupMaster() {
    }

    public Integer getgId() {
        return gId;
    }

    public void setgId(Integer gId) {
        this.gId = gId;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (gId != null ? gId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupMaster)) {
            return false;
        }
        GroupMaster other = (GroupMaster) object;
        if ((this.gId == null && other.gId != null) || (this.gId != null && !this.gId.equals(other.gId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.GroupMaster[ gId=" + gId + " ]";
    }

}
