/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author arzugurbanova
 */
@Entity
@Table(name = "cb_ekfgroup_to_updated_bx_1c")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findAll", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findById", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.id = :id"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByArtikul", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.artikul = :artikul"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByQuant", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.quant = :quant"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByBasePrice", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.basePrice = :basePrice"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByIshopPrice", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.ishopPrice = :ishopPrice"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByBamount", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.bamount = :bamount"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByBisprice", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.bisprice = :bisprice"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByBbsprice", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.bbsprice = :bbsprice"),
    @NamedQuery(name = "CbEkfgroupToUpdatedBx1c.findByName", query = "SELECT c FROM CbEkfgroupToUpdatedBx1c c WHERE c.name = :name")})
public class CbEkfgroupToUpdatedBx1c implements Serializable {
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_prop_cnt")
    private int bxPropCnt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f1c_prop_cnt")
    private int f1cPropCnt;
    
    @Size(max = 1000)
    @Column(name = "f1c_main_pict")
    private String f1cMainPict;
    @Size(max = 2000)
    @Column(name = "bx_main_pict")
    private String bxMainPict;
    @Size(max = 2000)
    @Column(name = "bx_add_pict1")
    private String bxAddPict1;
    @Size(max = 1000)
    @Column(name = "f1c_add_pict1")
    private String f1cAddPict1;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "f1c_sort_order")
    private int f1cSortOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_sort_order")
    private int bxSortOrder;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    @Column(name = "last_ekfgr_exch_dt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastEkfgrExchDt;
    @Size(max = 2147483647)
    @Column(name = "json_data")
    private String jsonData;
    @Column(name = "to_update")
    private Short toUpdate;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 512)
    @Column(name = "bx_name")
    private String bxName;

    @Size(max = 255)
    @Column(name = "parent_1c_id")
    private String parent1cId;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "f1c_id")
    private String f1cId;
    @Size(max = 36)
    @Column(name = "bx_group_external_code")
    private String bxGroupExternalCode;
    @Size(max = 36)
    @Column(name = "product_group_id")
    private String productGroupId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 150)
    @Column(name = "short_name")
    private String shortName;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private int id;
    @Size(max = 255)
    @Column(name = "artikul")
    private String artikul;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "quant")
    private BigDecimal quant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "base_price")
    private BigDecimal basePrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ishop_price")
    private BigDecimal ishopPrice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bamount")
    private BigDecimal bamount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bisprice")
    private BigDecimal bisprice;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bbsprice")
    private BigDecimal bbsprice;
    @Size(max = 512)
    @Column(name = "name")
    private String name;

    public CbEkfgroupToUpdatedBx1c() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArtikul() {
        return artikul;
    }

    public void setArtikul(String artikul) {
        this.artikul = artikul;
    }

    public BigDecimal getQuant() {
        return quant;
    }

    public void setQuant(BigDecimal quant) {
        this.quant = quant;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getIshopPrice() {
        return ishopPrice;
    }

    public void setIshopPrice(BigDecimal ishopPrice) {
        this.ishopPrice = ishopPrice;
    }

    public BigDecimal getBamount() {
        return bamount;
    }

    public void setBamount(BigDecimal bamount) {
        this.bamount = bamount;
    }

    public BigDecimal getBisprice() {
        return bisprice;
    }

    public void setBisprice(BigDecimal bisprice) {
        this.bisprice = bisprice;
    }

    public BigDecimal getBbsprice() {
        return bbsprice;
    }

    public void setBbsprice(BigDecimal bbsprice) {
        this.bbsprice = bbsprice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getF1cId() {
        return f1cId;
    }

    public void setF1cId(String f1cId) {
        this.f1cId = f1cId;
    }

    public String getBxGroupExternalCode() {
        return bxGroupExternalCode;
    }

    public void setBxGroupExternalCode(String bxGroupExternalCode) {
        this.bxGroupExternalCode = bxGroupExternalCode;
    }

    public String getProductGroupId() {
        return productGroupId;
    }

    public void setProductGroupId(String productGroupId) {
        this.productGroupId = productGroupId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getParent1cId() {
        return parent1cId;
    }

    public void setParent1cId(String parent1cId) {
        this.parent1cId = parent1cId;
    }

    public String getBxName() {
        return bxName;
    }

    public void setBxName(String bxName) {
        this.bxName = bxName;
    }
    
    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getLastEkfgrExchDt() {
        return lastEkfgrExchDt;
    }

    public void setLastEkfgrExchDt(Date lastEkfgrExchDt) {
        this.lastEkfgrExchDt = lastEkfgrExchDt;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public Short getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(Short toUpdate) {
        this.toUpdate = toUpdate;
    }
    
    public int getF1cSortOrder() {
        return f1cSortOrder;
    }

    public void setF1cSortOrder(int f1cSortOrder) {
        this.f1cSortOrder = f1cSortOrder;
    }

    public int getBxSortOrder() {
        return bxSortOrder;
    }

    public void setBxSortOrder(int bxSortOrder) {
        this.bxSortOrder = bxSortOrder;
    }
    
    public String getF1cMainPict() {
        return f1cMainPict;
    }

    public void setF1cMainPict(String f1cMainPict) {
        this.f1cMainPict = f1cMainPict;
    }

    public String getBxMainPict() {
        return bxMainPict;
    }

    public void setBxMainPict(String bxMainPict) {
        this.bxMainPict = bxMainPict;
    }

    public String getBxAddPict1() {
        return bxAddPict1;
    }

    public void setBxAddPict1(String bxAddPict1) {
        this.bxAddPict1 = bxAddPict1;
    }

    public String getF1cAddPict1() {
        return f1cAddPict1;
    }

    public void setF1cAddPict1(String f1cAddPict1) {
        this.f1cAddPict1 = f1cAddPict1;
    }
    
    public int getBxPropCnt() {
        return bxPropCnt;
    }

    public void setBxPropCnt(int bxPropCnt) {
        this.bxPropCnt = bxPropCnt;
    }

    public int getF1cPropCnt() {
        return f1cPropCnt;
    }

    public void setF1cPropCnt(int f1cPropCnt) {
        this.f1cPropCnt = f1cPropCnt;
    }
    
}
