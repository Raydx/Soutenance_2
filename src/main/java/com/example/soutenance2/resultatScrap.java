package com.example.soutenance2;

import java.time.LocalDate;

public class resultatScrap {
    String titre;
    int genre;
    LocalDate date;
    double prix;
    String desc;
    String lien;

    public resultatScrap(String titre, int genre, LocalDate date, double prix, String desc, String lien) {
        this.titre = titre;
        this.genre = genre;
        this.date = date;
        this.prix = prix;
        this.desc = desc;
        this.lien = lien;
    }
}
