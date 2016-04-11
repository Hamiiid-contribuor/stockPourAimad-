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
public class Livraison implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Demmande demmande;
    private String commentaire;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateLivraison;
    private BigDecimal montantTotal;
    @ManyToOne
    private Client client;
    @OneToMany(mappedBy = "livraison")
    private List<LivraisonItem> livraisonItems;
    @ManyToOne
    private Responsable responsable;
    @OneToMany(mappedBy = "livraison")
    private List<AvoirLivraison> avoirLivraisons;
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

    public List<AvoirLivraison> getAvoirLivraisons() {
        if (avoirLivraisons == null) {
            avoirLivraisons = new ArrayList();
        }
        return avoirLivraisons;
    }

    public void setAvoirLivraisons(List<AvoirLivraison> avoirLivraisons) {
        this.avoirLivraisons = avoirLivraisons;
    }

    public Demmande getDemmande() {
        if (demmande == null) {
            demmande = new Demmande();
        }
        return demmande;
    }

    public void setDemmande(Demmande demmande) {
        this.demmande = demmande;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(Date dateLivraison) {
        this.dateLivraison = dateLivraison;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<LivraisonItem> getLivraisonItems() {
        return livraisonItems;
    }

    public void setLivraisonItems(List<LivraisonItem> livraisonItems) {
        this.livraisonItems = livraisonItems;
    }

    public Responsable getResponsable() {
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
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
        if (!(object instanceof Livraison)) {
            return false;
        }
        Livraison other = (Livraison) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Livraison[ id=" + id + " ]";
    }

}
