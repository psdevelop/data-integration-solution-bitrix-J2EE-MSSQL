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
@Table(name = "tables_operating_state")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TablesOperatingState.findAll", query = "SELECT t FROM TablesOperatingState t"),
    @NamedQuery(name = "TablesOperatingState.findById", query = "SELECT t FROM TablesOperatingState t WHERE t.id = :id"),
    @NamedQuery(name = "TablesOperatingState.findByTableName", query = "SELECT t FROM TablesOperatingState t WHERE t.tableName = :tableName"),
    @NamedQuery(name = "TablesOperatingState.findByInuseby1C", query = "SELECT t FROM TablesOperatingState t WHERE t.inuseby1C = :inuseby1C"),
    @NamedQuery(name = "TablesOperatingState.findByInUseByCb", query = "SELECT t FROM TablesOperatingState t WHERE t.inUseByCb = :inUseByCb")})
public class TablesOperatingState implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "table_name")
    private String tableName;
    @Basic(optional = false)
    @NotNull
    @Column(name = "in_use_by_1C")
    private short inuseby1C;
    @Basic(optional = false)
    @NotNull
    @Column(name = "in_use_by_cb")
    private short inUseByCb;

    public TablesOperatingState() {
    }

    public TablesOperatingState(Integer id) {
        this.id = id;
    }

    public TablesOperatingState(Integer id, String tableName, short inuseby1C, short inUseByCb) {
        this.id = id;
        this.tableName = tableName;
        this.inuseby1C = inuseby1C;
        this.inUseByCb = inUseByCb;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public short getInuseby1C() {
        return inuseby1C;
    }

    public void setInuseby1C(short inuseby1C) {
        this.inuseby1C = inuseby1C;
    }

    public short getInUseByCb() {
        return inUseByCb;
    }

    public void setInUseByCb(short inUseByCb) {
        this.inUseByCb = inUseByCb;
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
        if (!(object instanceof TablesOperatingState)) {
            return false;
        }
        TablesOperatingState other = (TablesOperatingState) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.TablesOperatingState[ id=" + id + " ]";
    }
    
}
