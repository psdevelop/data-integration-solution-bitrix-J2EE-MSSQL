/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author s.poltarokov
 */
@Entity
@Table(name = "cb_settings")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbSettings.findAll", query = "SELECT c FROM CbSettings c"),
    @NamedQuery(name = "CbSettings.findById", query = "SELECT c FROM CbSettings c WHERE c.id = :id"),
    @NamedQuery(name = "CbSettings.findByName", query = "SELECT c FROM CbSettings c WHERE c.name = :name"),
    @NamedQuery(name = "CbSettings.findByIntVal", query = "SELECT c FROM CbSettings c WHERE c.intVal = :intVal"),
    @NamedQuery(name = "CbSettings.findByStrVal", query = "SELECT c FROM CbSettings c WHERE c.strVal = :strVal"),
    @NamedQuery(name = "CbSettings.findByDtVal", query = "SELECT c FROM CbSettings c WHERE c.dtVal = :dtVal")})
public class CbSettings implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "int_val")
    private int intVal;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "str_val")
    private String strVal;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_val")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dtVal;

    public CbSettings() {
    }

    public CbSettings(Integer id) {
        this.id = id;
    }

    public CbSettings(Integer id, String name, int intVal, String strVal, Date dtVal) {
        this.id = id;
        this.name = name;
        this.intVal = intVal;
        this.strVal = strVal;
        this.dtVal = dtVal;
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

    public int getIntVal() {
        return intVal;
    }

    public void setIntVal(int intVal) {
        this.intVal = intVal;
    }

    public String getStrVal() {
        return strVal;
    }

    public void setStrVal(String strVal) {
        this.strVal = strVal;
    }

    public Date getDtVal() {
        return dtVal;
    }

    public void setDtVal(Date dtVal) {
        this.dtVal = dtVal;
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
        if (!(object instanceof CbSettings)) {
            return false;
        }
        CbSettings other = (CbSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbSettings[ id=" + id + " ]";
    }
    
}
