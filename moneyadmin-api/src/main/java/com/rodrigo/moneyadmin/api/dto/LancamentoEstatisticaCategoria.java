package com.rodrigo.moneyadmin.api.dto;

import java.math.BigDecimal;

import com.rodrigo.moneyadmin.api.model.Categoria;

/*	DTO
 Objeto de Transferência de Dados (do inglês, Data transfer object, ou simplesmente DTO), é um
 padrão de projeto de software usado para transferir dados entre subsistemas de um software. DTOs são
 frequentemente usados em conjunção com objetos de acesso a dados para obter dados de um banco de dados.	
*/	

public class LancamentoEstatisticaCategoria {

	private Categoria categoria;		
	
	private BigDecimal total;

	public LancamentoEstatisticaCategoria(Categoria categoria, BigDecimal total) {
			
		this.categoria = categoria;
		this.total = total;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
		
}
