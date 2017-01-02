/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "bx_1cprod")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bx1CProd.findAll", query = "SELECT b FROM Bx1CProd b"),
    @NamedQuery(name = "Bx1CProd.findById", query = "SELECT b FROM Bx1CProd b WHERE b.id = :id"),
    @NamedQuery(name = "Bx1CProd.findByBxId", query = "SELECT b FROM Bx1CProd b WHERE b.bxId = :bxId"),
    @NamedQuery(name = "Bx1CProd.findByName", query = "SELECT b FROM Bx1CProd b WHERE b.name = :name"),
    @NamedQuery(name = "Bx1CProd.findByParentBxId", query = "SELECT b FROM Bx1CProd b WHERE b.parentBxId = :parentBxId"),
    @NamedQuery(name = "Bx1CProd.findByF1cId", query = "SELECT b FROM Bx1CProd b WHERE b.f1cId = :f1cId"),
    @NamedQuery(name = "Bx1CProd.findByParent1cId", query = "SELECT b FROM Bx1CProd b WHERE b.parent1cId = :parent1cId"),
    @NamedQuery(name = "Bx1CProd.findByArtikul", query = "SELECT b FROM Bx1CProd b WHERE b.artikul = :artikul"),
    @NamedQuery(name = "Bx1CProd.findByPrice", query = "SELECT b FROM Bx1CProd b WHERE b.price = :price"),
    @NamedQuery(name = "Bx1CProd.findByAmount", query = "SELECT b FROM Bx1CProd b WHERE b.amount = :amount")})
public class Bx1CProd implements Serializable {

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
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "name")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Column(name = "parent_bx_id")
    private int parentBxId;
    @Size(max = 255)
    @Column(name = "f1c_id")
    private String f1cId;
    @Size(max = 255)
    @Column(name = "parent_1c_id")
    private String parent1cId;
    @Size(max = 255)
    @Column(name = "artikul")
    private String artikul;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "amount")
    private BigDecimal amount;
    @Column(name = "bprice")
    private BigDecimal bprice;

    public Bx1CProd() {
    }

    public Bx1CProd(Integer id) {
        this.id = id;
    }

    public Bx1CProd(Integer id, int bxId, String name, int parentBxId) {
        this.id = id;
        this.bxId = bxId;
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

    public String getF1cId() {
        return f1cId;
    }

    public void setF1cId(String f1cId) {
        this.f1cId = f1cId;
    }

    public String getParent1cId() {
        return parent1cId;
    }

    public void setParent1cId(String parent1cId) {
        this.parent1cId = parent1cId;
    }

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public BigDecimal getBprice() {
        return bprice;
    }

    public void setBprice(BigDecimal bprice) {
        this.bprice = bprice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
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
        if (!(object instanceof Bx1CProd)) {
            return false;
        }
        Bx1CProd other = (Bx1CProd) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bx1CProd[ id=" + id + " ]";
    }
    
}
