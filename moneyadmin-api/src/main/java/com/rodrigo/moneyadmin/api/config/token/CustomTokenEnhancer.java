package com.rodrigo.moneyadmin.api.config.token;

import java.util.Map;
import java.util.HashMap;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.rodrigo.moneyadmin.api.security.UsuarioSistema;

//	Estrategia para aprimorar um token de acesso antes que ele seja armazenado 
//	por um {@link AuthorizationServerTokenServices}

//	Oferece uma oportunidade para a personalização de um token de acesso (por exemplo, 
// 	através de seu mapa de informações adicionais) durante
//	o processo de criação de um novo token para uso de um cliente.

public class CustomTokenEnhancer implements TokenEnhancer {

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken,
			OAuth2Authentication authentication) {

		UsuarioSistema usuarioSistema = (UsuarioSistema) authentication.getPrincipal();

		Map<String, Object> addInfo = new HashMap<>();
		addInfo.put("nome", usuarioSistema.getUsuario().getNome());

		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(addInfo);

		return accessToken;
	}

}
