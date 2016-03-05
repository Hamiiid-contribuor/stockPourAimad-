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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;

/**
 *
 * @author moulaYounes
 */
@Entity
public class Abonne implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String societe;
    private String nom;
    private String prenom;
    private String email;
    private String adresse;
    private String tel;
    private String header;
    private String footer;
    private String logo;
    @OneToMany(mappedBy = "abonne")
    private List<User> users;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateCreation;
    private int bloquer;
    private String commentaire;
    private String cheminDossier;
    @OneToMany(mappedBy = "abonne")
    private List<Responsable> responsables;
    @OneToOne
    private Responsable defaultResponsable;
    @OneToOne()
    private SecteurActivite secteurActivite;
    @OneToMany(mappedBy = "abonne")
    private List<Projet> projets;
    @OneToMany(mappedBy = "abonne")
    private List<Magasin> magasins;
    @OneToMany(mappedBy = "abonne")
    private List<Client> clients;
    @OneToMany(mappedBy = "abonne")
    private List<Fournisseur> fournisseurs;
    private double avance;
    @OneToMany(mappedBy = "abonne")
    private List<AchatDirect> achatDirects;
    @OneToMany(mappedBy = "abonne")
    private List<DevisDemmande> devisDemandes;
    @OneToMany(mappedBy = "abonne")
    private List<Famille> familles;

    public List<Famille> getFamilles() {
        if (familles == null) {
            familles = new ArrayList();
        }
        return familles;
    }

    public void setFamilles(List<Famille> familles) {
        this.familles = familles;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public double getAvance() {
        return avance;
    }

    public List<AchatDirect> getAchatDirects() {
        if (achatDirects == null) {
            achatDirects = new ArrayList();
        }
        return achatDirects;
    }

    public void setAchatDirects(List<AchatDirect> achatDirects) {
        this.achatDirects = achatDirects;
    }

    public List<DevisDemmande> getDevisDemandes() {
        if (devisDemandes == null) {
            devisDemandes = new ArrayList();
        }
        return devisDemandes;
    }

    public void setDevisDemandes(List<DevisDemmande> devisDemandes) {
        this.devisDemandes = devisDemandes;
    }

    public void setAvance(double avance) {
        this.avance = avance;
    }

    public List<Client> getClients() {
        if (clients == null) {
            clients = new ArrayList();
        }
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Fournisseur> getFournisseurs() {
        if (fournisseurs == null) {
            fournisseurs = new ArrayList();
        }
        return fournisseurs;
    }

    public void setFournisseurs(List<Fournisseur> fournisseurs) {
        this.fournisseurs = fournisseurs;
    }

    public List<Magasin> getMagasins() {
        if (magasins == null) {
            magasins = new ArrayList();
        }
        return magasins;
    }

    public void setMagasins(List<Magasin> magasins) {
        this.magasins = magasins;
    }

    public List<Projet> getProjets() {
        if (projets == null) {
            projets = new ArrayList();
        }
        return projets;
    }

    public void setProjets(List<Projet> projets) {
        this.projets = projets;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public SecteurActivite getSecteurActivite() {
        if (secteurActivite == null) {
            secteurActivite = new SecteurActivite();
        }
        return secteurActivite;
    }

    public void setSecteurActivite(SecteurActivite secteurActivite) {
        this.secteurActivite = secteurActivite;
    }

    public String getCheminDossier() {
        return cheminDossier;
    }

    public void setCheminDossier(String cheminDossier) {
        this.cheminDossier = cheminDossier;
    }

    public List<Responsable> getResponsables() {
        if (responsables == null) {
            responsables = new ArrayList();
        }
        return responsables;
    }

    public void setResponsables(List<Responsable> responsables) {
        this.responsables = responsables;
    }

    public Responsable getDefaultResponsable() {
        if (defaultResponsable == null) {
            defaultResponsable = new Responsable();
        }
        return defaultResponsable;
    }

    public void setDefaultResponsable(Responsable defaultResponsable) {
        this.defaultResponsable = defaultResponsable;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public List<User> getUsers() {
        if (users == null) {
            users = new ArrayList();
        }
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getBloquer() {
        return bloquer;
    }

    public void setBloquer(int bloquer) {
        this.bloquer = bloquer;
    }

    public String getSociete() {
        return societe;
    }

    public void setSociete(String societe) {
        this.societe = societe;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
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
        if (!(object instanceof Abonne)) {
            return false;
        }
        Abonne other = (Abonne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nom;
    }

}
