/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author moulaYounes
 */
@Entity
public class Commande extends OperationVenteAchat {

    
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCommande;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateEchance;
    private BigDecimal montantTotalReception;
    private BigDecimal montantTotalAvoir;
    private int etatReception;
    @ManyToOne
    private Fournisseur fournisseur;
    @OneToMany(mappedBy = "commande")
    private List<CommandeItem> commandeItems;
    @OneToMany(mappedBy = "commande")
    private List<PaiementCommande> paiementCommandes;
    @OneToMany(mappedBy = "commande")
    List<Reception> receptions;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Responsable responsable;
    @ManyToOne
    private Projet projet;
   

    public Commande() {

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

    public List<PaiementCommande> getPaiementCommandes() {
        if (paiementCommandes == null) {
            paiementCommandes = new ArrayList();
        }
        return paiementCommandes;
    }

    public void setPaiementCommandes(List<PaiementCommande> paiementCommandes) {
        this.paiementCommandes = paiementCommandes;
    }

    public List<Reception> getReceptions() {
        if (receptions == null) {
            receptions = new ArrayList();
        }
        return receptions;
    }

    public void setReceptions(List<Reception> receptions) {
        this.receptions = receptions;
    }

    public BigDecimal getMontantTotalReception() {
        if (montantTotalReception == null) {
            montantTotalReception = new BigDecimal(BigInteger.ZERO);
        }
        return montantTotalReception;
    }

    public void setMontantTotalReception(BigDecimal montantTotalReception) {
        this.montantTotalReception = montantTotalReception;
    }

    public BigDecimal getMontantTotalAvoir() {
        if (montantTotalAvoir == null) {
            montantTotalAvoir = new BigDecimal(BigInteger.ZERO);
        }
        return montantTotalAvoir;
    }

    public void setMontantTotalAvoir(BigDecimal montantTotalAvoir) {
        this.montantTotalAvoir = montantTotalAvoir;
    }

   

    public Date getDateCommande() {
        if (dateCommande == null) {
            dateCommande = new Date();
        }
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Date getDateEchance() {
        if (dateEchance == null) {
            dateEchance = new Date();
        }
        return dateEchance;
    }

    public void setDateEchance(Date dateEchance) {
        this.dateEchance = dateEchance;
    }

   

    public int getEtatReception() {
        return etatReception;
    }

    public void setEtatReception(int etatReception) {
        this.etatReception = etatReception;
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

    public List<CommandeItem> getCommandeItems() {
        if (commandeItems == null) {
            commandeItems = new ArrayList();
        }
        return commandeItems;
    }

    public void setCommandeItems(List<CommandeItem> commandeItems) {
        this.commandeItems = commandeItems;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + Objects.hashCode(this.id);
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
        final Commande other = (Commande) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Commande{" + "id=" + id + ", reference=" + reference + ", commentaire=" + commentaire + ", dateCommande="
                + dateCommande + ", dateEchance=" + dateEchance + ", montantTotal=" + montantTotal + ", paiement=" + paiement
                + ", paiementEffetEnCour=" + paiementEffetEnCour + ", etatReception=" + etatReception
                + ", tva=" + tva + ", fournisseur=" + getFournisseur().getNom() + ", abonne=" + abonne.getNom()
                + ", projet=" + getProjet().getNom() + '}';
    }

   
}
