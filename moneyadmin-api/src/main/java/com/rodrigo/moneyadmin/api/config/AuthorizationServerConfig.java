package com.rodrigo.moneyadmin.api.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import com.rodrigo.moneyadmin.api.config.token.CustomTokenEnhancer;

@Profile("oauth-security")
@Configuration
//A anotacao Spring @Configuration faz parte da estrutura principal do Spring Boot.
//A anotacao Spring Configuration indica que a classe possui metodos de definicao @Bean.
//Portanto, o conteiner Spring pode processar a classe e gerar o Spring Beans para ser
//usado no aplicativo.

@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// O OAuth 2.0 é um protocolo que permite aos usuarios ter acesso limitado a
	// recursos da api sem precisar expor suas credenciais
	// O OAuth Ele faz a autentificacao e autorizacao

	// Vamos usar o token JWT do oauth para autentificacao e autorizacao
	// JWTs são credenciais, que podem conceder acesso aos recursos.

	// Um token e um indentificador para saber se o usuario que estar fazendo a
	// requisicao tem
	// acesso ou nao a nossa api entao antes de fazer uma resicao nos recursos da
	// nossa api
	// o usuario tem que ter um
	// token valido apos ser validado ai sim o usuario poderar fazer requisicoes
	// so que tem um porem o JWT token vem com autorizacoes pre definidas nele entao
	// um
	// usuario
	// pode ter acesso total a aplicacao ou com certas restricoes
	// OBS: o Token JWT ele vem encodado e se divide em tres partes
	// Header - payload - verify sig nature

	@Autowired
	private AuthenticationManager authenticationManager;

//	Para que uma instancia do tipo authenticationManager possa ser injetada em algum dos 
//	pontos de injecao e preciso que ela se torne um bean Spring.
//	Fazemos isso anotando ela com a anotacao @Configuration -
//  @Component - @Respository - @Service - @Controller
//  A partir do momento que usamos a anotacao 
//  @Autowired em cima da propiedade estamos fazendo uma injecao de dependencia naquele momento
//  O spring estara providenciando uma instancia daquele atributo atraves da injecao de depencia

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

		clients.inMemory().withClient("angular")
		  .secret("$2a$10$QhN9FCkKPg/LURVrtZSU7.m/sLR0wsUZCMhkBy9SZUUhly1gp1WTO")
		  .scopes("read", "write").authorizedGrantTypes("password", "refresh_token")
		  .accessTokenValiditySeconds(1800).refreshTokenValiditySeconds(3600 * 24).and()
		  .withClient("mobile")
		  .secret("$2a$10$9R9rH0eZCW84.Ddg4r.CWewUu66wc5RILZITG9qP4gVE2yFlxT6Ue")
		  .scopes("read")
		  .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(1800)
		  .refreshTokenValiditySeconds(3600 * 24).and()
		  .withClient("desktop")
		  .secret("$2a$10$FpPsGj9FipwK.9j2gV8Gt.QVrsrYZvbjr898hR/s/iYgz0PFat7lu")
		  .scopes("read")
		  .authorizedGrantTypes("password", "refresh_token").accessTokenValiditySeconds(1800)
		  .refreshTokenValiditySeconds(3600 * 24);
	}
// Em .authorizedGrantTypes("password", "refresh_token") sao os tipos de concessao autorizadas
// no caso "password" é o password flow no caso a aplicação vai receber usuario e senha e enviar
// para pegar o acesstoken.
// E o "refresh_token" é o refreshToken que é retornado junto com os acesstoken podemos utilizar
// para fazer uma requisição http usando o refreshtoken
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
		tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), 
				accessTokenConverter()));

		endpoints
		   .tokenStore(tokenStore())
		   .tokenEnhancer(tokenEnhancerChain)
		   .reuseRefreshTokens(false)
		   .userDetailsService(this.userDetailsService)
		   .authenticationManager(this.authenticationManager);

		// AuthenticationManager: Para procurar usuarios finais (nao clientes) no caso o
		// que foi retornado do banco e nao o cliente que e a aplicacao
		// TokenStore: Para gerar e recuperar tokens
		// AccessTokenConverter: Para converter tokens de acesso em diferentes formatos,
		// como JWT.

	}

	// O userDetailService e usado para trazer o usuario do banco de dados pelo
	// username
	// nesse caso escolhemos o username o email do usuario
	// O userDetailService vai pegar a informacao de username da requisicao
	// E la nos colocamos o username o email
	// Entao o userDetailService vai trabalhar com esse email para trazer o usuario
	// do banco
	// Uma vez que traz esse usuario ele tera o email a senha e as autorizacoes
	// depois que o userDatailService retorna essas propiedades e a vez do
	// authenticationManager
	// fazer as verificaoes dos dois lados
	// ele pega as propiedades retornadas pelo userDatailService e compara com as
	// informacoes
	// vindas na requisicao caso uma das propiedades nao seja igual a que o
	// userDatailsService retornou
	// entao e retornado um erro ao usuario de acesso negado

	// O AuthenticationManager e uma classe estatica que gerencia os modulos de
	// autenticacao que um aplicativo usa. Quando uma solicitacao e feita aos
	// recursos protegidos, o AuthenticationManager chama o metodo Authenticate para
	// obter uma instancia de Autorizacao para usar nas solicitacoes subsequentes

	@Bean // Basicamente quando você coloca a anotação @Bean, você está dizendo pro Spring
	// que quer criar esse objeto e deixar ele disponível para outras classes
	// utilizarem ele como dependência.
	// você precisa explicitamente configurar o bean ao invés de
	// deixar o spring automaticamente fazer e ao fazer a configuracao e colcoar o
	// @Bean o Spring vai entender que deverar ser reaproveitada essa configuracao
	// desse @Bean
	// em toda aplicacao
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
		accessTokenConverter.setSigningKey("algaworks"); // Configurando a chave do token
		return accessTokenConverter; // retornando o acess token do tipo Jwt configurado com a chave de de
										// confirmacao

	}

	@Bean
	public TokenStore tokenStore() {

		return new JwtTokenStore(accessTokenConverter());
	}

	@Bean
	public TokenEnhancer tokenEnhancer() {

		return new CustomTokenEnhancer();
	}
}