-- phpMyAdmin SQL Dump
-- version 4.1.4
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Mer 13 Janvier 2016 à 18:46
-- Version du serveur :  5.6.15-log
-- Version de PHP :  5.4.24

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `stock_en_ligne_v4`
--

--
-- Contenu de la table `abonne`
--

INSERT INTO `abonne` (`ID`, `ADRESSE`, `AVANCE`, `BLOQUER`, `CHEMINDOSSIER`, `COMMENTAIRE`, `DATECREATION`, `EMAIL`, `FOOTER`, `HEADER`, `LOGO`, `NOM`, `PRENOM`, `SOCIETE`, `TEL`, `DEFAULTRESPONSABLE_ID`, `SECTEURACTIVITE_ID`) VALUES
(1, 'jdhns', 20, 0, 'jhdd', 'sd d ', '2015-11-11', 'djnnd', 'nfhd', 'dd', 'walo', 'njdnijd', 'ijndnijd', 'djndn', 'njchdh', NULL, 1),
(2, 'soc 1', 0, 0, 'hhhh', 'hbbbh', '2015-11-18', 'soc 1', 'soc 1', 'soc 1', 'uhubhb', 'soc 1', 'soc 1', 'soc 1', 'soc 1', NULL, 2);

--
-- Contenu de la table `commande`
--

INSERT INTO `commande` (`ID`, `COMMENTAIRE`, `DATECOMMANDE`, `DATEECHANCE`, `ETATRECEPTION`, `MODIFIER`, `MONTANTTOTAL`, `MONTANTTOTALAVOIR`, `MONTANTTOTALRECEPTION`, `PAIEMENT`, `PAIEMENTEFFETENCOUR`, `REFERENCE`, `REFERENCEINDEX`, `REFERENCEPRIFFIX`, `REFERENCESUFFIX`, `SUPPRIMER`, `TVA`, `ABONNE_ID`, `FOURNISSEUR_ID`, `PROJET_ID`, `RESPONSABLE_ID`) VALUES
(2, 'TANI', '2016-01-13', '2016-01-13', 1, 0, '813', '0', '533', '120', '0', 'A-2', 2, 'A', '', 0, '20', 1, 3, 1, 1);

--
-- Contenu de la table `commandeitem`
--

INSERT INTO `commandeitem` (`ID`, `PRIX`, `QTE`, `QTEAVOIR`, `QTERECU`, `COMMANDE_ID`, `PRODUIT_ID`) VALUES
(4, '8', '10', '0', '10', 2, 3),
(5, '11', '3', '0', '3', 2, 1),
(6, '7', '100', '0', '60', 2, 4);

--
-- Contenu de la table `famille`
--

INSERT INTO `famille` (`ID`, `LIBELLE`, `ABONNE_ID`) VALUES
(1, 'LAMPE', 1),
(2, 'ZAJE', 1);

--
-- Contenu de la table `fournisseur`
--

INSERT INTO `fournisseur` (`ID`, `ADRESSE`, `BLOQUER`, `CREANCE`, `DESCRIPTION`, `DETAILBLOQUAGE`, `EMAIL`, `MODIFIER`, `NOM`, `NOMREPRESENTANT`, `SUPPRIMER`, `TEL`, `ABONNE_ID`) VALUES
(1, 'hbhbbh', 0, '111', 'gvgyvgvyygv', '', 'vggvg', 0, 'four 1', 'GRR', 1, 'fgvvggv', 1),
(2, 'hhg', 0, '22', 'uuhuhu', 'kljnjnj', 'uhuhuhu', 0, 'four 2', 'uuuhhu', 0, 'uhhuuhhu', 2),
(3, 'four3', 0, '222', 'four3', 'four3', 'four3', 0, 'four3', 'four3', 0, 'four3', 1);

--
-- Contenu de la table `magasin`
--

INSERT INTO `magasin` (`ID`, `ADRESSE`, `MODIFIER`, `NOM`, `SUPPRIMER`, `ABONNE_ID`) VALUES
(1, 'jnedh d', 0, 'MAG 1', 0, 1),
(2, 'dnfj', 0, 'MAG 2', 0, 1),
(3, 'hbhbdf', 0, 'MAGASIN 3', 0, 2),
(4, 'djfj', 0, 'MAGASIN 4', 0, 2);

--
-- Contenu de la table `paiementcommande`
--

INSERT INTO `paiementcommande` (`ID`, `DATEECHEANCE`, `DATEENCAISSEMENT`, `DATEPAIEMENT`, `DESCRIPTION`, `ENCAISSER`, `MODE`, `MODIFIER`, `MONTANT`, `SUPPRIMER`, `COMMANDE_ID`) VALUES
(2, '2015-12-08', NULL, '2016-01-28', 'enr ejeee', 1, 1, 0, '20', 0, 2),
(3, '2015-12-29', '2016-01-12', '2015-12-16', 'sjhdhjv', 1, 3, 0, '100', 0, 2);

--
-- Contenu de la table `produit`
--

INSERT INTO `produit` (`ID`, `LIBELLE`, `MODIFIER`, `QTEGLOBALSTOCK`, `QTEPARSTOCK`, `REFERENCE`, `SEUILALERT`, `SUPPRIMER`, `ABONNE_ID`, `FAMILLE_ID`, `UNITEMESURE_ID`) VALUES
(1, 'tid', 0, '114', NULL, 'EE', 2, 0, NULL, 2, 1),
(2, 'JABIR', 0, '333', NULL, 'FF33', 3, 0, NULL, 2, 1),
(3, 'dejaja', 0, '32', NULL, 'HHH', 2, 0, NULL, 2, 1),
(4, 'telaja', 0, '104', NULL, 'MMM', 3, 0, NULL, 2, 1);

--
-- Contenu de la table `projet`
--

INSERT INTO `projet` (`ID`, `DATEDEBUT`, `DATEFIN`, `MESSAGE`, `MODIFIER`, `NOM`, `SUPPRIMER`, `ABONNE_ID`) VALUES
(1, '2015-11-04', '2015-11-10', 'hada projet lwel', 0, 'pr1', 0, 1),
(2, '2015-11-11', '2015-11-27', 'prj tani', 0, 'prj 2', 0, 2);

--
-- Contenu de la table `reception`
--

INSERT INTO `reception` (`ID`, `COMMENTAIRE`, `DATERECEPTION`, `MODIFIER`, `MONTANTTOTAL`, `SUPPRIMER`, `COMMANDE_ID`, `RESPONSABLE_ID`, `AVOIRRECEPTION_ID`) VALUES
(1, 'REC 1', '2016-01-22', 0, '583', 0, 1, 1, NULL),
(2, 'arab karim', '2016-01-22', 0, '490', 0, 2, 1, NULL),
(4, 'tanyia', '2016-01-25', 0, '43', 0, 2, 1, NULL);

--
-- Contenu de la table `receptionitem`
--

INSERT INTO `receptionitem` (`ID`, `QTE`, `QTEAVOIR`, `MAGASIN_ID`, `PRODUIT_ID`, `RECEPTION_ID`) VALUES
(1, '4', '0', 1, 1, 1),
(2, '3', '0', 1, 3, 1),
(3, '70', '0', 1, 2, 1),
(4, '6', '0', 1, 3, 2),
(5, '2', '0', 1, 1, 2),
(6, '60', '0', 1, 4, 2),
(11, '4', '0', 1, 3, 4),
(10, '1', '0', 1, 1, 4);

--
-- Contenu de la table `responsable`
--

INSERT INTO `responsable` (`ID`, `EMAIL`, `MODIFIER`, `NOM`, `PRENOM`, `SUPPRIMER`, `TEL`, `ABONNE_ID`) VALUES
(1, 'nonos', 0, 'nonos', 'nonos', 0, 'nonos', 1),
(2, 'koko', 0, 'koko', 'koko', 0, 'koko', 1);

--
-- Contenu de la table `secteuractivite`
--

INSERT INTO `secteuractivite` (`ID`, `NOM`) VALUES
(1, 'ELECTRICITE'),
(2, 'PLOMBERIE'),
(3, 'DROGUERIE'),
(4, 'PIECE AUTO');

--
-- Contenu de la table `sequence`
--


--
-- Contenu de la table `stock`
--

INSERT INTO `stock` (`ID`, `QTE`, `QTEDEFFECTUEUX`, `ABONNE_ID`, `MAGASIN_ID`, `PRODUIT_ID`) VALUES
(1, '37', NULL, 1, 1, 1),
(2, '110', NULL, 1, 1, 2),
(3, '100', NULL, 1, 2, 1),
(4, '50', NULL, 1, 2, 3),
(5, '30', NULL, 1, 2, 4),
(6, '10', '0', 1, 1, 3),
(7, '60', '0', 1, 1, 4);

--
-- Contenu de la table `unitemesure`
--

INSERT INTO `unitemesure` (`ID`, `NOM`) VALUES
(1, 'M2'),
(2, 'M3'),
(3, 'Litre'),
(4, 'Tonne');

--
-- Contenu de la table `user`
--

INSERT INTO `user` (`LOGIN`, `BLOCKED`, `EMAIL`, `NOM`, `PASSWORD`, `PRENOM`, `TEL`, `ABONNE_ID`, `ROLE_ID`) VALUES
('chocho', 0, 'chocho', 'chocho', 'chocho', 'chocho', 'chocho', 1, NULL),
('log1', 0, 'soso', 'soso', 'pass1', 'soso', 'soso', 1, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
