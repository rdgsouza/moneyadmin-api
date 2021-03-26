package com.rodrigo.moneyadmin.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Profile("oauth-security")
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/categorias").permitAll()
		      .anyRequest().authenticated()
		   	  .and()
	    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
	    .csrf().disable();
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.stateless(true);
	}

	@Bean
	// Basicamente quando você coloca a anotação @Bean, você está dizendo pro Spring
	// que quer criar esse objeto e deixar ele disponível para outras classes
	// utilizarem ele como dependência.
	// você precisa explicitamente configurar o bean ao invés de
	// deixar o spring automaticamente fazer e ao fazer a configuracao e colcoar o
	// @Bean o Spring vai entender que deverar ser reaproveitada essa configuracao
	// desse @Bean
	// em toda aplicacao
	
	//Não basta apenas adicionar a ananotação 
	//@EnableGlobalMethodSecurity(prePostEnabled = true)
	//para configurarmos as permissões de acesso aos metodos da nossa api 
	//como tbm precisamos do
	//metodo abaixo pra permite de fato que facamos as configuracoes de permissoes nos metodos
	//da nossa api
	public MethodSecurityExpressionHandler createExpressionHandler() {

		return new OAuth2MethodSecurityExpressionHandler();
	}
}