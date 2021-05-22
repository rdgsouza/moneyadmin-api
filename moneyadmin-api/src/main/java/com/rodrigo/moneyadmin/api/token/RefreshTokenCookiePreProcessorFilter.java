package com.rodrigo.moneyadmin.api.token;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.catalina.util.ParameterMap;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RefreshTokenCookiePreProcessorFilter implements Filter {

	// Nos passamos o refresh token para o cookie na classe
	// RefreshTokenPostProcessor
	// pois quando for feita a solicitacao de um acess token no oauth/token
	// o refresh token sera passado para o cookie foi assim que implementamos
	// Mais quando for feito um pedido de um novo acess token pelo refresh token
	// o refresh token nao estara mais no header da requisicao e sim em um cookie
	// entao precisamo pegar o valor desse refresh token do cookie e passar para
	// requisicao
	// que e um mapa de parametros da requisicao e para isso criamos um filtro
	// abaixo para
	// intercepitar essa requisicao e pegar o cookie que estar no metodo doFilter
	// Entao conseguimos pegar o valor do cookie so que acontece que quando a
	// requisicao
	// estar pronta nao e possivel mexer mais no caso adicionar nada
	// entao a solucao foi crair a classe MyServeletRequestWrapper e o que ela vai
	// fazer e um
	// wrapper do ServeletRequest e ja existe ate uma classe para esse proposito que
	// e classe
	// HttpServeletRequestWrapper onde vamos estender ela na classe estatica abaixo
	// chamada de
	// MyServletRequestWrapper e nela temos um metodo construtor onde vamos passar
	// parametros
	// um paramtro request e outro o valor do refresh token dai o que vamos fazer e
	// um embrulho
	// do refresh token para essa requisicao resumindo nos vamos passar um novo
	// parametro
	// para requisicao atraves do mapa de parametro getParameterMap
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;

		if ("/oauth/token".equalsIgnoreCase(req.getRequestURI())
				&& "refresh_token".equals(req.getParameter("grant_type")) && req.getCookies() != null) {

			String refreshToken = Stream.of(req.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName()))
					.findFirst().map(cookie -> cookie.getValue()).orElse(null);

			req = new MyServletRequestWrapper(req, refreshToken);
            
		}

		// Antes de fazer assim :

//		String refreshToken = Stream.of(req.getCookies()).filter(cookie -> "refreshToken".equals(cookie.getName()))
//				.findFirst().map(cookie -> cookie.getValue()).orElse(null);

		// Era asssim:
		/*
		 * for (Cookie cookie : req.getCookies()) {
		 * 
		 * if (cookie.getName().equals("refreshToken")) {
		 * 
		 * String refreshToken = cookie.getValue();
		 * 
		 * req = new MyServletRequestWrapper(req, refreshToken); } }
		 */

		// Mudamos isso porque ?

//		Antes do Java 8 um cenario normalmente encontrado, era ter uma lista populada e
//      percorrer usando um looping com um for tradicional no caso o foreach que
//		percorreria todos os elementos de uma Collection no caso os elementos seriam os 
//		cookies existente que ficariam dentro dessa Collection
//		O que fizemos e é opcional, porém torna o código mais fácil de se ler.:
//		Trocamos a forma de pegar o valor do cookie utilizando a API de stream
//		sem precisar fazer um looping com um foreach
//		Transformamos o array de cookies em um Stream, com o comando Stream.of(...)
//		Filtramos os dados do Stream para que retorne apenas o que tenha o nome refreshToken
//		Obter o primeiro objeto do Stream (caso exista)
//		Transformá-lo de cookie em uma String com o seu valor.
//		Caso não tenha encontrado um cookie com o nome refreshToken, retorna null.
//		
		// fonte a respeito do

	chain.doFilter(req, response);// E aqui damos continuidade do filtro com o mesmo request
	                             // so que com um novo valor no header que o refresh token
	}

	static class MyServletRequestWrapper extends HttpServletRequestWrapper {

		private String refreshToken;

		public MyServletRequestWrapper(HttpServletRequest request, String refreshToken) {

			super(request);
			this.refreshToken = refreshToken;
		}

		@Override
		public Map<String, String[]> getParameterMap() {

			// Mapa de parametros recebe como parametro um ServletRequest para pegar o mapa
			// de parametros esse ServletRequest foi setado no construtor da classe acima
			// no super(request)
			ParameterMap<String, String[]> map = new ParameterMap<>(getRequest().getParameterMap());
			map.put("refresh_token", new String[] { refreshToken });// novo valor para o paramero refresh_token
			map.setLocked(true);// setamos que o mapa sera travado depois que estiver com o novo valor em seu
								// parametro
			return map; // retornamos um novo mapa de parametros onde se encontra o novo valor
						// que e o refreshtoken

			// Quando o metodo construtor for chamado nos vamos passar o HttpServletRequest
			// e o
			// refreshToken em seguida e chamodo o public Map<String, String[]>
			// getParameterMap()
			// que estar com a anotaçao @Override
			// e nesse metodo e onde vai ser trabalhado o parametro refresh_token para 
			//receber um o refresh token
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
}
