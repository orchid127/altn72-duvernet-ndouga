package com.altn72duvernetndouga.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Règles d’accès de l’application : tout est protégé sauf la page de connexion
 **/
@Configuration
public class SecuriteConfig {

    /**
     * BCrypt ajoute un sel aléatoire : deux mots de passe identiques donnent deux empreintes différente
     **/

    @Bean
    public PasswordEncoder encodeurMotDePasse() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Définit les pages publiques, la page de connexion et la déconnexion.
     **/
    @Bean
    public SecurityFilterChain filtreSecurite(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(autorisations -> autorisations
                        .requestMatchers("/connexion", "/css/**", "/error").permitAll()
                        .anyRequest().authenticated())
                .formLogin(formulaire -> formulaire
                        .loginPage("/connexion")
                        .usernameParameter("login")
                        .passwordParameter("motDePasse")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/connexion?erreur"))
                .logout(deconnexion -> deconnexion
                        .logoutUrl("/deconnexion")
                        .logoutSuccessUrl("/connexion?deconnecte"));
        return http.build();
    }
}
