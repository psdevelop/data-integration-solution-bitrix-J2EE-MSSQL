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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author arzugurbanova
 */
@Entity
@Table(name = "cb_ekfgroup_del_1csect_from_bx")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbEkfgroupDel1csectFromBx.findAll", query = "SELECT c FROM CbEkfgroupDel1csectFromBx c"),
    @NamedQuery(name = "CbEkfgroupDel1csectFromBx.findById", query = "SELECT c FROM CbEkfgroupDel1csectFromBx c WHERE c.id = :id"),
    @NamedQuery(name = "CbEkfgroupDel1csectFromBx.findByName", query = "SELECT c FROM CbEkfgroupDel1csectFromBx c WHERE c.name = :name")})
public class CbEkfgroupDel1csectFromBx implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_id")
    private int bxId;

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 255)
    @Column(name = "id")
    private String id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;

    public CbEkfgroupDel1csectFromBx() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBxId() {
        return bxId;
    }

    public void setBxId(int bxId) {
        this.bxId = bxId;
    }
    
}
