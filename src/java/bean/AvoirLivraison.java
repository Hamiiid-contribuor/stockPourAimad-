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
public class AvoirLivraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateAvoirLivraison;
    private String message;
     private BigDecimal montant;
    private BigDecimal montantRetourne;
    @OneToMany(mappedBy = "avoirLivraison") 
    private List<AvoirLivraisonItem> avoirLivraisonItems;
    @ManyToOne
    private Livraison livraison;
    private boolean modifier;
    private boolean supprimer;

    public boolean isModifier() {
        return modifier;
    }

    public void setModifier(boolean modifier) {
        this.modifier = modifier;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Livraison getLivraison() {
        if (livraison == null) {
            livraison = new Livraison();
        }
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public Date getDateAvoirLivraison() {
        return dateAvoirLivraison;
    }

    public void setDateAvoirLivraison(Date dateAvoirLivraison) {
        this.dateAvoirLivraison = dateAvoirLivraison;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

   
    public BigDecimal getMontant() {
        if (montant == null) {
            montant = new BigDecimal(0);
        }
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

     public BigDecimal getMontantRetourne() {
        if (montantRetourne == null) {
            montantRetourne = new BigDecimal(0);
        }
        return montantRetourne;
    }

    public void setMontantRetourne(BigDecimal montantRetourne) {
        this.montantRetourne = montantRetourne;
    }
    
    public List<AvoirLivraisonItem> getAvoirLivraisonItems() {
        if (avoirLivraisonItems == null) {
            avoirLivraisonItems = new ArrayList();
        }
        return avoirLivraisonItems;
    }

    public void setAvoirLivraisonItems(List<AvoirLivraisonItem> avoirLivraisonItems) {
        this.avoirLivraisonItems = avoirLivraisonItems;
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
        if (!(object instanceof AvoirLivraison)) {
            return false;
        }
        AvoirLivraison other = (AvoirLivraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirLivraison[ id=" + id + " ]";
    }

}
