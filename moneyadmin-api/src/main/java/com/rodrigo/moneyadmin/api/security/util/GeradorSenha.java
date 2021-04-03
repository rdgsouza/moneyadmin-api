package com.rodrigo.moneyadmin.api.security.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		System.out.println(encoder.encode("rodrigo21")); // Usuario Rodrigo Souza. email: rdsouza.c@gmail.com
//	    System.out.println(encoder.encode("souza07")); //Usuario Rodrigues Souza. email: rdsouza.cs10@gmail.com 
	}
}
