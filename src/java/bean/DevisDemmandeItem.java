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
public class DevisDemmandeItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    private BigDecimal prix;
    @ManyToOne
    private DevisDemmande devisDemmande;

    public DevisDemmande getDevisDemmande() {
        if (devisDemmande == null) {
            devisDemmande = new DevisDemmande();
        }
        return devisDemmande;
    }

    public void setDevisDemmande(DevisDemmande devisDemmande) {
        this.devisDemmande = devisDemmande;
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
        final DevisDemmandeItem other = (DevisDemmandeItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
