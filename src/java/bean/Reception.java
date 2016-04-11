/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
public class Reception implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Commande commande;
    private String commentaire;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateReception;
    private BigDecimal montantTotal;
    @OneToMany(mappedBy = "reception")
    List<ReceptionItem> receptionItems;
    @ManyToOne
    private Responsable responsable;
    @OneToOne
    private AvoirReception avoirReception;
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

    public AvoirReception getAvoirReception() {
        if (avoirReception == null) {
            avoirReception = new AvoirReception();
        }
        return avoirReception;
    }

    public void setAvoirReception(AvoirReception avoirReception) {
        this.avoirReception = avoirReception;
    }

    public Responsable getResponsable() {
        if (responsable == null) {
            responsable = new Responsable();
        }
        return responsable;
    }

    public void setResponsable(Responsable responsable) {
        this.responsable = responsable;
    }

    public Commande getCommande() {
        if (commande == null) {
            commande = new Commande();
        }
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Date getDateReception() {
        return dateReception;
    }

    public void setDateReception(Date dateReception) {
        this.dateReception = dateReception;
    }

    public BigDecimal getMontantTotal() {
        if (montantTotal == null) {
            montantTotal = new BigDecimal(0);
        }
        return montantTotal;
    }

    public void setMontantTotal(BigDecimal montantTotal) {
        this.montantTotal = montantTotal;
    }

    public List<ReceptionItem> getReceptionItems() {
        if (receptionItems == null) {
            receptionItems = new ArrayList();
        }
        return receptionItems;
    }

    public void setReceptionItems(List<ReceptionItem> receptionItems) {
        this.receptionItems = receptionItems;
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
        if (!(object instanceof Reception)) {
            return false;
        }
        Reception other = (Reception) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Reception[ id=" + id + " ]";
    }


}
