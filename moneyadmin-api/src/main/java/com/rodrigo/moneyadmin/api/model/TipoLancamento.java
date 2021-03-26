package com.rodrigo.moneyadmin.api.model;

public enum TipoLancamento {

	RECEITA("Receita"), // Como vamos exibir essas descrições se é receita ou se é despesa no relatorio
	// Então para nao deixar em caixa alta "RECEITA" "DESPESA" vamos confirugar a sua exibição Formando da
	// maneira que queremos mostrar que no caso dessa forma: "Receita" "Despesa" A primeira letra maiuscula e
	// o restante minuscula entao depois de colocar o valor do enum abrimos parenteses e entre aspas duplas
	// colocamos a nossa formatação de texto
	DESPESA("Despesa");
	
	// Vamos fazer uma configuração abaixo para na que na hora de exibir no nosso relatorio 
	// usemos a variavel descricao para dizer se e uma receita ou despesa
	
	private final String descricao;	
	
	TipoLancamento(String descricao) {
		this.descricao = descricao;	
	}
	
	public String getDescricao() {
		return descricao;
	}	
	
}
