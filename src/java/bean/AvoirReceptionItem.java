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
public class AvoirReceptionItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private AvoirReception avoirReception;
    @ManyToOne
    private Magasin magasin;

    public Magasin getMagasin() {
        if (magasin == null) {
            magasin = new Magasin();
        }
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public AvoirReception getAvoirReception() {
        if (avoirReception == null) {
            avoirReception = new AvoirReception();
        }
        return avoirReception;
    }

    public void setAvoirReception(AvoirReception avoirReception) {
        this.avoirReception = avoirReception;
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
        if (!(object instanceof AvoirReceptionItem)) {
            return false;
        }
        AvoirReceptionItem other = (AvoirReceptionItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AvoirReceptionItem{" + "produit=" + produit.getLibelle() + ", qte=" + qte + ", magasin=" + magasin.getNom() + '}';
    }

}
