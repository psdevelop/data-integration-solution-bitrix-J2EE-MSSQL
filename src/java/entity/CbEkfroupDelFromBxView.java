/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author arzugurbanova
 */
@Entity
@Table(name = "cb_ekfroup_del_from_bx_view")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CbEkfroupDelFromBxView.findAll", query = "SELECT c FROM CbEkfroupDelFromBxView c"),
    @NamedQuery(name = "CbEkfroupDelFromBxView.findById", query = "SELECT c FROM CbEkfroupDelFromBxView c WHERE c.id = :id")})
public class CbEkfroupDelFromBxView implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Size(max = 255)
    @Column(name = "id")
    private String id;

    public CbEkfroupDelFromBxView() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
