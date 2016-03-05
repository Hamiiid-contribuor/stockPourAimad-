/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author moulaYounes
 */
@Entity
public class AvoirReception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateAvoirReception;
    private String message; 
    private BigDecimal montant;// sigma(prix*qte de avoirItem)
    private BigDecimal montantRetourne;
    @OneToMany(mappedBy = "avoirReception",cascade =CascadeType.REMOVE )
    private List<AvoirReceptionItem> avoirReceptionItems;
    @ManyToOne
    private Reception reception;
    private boolean modifier;
    private boolean supprimer;
  
    public boolean isModifier() {
        return modifier;
    }

    public void setModifier(boolean modifier) {
        this.modifier = modifier;
    }

    public boolean isSupprimer() {
        return supprimer;
    }

    public void setSupprimer(boolean supprimer) {
        this.supprimer = supprimer;
    }

    public Reception getReception() {
        if (reception == null) {
            reception = new Reception();
        }
        return reception;
    }

    public void setReception(Reception reception) {
        this.reception = reception;
    }

    public Date getDateAvoirReception() {
        return dateAvoirReception;
    }

    public void setDateAvoirReception(Date dateAvoirReception) {
        this.dateAvoirReception = dateAvoirReception;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getMontant() {
        if (montant == null) {
            montant = new BigDecimal(0);
        }
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

     public BigDecimal getMontantRetourne() {
        if (montantRetourne == null) {
            montantRetourne = new BigDecimal(0);
        }
        return montantRetourne;
    }

    public void setMontantRetourne(BigDecimal montantRetourne) {
        this.montantRetourne = montantRetourne;
    }
    public List<AvoirReceptionItem> getAvoirReceptionItems() {
        if (avoirReceptionItems == null) {
            avoirReceptionItems = new ArrayList();
        }
        return avoirReceptionItems;
    }

    public void setAvoirReceptionItems(List<AvoirReceptionItem> avoirReceptionItems) {
        this.avoirReceptionItems = avoirReceptionItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof AvoirReception)) {
            return false;
        }
        AvoirReception other = (AvoirReception) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirReception[ id=" + id + " ]";
    }

}
