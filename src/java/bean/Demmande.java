/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
public class Demmande extends OperationVenteAchat {

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDemmande;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEchance;

    private boolean etatLivraison;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "demmande")
    private List<DemmandeItem> demmandeItems;
    @OneToMany(mappedBy = "demmande")
    private List<PaiementDemmande> paiementDemandes;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Projet projet;
    @ManyToOne
    private Responsable responsable;
    

    public Responsable getResponsable() {
        if (responsable == null) {
            responsable = new Responsable();
        }
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
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

    public Abonne getAbonne() {
        if (abonne == null) {
            abonne = new Abonne();
        }
        return abonne;
    }

    public void setAbonne(Abonne abonne) {
        this.abonne = abonne;
    }

    public List<PaiementDemmande> getPaiementDemandes() {
        if (paiementDemandes == null) {
            paiementDemandes = new ArrayList();
        }
        return paiementDemandes;
    }

    public void setPaiementDemandes(List<PaiementDemmande> paiementDemandes) {
        this.paiementDemandes = paiementDemandes;
    }

    public Date getDateDemmande() {
        return dateDemmande;
    }

    public void setDateDemmande(Date dateDemmande) {
        this.dateDemmande = dateDemmande;
    }

    public boolean getEtatLivraison() {
        return etatLivraison;
    }

    public void setEtatLivraison(boolean etatLivraison) {
        this.etatLivraison = etatLivraison;
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

    public Date getDateCommande() {
        return dateDemmande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateDemmande = dateCommande;
    }

    public Date getDateEchance() {
        return dateEchance;
    }

    public void setDateEchance(Date dateEchance) {
        this.dateEchance = dateEchance;
    }

    public List<DemmandeItem> getDemmandeItems() {
        if (demmandeItems == null) {
            demmandeItems = new ArrayList();
        }
        return demmandeItems;
    }

    public void setDemmandeItems(List<DemmandeItem> demmandeItems) {
        this.demmandeItems = demmandeItems;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Demmande other = (Demmande) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
