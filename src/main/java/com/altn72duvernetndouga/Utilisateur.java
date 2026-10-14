package com.altn72duvernetndouga;

import jakarta.persistence.*;

@Entity
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String motDePasseHache;

    @Column(nullable = false)
    private String prenom;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Profil profil;

    protected Utilisateur() {
    }

    public Utilisateur(String login, String motDePasseHache, String prenom, Profil profil) {
        this.login = login;
        this.motDePasseHache = motDePasseHache;
        this.prenom = prenom;
        this.profil = profil;
    }

    public Profil getProfil() {
        return profil;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMotDePasseHache() {
        return motDePasseHache;
    }

    public String getLogin() {
        return login;
    }

    public Long getId() {
        return id;
    }

}
