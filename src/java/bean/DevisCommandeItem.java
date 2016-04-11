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
public class DevisCommandeItem extends ProduitItemWithoutPrix {

    private static final long serialVersionUID = 1L;

    private BigDecimal prix;
    @ManyToOne
    private DevisCommande devisCommande;

    public DevisCommande getDevisCommande() {
        if (devisCommande == null) {
            devisCommande = new DevisCommande();
        }
        return devisCommande;
    }

    public void setDevisCommande(DevisCommande devisCommande) {
        this.devisCommande = devisCommande;
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
        final DevisCommandeItem other = (DevisCommandeItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
