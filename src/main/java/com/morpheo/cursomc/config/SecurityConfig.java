package com.morpheo.cursomc.config;

import com.morpheo.cursomc.security.JWTAuthenticationFilter;
import com.morpheo.cursomc.security.JWTAuthorizationFilter;
import com.morpheo.cursomc.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private Environment env;
    @Autowired
    private JWTUtil jwtUtil;

    /*
    * Aqui foi injetado a interface do SpringSecurity (UserDetailsService)
    * mas o Spring consegue identificar no sistema uma
    * implementação dessa interface, então ele encontrarar
    * a implementação feita (UserDetailServiceImpl), assim
    * injetará uma instancia aqui. Essa instância vai ser
    * usada para saber quem vai ser o cara
    * que é capaz de buscar um usuário por email.
    * */
    @Qualifier("userDetailServiceImpl")
    @Autowired
    private UserDetailsService userDetailsService;



    private static final String[] PUBLIC_MATCHERS = {
            "/h2-console/**"
    };

    private static final String[] PUBLIC_MATCHERS_GET = {
            "/produtos/**",
            "/categorias/**",
            "/estados/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
            "/clientes",
            "/cliente/picture",
            "/auth/forgot/**"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //Pegando os profiles ativos no projeto, se tiver algum ativo ele libera
        if (Arrays.asList(env.getActiveProfiles()).contains("test"))
            http.headers().frameOptions().disable();

        //De modo geral pode-se desabilitar proteção a CSRF em sistemas stateless
        http.cors().and().csrf().disable();

        http.authorizeRequests()
                //Somente o método GET está liberado para Categorias
                .antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
                .antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
                .antMatchers(PUBLIC_MATCHERS).permitAll()
                .anyRequest().authenticated();


        //Para assegurar que o backEnd não vai criar sessão de User
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //Registrando o método da classe (JWTAuthenticationFilter) para funcionar a autenticação
        http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));

        //Registrando o método da classe (JWTAuthorizationFilter) para funcionar a autorização
        http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /*Para que requisições de multiplas fontes sejam feitas é necessário um
    * Bean de configuração de Cors
    * Configurações básicas aplicadas.*/
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @ Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
