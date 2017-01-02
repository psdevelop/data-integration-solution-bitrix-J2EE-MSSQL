/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Poltarokov SP
 */
@Entity
@Table(name = "bx_bsect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "BxBsect.findAll", query = "SELECT b FROM BxBsect b"),
    @NamedQuery(name = "BxBsect.findById", query = "SELECT b FROM BxBsect b WHERE b.id = :id"),
    @NamedQuery(name = "BxBsect.findByBxId", query = "SELECT b FROM BxBsect b WHERE b.bxId = :bxId"),
    @NamedQuery(name = "BxBsect.findByF1cId", query = "SELECT b FROM BxBsect b WHERE b.f1cId = :f1cId"),
    @NamedQuery(name = "BxBsect.findByName", query = "SELECT b FROM BxBsect b WHERE b.name = :name"),
    @NamedQuery(name = "BxBsect.findByParentBxId", query = "SELECT b FROM BxBsect b WHERE b.parentBxId = :parentBxId")})
public class BxBsect implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_id")
    private int bxId;
    @Basic(optional = false)
    @Size(min = 1, max = 255)
    @Column(name = "f1c_id")
    private String f1cId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "parent_bx_id")
    private int parentBxId;

    public BxBsect() {
    }

    public BxBsect(Integer id) {
        this.id = id;
    }

    public BxBsect(Integer id, int bxId, String f1cId, String name, int parentBxId) {
        this.id = id;
        this.bxId = bxId;
        this.f1cId = f1cId;
        this.name = name;
        this.parentBxId = parentBxId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getBxId() {
        return bxId;
    }

    public void setBxId(int bxId) {
        this.bxId = bxId;
    }

    public String getF1cId() {
        return f1cId;
    }

    public void setF1cId(String f1cId) {
        this.f1cId = f1cId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentBxId() {
        return parentBxId;
    }

    public void setParentBxId(int parentBxId) {
        this.parentBxId = parentBxId;
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
        if (!(object instanceof BxBsect)) {
            return false;
        }
        BxBsect other = (BxBsect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.BxBsect[ id=" + id + " ]";
    }
    
}
