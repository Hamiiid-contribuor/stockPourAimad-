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
public class CommandeItem extends ProduitItemWithoutPrix {

    @ManyToOne
    private Commande commande;
    private BigDecimal qteRecu;
    private BigDecimal prix;
    private BigDecimal qteAvoir;

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public BigDecimal getQteRecu() {
        if (qteRecu == null) {
            qteRecu = new BigDecimal(0);
        }
        return qteRecu;
    }

    public void setQteRecu(BigDecimal qteRecu) {
        this.qteRecu = qteRecu;
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

    public CommandeItem() {
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
        final CommandeItem other = (CommandeItem) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
