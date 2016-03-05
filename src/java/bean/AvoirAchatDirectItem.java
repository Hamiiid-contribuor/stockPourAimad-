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
public class AvoirAchatDirectItem extends ProduitItemWithoutPrix {

    @ManyToOne
    private AvoirAchatDirect avoirAchatDirect;
    @ManyToOne
    private Stock stock;

    public AvoirAchatDirect getAvoirAchatDirect() {
        if (avoirAchatDirect == null) {
            avoirAchatDirect = new AvoirAchatDirect();
        }
        return avoirAchatDirect;
    }

    public void setAvoirAchatDirect(AvoirAchatDirect avoirAchatDirect) {
        this.avoirAchatDirect = avoirAchatDirect;
    }

    public Stock getStock() {
        if (stock == null) {
            stock = new Stock();
        }
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
        if (!(object instanceof AvoirAchatDirectItem)) {
            return false;
        }
        AvoirAchatDirectItem other = (AvoirAchatDirectItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirAchatDirectItem[ id=" + id + " ]";
    }

}
