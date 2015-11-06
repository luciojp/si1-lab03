package global;

import java.io.File;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import models.Anunciante;
import models.Anuncio;
import models.Contato;
import models.EstiloQueGosta;
import models.EstiloQueNaoGosta;
import models.Instrumento;
import models.DAO.GenericDAO;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;

public class Global extends GlobalSettings {
	private static GenericDAO dao = new GenericDAO();

	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@SuppressWarnings("resource")
			@Override
			public void invoke() throws Throwable {
				/*
				 * Cadastrar estilos no Bando de Dados
				 */
				Scanner in;
				in = new Scanner(new FileReader(new File("app/estilos.dat")
						.getCanonicalPath()));
				while (in.hasNextLine()) {
					String nomeEstilo = in.nextLine();
					dao.persist(new EstiloQueGosta(nomeEstilo));
					dao.persist(new EstiloQueNaoGosta(nomeEstilo));
				}

				dao.flush();

				/*
				 * Cadastrar instrumentos no Bandode Dados
				 */
				in = new Scanner(new FileReader(
						new File("app/instrumentos.dat").getCanonicalPath()));
				while (in.hasNextLine()) {
					String nomeInstrumentos = in.nextLine();
					dao.persist(new Instrumento(nomeInstrumentos));
				}

				dao.flush();

				/*
				 * Teste
				 */
				for (int i = 0; i < 10; i++) {
					Anuncio anuncio = new Anuncio();
					Anunciante anunciante = new Anunciante();
					Contato contatos = new Contato();
					List<Instrumento> instrumentos = new ArrayList<>();
					List<EstiloQueGosta> gosta = new ArrayList<>();
					List<EstiloQueNaoGosta> naoGosta = new ArrayList<>();

					if (i % 2 == 0) {
						anuncio.setTitulo("Quero tocar ocasionalmente");
						anuncio.setDescricao("Procuro eventos para tocar ocasionalmente.Procuro eventos para tocar ocasionalmente.Procuro eventos para tocar ocasionalmente.Procuro eventos para tocar ocasionalmente.Procuro eventos para tocar ocasionalmente.");
						anuncio.setPalavraChave("javaIsLife");
						
						anunciante.setCidade("João Pessoa");
						anunciante.setBairro("Altiplano");
						anunciante.setOndeTocar("Em Banda");

						instrumentos.add(new Instrumento("Violino"));
						instrumentos.add(new Instrumento("Violoncelo"));
						anunciante.setListInstrumentos(instrumentos);

						gosta.add(new EstiloQueGosta("Erudito"));
						gosta.add(new EstiloQueGosta("Instrumental"));
						gosta.add(new EstiloQueGosta("MPB"));
						anunciante.setListEstilos(gosta);

						naoGosta.add(new EstiloQueNaoGosta("Funk"));
						naoGosta.add(new EstiloQueNaoGosta("Funk Carioca"));
						anunciante.setListEstilosQueNaoGosta(naoGosta);

						contatos.setEmail("celloandviolin@gmail.com");
						contatos.setFacebook("http://www.facebook.com.br/celloandviolin");
						contatos.setTelefone("(83)99123-4567");
						dao.persist(contatos);
						dao.flush();
						anunciante.setContato(contatos);

						dao.persist(anunciante);
						dao.flush();

						anuncio.setAnunciante(anunciante);

						dao.persist(anuncio);
						dao.flush();
					} else {
						anuncio.setTitulo("Procuro banda de MPB/Clássico");
						anuncio.setDescricao("Procuro uma banda que seja voltada para o estilo MPB/Clássico, tenho disponibilidade todos os finais de semana. Para enventos durante a semana, combinar antecipadamente.");
						anuncio.setPalavraChave("PythonNaoEhTaoLegal");
						
						anunciante.setCidade("João Pessoa");
						anunciante.setBairro("Torre");
						anunciante.setOndeTocar("Ocasionalmente");

						instrumentos.add(new Instrumento("Clarinete"));
						instrumentos.add(new Instrumento("Banjo"));
						anunciante.setListInstrumentos(instrumentos);

						gosta.add(new EstiloQueGosta("Jazz"));
						gosta.add(new EstiloQueGosta("Instrumental"));
						anunciante.setListEstilos(gosta);

						naoGosta.add(new EstiloQueNaoGosta("Funk"));
						naoGosta.add(new EstiloQueNaoGosta("Folk"));
						anunciante.setListEstilosQueNaoGosta(naoGosta);

						contatos.setEmail("umEmailAEh@hotmail.com");
						contatos.setFacebook("http://www.facebook.com.br/seiLaJovem");
						contatos.setTelefone("(83)99876-5432");
						dao.persist(contatos);
						dao.flush();
						anunciante.setContato(contatos);

						dao.persist(anunciante);
						dao.flush();

						anuncio.setAnunciante(anunciante);

						dao.persist(anuncio);
						dao.flush();
					}
				}
			}
		});
	}

	public void onStop(Application app) {
		Logger.info("Aplicação desligada...");
	}
}
