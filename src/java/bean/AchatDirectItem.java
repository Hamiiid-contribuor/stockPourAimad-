/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 *
 * @author moulaYounes
 */
@Entity
public class AchatDirectItem extends ProduitItemWithoutPrix  {

    private static final long serialVersionUID = 1L;
   
    @ManyToOne
    private AchatDirect achatDirect;
    @ManyToOne
    private Stock stock ;
    private BigDecimal prix;
    private BigDecimal qteAvoir;

    public AchatDirect getAchatDirect() {
        if(achatDirect==null){
            achatDirect= new AchatDirect();
        }
        return achatDirect;
    }

    public void setAchatDirect(AchatDirect achatDirect) {
        this.achatDirect = achatDirect;
    }

   

    public BigDecimal getPrix() {
         if(prix==null){
            prix= new BigDecimal(0);
        }
        return prix;
    }

    public void setPrix(BigDecimal prix) {
        this.prix = prix;
    }

    public BigDecimal getQteAvoir() {
         if(qteAvoir==null){
            qteAvoir= new BigDecimal(0);
        }
        return qteAvoir;
    }

    public void setQteAvoir(BigDecimal qteAvoir) {
        this.qteAvoir = qteAvoir;
    }

    public Stock getStock() {
         if(stock==null){
            stock= new Stock();
        }
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
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
        if (!(object instanceof AchatDirectItem)) {
            return false;
        }
        AchatDirectItem other = (AchatDirectItem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AchatDirectItem[ id=" + id + " ]";
    }

}
