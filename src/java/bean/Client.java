/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author moulaYounes
 */
@Entity
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String nom;
    private String adresse;
    private String email;
    private String tel;
    private String description;
    private BigDecimal creance;
    @OneToMany(mappedBy = "client")
    private List<Demmande> demmandes;
    @ManyToOne
    private Abonne abonne;
    @OneToMany(mappedBy = "client")
    private List<VenteDirect> venteDirects;
    private boolean bloquer;
    private String detailBloquage;

    public boolean isBloquer() {
        return bloquer;
    }

    public void setBloquer(boolean bloquer) {
        this.bloquer = bloquer;
    }

    public String getDetailBloquage() {
        return detailBloquage;
    }

    public void setDetailBloquage(String detailBloquage) {
        this.detailBloquage = detailBloquage;
    }

    public Abonne getAbonne() {
        if (abonne == null) {
            abonne = new Abonne();
        }
        return abonne;
    }

    public List<VenteDirect> getVenteDirects() {
        if (venteDirects == null) {
            venteDirects = new ArrayList();
        }
        return venteDirects;
    }

    public void setVenteDirects(List<VenteDirect> venteDirects) {
        this.venteDirects = venteDirects;
    }

    public void setAbonne(Abonne abonne) {
        this.abonne = abonne;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCreance() {
        if (creance == null) {
            creance = new BigDecimal(0);
        }
        return creance;
    }

    public void setCreance(BigDecimal creance) {
        this.creance = creance;
    }

    public List<Demmande> getDemmandes() {
        if (demmandes == null) {
            demmandes = new ArrayList();
        }
        return demmandes;
    }

    public void setDemmandes(List<Demmande> demmandes) {
        this.demmandes = demmandes;
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
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nom;
    }

}
