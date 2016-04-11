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
public class AvoirVenteDirectItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private AvoirVenteDirect avoirVenteDirect;
    @ManyToOne
    private Stock Stock;

    public Stock getStock() {
        if (Stock == null) {
            Stock = new Stock();
        }
        return Stock;
    }

    public void setStock(Stock Stock) {
        this.Stock = Stock;
    }

    public AvoirVenteDirect getAvoirVenteDirect() {
        if (avoirVenteDirect == null) {
            avoirVenteDirect = new AvoirVenteDirect();
        }
        return avoirVenteDirect;
    }

    public void setAvoirVenteDirect(AvoirVenteDirect avoirVenteDirect) {
        this.avoirVenteDirect = avoirVenteDirect;
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
        if (!(object instanceof AvoirVenteDirectItem)) {
            return false;
        }
        AvoirVenteDirectItem other = (AvoirVenteDirectItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirVenteDirectItem[ id=" + id + " ]";
    }

}
