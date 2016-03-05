/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class AvoirLivraisonItem extends ProduitItemWithoutPrix {

    @ManyToOne
    private AvoirLivraison avoirLivraison;
    @ManyToOne
    private Stock stock;

    public Stock getStock() {
        if (stock == null) {
            stock = new Stock();
        }
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public AvoirLivraison getAvoirLivraison() {
        if (avoirLivraison == null) {
            avoirLivraison = new AvoirLivraison();
        }
        return avoirLivraison;
    }

    public void setAvoirLivraison(AvoirLivraison avoirLivraison) {
        this.avoirLivraison = avoirLivraison;
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
        if (!(object instanceof AvoirLivraisonItem)) {
            return false;
        }
        AvoirLivraisonItem other = (AvoirLivraisonItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirLivraisonItem[ id=" + id + " ]";
    }

}
