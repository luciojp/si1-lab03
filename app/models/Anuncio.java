package models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@SuppressWarnings("serial")
@Entity
@Table(name = "ANUNCIO")
public class Anuncio implements Serializable {
	
	@Id
	@GeneratedValue
	private long id;

	@Column(name = "titulo", nullable = false)
	private String titulo;

	@Column(name = "descricao", nullable = false)
	private String descricao;

	@Temporal(TemporalType.DATE)
	@Column(name = "data")
	private Calendar dataDeCriacao;

	@OneToOne(orphanRemoval = true)
	private Anunciante anunciante;

	@Column(nullable = false)
	private String palavraChave;
	
	public Anuncio() {
		setDataDeCriacao(Calendar.getInstance());
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) throws Exception {
		if (titulo.trim().equals("") || titulo == null) {
			throw new Exception("Insira um titulo ao anuncio");
		}
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) throws Exception {
		if (descricao.trim().equals("") || descricao == null) {
			throw new Exception("Insira um descriçao ao anuncio.");
		}
		if (descricao.length() < 150) {
			throw new Exception(
					"Insira uma descrisao de no minimo 100 caracters.");
		}
		this.descricao = descricao;
	}

	public Calendar getDataDeCriacao() {
		return dataDeCriacao;
	}

	public String getData(){
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String dataCriacao = formato.format(getDataDeCriacao().getTime());
		return dataCriacao;
	}
	
	private void setDataDeCriacao(Calendar dataDeCriacao) {
		this.dataDeCriacao = dataDeCriacao;
	}

	public Anunciante getAnunciante() {
		return anunciante;
	}

	public void setAnunciante(Anunciante anunciante) {
		this.anunciante = anunciante;
	}

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) throws Exception {
		if(palavraChave.trim().equals("") || palavraChave == null){
			throw new Exception("Insira uma palavra chave ao anuncio para que voce um dia possa excluir ele.");
		}
		this.palavraChave = palavraChave;
	}
}
