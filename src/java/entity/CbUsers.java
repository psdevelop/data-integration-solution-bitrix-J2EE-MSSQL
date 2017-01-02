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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "cb_users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbUsers.findAll", query = "SELECT c FROM CbUsers c"),
    @NamedQuery(name = "CbUsers.findById", query = "SELECT c FROM CbUsers c WHERE c.id = :id"),
    @NamedQuery(name = "CbUsers.findByName", query = "SELECT c FROM CbUsers c WHERE c.name = :name"),
    @NamedQuery(name = "CbUsers.findByEkfapikey", query = "SELECT c FROM CbUsers c WHERE c.ekfapikey = :ekfapikey"),
    @NamedQuery(name = "CbUsers.findByPwd", query = "SELECT c FROM CbUsers c WHERE c.pwd = :pwd"),
    @NamedQuery(name = "CbUsers.findByLogin", query = "SELECT c FROM CbUsers c WHERE c.login = :login")})
public class CbUsers implements Serializable {

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
    @Size(max = 255)
    @Column(name = "ekfapikey")
    private String ekfapikey;
    @Size(max = 255)
    @Column(name = "pwd")
    private String pwd;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "login")
    private String login;
    @JoinColumn(name = "privilegy_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private CbPrivilegies privilegyId;
    @JoinColumn(name = "user_group_id", referencedColumnName = "id")
    @ManyToOne
    private CbUserGroups userGroupId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<CbUsersActivity> cbUsersActivityCollection;
    @OneToMany(mappedBy = "userId")
    private Collection<CbLogs> cbLogsCollection;

    public CbUsers() {
    }

    public CbUsers(Integer id) {
        this.id = id;
    }

    public CbUsers(Integer id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
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

    public String getEkfapikey() {
        return ekfapikey;
    }

    public void setEkfapikey(String ekfapikey) {
        this.ekfapikey = ekfapikey;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public CbPrivilegies getPrivilegyId() {
        return privilegyId;
    }

    public void setPrivilegyId(CbPrivilegies privilegyId) {
        this.privilegyId = privilegyId;
    }

    public CbUserGroups getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(CbUserGroups userGroupId) {
        this.userGroupId = userGroupId;
    }

    @XmlTransient
    public Collection<CbUsersActivity> getCbUsersActivityCollection() {
        return cbUsersActivityCollection;
    }

    public void setCbUsersActivityCollection(Collection<CbUsersActivity> cbUsersActivityCollection) {
        this.cbUsersActivityCollection = cbUsersActivityCollection;
    }

    @XmlTransient
    public Collection<CbLogs> getCbLogsCollection() {
        return cbLogsCollection;
    }

    public void setCbLogsCollection(Collection<CbLogs> cbLogsCollection) {
        this.cbLogsCollection = cbLogsCollection;
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
        if (!(object instanceof CbUsers)) {
            return false;
        }
        CbUsers other = (CbUsers) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.CbUsers[ id=" + id + " ]";
    }
    
}
