package models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "ANUNCIANTE")
public class Anunciante implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "id")
	private long id;

	@Column(name = "cidade", nullable = false)
	private String cidade;

	@Column(name = "bairro", nullable = false)
	private String bairro;

	@Column(name = "ondeTocar",nullable = false)
	private String ondeTocar;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Instrumento> listInstrumentos;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstiloQueGosta> listEstilos;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<EstiloQueNaoGosta> listEstilosNaoGosta;

	@OneToOne(orphanRemoval = true)
	private Contato contato;

	public Anunciante() {
		listInstrumentos = new ArrayList<Instrumento>();
		listEstilos = new ArrayList<EstiloQueGosta>();
		listEstilosNaoGosta = new ArrayList<EstiloQueNaoGosta>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) throws Exception {
		if (cidade == null || cidade.trim().equals("")) {
			throw new Exception("Insira o nome da sua cidade.");
		}
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) throws Exception {
		if (bairro == null || bairro.trim().equals("")) {
			throw new Exception("Insira o nome do seu bairro.");
		}
		this.bairro = bairro;
	}

	public String getOndeTocar() {
		return ondeTocar;
	}

	public void setOndeTocar(String ondeTocar) throws Exception {
		if(ondeTocar.trim().equals("")){
			throw new Exception("Insira onde voce quer tocar.");
		}
		this.ondeTocar = ondeTocar;
	}

	public List<Instrumento> getListInstrumentos() {
		return listInstrumentos;
	}

	public void setListInstrumentos(List<Instrumento> listInstrumentos)
			throws Exception {
		if (listInstrumentos.isEmpty()) {
			throw new Exception("Insira pelo menos um instrumento a sua lista.");
		}
		this.listInstrumentos = listInstrumentos;
	}

	public List<EstiloQueGosta> getListEstilos() {
		return listEstilos;
	}

	public void setListEstilos(List<EstiloQueGosta> listEstilos) {
		this.listEstilos = listEstilos;
	}

	public List<EstiloQueNaoGosta> getListEstilosQueNaoGosta() {
		return listEstilosNaoGosta;
	}

	public void setListEstilosQueNaoGosta(
			List<EstiloQueNaoGosta> listEstilosNaoGosta) {
		this.listEstilosNaoGosta = listEstilosNaoGosta;
	}

	public Contato getContato() {
		return contato;
	}

	public void setContato(Contato contato) throws Exception {
		if (contato.getEmail() == null && contato.getFacebook() == null
				&& contato.getTelefone() == null
				|| contato.getEmail().trim().equals("")
				&& contato.getFacebook().trim().equals("")
				&& contato.getTelefone().trim().equals("")) {
			throw new Exception("Insira pelo menos uma forma de contato.");

		}
		this.contato = contato;
	}
}