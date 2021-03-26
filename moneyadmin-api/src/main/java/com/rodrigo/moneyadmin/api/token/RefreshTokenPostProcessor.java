package com.rodrigo.moneyadmin.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.rodrigo.moneyadmin.api.config.property.MoneyAdminApiProperty;

@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {

	@Autowired
	private MoneyAdminApiProperty algamoneyApiProperty;

	// O parametro que colocamos na interface de implementacao acima como generico
	// que no caso e o <OAuth2AccessToken> ele é o tipo de dado que queremos
	// interceptar para ser tratado antes de ser dado uma resposta para o usuario

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		return returnType.getMethod().getName().equals("postAccessToken");

		// Quando for feita uma requisicao em localhost:8080/oauth/token para pegarmos
		// um token de acesso a api e ela retornar um acess token junto com o refresh
		// token
		// Queremos que nesse retorno o refresh token seja passado para um cookie entao
		// quando a requisicao chegar na api e o objeto OAuth2AccessToken for criado e o
		// metodo postAcessToken for
		// chamado para criar o acess token e refresh token então iremos fazer o
		// procedimento abaixo para adionarmos apenas o refresh token no
		// cookie e como resposta deixando apenas o acess token no corpo da respota
		// dessa forma
		// garantimos que aplicação front-end não tenha acesso ao refresh token e fique
		// apenas com o token que dura 20 segundos fortalecendo assim a seguranca da
		// nossa api e ao termino desses 20 segundo a aplicacao front-end tera uma
		// logica
		// que enviara uma requisicao para api e nessa requisicao ira o cookie e dentro
		// as
		// informacoes do refresh token e no corpo da requisicao vai tbm informacoes do
		// refresh token que estao contidas no cookie
		// com isso sera retornado o acess token que dura 20
		// segundos para continuarmos fazendo requisicoes na nossa api e esse ciclo se
		// repete ate o usuario sair da aplicacao isso fortalece
		// nosso sistema pois se caso o acess token seja de alguma forma interceptado na
		// rede ele tera a duracao de 20 segundos entao não dara para o receptor fazer
		// muita coisa pois seu tempo é apenas de 20 segundos depois desse tempo e
		// gerado outro acess token
	}

	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		HttpServletRequest req = ((ServletServerHttpRequest) request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse) response).getServletResponse();

		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;

		String refreshToken = body.getRefreshToken().getValue();// pegando o valor do token
		adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		removerRefreshTokenDoBody(token);

		return body;
	}

	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {

		token.setRefreshToken(null);
	}

	private void adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest req, HttpServletResponse resp) {

		Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);// o primeiro parametro e o nome do cookie
																				// o segundo e o valor
		refreshTokenCookie.setHttpOnly(true);// E um cookie to tipo http
		refreshTokenCookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());// e um cookie https ? se
																							// estiver em
																							// desenvovilmento sera
																							// false se estiver em
																							// producacao sera true
																							// porque no profile do
																							// spring colocamos no
																							// arquivo para que essa
																							// propiedade seja true
																							// quando subir para
																							// producacao
		refreshTokenCookie.setPath(req.getContextPath() + "/oauth/token");
		// A propriedade PATH de um cookie é necessária para saber quando o cookie
		// sera enviado na requisicao.
		// req.getContextPath() vai retornar uma String com o valor da URL da nossa
		// aplicação. Concatenamos esse valor com o endpoint "/oauth/token", pois assim
		// as requisicoes nesse endpoint receberao o cookie.

		refreshTokenCookie.setMaxAge(2592000);// essa e a duracao do cookie na aplicacao front-end que e 30 dias caso
												// ela nao seja removida manualmente sera removida automaticamente apos
												// 30 dias
		resp.addCookie(refreshTokenCookie);// adcionando o cookie na resposta do HttpServletResponse

	}

}
