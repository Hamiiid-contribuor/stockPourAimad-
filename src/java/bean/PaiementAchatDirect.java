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
public class PaiementAchatDirect extends Paiement {

    @ManyToOne
    private AchatDirect achatDirect;

    public AchatDirect getAchatDirect() {
        return achatDirect;
    }

    public void setAchatDirect(AchatDirect achatDirect) {
        this.achatDirect = achatDirect;
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
        if (!(object instanceof PaiementAchatDirect)) {
            return false;
        }
        PaiementAchatDirect other = (PaiementAchatDirect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.PaiementAchatDirect[ id=" + id + " ]";
    }

}
