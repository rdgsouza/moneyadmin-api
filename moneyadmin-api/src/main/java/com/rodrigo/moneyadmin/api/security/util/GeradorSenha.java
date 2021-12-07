package com.rodrigo.moneyadmin.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

//		System.out.println(encoder.encode("r1234")); // Usuario Rodrigo Souza. email: rdsouza.c@gmail.com
	    System.out.println(encoder.encode("r4321")); //Usuario Rodrigues Souza. email: rdsouza.cs2@gmail.com 
	}
}
