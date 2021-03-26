package com.rodrigo.moneyadmin.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	
	public static void main(String[] args) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//      System.out.println(encoder.encode("souz@456")); //Usuario Rodrigo Souza
	  System.out.println(encoder.encode("souz@654")); //Usuario Rodrigues Souza
	}
}
	