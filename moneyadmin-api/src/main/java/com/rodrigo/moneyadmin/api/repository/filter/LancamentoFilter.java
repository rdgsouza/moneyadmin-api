package com.rodrigo.moneyadmin.api.repository.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.rodrigo.moneyadmin.api.model.Pessoa;

public class LancamentoFilter {

	// Classe que representa os filtros seram feitos nas consultas query string
    //Essa classe sera passada como parametro para ser feito o filtro
	private String descricao;

	private Pessoa pessoa;

	@DateTimeFormat(pattern = "yyyy-MM-dd") // Formato da data que estar no banco de dados
	// obs: a api ainda nao fez uma forma de aceitar em varios formatos entao
	// especifique esse formato para não da erro na hora de fazer a consulta
	private LocalDate dataVencimentoDe;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataVencimentoAte;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public LocalDate getDataVencimentoDe() {
		return dataVencimentoDe;
	}

	public void setDataVencimentoDe(LocalDate dataVencimentoDe) {
		this.dataVencimentoDe = dataVencimentoDe;
	}

	public LocalDate getDataVencimentoAte() {
		return dataVencimentoAte;
	}

	public void setDataVencimentoAte(LocalDate dataVencimentoAte) {
		this.dataVencimentoAte = dataVencimentoAte;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}
}
