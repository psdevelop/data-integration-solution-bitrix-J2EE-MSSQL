/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "cb_privilegies")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbPrivilegies.findAll", query = "SELECT c FROM CbPrivilegies c"),
    @NamedQuery(name = "CbPrivilegies.findById", query = "SELECT c FROM CbPrivilegies c WHERE c.id = :id"),
    @NamedQuery(name = "CbPrivilegies.findByName", query = "SELECT c FROM CbPrivilegies c WHERE c.name = :name"),
    @NamedQuery(name = "CbPrivilegies.findByQuotaCoeff", query = "SELECT c FROM CbPrivilegies c WHERE c.quotaCoeff = :quotaCoeff")})
public class CbPrivilegies implements Serializable {

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
    @Column(name = "quota_coeff")
    private int quotaCoeff;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "privilegyId")
    private Collection<CbUsers> cbUsersCollection;

    public CbPrivilegies() {
    }

    public CbPrivilegies(Integer id) {
        this.id = id;
    }

    public CbPrivilegies(Integer id, String name, int quotaCoeff) {
        this.id = id;
        this.name = name;
        this.quotaCoeff = quotaCoeff;
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

    public int getQuotaCoeff() {
        return quotaCoeff;
    }

    public void setQuotaCoeff(int quotaCoeff) {
        this.quotaCoeff = quotaCoeff;
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
        if (!(object instanceof CbPrivilegies)) {
            return false;
        }
        CbPrivilegies other = (CbPrivilegies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbPrivilegies[ id=" + id + " ]";
    }
    
}
