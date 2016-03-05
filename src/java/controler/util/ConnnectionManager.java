/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

import bean.Abonne;
import bean.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ejb.EJB;
import javax.faces.bean.ApplicationScoped;
import javax.inject.Named;
import service.AbonneFacade;

/**
 *
 * @author moulaYounes
 */
@Named("connnectionManager")
@ApplicationScoped
public class ConnnectionManager {

    private static List<Abonne> abonnes;
    @EJB
    private AbonneFacade abonneFacade;
   

    public static List<Abonne> getAbonnes() {
        if (abonnes == null) {
            abonnes = new ArrayList<>();
        }
        return abonnes;
    }

    private int findAbonnePosition(Abonne myAbonne) {
        int i = 0;
        for (Abonne abonne : getAbonnes()) {
            if (Objects.equals(myAbonne.getId(), abonne.getId())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private int findUserPosition(Abonne myAbonne, User myUser) {
        int i = 0;
        for (User user : myAbonne.getUsers()) {
            if (myUser.getLogin().equals(user.getLogin())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void attachUserToAbonneAndToSession(User user, Abonne myAbonne) {
        myAbonne.getUsers().add(user);
        getAbonnes().add(myAbonne);
        user.setAbonne(myAbonne);
        SessionUtil.setAttribute("user", user);
    }

    public int initUserSession(User user, Abonne myAbonne) {
        if (myAbonne == null || myAbonne.getId() == null) {
            return -1;
        } else {
//            myAbonne = abonneFacade.findByUser(user);
            int abonnePosition = findAbonnePosition(myAbonne);
            if (abonnePosition != -1) {
                int userPosition = findUserPosition(myAbonne, user);
                if (userPosition == -1) {
                    attachUserToAbonneAndToSession(user, myAbonne);
                    return 1;
                }
                return 2; // user se connecte sans se deconnecter au paravant
            } else {
                abonneFacade.initAbonneParams(myAbonne);
                attachUserToAbonneAndToSession(user, myAbonne);
                return 3;
            }
        }
    }

 
    public ConnnectionManager() {
    }

}
