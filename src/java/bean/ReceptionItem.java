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
public class ReceptionItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    private BigDecimal qteAvoir;
    @ManyToOne
    private Magasin magasin;
    @ManyToOne
    private Reception reception;

    public Reception getReception() {
        if (reception == null) {
            reception = new Reception();
        }
        return reception;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
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
        final ReceptionItem other = (ReceptionItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ReceptionItem{" + "produit=" + produit.getLibelle() + ", qte=" + qte + '}';
    }

}
