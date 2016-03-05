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
public class AvoirVenteDirect implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateAvoirVenteDirect;
    private String message;
    private BigDecimal montant;
    private BigDecimal montantRetourne;
    @OneToMany(mappedBy = "avoirVenteDirect")
    private List<AvoirVenteDirectItem> avoirVenteDirectItems;
    @ManyToOne
    private VenteDirect venteDirect;
    private boolean modifier;
    private boolean supprimer;

    
     public BigDecimal getMontantRetourne() {
        if (montantRetourne == null) {
            montantRetourne = new BigDecimal(0);
        }
        return montantRetourne;
    }

    public void setMontantRetourne(BigDecimal montantRetourne) {
        this.montantRetourne = montantRetourne;
    }
    
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

    public Date getDateAvoirVenteDirect() {
        return dateAvoirVenteDirect;
    }

    public void setDateAvoirVenteDirect(Date dateAvoirVenteDirect) {
        this.dateAvoirVenteDirect = dateAvoirVenteDirect;
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

    public List<AvoirVenteDirectItem> getAvoirVenteDirectItems() {
        if (avoirVenteDirectItems == null) {
            avoirVenteDirectItems = new ArrayList();
        }
        return avoirVenteDirectItems;
    }

    public void setAvoirVenteDirectItems(List<AvoirVenteDirectItem> avoirVenteDirectItems) {
        this.avoirVenteDirectItems = avoirVenteDirectItems;
    }

    public VenteDirect getVenteDirect() {
        if (venteDirect == null) {
            venteDirect = new VenteDirect();
        }
        return venteDirect;
    }

    public void setVenteDirect(VenteDirect venteDirect) {
        this.venteDirect = venteDirect;
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
        if (!(object instanceof AvoirVenteDirect)) {
            return false;
        }
        AvoirVenteDirect other = (AvoirVenteDirect) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.AvoirVenteDirect[ id=" + id + " ]";
    }

}
