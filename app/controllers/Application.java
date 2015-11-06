package controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import models.Anunciante;
import models.Anuncio;
import models.Contato;
import models.EstiloQueGosta;
import models.EstiloQueNaoGosta;
import models.Instrumento;
import models.DAO.GenericDAO;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

public class Application extends Controller {
	private static final GenericDAO dao = new GenericDAO();
	private static Integer contadorDeAnunciosResolvidos = new Integer(0);
	
	@Transactional
	public static Result index() {
		return ok(views.html.index.render("Meeting my band",
				dao.findAllByClass(Anuncio.class),contadorDeAnunciosResolvidos));
	}

	/*
	 * Carrega os dados do anuncio especifico
	 */
	@Transactional
	public static Result detalhesAnuncio(Long id) {
		return ok(views.html.detalhes.render(dao.findByEntityId(Anuncio.class,
				id)));
	}

	/*
	 * Cadastrar um novo anuncio
	 */
	@Transactional
	public static Result novoAnuncio() {
		/*
		 * Cria objetos
		 */
		Anuncio anuncio = new Anuncio();
		Anunciante anunciante = new Anunciante();
		Contato contatos = new Contato();

		DynamicForm requestAnuncio = Form.form().bindFromRequest();

		try {
			/*
			 * Dados do anucio
			 */
			anuncio.setTitulo(requestAnuncio.get("titulo"));
			anuncio.setDescricao(requestAnuncio.get("descricao"));
			anuncio.setPalavraChave(requestAnuncio.get("palavra-chave"));

			/*
			 * Dados do anunciante
			 */
			anunciante.setBairro(requestAnuncio.get("bairro"));
			anunciante.setCidade(requestAnuncio.get("cidade"));
			anunciante.setOndeTocar(requestAnuncio.get("ondeTocar"));

			/*
			 * Requisiçao com multiplos objetos
			 */
			anunciante.setListInstrumentos(getSelecionadosInstrumentos());
			anunciante.setListEstilos(getSelecionadosEstiloQueGosta());
			anunciante
					.setListEstilosQueNaoGosta(getSelecionadosEstiloQueNaoGosta());

			/*
			 * Dados de contatos
			 */
			contatos.setEmail(requestAnuncio.get("email"));
			contatos.setFacebook(requestAnuncio.get("facebook"));
			contatos.setTelefone(requestAnuncio.get("telefone"));

			/*
			 * Atualiza dados do contato no banco de dados
			 */
			dao.persist(contatos);
			dao.flush();
			anunciante.setContato(contatos);
			/*
			 * Atualiza dados do anunciante no banco de dados
			 */
			dao.persist(anunciante);
			dao.flush();
			anuncio.setAnunciante(anunciante);
			/*
			 * Atualiza dados do anuncio no banco de dados
			 */
			dao.persist(anuncio);
			dao.flush();
		} catch (Exception e) {
			e.getMessage();
			return redirect(routes.Application.getListasCadastrar());
		}

		return redirect(routes.Application.index());
	}
	
	/*
	 * Finalizar Anuncio
	 */
	@Transactional
	public static Result deletarAnuncio(long id) {
		DynamicForm requestDeletar = Form.form().bindFromRequest();
		Anuncio anuncioADeletar = dao.findByEntityId(Anuncio.class, id);

		if (anuncioADeletar.getPalavraChave().equals(
				requestDeletar.get("palavra-chave")) && requestDeletar.get("resolvido").equals("SIM")) {
			dao.remove(anuncioADeletar);
			contadorDeAnunciosResolvidos+= 1;
		} else if(anuncioADeletar.getPalavraChave().equals(requestDeletar.get("palavra-chave")) && requestDeletar.get("resolvido").equals("NAO")){
			dao.remove(anuncioADeletar);
		}
		return redirect(routes.Application.index());
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public static Result pesquisarAnuncio(){
		DynamicForm requestPesquisa = Form.form().bindFromRequest();
		
		if(requestPesquisa.get("pesquisa").equals("Em Banda") || requestPesquisa.get("pesquisa").equals("Ocasionalmente")){
			Query consultaEmBanda =	dao.createQuery("FROM Anuncio a WHERE a.anunciante.id in (SELECT n.id FROM Anunciante n WHERE n.ondeTocar = :parametro)");
			
			consultaEmBanda.setParameter("parametro", requestPesquisa.get("pesquisa"));
			
			return ok(views.html.index.render("Meeting my band",
					consultaEmBanda.getResultList(),contadorDeAnunciosResolvidos));
		}else if(requestPesquisa.get("pesquisa").equals("PalavraChave")){
			Query consultaPalavraChave = dao.createQuery("FROM Anuncio a WHERE a.titulo LIKE :palavra OR a.descricao LIKE :palavra");
			
			consultaPalavraChave.setParameter("palavra", "%"+requestPesquisa.get("valuePesquisa")+"%");
			
			return ok(views.html.index.render("Meeting my band",
					consultaPalavraChave.getResultList(),contadorDeAnunciosResolvidos));
		}else if(requestPesquisa.get("pesquisa").equals("Instrumento")){
			Query consultaInstrumentos = dao.createQuery("SELECT a FROM Anuncio a, Instrumento i WHERE i.nome = :instrumento");
			
			consultaInstrumentos.setParameter("instrumento",requestPesquisa.get("valuePesquisa"));
			
			List<Anuncio> listAnuncio = new ArrayList<>();
			
			for (Object anuncio : consultaInstrumentos.getResultList()) {
				for (Instrumento instrumento : ((Anuncio) anuncio).getAnunciante().getListInstrumentos()) {
					if (requestPesquisa.get("valuePesquisa").equals(instrumento.getNome()) && !listAnuncio.contains(((Anuncio) anuncio))) {
						listAnuncio.add(((Anuncio) anuncio));
					}
				}
			}
			
			return ok(views.html.index.render("Meeting my band",
					listAnuncio,contadorDeAnunciosResolvidos));
		}else if(requestPesquisa.get("pesquisa").equals("Estilo")){
			Query consultaInstrumentos = dao.createQuery("SELECT a FROM Anuncio a, EstiloQueGosta e WHERE e.nome = :estilo");
			
			consultaInstrumentos.setParameter("estilo",requestPesquisa.get("valuePesquisa"));
			
			List<Anuncio> listAnuncio = new ArrayList<>();
			
			for (Object anuncio : consultaInstrumentos.getResultList()) {
				for (EstiloQueGosta estilo : ((Anuncio) anuncio).getAnunciante().getListEstilos()) {
					if (requestPesquisa.get("valuePesquisa").equals(estilo.getNome()) && !listAnuncio.contains(((Anuncio) anuncio))) {
						listAnuncio.add(((Anuncio) anuncio));
					}
				}
			}
			
			return ok(views.html.index.render("Meeting my band",
					listAnuncio,contadorDeAnunciosResolvidos));
		}		
			
		return redirect(routes.Application.index());
	}

	/*
	 * Carrega as listas de estilos e instrumentos usadas no formulario de
	 * cadastro
	 */
	@Transactional
	public static Result getListasCadastrar() {
		List<EstiloQueGosta> listDeEstiloQueGosta = dao
				.findAllByClass(EstiloQueGosta.class);
		List<EstiloQueNaoGosta> listDeEstiloQueNaoGosta = dao
				.findAllByClass(EstiloQueNaoGosta.class);
		List<Instrumento> listDeInstrumentos = dao
				.findAllByClass(Instrumento.class);

		return ok(views.html.cadastrar.render(listDeEstiloQueGosta,
				listDeEstiloQueNaoGosta, listDeInstrumentos));
	}

	@Transactional
	private static List<Instrumento> getSelecionadosInstrumentos() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();

		String[] checkedInstrumentos = map.get("instrumentos");
		List<Instrumento> listInstrumentos = new ArrayList<>();

		if (checkedInstrumentos != null) {
			List<String> listaIdInstrumentos = Arrays
					.asList(checkedInstrumentos);
			for (String id : listaIdInstrumentos) {
				Long idInstrumento = Long.parseLong(id);
				Instrumento instrumento = dao.findByEntityId(Instrumento.class,
						idInstrumento);
				if (instrumento != null) {
					listInstrumentos.add(instrumento);
				}
			}
		}

		return listInstrumentos;
	}

	@Transactional
	private static List<EstiloQueGosta> getSelecionadosEstiloQueGosta() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();

		String[] checkedEstilos = map.get("estilosQueGosta");
		List<EstiloQueGosta> listEstilos = new ArrayList<>();

		if (checkedEstilos != null) {
			List<String> listaIdEstilos = Arrays.asList(checkedEstilos);
			for (String id : listaIdEstilos) {
				Long idEstilo = Long.parseLong(id);
				EstiloQueGosta estilo = dao.findByEntityId(
						EstiloQueGosta.class, idEstilo);
				if (estilo != null) {
					listEstilos.add(estilo);
				}
			}
		}

		return listEstilos;
	}

	@Transactional
	private static List<EstiloQueNaoGosta> getSelecionadosEstiloQueNaoGosta() {
		Map<String, String[]> map = request().body().asFormUrlEncoded();

		String[] checkedEstilos = map.get("estilosQueNaoGosta");
		List<EstiloQueNaoGosta> listEstilos = new ArrayList<>();

		if (checkedEstilos != null) {
			List<String> listaIdEstilos = Arrays.asList(checkedEstilos);
			for (String id : listaIdEstilos) {
				Long idEstilo = Long.parseLong(id);
				EstiloQueNaoGosta estilo = dao.findByEntityId(
						EstiloQueNaoGosta.class, idEstilo);
				if (estilo != null) {
					listEstilos.add(estilo);
				}
			}
		}

		return listEstilos;
	}

}
