package com.morpheo.cursomc.security;

import com.morpheo.cursomc.domain.models.enums.Perfil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class UserSS implements UserDetails{

    private Integer id;
    private String email;
    private String senha;
    private Collection<? extends GrantedAuthority> authorities;

    public UserSS() {
    }

    /**
    * Neste construtor foi feito um massete, onde no lugar
    * de receber uma lista de <strong>{@code Collection<? extends GrantedAuthority> authorities}</strong>
    * foi feito uma lista de perfis <strong>{@code Set<Perfil> perfils}</strong>.
    * para pegar os perfis de usuário da classe enum Perfil.
    *
    * <p>
    *     Convertendo o conjunto para a lista interna que o SpringSecurity
    *     precisa ({@code Collection<? extends GrantedAuthority> authorities}),
    *     através do {@literal stream().map(x ->
    *                 new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toSet());}
    * </p>
    **/
    public UserSS(Integer id, String email, String senha,
                  Set<Perfil> perfils) {

        this.id = id;
        this.email = email;
        this.senha = senha;
        this.authorities = perfils.stream().map(x ->
                new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toSet());
    }

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasHole(Perfil perfil) {
        //Testa se este usuário possui um dado perfil
        return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
    }
}
