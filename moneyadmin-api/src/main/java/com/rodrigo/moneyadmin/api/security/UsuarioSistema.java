package com.rodrigo.moneyadmin.api.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import com.rodrigo.moneyadmin.api.model.Usuario;

public class UsuarioSistema extends User{
	
	private static final long serialVersionUID = 1L;

	private Usuario usuario;

	@Autowired
	 public TokenEnhancer tokenEnhancer; 
		
	public UsuarioSistema(Usuario usuario, Collection<? extends GrantedAuthority> authorities) {
		super(usuario.getEmail(), usuario.getSenha(), authorities);
		
		this.usuario = usuario;
	}

	public Usuario getUsuario() {
		return usuario;
	}
}
