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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cb_users_activity")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbUsersActivity.findAll", query = "SELECT c FROM CbUsersActivity c"),
    @NamedQuery(name = "CbUsersActivity.findById", query = "SELECT c FROM CbUsersActivity c WHERE c.id = :id"),
    @NamedQuery(name = "CbUsersActivity.findByObjectName", query = "SELECT c FROM CbUsersActivity c WHERE c.objectName = :objectName"),
    @NamedQuery(name = "CbUsersActivity.findByActivityCounter", query = "SELECT c FROM CbUsersActivity c WHERE c.activityCounter = :activityCounter")})
public class CbUsersActivity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "object_name")
    private String objectName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "activity_counter")
    private int activityCounter;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CbUsers userId;

    public CbUsersActivity() {
    }

    public CbUsersActivity(Integer id) {
        this.id = id;
    }

    public CbUsersActivity(Integer id, String objectName, int activityCounter) {
        this.id = id;
        this.objectName = objectName;
        this.activityCounter = activityCounter;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public int getActivityCounter() {
        return activityCounter;
    }

    public void setActivityCounter(int activityCounter) {
        this.activityCounter = activityCounter;
    }

    public CbUsers getUserId() {
        return userId;
    }

    public void setUserId(CbUsers userId) {
        this.userId = userId;
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
        if (!(object instanceof CbUsersActivity)) {
            return false;
        }
        CbUsersActivity other = (CbUsersActivity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbUsersActivity[ id=" + id + " ]";
    }
    
}
