package com.rodrigo.moneyadmin.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Profile("oauth-security")
@Configuration
@EnableWebSecurity
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

	// Precisaremos criar uma nova classe de configuração para evitarmos problemas
	// com algumas das dependencias que usamos no projeto
	// como por exemplo a dependencia

	// <dependency>
	// <groupId>org.springframework.security.oauth</groupId>x
	// <artifactId>spring-security-oauth2</artifactId>
	// <version>2.3.5.RELEASE</version>
	// </dependency>

	// Sem essa classe, nao sera possivel injetar a propriedade
	// AuthenticationManager que esta como dependencia em AuthorizationServerConfig.
	// Por isso precisamos definir como um Bean em nossa classe de configuracao.

	// Tambem teremos problema com o PasswordEncoder, que precisamos definir nessa
	// nova versao.

	// PS.: NoOpPasswordEncoder esta como deprecated por nao ser recomendado sua
	// utilizacao (pois nao realiza nenhum tipo de encriptacao) entao no metodo
	// passwordEncoder()
	// iremos retornar o BCryptPasswordEncoder()
	@Bean
	// Basicamente quando você coloca a anotação @Bean, você está dizendo pro Spring
	// que quer criar esse objeto e deixar ele disponível para outras classes
	// utilizarem ele como dependência.
	// você precisa explicitamente configurar o bean ao invés de
	// deixar o spring automaticamente fazer e ao fazer a configuracao e colcoar o
	// @Bean o Spring vai entender que deverar ser reaproveitada essa configuracao
	// desse @Bean
	// em toda aplicacao
	@Override
	public AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	// Interface de serviço para codificacao de senhas. A implementacao preferida e
	// BCryptPasswordEncoder.
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}