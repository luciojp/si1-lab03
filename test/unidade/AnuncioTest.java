package unidade;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import models.Anunciante;
import models.Anuncio;
import models.Contato;
import models.Instrumento;
import models.DAO.GenericDAO;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AnuncioTest extends AbstractTest {
	private GenericDAO dao;
	private Anuncio anuncio;
	private Anunciante anunciante;
	private Contato contato;
	private List<Anuncio> listAnuncio;
	private List<Instrumento> listInstrumento;

	@Before
	public void criarObjetos() {
		dao = new GenericDAO();
		anuncio = new Anuncio();
		anunciante = new Anunciante();
		contato = new Contato();
		listAnuncio = new ArrayList<Anuncio>();
		listInstrumento = new ArrayList<>();
	}

	@Test
	public void deveIniciarSemAnuncio() {
		listAnuncio = dao.findAllByClass(Anuncio.class);
		assertThat(listAnuncio.size()).isEqualTo(0);
	}

	@Test
	public void deveExistirPeloMenosUmaFormaDeContato() {
		contato.setEmail("");
		contato.setFacebook("");
		contato.setTelefone("");

		try {
			anunciante.setContato(contato);
		} catch (Exception e) {
			Assert.assertEquals("Insira pelo menos uma forma de contato.",
					e.getMessage());
		}
		
		try {
			Contato newContato = new Contato();
			newContato.setEmail("celloandviolin@gmail.com");
			newContato.setFacebook(null);
			newContato.setTelefone(null);
			Assert.assertEquals("celloandviolin@gmail.com", newContato.getEmail());
			anunciante.setContato(newContato);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Deveria Ter Passado.");
		}
	}

	@Test
	public void deveTerUmaListaDeInstrumentos() {
		try {
			anunciante.setListInstrumentos(listInstrumento);
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals(
					"Insira pelo menos um instrumento a sua lista.",
					e.getMessage());
		}

		try {
			listInstrumento.add(new Instrumento("Piano"));
			listInstrumento.add(new Instrumento("Bateria"));
			anunciante.setListInstrumentos(listInstrumento);
			Assert.assertEquals(anunciante.getListInstrumentos(),
					listInstrumento);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Deveria Ter Passado.");
		}
	}

	@Test
	public void deveTerUmTitulo() {
		try {
			anuncio.setTitulo("");
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals("Insira um titulo ao anuncio", e.getMessage());
		}

		try {
			anuncio.setTitulo("Quero Tocar Ocasionalmente");
		} catch (Exception e) {
			Assert.fail("Deveria Ter Passado.");
		}
	}

	@Test
	public void deveTerUmaDescrisao() {
		try {
			anuncio.setDescricao("");
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals("Insira um descriçao ao anuncio.",
					e.getMessage());
		}

		try {
			anuncio.setDescricao("Quero Tocar Ocasionalmente");
		} catch (Exception e) {
			Assert.assertEquals(
					"Insira uma descrisao de no minimo 100 caracters.",
					e.getMessage());
		}
	}

	@Test
	public void deveTerUmaPalavraChave() {
		try {
			anuncio.setPalavraChave("");
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals(
					"Insira uma palavra chave ao anuncio para que voce um dia possa excluir ele.",
					e.getMessage());
		}

		try {
			anuncio.setPalavraChave("javaislife");
			Assert.assertEquals("javaislife", anuncio.getPalavraChave());
		} catch (Exception e) {
			Assert.fail("Deveria Ter Passado.");
		}
	}
	
	@Test
	public void deveTerUmCidade(){
		try {
			anunciante.setCidade("");
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals(
					"Insira o nome da sua cidade.",
					e.getMessage());
		}

		try {
			anunciante.setCidade("João Pessoa");
			Assert.assertEquals("João Pessoa", anunciante.getCidade());
		} catch (Exception e) {
			Assert.fail("Deveria Ter Passado.");
		}
	}
	
	@Test
	public void deveTerUmBairro(){
		try {
			anunciante.setBairro("");
			Assert.fail("Deveria Ter Falhado.");
		} catch (Exception e) {
			Assert.assertEquals(
					"Insira o nome do seu bairro.",
					e.getMessage());
		}

		try {
			anunciante.setCidade("Altiplano");
			Assert.assertEquals("Altiplano", anunciante.getCidade());
		} catch (Exception e) {
			Assert.fail("Deveria Ter Passado.");
		}
	}
	
}
