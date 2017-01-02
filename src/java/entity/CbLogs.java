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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
 * @author Poltarokov SP
 */
@Entity
@Table(name = "cb_logs")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbLogs.findAll", query = "SELECT c FROM CbLogs c ORDER BY c.id DESC"),
    @NamedQuery(name = "CbLogs.findById", query = "SELECT c FROM CbLogs c WHERE c.id = :id"),
    @NamedQuery(name = "CbLogs.findByDt", query = "SELECT c FROM CbLogs c WHERE c.dt = :dt"),
    @NamedQuery(name = "CbLogs.findByLogText", query = "SELECT c FROM CbLogs c WHERE c.logText = :logText"),
    @NamedQuery(name = "CbLogs.findByCaption", query = "SELECT c FROM CbLogs c WHERE c.caption = :caption"),
    @NamedQuery(name = "CbLogs.findByLogType", query = "SELECT c FROM CbLogs c WHERE c.logType = :logType ORDER BY c.id DESC")})
public class CbLogs implements Serializable {

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private CbUsers userId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dt;
    @Size(max = 2147483647)
    @Column(name = "log_text")
    private String logText;
    @Size(max = 255)
    @Column(name = "caption")
    private String caption;
    @Size(max = 20)
    @Column(name = "log_type")
    private String logType;

    public CbLogs() {
    }

    public CbLogs(Integer id) {
        this.id = id;
    }

    public CbLogs(Integer id, Date dt) {
        this.id = id;
        this.dt = dt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
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
        if (!(object instanceof CbLogs)) {
            return false;
        }
        CbLogs other = (CbLogs) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbLogs[ id=" + id + " ]";
    }

    public CbUsers getUserId() {
        return userId;
    }

    public void setUserId(CbUsers userId) {
        this.userId = userId;
    }
    
}
