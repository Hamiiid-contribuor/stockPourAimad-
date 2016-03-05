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
import java.util.Objects;
import javax.persistence.Entity;
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
public class DevisCommande implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String commentaire;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateDevisCommande;
    private BigDecimal montantTotal;
    private BigDecimal tva;
    @ManyToOne
    private Fournisseur fournisseur;
    @OneToMany(mappedBy = "devisCommande")
    private List<DevisCommandeItem> devisCommandeItems;
    @ManyToOne
    private Abonne abonne;
    @ManyToOne
    private Projet projet;
    @OneToOne
    private Commande commande;
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

    public BigDecimal getTva() {
        if (tva == null) {
            tva = new BigDecimal(0);
        }
        return tva;
    }

    public void setTva(BigDecimal tva) {
        this.tva = tva;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateDevisCommande() {
        return dateDevisCommande;
    }

    public void setDateDevisCommande(Date dateDevisCommande) {
        this.dateDevisCommande = dateDevisCommande;
    }

    public BigDecimal getMontantTotal() {
          if (montantTotal == null) {
            montantTotal = new BigDecimal(0);
        }
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
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

    public List<DevisCommandeItem> getDevisCommandeItems() {
        if (devisCommandeItems == null) {
            devisCommandeItems = new ArrayList();
        }
        return devisCommandeItems;
    }

    public void setDevisCommandeItems(List<DevisCommandeItem> devisCommandeItems) {
        this.devisCommandeItems = devisCommandeItems;
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

    public Commande getCommande() {
        if (commande == null) {
            commande = new Commande();
        }
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Objects.hashCode(this.id);
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
        final DevisCommande other = (DevisCommande) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
