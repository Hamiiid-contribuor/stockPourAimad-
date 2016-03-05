/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class PaiementVenteDirect extends Paiement {

    @ManyToOne
    private VenteDirect venteDirect;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    public void setVenteDirect(VenteDirect venteDirect) {
        this.venteDirect = venteDirect;
    }

    public VenteDirect getVenteDirect() {
        if (venteDirect == null) {
            venteDirect = new VenteDirect();
        }
        return venteDirect;
    }

    public boolean isEncaisser() {
        return encaisser;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PaiementVenteDirect other = (PaiementVenteDirect) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
