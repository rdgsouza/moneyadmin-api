package com.rodrigo.moneyadmin.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rodrigo.moneyadmin.api.config.property.MoneyAdminApiProperty;

@RestController
@RequestMapping("/tokens")
public class TokenResource {

	@Autowired
	private MoneyAdminApiProperty algamoneyProperty;

	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(algamoneyProperty.getSeguranca().isEnableHttps());
		cookie.setPath(req.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);

		// Esse metodo recebe os paramentros HttpServletRequest e HttpServletResponse
		// para trabalharmos em cima da requesicao
		// Quando feita a requisicao sera criado um cookie onde sera apontada a pasta
		// oauth/token essa pasta foi configurada no cookie que estar no browser
		// entao quando retornar com a resposta o browser vai enteder que se refere ao
		// mesmo cookie
		// que estar no browser com isso e alterado o valor do cokkie do browser
		// com o valor null e com o tempo zerado
		// assim o browser entende que o tempo do cookie acabou e ao mandar a requisicao
		// novamente
		// o cookie nao tera mas o valor do refreshtoken para pedir um novo acess token
		// pois seu valor agora e null
		// quando fizer a requisicacao novamente pedindo um novo acess token pelo
		// refresh token
		// o cookie estara com o valor null e autenticacao sera negada pois nao tem no cokkie
		//o valor do refresh token para ser autenticado e retornado um acess token.

		// A propriedade PATH de um cookie é necessária para saber quando o cookie
		// sera enviado na requisicao.
		// req.getContextPath() vai retornar uma String com o valor da URL da nossa
		// aplicação. Concatenamos esse valor com o endpoint "/oauth/token", pois assim
		// as requisicoes nesse endpoint receberao o cookie.xxxxxxxxxx

		resp.addCookie(cookie);

		resp.setStatus(HttpStatus.NO_CONTENT.value());
	}
}
