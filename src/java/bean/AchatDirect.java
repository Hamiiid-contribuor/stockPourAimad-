/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author moulaYounes
 */
@Entity
public class AchatDirect extends OperationVenteAchat {

    private static final long serialVersionUID = 1L;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateAchatDirect;
    @ManyToOne
    private Fournisseur fournisseur;
    @OneToMany(mappedBy = "achatDirect")
    private List<AchatDirectItem> achatDirectItems;
    @OneToMany(mappedBy = "achatDirect")
    private List<PaiementAchatDirect> paiementAchatDirects;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Projet projet;
    @ManyToOne
    private Responsable responsable;
    @OneToMany(mappedBy = "achatDirect")
    private List<AvoirAchatDirect> achatDirects;

    public Date getDateAchatDirect() {
        return dateAchatDirect;
    }

    public void setDateAchatDirect(Date dateAchatDirect) {
        this.dateAchatDirect = dateAchatDirect;
    }

    public Fournisseur getFournisseur() {
        if (fournisseur == null) {
            fournisseur = new Fournisseur();
        }
        return fournisseur;
    }

    public void setFournisseur(Fournisseur fournisseur) {
        this.fournisseur = fournisseur;
    }

    public List<AchatDirectItem> getAchatDirectItems() {
        if (achatDirectItems == null) {
            achatDirectItems = new ArrayList();
        }
        return achatDirectItems;
    }

    public void setAchatDirectItems(List<AchatDirectItem> achatDirectItems) {
        this.achatDirectItems = achatDirectItems;
    }

    public List<PaiementAchatDirect> getPaiementAchatDirects() {
        if (paiementAchatDirects == null) {
            paiementAchatDirects = new ArrayList();
        }
        return paiementAchatDirects;
    }

    public void setPaiementAchatDirects(List<PaiementAchatDirect> paiementAchatDirects) {
        this.paiementAchatDirects = paiementAchatDirects;
    }

    public Abonne getAbonne() {
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

    public Responsable getResponsable() {
        if (responsable == null) {
            responsable = new Responsable();
        }
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public List<AvoirAchatDirect> getAchatDirects() {
        if (achatDirects == null) {
            achatDirects = new ArrayList();
        }
        return achatDirects;
    }

    public void setAchatDirects(List<AvoirAchatDirect> achatDirects) {
        this.achatDirects = achatDirects;
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
        if (!(object instanceof AchatDirect)) {
            return false;
        }
        AchatDirect other = (AchatDirect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AchatDirect[ id=" + id + " ]";
    }

}
