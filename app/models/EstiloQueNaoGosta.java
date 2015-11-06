package models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "ESTILO_QUE_NAO_GOSTA")
public class EstiloQueNaoGosta implements Serializable {
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	public EstiloQueNaoGosta() {

	}

	public EstiloQueNaoGosta(String nome) {
		setNome(nome);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
