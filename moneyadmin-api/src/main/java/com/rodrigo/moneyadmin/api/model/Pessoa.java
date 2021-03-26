package com.rodrigo.moneyadmin.api.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "pessoa")
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@NotNull
	private String nome;

	@Embedded
	private Endereco endereco;
    // A anotação @Embedded
    // Especifica um campo ou propriedade persistente de uma entidade cuja
    // valor é uma instância de uma classe incorporável

	@NotNull
	private Boolean ativo;

	@JsonIgnoreProperties("pessoa") // Colocamos essa anotação por que quando ele for gerar o Json da entidade
	// pessoa ele vai ler todos os atributos e quando chegar no atributo contato entao ele entra na entidade
	// contato chegando la ele ler todos os atributos e dentro da entidade contato existe uma propiedade
	// pessoa entao ele entra na entidade pessoa e faz a leitura novamente entao fica em um loop
	// e no final das contas não consegue fazer a leitura para gerar o json e então a pilha explode
	// dando o erro stackoverflowerror - pilha estourada.
	// para resolver isso colocamos a anotação @JsonIgnoreProperties("pessoa") 
	// A classe pessoa precisa que leia a entidade contato mas quando chega na entidade pessoa e encontra
	// a variavel pessoa da entidade Pessoa na classe Contato nao precisa que leia essa propiedade
	// para isso fazemos dessa forma.
	// E porque não colocamos JsonIgnore na popiedade pessoa dentro da classe Contato
	// Porque se agente precisar criar uma classe de recurso de Contato daria um erro na propiedade pessoa
	// Ja que a entidade Contato estar mapeada e inclui a propiedade pessoa.
	// Fonte: https://www.algaworks.com/aulas/1726/resolvendo-o-stackoverflowerror-com-jsonignoreproperties/
	@Valid
	@OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL,
	orphanRemoval = true)
	private List<Contato> contatos;
//	No atributo "contatos", devemos informar qual atributo de "Contato" faz o mapeamento de "Pessoa":
//	A propriedade cascade permite que o atributo anotado dessa forma sofra uma alteração em cascata 
//	com a entidade que está vinculado.
//	Neste caso, quando você informar os dados de uma pessoa e enviar junto as informações de contato,
//	quando for feito a persistência da pessoa, os contatos também serão persistidos, criando este efeito
//	cascata.
//	Quando se usa o CascadeType.ALL, significa que qualquer alteração na entidade Pessoa, deva refletir
//	também nos seus contatos.
//	Então, neste caso, se você remover uma Pessoa que tem 4 contatos associados à ela, os 4 contatos
//	serão removidos primeiro, e depois a própria Pessoa. Isso vale para criação, remoção, atualização, etc.

//  Fonte: https://www.algaworks.com/forum/topicos/81686/sobre-a-anotacao-manytoone-e-onetomany-mappedby-pessoa/
//  Fonte: https://www.algaworks.com/forum/topicos/72172/duvida-sobre-o-cascade/
	
	
	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	public List<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(List<Contato> contatos) {
		this.contatos = contatos;
	}

	//As anotações abaixo e para que jackson e o hibernet não entenda o metodo isInativo
	//como uma propiedade entao essa anotacao significa que e para nao fazer serelizacao 
	//O @JsonIgnore e para jackson e o @Transient para o hibernate nao fazerem serelizacao
	@JsonIgnore
	@Transient
	public boolean isInativo() {
		return !this.ativo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
}
