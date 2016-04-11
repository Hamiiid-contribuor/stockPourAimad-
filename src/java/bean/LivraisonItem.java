/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class LivraisonItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private Demmande demande;

    private BigDecimal qteAvoir;
    @ManyToOne
    private Magasin stock;
    @ManyToOne
    private Livraison livraison;

    public Livraison getLivraison() {
        if (livraison == null) {
            livraison = new Livraison();
        }
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public BigDecimal getQteAvoir() {
        if (qteAvoir == null) {
            qteAvoir = new BigDecimal(0);
        }
        return qteAvoir;
    }

    public void setQteAvoir(BigDecimal qteAvoir) {
        this.qteAvoir = qteAvoir;
    }

    public Magasin getStock() {
        if (stock == null) {
            stock = new Magasin();
        }
        return stock;
    }

    public void setStock(Magasin stock) {
        this.stock = stock;
    }

    public Demmande getDemande() {
        if (demande == null) {
            demande = new Demmande();
        }
        return demande;
    }

    public void setDemande(Demmande demande) {
        this.demande = demande;
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
        if (!(object instanceof LivraisonItem)) {
            return false;
        }
        LivraisonItem other = (LivraisonItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.LivraisonItem[ id=" + id + " ]";
    }

}
