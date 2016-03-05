/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class DemmandeItem extends ProduitItemWithoutPrix {

    private BigDecimal qteLivree;
    private BigDecimal prix;
    private BigDecimal qteAvoir;
    @ManyToOne
    private Demmande demmande;
    @ManyToOne
    private Magasin magasin;

    public Demmande getDemmande() {
        if (demmande == null) {
            demmande = new Demmande();
        }
        return demmande;
    }

    public void setDemmande(Demmande demmande) {
        this.demmande = demmande;
    }

    public Magasin getMagasin() {
        if (magasin == null) {
            magasin = new Magasin();
        }
        return magasin;
    }

    public void setMagasin(Magasin magasin) {
        this.magasin = magasin;
    }

    public BigDecimal getQteLivree() {
        if (qteLivree == null) {
            qteLivree = new BigDecimal(0);
        }
        return qteLivree;
    }

    public void setQteLivree(BigDecimal qteLivree) {
        this.qteLivree = qteLivree;
    }

    public BigDecimal getPrix() {
        if (prix == null) {
            prix = new BigDecimal(0);
        }
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final DemmandeItem other = (DemmandeItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
