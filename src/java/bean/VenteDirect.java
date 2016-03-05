/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author moulaYounes
 */
@Entity
public class VenteDirect extends OperationVenteAchat {

    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "venteDirect")
    private List<VenteDirectItem> venteDirectItems;
    @OneToMany(mappedBy = "venteDirect")
    private List<PaiementVenteDirect> paiementVenteDirects;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Projet projet;
    @ManyToOne
    private Responsable responsable;
    @OneToMany(mappedBy = "venteDirect")
    private List<AvoirVenteDirect> avoirVenteDirects;

    public List<AvoirVenteDirect> getAvoirVenteDirects() {
        if (avoirVenteDirects == null) {
            avoirVenteDirects = new ArrayList();
        }
        return avoirVenteDirects;
    }

    public void setAvoirVenteDirects(List<AvoirVenteDirect> avoirVenteDirects) {
        this.avoirVenteDirects = avoirVenteDirects;
    }

    public Responsable getResponsable() {
        if (responsable == null) {
            responsable = new Responsable();
        }
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<VenteDirectItem> getVenteDirectItems() {
        if (venteDirectItems == null) {
            venteDirectItems = new ArrayList();
        }
        return venteDirectItems;
    }

    public void setVenteDirectItems(List<VenteDirectItem> venteDirectItems) {
        this.venteDirectItems = venteDirectItems;
    }

    public List<PaiementVenteDirect> getPaiementVenteDirects() {
        if (paiementVenteDirects == null) {
            paiementVenteDirects = new ArrayList();
        }
        return paiementVenteDirects;
    }

    public void setPaiementVenteDirects(List<PaiementVenteDirect> paiementVenteDirects) {
        this.paiementVenteDirects = paiementVenteDirects;
    }

    public Abonne getAbonne() {
        if (abonne == null) {
            abonne = new Abonne();
        }
        return abonne;
    }

    public void setAbonne(Abonne abonne) {
        this.abonne = abonne;
    }

    public Projet getProjet() {
        if (projet == null) {
            projet = new Projet();
        }
        return projet;
    }

    public void setProjet(Projet projet) {
        this.projet = projet;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof VenteDirect)) {
            return false;
        }
        VenteDirect other = (VenteDirect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.VenteDirect[ id=" + id + " ]";
    }

}
