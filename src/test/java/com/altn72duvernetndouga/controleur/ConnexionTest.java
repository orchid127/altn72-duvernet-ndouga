package com.altn72duvernetndouga.controleur;

import com.altn72duvernetndouga.modele.Utilisateur;
import com.altn72duvernetndouga.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Vérifie le comportement de F1 : accès protégé, connexion réussie ou refusée, déconnexion.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ConnexionTest {

    private static final String MOT_DE_PASSE_TEST = "motdepasse-test";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder encodeurMotDePasse;

    /**
     * La base H2 est vide au départ : on crée avant chaque test l'utilisateur qui servira à se connecter.
     */
    @BeforeEach
    void creerUtilisateurDeTest() {
        utilisateurRepository.deleteAll();
        String motDePasseHache = encodeurMotDePasse.encode(MOT_DE_PASSE_TEST);
        utilisateurRepository.save(new Utilisateur(1, motDePasseHache, "Camille", "RESPONSABLE"));
    }

    @Test
    void visiteurNonConnecteEstRedirigeVersLaConnexion() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/connexion"));
    }

    @Test
    void mauvaisMotDePasseRenvoieVersLaConnexionAvecErreur() throws Exception {
        mockMvc.perform(formLogin("/connexion")
                        .userParameter("login")
                        .passwordParam("motDePasse")
                        .user("1")
                        .password("mauvais-mot-de-passe"))
                .andExpect(redirectedUrl("/connexion?erreur"))
                .andExpect(unauthenticated());
    }

    @Test
    void bonsIdentifiantsOuvrentLaSession() throws Exception {
        mockMvc.perform(formLogin("/connexion")
                        .userParameter("login")
                        .passwordParam("motDePasse")
                        .user("1")
                        .password(MOT_DE_PASSE_TEST))
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated());
    }

    @Test
    void deconnexionRedirigeVersLaConnexion() throws Exception {
        mockMvc.perform(logout("/deconnexion"))
                .andExpect(redirectedUrl("/connexion?deconnecte"))
                .andExpect(unauthenticated());
    }
}