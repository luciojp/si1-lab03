package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "INSTRUMENTO")
public class Instrumento {
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "nome", nullable = false)
	private String nome;

	public Instrumento() {

	}
	
	public Instrumento(String nome){
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
