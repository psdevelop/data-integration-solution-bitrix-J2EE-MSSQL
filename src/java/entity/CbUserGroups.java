/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Poltarokov SP
 */
@Entity
@Table(name = "cb_user_groups")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbUserGroups.findAll", query = "SELECT c FROM CbUserGroups c"),
    @NamedQuery(name = "CbUserGroups.findById", query = "SELECT c FROM CbUserGroups c WHERE c.id = :id"),
    @NamedQuery(name = "CbUserGroups.findByName", query = "SELECT c FROM CbUserGroups c WHERE c.name = :name"),
    @NamedQuery(name = "CbUserGroups.findByCode", query = "SELECT c FROM CbUserGroups c WHERE c.code = :code")})
public class CbUserGroups implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "code")
    private String code;
    @OneToMany(mappedBy = "userGroupId")
    private Collection<CbUsers> cbUsersCollection;

    public CbUserGroups() {
    }

    public CbUserGroups(Integer id) {
        this.id = id;
    }

    public CbUserGroups(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @XmlTransient
    public Collection<CbUsers> getCbUsersCollection() {
        return cbUsersCollection;
    }

    public void setCbUsersCollection(Collection<CbUsers> cbUsersCollection) {
        this.cbUsersCollection = cbUsersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CbUserGroups)) {
            return false;
        }
        CbUserGroups other = (CbUserGroups) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbUserGroups[ id=" + id + " ]";
    }
    
}
