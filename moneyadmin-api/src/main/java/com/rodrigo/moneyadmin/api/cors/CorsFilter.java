package com.rodrigo.moneyadmin.api.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.rodrigo.moneyadmin.api.config.property.MoneyAdminApiProperty;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter implements Filter {

	@Autowired
	private MoneyAdminApiProperty algamoneyApiProperty;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOriginPermitida());
		response.setHeader("Access-Control-Allow-Credentials", "true");

 //Quando a aplicacao front end e levantada em um servidor onde a porta e diferente da
 //aplicacao back-end acontece um vereficao inicial da parte do Browser 
 //com a api. Para verificar se a porta que estar fazendo essa requisicacao para api 
 //tem autorizacao.
 //Essa solicitacao inicial do browser e um padrao de seguranca adotado por todos os Browser
 //Para garantir que de fato existe a autorizacao na origem cruzada.
 //Então o browser inicialmente envia um metodo OPTION e no retorno o Browser espera 
 //receber de volta o header necessario para saber qual e a origem que e permitida pela api. 
 //no caso o nome do header e que o browser espera receber e:		
 //"Access-Control-Allow-Origin" , "e a porta"
 //Quando nao estamos trabalhando com o oauth2 podemos expcificar isso diretamente na 
 //nossa api pela notacao: 
 //@CrossOrigin(origins = "http://localhost:9000") e expicificar a porta que e permitida
 //Como estamos usando o oauth na api e é o browser que faz essa requisicao inicial com o OPTION 
 //e nao nos entao nessa requisicao inicial que o Browser faz nao e retornada a origem
 //no header entao o metodo OPTION vem sem resposta de crossOrigin por que ele esbarra na 
 //segurança da nossa api que estar com oauth2 sendo assim nos nao conseguimos espeficicar
 //a origem que e permitida para o browser.
 //Entao o que fizemos foi interceptar a resposta da requisicao do tipo OPTIONS 
 //e passa o header necessario para que possa dar continuidade nas requicoes da nossa api
 //entao o browser vai saber que o crossOrigin permitido vai ser o que 
 //especificamos na nossa api, e vamos retorna um outro header para o navegador
 //esse header vai informa ao navegador que nossa api aceita cookies que e 
 //considerado um tipo de Credentials.
		
 //Entao o retorno dos headers na resposta da requisicao feita com OPTION fica assim:
 //response.setHeader("Access-Control-Allow-Origin", algamoneyApiProperty.getOriginPermitida());
 //response.setHeader("Access-Control-Allow-Credentials", "true");

		
 // Mas antes que a resposta do metodo http OPTION seja retornada para o navegador vamos 
 //fazer um if para verificar se a requisicao que estar sendo feita tem algum OPTION
 //caso e uma requisicao com metodo OPTION entao vamos adionar os headers necessarios
 //para nossa aplicao fazer as requisicoes com os headers necessarios que no caso sao os 
 //headers que o sprig security ouath2 precisa para autenticar o usuario.
 //sendo uma resicao do tipo OPTION entao entende-se que e preciso adcionar os outros headers		
 //necessarios para poder fazer autenticação com o oauth2 se nao fizer esse if nao
 //conseguiremos ter acesso api por que caso seja um OPTION ele nao vai retonar os headers
 //necessarios para fazer a a requisicao posterior corretamente para api. 		
		
		// OBS: quando configuramamos response.setHeader("Access-Control-Max-Age",
		// "3600"); estamos dizendo que esses headers ficaram em cache por uma hora e
		// que nao sera necessario fazer nenhuma requisicao com metodo OPTIONS durante
		// esse tempo.
		// Entao como estar em cache os headers e a requisicao nao caira mas no if por
		// que nao estar vindo mas nenhum OPTIONS entao vai cair no else.
		// Como o filtro nao caiu no if entao nao sera necessario passar os heards pois
		// headers estao em cache entao o fluxo requisicao segue normal no else com
		// headers ainda em cache ate que de o tempo de uma hora para ser enviado um
		// OPTIONS novamente.

		// Entao resumindo:
		// A solucao foi criar um filtro para que toda vez que o browser fizer uma
		// requisicao com o metodo OPTIONS, iremos adcionar os headers abaixo para que
		// possamos na requisicao seguinte por exempo um post ser mandado nos seus 
		// headers as informacoes necesserias para fazer a requisição na api mesmo
		// que esteja usando o ouath2.
		
	//OBS: Caso nao estivessemos usando o oauth2 na nossa api poderiamos fazer uma requisicao
	//sem precisar do filtro pois nao esbarraria na seguranca oauth.
	
//OBS2: Caso vc for deixar api ser acessada por qualquer origem entao vc deve tirar o 
//&& algamoneyApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))) do if
// deixando apenas if ("OPTIONS".equals(request.getMethod()))
//Por que ja que api vai ser acessada por qualque origem nao tem motivo de verificar se a origem		
//e igual entao verificamos apenas o se tem o metodo OPTION
 if ("OPTIONS".equals(request.getMethod())
     		&& algamoneyApiProperty.getOriginPermitida().equals(request.getHeader("Origin"))) {
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, DELETE, PUT, OPTIONS");
		response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept");
		response.setHeader("Access-Control-Max-Age", "3600");// Tempo para p o browser fazer uma requisicao com
																	// OPTIONS para api enquanto nao der esse termpo nao
																	// sera enviada com o
																	// OPTIONS e entao nao passa pelo if e vai direto
																	// para o else seguindo o fluxo
																	// normal do request e response ja com headers
																	// passados anteriormente

			response.setStatus(HttpServletResponse.SC_OK);// status de resposta da requisicao
//  Vc pode testar na hora que passa pelo option se vc subir a aplicao front end em uma nova porta vc 
//	vera que de primeira cai aqui no if e depois nas requisicoes seguintess seguem o fluxo normal
//	System.out.println("Passou no OPTIONS");
//	System.out.println("Pegando o Method: " + request.getMethod());

		} else {
			chain.doFilter(req, resp);
//			System.out.println("Não precisou passar no OPTIONS porque ja ta liberado e "
//					+ "\n estar no prazo de uma hora que foi o tempo para "
//					+ "\n fazer requisicoes para o cross origin http://localhost:8000 ");
		}

	}

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}