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
public class VenteDirectItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    private VenteDirect venteDirect;
    private double prix;
    private double qteAvoir;
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

    public VenteDirect getVenteDirect() {
        if (venteDirect == null) {
            venteDirect = new VenteDirect();
        }
        return venteDirect;
    }

    public void setVenteDirect(VenteDirect venteDirect) {
        this.venteDirect = venteDirect;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public double getQteAvoir() {
        return qteAvoir;
    }

    public void setQteAvoir(double qteAvoir) {
        this.qteAvoir = qteAvoir;
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
        if (!(object instanceof VenteDirectItem)) {
            return false;
        }
        VenteDirectItem other = (VenteDirectItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.VenteDirectItem[ id=" + id + " ]";
    }

}
