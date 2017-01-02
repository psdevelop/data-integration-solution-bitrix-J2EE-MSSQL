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
@Table(name = "cb_ekfgroup_add_1csect_to_bx")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbEkfgroupAdd1csectToBx.findAll", query = "SELECT c FROM CbEkfgroupAdd1csectToBx c"),
    @NamedQuery(name = "CbEkfgroupAdd1csectToBx.findById", query = "SELECT c FROM CbEkfgroupAdd1csectToBx c WHERE c.id = :id"),
    @NamedQuery(name = "CbEkfgroupAdd1csectToBx.findByName", query = "SELECT c FROM CbEkfgroupAdd1csectToBx c WHERE c.name = :name"),
    @NamedQuery(name = "CbEkfgroupAdd1csectToBx.findByParentId", query = "SELECT c FROM CbEkfgroupAdd1csectToBx c WHERE c.parentId = :parentId")})
public class CbEkfgroupAdd1csectToBx implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "id")
    private String id;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 36)
    @Column(name = "parent_id")
    private String parentId;

    public CbEkfgroupAdd1csectToBx() {
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
}
