/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author ayoub
 */
@Entity
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private Long id;
    private String corps;
    private String objet;
    @ManyToOne
    private User user;
    private boolean  lu;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateEnvoi = new Date();
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dateLecture ;
    private String detail;

    public Date getDateLecture() {
        return dateLecture;
    }

    public void setDateLecture(Date dateLecture) {
        this.dateLecture = dateLecture;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
    
    


    @OneToMany(mappedBy = "message")
    private List<MessageReception> messageReceptions;
    
    @OneToMany(mappedBy = "message")
    private List<PiecesJointes> piecesJointeses;

    public List<PiecesJointes> getPiecesJointeses() {
        if(piecesJointeses == null){
            piecesJointeses = new ArrayList<>();
        }
        return piecesJointeses;
    }

    public void setPiecesJointeses(List<PiecesJointes> piecesJointeses) {
        this.piecesJointeses = piecesJointeses;
    }
    
    
    
    
    
    
    public User getUser() {
        if(user == null){
            user = new User();
        }
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCorps() {
        return corps;
    }

    public void setCorps(String corps) {
        this.corps = corps;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public boolean isLu() {
        return lu;
    }

    public void setLu(boolean lu) {
        this.lu = lu;
    }

    public Date getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Date dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    

    public List<MessageReception> getMessageReceptions() {
        if(messageReceptions==null){
            messageReceptions= new ArrayList();
        }
        return messageReceptions;
    }

    public void setMessageReceptions(List<MessageReception> messageReceptions) {
        this.messageReceptions = messageReceptions;
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
        if (!(object instanceof Message)) {
            return false;
        }
        Message other = (Message) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Message[ id=" + id + " ]";
    }
    
}
