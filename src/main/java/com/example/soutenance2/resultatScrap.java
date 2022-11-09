package com.example.soutenance2;

import java.time.LocalDate;

/**
 * La classe resultatScrap
 * Contient les données scrappées pour UN résultat.
 */
public class resultatScrap {
    String titre;
    int genre;
    LocalDate date;
    double prix;
    String desc;
    String lien;

    /**
     * Constructeur de la classe resultatScrap
     *
     * @param titre Titre de l'œuvre, nom de l'artiste
     * @param genre Le genre musical
     * @param date  La date d'édition
     * @param prix  Le prix
     * @param desc  Description du produit
     * @param lien  Lien qui mène au produit
     */
    public resultatScrap(String titre, int genre, LocalDate date, double prix, String desc, String lien) {
        this.titre = titre;
        this.genre = genre;
        this.date = date;
        this.prix = prix;
        this.desc = desc;
        this.lien = lien;
    }
}
