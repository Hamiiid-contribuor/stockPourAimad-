/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author Younes
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public   class ProduitItem extends ProduitItemWithoutPrix {

    protected BigDecimal prix;

    public BigDecimal getPrix() {
        if (prix == null) {
            prix = new BigDecimal(0);
        }
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

}
