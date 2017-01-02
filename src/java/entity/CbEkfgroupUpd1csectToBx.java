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
import javax.persistence.Lob;
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
@Table(name = "cb_ekfgroup_upd_1csect_to_bx")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findAll", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c"),
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findBySid", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c WHERE c.sid = :sid"),
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findByBxname", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c WHERE c.bxname = :bxname"),
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findByF1cname", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c WHERE c.f1cname = :f1cname"),
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findByBxparentId", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c WHERE c.bxparentId = :bxparentId"),
    @NamedQuery(name = "CbEkfgroupUpd1csectToBx.findByF1cparentId", query = "SELECT c FROM CbEkfgroupUpd1csectToBx c WHERE c.f1cparentId = :f1cparentId")})
public class CbEkfgroupUpd1csectToBx implements Serializable {
    
    @Size(max = 2147483647)
    @Column(name = "f1c_seo_alias_url")
    private String f1cSeoAliasUrl;
    @Size(max = 2147483647)
    @Column(name = "f1c_seo_title")
    private String f1cSeoTitle;
    @Size(max = 2147483647)
    @Column(name = "f1c_seo_h1")
    private String f1cSeoH1;
    @Size(max = 500)
    @Column(name = "bx_seo_alias_url")
    private String bxSeoAliasUrl;
    @Size(max = 500)
    @Column(name = "bx_seo_title")
    private String bxSeoTitle;
    @Size(max = 500)
    @Column(name = "bx_seo_h1")
    private String bxSeoH1;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_sort_order")
    private int bxSortOrder;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f1c_sort_order")
    private int f1cSortOrder;
    
    @Size(max = 2147483647)
    @Column(name = "f1c_descripts_json")
    private String f1cDescriptsJson;
    @Size(max = 2147483647)
    @Column(name = "f1c_gabarits_json")
    private String f1cGabaritsJson;
    @Size(max = 2147483647)
    @Column(name = "f1c_docs_json")
    private String f1cDocsJson;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "bx_descripts_json")
    private String bxDescriptsJson;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "bx_gabarits_json")
    private String bxGabaritsJson;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "bx_docs_json")
    private String bxDocsJson;
    
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "bx_filter_props")
    private String bxFilterProps;
    @Size(max = 8000)
    @Column(name = "f1c_filter_props")
    private String f1cFilterProps;
    
    @Size(max = 8000)
    @Column(name = "f1c_advants")
    private String f1cAdvants;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "bx_collapsevc")
    private short bxCollapsevc;
    @Basic(optional = false)
    @NotNull
    @Column(name = "f1c_collapsevc")
    private short f1cCollapsevc;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "bx_advants")
    private String bxAdvants;
    
    @Size(max = 2000)
    @Column(name = "f1c_picture")
    private String f1cPicture;
    @Size(max = 2000)
    @Column(name = "f1c_mcatalog")
    private String f1cMcatalog;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "bx_mcatalog")
    private String bxMcatalog;
    
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_video_description")
    private String f1cVideoDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 5000)
    @Column(name = "bx_video_description")
    private String bxVideoDescription;

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 255)
    @Column(name = "sid")
    private String sid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bxname")
    private String bxname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "f1cname")
    private String f1cname;
    @Size(max = 255)
    @Column(name = "bxparent_id")
    private String bxparentId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 36)
    @Column(name = "f1cparent_id")
    private String f1cparentId;
    //@Basic(optional = false)
    //@NotNull
    //@Size(min = 1, max = 255)
    //@Column(name = "cb_bxgroup_id")
    //private String cbBxgroupId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_full_description")
    private String f1cFullDescription;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_description")
    private String f1cDescription;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_type_completing")
    private String f1cTypeCompleting;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_char_gabarits")
    private String f1cCharGabarits;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_short_description")
    private String f1cShortDescription;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "f1c_documentation")
    private String f1cDocumentation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "bx_description")
    private String bxDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "bx_full_description")
    private String bxFullDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "bx_type_completing")
    private String bxTypeCompleting;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2000)
    @Column(name = "bx_short_description")
    private String bxShortDescription;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8000)
    @Column(name = "bx_char_gabarits")
    private String bxCharGabarits;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "bx_documentation")
    private String bxDocumentation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "bx_picture")
    private String bxPicture;

    public CbEkfgroupUpd1csectToBx() {
    }
    
    public String getF1cVideoDescription() {
        return f1cVideoDescription;
    }

    public void setF1cVideoDescription(String f1cVideoDescription) {
        this.f1cVideoDescription = f1cVideoDescription;
    }

    public String getBxVideoDescription() {
        return bxVideoDescription;
    }

    public void setBxVideoDescription(String bxVideoDescription) {
        this.bxVideoDescription = bxVideoDescription;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getBxname() {
        return bxname;
    }

    public void setBxname(String bxname) {
        this.bxname = bxname;
    }

    public String getF1cname() {
        return f1cname;
    }

    public void setF1cname(String f1cname) {
        this.f1cname = f1cname;
    }

    public String getBxparentId() {
        return bxparentId;
    }

    public void setBxparentId(String bxparentId) {
        this.bxparentId = bxparentId;
    }

    public String getF1cparentId() {
        return f1cparentId;
    }

    public void setF1cparentId(String f1cparentId) {
        this.f1cparentId = f1cparentId;
    }
    
    //public String getCbBxgroupId() {
    //    return cbBxgroupId;
    //}

    //public void setCbBxgroupId(String cbBxgroupId) {
    //    this.cbBxgroupId = cbBxgroupId;
    //}
    
    public String getF1cFullDescription() {
        return f1cFullDescription;
    }

    public void setF1cFullDescription(String f1cFullDescription) {
        this.f1cFullDescription = f1cFullDescription;
    }

    public String getF1cDescription() {
        return f1cDescription;
    }

    public void setF1cDescription(String f1cDescription) {
        this.f1cDescription = f1cDescription;
    }

    public String getF1cTypeCompleting() {
        return f1cTypeCompleting;
    }

    public void setF1cTypeCompleting(String f1cTypeCompleting) {
        this.f1cTypeCompleting = f1cTypeCompleting;
    }

    public String getF1cCharGabarits() {
        return f1cCharGabarits;
    }

    public void setF1cCharGabarits(String f1cCharGabarits) {
        this.f1cCharGabarits = f1cCharGabarits;
    }

    public String getF1cShortDescription() {
        return f1cShortDescription;
    }

    public void setF1cShortDescription(String f1cShortDescription) {
        this.f1cShortDescription = f1cShortDescription;
    }

    public String getF1cDocumentation() {
        return f1cDocumentation;
    }

    public void setF1cDocumentation(String f1cDocumentation) {
        this.f1cDocumentation = f1cDocumentation;
    }

    public String getBxDescription() {
        return bxDescription;
    }

    public void setBxDescription(String bxDescription) {
        this.bxDescription = bxDescription;
    }

    public String getBxFullDescription() {
        return bxFullDescription;
    }

    public void setBxFullDescription(String bxFullDescription) {
        this.bxFullDescription = bxFullDescription;
    }

    public String getBxTypeCompleting() {
        return bxTypeCompleting;
    }

    public void setBxTypeCompleting(String bxTypeCompleting) {
        this.bxTypeCompleting = bxTypeCompleting;
    }

    public String getBxShortDescription() {
        return bxShortDescription;
    }

    public void setBxShortDescription(String bxShortDescription) {
        this.bxShortDescription = bxShortDescription;
    }

    public String getBxCharGabarits() {
        return bxCharGabarits;
    }

    public void setBxCharGabarits(String bxCharGabarits) {
        this.bxCharGabarits = bxCharGabarits;
    }

    public String getBxDocumentation() {
        return bxDocumentation;
    }

    public void setBxDocumentation(String bxDocumentation) {
        this.bxDocumentation = bxDocumentation;
    }

    public String getBxPicture() {
        return bxPicture;
    }

    public void setBxPicture(String bxPicture) {
        this.bxPicture = bxPicture;
    }
    
    public String getF1cPicture() {
        return f1cPicture;
    }

    public void setF1cPicture(String f1cPicture) {
        this.f1cPicture = f1cPicture;
    }

    public String getF1cMcatalog() {
        return f1cMcatalog;
    }

    public void setF1cMcatalog(String f1cMcatalog) {
        this.f1cMcatalog = f1cMcatalog;
    }

    public String getBxMcatalog() {
        return bxMcatalog;
    }

    public void setBxMcatalog(String bxMcatalog) {
        this.bxMcatalog = bxMcatalog;
    }
    
    public short getBxCollapsevc() {
        return bxCollapsevc;
    }

    public void setBxCollapsevc(short bxCollapsevc) {
        this.bxCollapsevc = bxCollapsevc;
    }

    public short getF1cCollapsevc() {
        return f1cCollapsevc;
    }

    public void setF1cCollapsevc(short f1cCollapsevc) {
        this.f1cCollapsevc = f1cCollapsevc;
    }

    public String getBxAdvants() {
        return bxAdvants;
    }

    public void setBxAdvants(String bxAdvants) {
        this.bxAdvants = bxAdvants;
    }

    public String getF1cAdvants() {
        return f1cAdvants;
    }

    public void setF1cAdvants(String f1cAdvants) {
        this.f1cAdvants = f1cAdvants;
    }
    
    public String getBxFilterProps() {
        return bxFilterProps;
    }

    public void setBxFilterProps(String bxFilterProps) {
        this.bxFilterProps = bxFilterProps;
    }

    public String getF1cFilterProps() {
        return f1cFilterProps;
    }

    public void setF1cFilterProps(String f1cFilterProps) {
        this.f1cFilterProps = f1cFilterProps;
    }
    
    public String getF1cDescriptsJson() {
        return f1cDescriptsJson;
    }

    public void setF1cDescriptsJson(String f1cDescriptsJson) {
        this.f1cDescriptsJson = f1cDescriptsJson;
    }

    public String getF1cGabaritsJson() {
        return f1cGabaritsJson;
    }

    public void setF1cGabaritsJson(String f1cGabaritsJson) {
        this.f1cGabaritsJson = f1cGabaritsJson;
    }

    public String getF1cDocsJson() {
        return f1cDocsJson;
    }

    public void setF1cDocsJson(String f1cDocsJson) {
        this.f1cDocsJson = f1cDocsJson;
    }

    public String getBxDescriptsJson() {
        return bxDescriptsJson;
    }

    public void setBxDescriptsJson(String bxDescriptsJson) {
        this.bxDescriptsJson = bxDescriptsJson;
    }

    public String getBxGabaritsJson() {
        return bxGabaritsJson;
    }

    public void setBxGabaritsJson(String bxGabaritsJson) {
        this.bxGabaritsJson = bxGabaritsJson;
    }

    public String getBxDocsJson() {
        return bxDocsJson;
    }

    public void setBxDocsJson(String bxDocsJson) {
        this.bxDocsJson = bxDocsJson;
    }
    
    public int getBxSortOrder() {
        return bxSortOrder;
    }

    public void setBxSortOrder(int bxSortOrder) {
        this.bxSortOrder = bxSortOrder;
    }

    public int getF1cSortOrder() {
        return f1cSortOrder;
    }

    public void setF1cSortOrder(int f1cSortOrder) {
        this.f1cSortOrder = f1cSortOrder;
    }
    
    public String getF1cSeoAliasUrl() {
        return f1cSeoAliasUrl;
    }

    public void setF1cSeoAliasUrl(String f1cSeoAliasUrl) {
        this.f1cSeoAliasUrl = f1cSeoAliasUrl;
    }

    public String getF1cSeoTitle() {
        return f1cSeoTitle;
    }

    public void setF1cSeoTitle(String f1cSeoTitle) {
        this.f1cSeoTitle = f1cSeoTitle;
    }

    public String getF1cSeoH1() {
        return f1cSeoH1;
    }

    public void setF1cSeoH1(String f1cSeoH1) {
        this.f1cSeoH1 = f1cSeoH1;
    }

    public String getBxSeoAliasUrl() {
        return bxSeoAliasUrl;
    }

    public void setBxSeoAliasUrl(String bxSeoAliasUrl) {
        this.bxSeoAliasUrl = bxSeoAliasUrl;
    }

    public String getBxSeoTitle() {
        return bxSeoTitle;
    }

    public void setBxSeoTitle(String bxSeoTitle) {
        this.bxSeoTitle = bxSeoTitle;
    }

    public String getBxSeoH1() {
        return bxSeoH1;
    }

    public void setBxSeoH1(String bxSeoH1) {
        this.bxSeoH1 = bxSeoH1;
    }
    
}
