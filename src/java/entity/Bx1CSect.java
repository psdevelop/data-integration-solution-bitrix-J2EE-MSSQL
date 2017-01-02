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
@Table(name = "bx_1csect")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bx1CSect.findAll", query = "SELECT b FROM Bx1CSect b"),
    @NamedQuery(name = "Bx1CSect.findById", query = "SELECT b FROM Bx1CSect b WHERE b.id = :id"),
    @NamedQuery(name = "Bx1CSect.findByBxId", query = "SELECT b FROM Bx1CSect b WHERE b.bxId = :bxId"),
    @NamedQuery(name = "Bx1CSect.findByName", query = "SELECT b FROM Bx1CSect b WHERE b.name = :name"),
    @NamedQuery(name = "Bx1CSect.findByParentBxId", query = "SELECT b FROM Bx1CSect b WHERE b.parentBxId = :parentBxId"),
    @NamedQuery(name = "Bx1CSect.findByF1cId", query = "SELECT b FROM Bx1CSect b WHERE b.f1cId = :f1cId"),
    @NamedQuery(name = "Bx1CSect.findByParent1cId", query = "SELECT b FROM Bx1CSect b WHERE b.parent1cId = :parent1cId")})
public class Bx1CSect implements Serializable {
    
    @Size(max = 2147483647)
    @Column(name = "descripts_json")
    private String descriptsJson;
    @Size(max = 2147483647)
    @Column(name = "gabarits_json")
    private String gabaritsJson;
    @Size(max = 2147483647)
    @Column(name = "docs_json")
    private String docsJson;
    
    @Size(max = 8000)
    @Column(name = "filter_props")
    private String filterProps;
    
    @Size(max = 8000)
    @Column(name = "advants")
    private String advants;
    
    @Column(name = "collapse_vc")
    private Short collapseVc;
    
    @Size(max = 2000)
    @Column(name = "mcatalog")
    private String mcatalog;
    
    @Size(max = 5000)
    @Column(name = "video_description")
    private String videoDescription;

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
    @Size(min = 1, max = 255)
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
    
    @Size(max = 2147483647)
    @Column(name = "full_description")
    private String fullDescription;
    @Size(max = 8000)
    @Column(name = "type_completing")
    private String typeCompleting;
    @Size(max = 8000)
    @Column(name = "char_gabarits")
    private String charGabarits;
    @Size(max = 2000)
    @Column(name = "short_desription")
    private String shortDesription;
    @Size(max = 2147483647)
    @Column(name = "documentation")
    private String documentation;
    @Size(max = 8000)
    @Column(name = "description")
    private String description;
    @Size(max = 255)
    @Column(name = "picture")
    private String picture;

    public Bx1CSect() {
    }

    public Bx1CSect(Integer id) {
        this.id = id;
    }

    public Bx1CSect(Integer id, int bxId, String name, int parentBxId) {
        this.id = id;
        this.bxId = bxId;
        this.name = name;
        this.parentBxId = parentBxId;
    }
    
    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
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
    
    public String getFullDescription() {
        return fullDescription;
    }

    public void setFullDescription(String fullDescription) {
        this.fullDescription = fullDescription;
    }

    public String getTypeCompleting() {
        return typeCompleting;
    }

    public void setTypeCompleting(String typeCompleting) {
        this.typeCompleting = typeCompleting;
    }

    public String getCharGabarits() {
        return charGabarits;
    }

    public void setCharGabarits(String charGabarits) {
        this.charGabarits = charGabarits;
    }

    public String getShortDesription() {
        return shortDesription;
    }

    public void setShortDesription(String shortDesription) {
        this.shortDesription = shortDesription;
    }

    public String getDocumentation() {
        return documentation;
    }

    public void setDocumentation(String documentation) {
        this.documentation = documentation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public String getMcatalog() {
        return mcatalog;
    }

    public void setMcatalog(String mcatalog) {
        this.mcatalog = mcatalog;
    }
    
    public Short getCollapseVc() {
        return collapseVc;
    }

    public void setCollapseVc(Short collapseVc) {
        this.collapseVc = collapseVc;
    }
    
    public String getAdvants() {
        return advants;
    }

    public void setAdvants(String advants) {
        this.advants = advants;
    }
    
    public String getFilterProps() {
        return filterProps;
    }

    public void setFilterProps(String filterProps) {
        this.filterProps = filterProps;
    }
    
    public String getDescriptsJson() {
        return descriptsJson;
    }

    public void setDescriptsJson(String descriptsJson) {
        this.descriptsJson = descriptsJson;
    }

    public String getGabaritsJson() {
        return gabaritsJson;
    }

    public void setGabaritsJson(String gabaritsJson) {
        this.gabaritsJson = gabaritsJson;
    }

    public String getDocsJson() {
        return docsJson;
    }

    public void setDocsJson(String docsJson) {
        this.docsJson = docsJson;
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
        if (!(object instanceof Bx1CSect)) {
            return false;
        }
        Bx1CSect other = (Bx1CSect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.Bx1CSect[ id=" + id + " ]";
    }
    
}
