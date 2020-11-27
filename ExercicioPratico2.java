import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ExercicioPratico2{
	ObjectMapper om = new ObjectMapper();


	static String endereco = "http://localhost:8080/";
	static String createWorld = "createWorld";
	static String createWorldTest = "createWorldTest";
	static String createAgent = "createAgent";
	static String createRoom = "createRoom";
	static String addAgentsToRoom = "addAgentsToRoom";
	static String removeAgentsFromRoom = "removeAgentsFromRoom";
	static String sendMessageToRoom = "sendMessageToRoom";
	static String sendMessageToAgent = "sendMessageToAgent";
	static String executePlan = "executePlan/";


	public static class Agente{
		//Classe representando os agentes (no fim, nao foi usada no codigo)
		@JsonProperty("id")
		public String id;

		@JsonProperty("name")
		public String nome;

		@JsonProperty("messages")
		public List<Mensagem> mensagens;

		public String toString() 
		{ 
			return id + " " + nome + " " + mensagens + " "; 
		} 

	}
	
	public static class Mensagem{
		//Classe representando as mensagens
		@JsonProperty("to")
		public String to;

		@JsonProperty("from")
		public String from;

		@JsonProperty("message")
		public String corpo;

		@JsonProperty("timestamp")
		public String dataHora;

		public String toString() 
		{ 
			return to + " " + from + " " + corpo + " " + dataHora + " "; 
		} 

	}

	
	public static class Sala{
		//Classe representando as salas (no fim, nao foi usada no codigo)
		@JsonProperty("id")
		public String id;

		@JsonProperty("name")
		public String nome;

		@JsonProperty("agents")
		public List<Agente> agentes;

		@JsonProperty("messages")
		public List<Mensagem> mensagens;

		public String toString() 
		{ 
			return id + " " + nome + " " + agentes + " " + mensagens + " "; 
		} 

	}

	public static String acessaUrl (String requestMethod, String url) throws IOException {
		//metodo responsavel por efetuar o acesso a url relacionada a cada chamada REST
		try {
			URL urlAcessada = new URL(url);
			HttpURLConnection con = (HttpURLConnection) urlAcessada.openConnection();
			con.setRequestMethod(requestMethod);
			con.setRequestProperty("Content-Type", "application/json");
			//String contentType = con.getHeaderField("Content-Type");
			con.setConnectTimeout(50000);
			con.setReadTimeout(50000);
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			return content.toString();
		} catch (MalformedURLException e){
			e.printStackTrace();
			return "erro";
		}

	}
	
	static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	public static String consultaSala(int idSala) throws IOException{
		//metodo responsavel por obter uma descricao da sala em json
		String url = endereco+"room/"+idSala;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);

	}

	public static String criaAgente(String name) throws IOException{
		//metodo responsavel por executar a chamada createAgent
		String charset = "UTF-8";
		String query = String.format("name=%s",
				URLEncoder.encode(name,charset));
		String url = endereco+createAgent+"?"+query;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);
	}

	public static String executaPlano(int idAgente) throws IOException{
		//metodo responsavel por executar o plano de um determinado agente
		String url = endereco+executePlan+idAgente;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);
	}


	public static String acessaMensagem(int idAgente) throws IOException{
		//metodo responsavel por executar a lista de mensagens recebidas pelo agente idAgente
		String url = endereco+"/agent/messages/"+idAgente;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);
	}

	public static String criaSala(String name) throws IOException{
		//metodo responsavel por executar a chamada createRoom
		String charset = "UTF-8";
		String query = String.format("name=%s",
				URLEncoder.encode(name,charset));
		String url = endereco+createRoom+"?"+query;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM, url);
	}
	
	public static List<Mensagem> bubbleSort(List<Mensagem> listaMensagem){
		for(int i=0;i<listaMensagem.size();i++) {
			for(int j=0;j<listaMensagem.size()-1;j++) {
				if(LocalDateTime.parse(listaMensagem.get(j).dataHora).isAfter(LocalDateTime.parse(listaMensagem.get(j+1).dataHora))){
					Mensagem aux = listaMensagem.get(j);
					listaMensagem.set(j, listaMensagem.get(j+1));
					listaMensagem.set(j+1, aux);
				}
			}
			
		}
		return listaMensagem;
	}


	public static String tiraAgenteDaSala(int idSala, int idAgente) throws IOException{
		//metodo responsavel por executar a chamada removeAgentFromRoom
		String id1 = Integer.toString(idSala);
		String id2 = Integer.toString(idAgente);
		String charset = "UTF-8";
		String query1 = String.format("room=%s",
				URLEncoder.encode(id1,charset));
		String query2 = String.format("agents=%s",
				URLEncoder.encode(id2,charset));
		String url = endereco+removeAgentsFromRoom+"?"+query1+"&"+query2;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM, url);
	}


	public static String adicionaAgenteASala(int idSala, int idAgente) throws IOException{
		//metodo responsavel por executar a chamada addAgentToRoom
		String id1 = Integer.toString(idSala);
		String id2 = Integer.toString(idAgente);
		String charset = "UTF-8";
		String query1 = String.format("room=%s",
				URLEncoder.encode(id1,charset));
		String query2 = String.format("agents=%s",
				URLEncoder.encode(id2,charset));
		String url = endereco+addAgentsToRoom+"?"+query1+"&"+query2;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);
	}




	public static String configuraAcao(int idAg, int idSalaC, int idSalaS, String corpo1, String corpo2) throws IOException{
		//metodo responsavel por configurar os planos de acao do agente idAg. Entradas: id do agente, id da sala de destino,
		//id da sala original, corpo das mensagens a serem enviadas
		String idAgente = Integer.toString(idAg);
		String idSalaChegada = Integer.toString(idSalaC);
		String idSalaSaida = Integer.toString(idSalaS);
		String minhaUrl = endereco+"agent/setActions?id="+idAgente+"&actions=";
		URL url = new URL (minhaUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("POST");

		ObjectMapper om = new ObjectMapper();
		ObjectMapper m1 = new ObjectMapper();
		ObjectMapper m2 = new ObjectMapper();

		ObjectNode acao1 = om.createObjectNode();
		ObjectNode acao2 = om.createObjectNode();
		ObjectNode acao3 = om.createObjectNode();
		ObjectNode acao4 = om.createObjectNode();
		ObjectNode mensagem1 = m1.createObjectNode();
		ObjectNode mensagem2 = m2.createObjectNode();

		ArrayNode acoes = om.createArrayNode();
		ObjectMapper om2 = new ObjectMapper();
		ObjectNode plano = om2.createObjectNode();
		String jsonInputString = " ";
		try {
			acao1.put("actionType","leave");
			acao1.put("actionParameter",idSalaSaida);
			mensagem1.put("from",idAgente);
			mensagem1.put("to",idSalaSaida);
			mensagem1.put("message",corpo1);
			LocalDateTime currentDateTime = LocalDateTime.now();
			String formattedDateTime = currentDateTime.format(formatter);
			mensagem1.put("timestamp", formattedDateTime);
			acao2.put("actionType","message");
			acao2.set("actionParameter",mensagem1);
			acao3.put("actionType","move");
			acao3.put("actionParameter",idSalaChegada);
			mensagem2.put("from",idAgente);
			mensagem2.put("to",idSalaChegada);
			mensagem2.put("message",corpo2);			
			LocalDateTime currentDateTime2 = LocalDateTime.now();
			String formattedDateTime2 = currentDateTime2.format(formatter);
			mensagem2.put("timestamp", formattedDateTime2);
			acao4.put("actionType","message");
			acao4.set("actionParameter",mensagem2);
			acoes.addAll(Arrays.asList(acao1, acao2, acao3,acao4));
			plano.set("actions", acoes);
			jsonInputString = om2.writerWithDefaultPrettyPrinter().writeValueAsString(plano);
			String filename1 = "C:/Users/Daniel/eclipse-workspace/PCS/src/"+"plano-"+idAgente+".json";
			String filename2 = "C:/Users/Daniel/eclipse-workspace/PCS/src/"+"plano-"+idAgente+".txt";
			om2.writerWithDefaultPrettyPrinter().writeValue(new File(filename1), plano);
			om2.writerWithDefaultPrettyPrinter().writeValue(new File(filename2), plano);
		} 
		catch(Exception ex) {
			ex.printStackTrace();
		}		

		con.setRequestProperty("Content-Type", "application/json; utf-8");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		try(OutputStream os = con.getOutputStream()) {
			byte[] input = jsonInputString.getBytes("utf-8");
			os.write(input, 0, input.length);			
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}		
		StringBuilder response = new StringBuilder();
		try(BufferedReader br = new BufferedReader(
				new InputStreamReader(con.getInputStream(), "utf-8"))) {

			String responseLine = null;
			while ((responseLine = br.readLine()) != null) {
				response.append(responseLine.trim());
			}
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return response.toString();
	}



	public static String enviaMensagemASala(int from, int to, String mensagem) throws IOException{
		//metodo responsavel por executar a chamada sendMessageToRoom
		String id1 = Integer.toString(from);
		String id2 = Integer.toString(to);
		String charset = "UTF-8";
		String query1 = String.format("from=%s",
				URLEncoder.encode(id1,charset));
		String query2 = String.format("to=%s",
				URLEncoder.encode(id2,charset));
		String query3 = String.format("message=%s",
				URLEncoder.encode(mensagem,charset));
		String url = endereco+sendMessageToRoom+"?"+query1+"&"+query2+"&"+query3;
		String rM = "GET";
		return ExercicioPratico2.acessaUrl(rM,url);
	}

	public static int sorteia(double[] array) {
		//Esta classe recebe um array do tipo double contendo uma distribuicao de probabilidades
		//e, com base nos valores do array, sorteia um indice desse vetor com
		//probabilidade diferente de zero
		Random rand = new Random();
		double aleatorio = rand.nextDouble();
		ArrayList<Double> probabilidadeAcumulada = new ArrayList<Double>();
		ArrayList<Integer> vetIndices = new ArrayList<Integer>();
		for(int i=0;i<array.length;i++) {
			if(array[i]!=0) {
				if(probabilidadeAcumulada.size()==0) {
					probabilidadeAcumulada.add(array[i]);
					vetIndices.add(i);
				}
				else {
					probabilidadeAcumulada.add(array[i]+probabilidadeAcumulada.get(probabilidadeAcumulada.size()-1));
					vetIndices.add(i);
				}

			}
		}
		boolean encontrou = false;
		int j=0;
		int indice = probabilidadeAcumulada.size();
		while(j<probabilidadeAcumulada.size()&&!encontrou) {
			if(aleatorio<=probabilidadeAcumulada.get(j)) {
				indice = vetIndices.get(j);
				encontrou = true;
			}
			j++;
		}
		return indice;
	}

	public static void main(String[] args) throws IOException{
		//Aqui comeca o mecanismo de controle
		//Criacao do Mundo:
		String criaMundo = endereco + java.net.URLEncoder.encode(createWorld, "UTF-8");
		ExercicioPratico2.acessaUrl("GET",criaMundo);

		//
		int numSalas = 5;
		int numAgentes = 15;
		//Vetor com os nomes das salas (obs.: nao pode conter numeros para nao confundir com
		//os IDs):
		String[] vetSalas = {"abc","def","ghi","jkl","mno"};
		int[] idSalas = new int[numAgentes];
		int[] idAgentes = new int[numAgentes];
		//Vetor com os nomes dos agentes (obs.: nao pode conter numeros para nao confundir com
		//os IDs):		
		String[] vetAgentes = {"Augusto","Bruna","Cecilia","Denis","Eduardo","Fernando","Geraldo","Haroldo","Igor","Joao","Karol","Leandro","Meire","Nestor","Otavio"};
		String[] salaDoAgente = new String[numAgentes];
		String[] registroSalasOriginais = new String[numAgentes];

		//Sao atribuidos "IDs internos" para facilitar a manipulacao do codigo; seguem
		//basicamente a ordem de criacao dos agentes/salas
		int[] salaDoAgenteIDInterno = new int[numAgentes];
		int[] salaDestinoIDInterno = new int[numAgentes];
		int[] numAgPorSalaPlanejado = new int[numSalas];
		int[] numAgPorSala = new int[numSalas];
		//Capacidade minima e maxima (e intermediaria) das salas: 
		int capMax = 4;
		int capMed = 3;
		int capMin = 2;
		//vetor que armazena quantas salas ha com 2, 3 ou 4 ocupantes a cada momento
		//do planejamento
		int[] vetCap = {capMin,capMed,capMax};
		int[] vetNumCap = {0, 0, 0};		
		if (numAgentes%numSalas==0) {
			//Criacao das salas e dos agentes
			int razao = numAgentes/numSalas;
			int[][] distOriginal = new int[numSalas][razao];
			int cont = 0;
			for(int i=1; i<= numSalas; i++) {
				String s = vetSalas[i-1];
				String salaCriada = ExercicioPratico2.criaSala(s);
				idSalas[i-1]=Integer.parseInt(salaCriada.replaceAll("\\D+",""));
				for(int j =1; j<= razao; j++) {
					cont++;
					numAgPorSala[i-1]++;
					String t = vetAgentes[cont-1];
					salaDoAgente[cont-1] = s;
					salaDoAgenteIDInterno[cont-1] = i-1;
					distOriginal[i-1][j-1] = cont-1;
					registroSalasOriginais[cont-1] = "Sala original do agente "+vetAgentes[cont-1]+": "+s+"\n";
					idAgentes[cont-1]=Integer.parseInt(ExercicioPratico2.criaAgente(t).replaceAll("\\D+",""));
					//Insercao do agente recem criado na sua respectiva sala
					ExercicioPratico2.adicionaAgenteASala(idSalas[i-1], idAgentes[cont-1]);
				}
			}
			//Agora, o mecanismo de controle vai determinar de maneira aleatoria quais 
			//serao as salas de Destino de cada um dos agentes (sem move-los ainda)
			String[] sorteado = new String[numAgentes];
			String[] Mensagens = new String[2*numAgentes];
			String[] Mensagens2 = new String[2*numAgentes];
			for(int k=0;k<numAgentes;k++) {
				double vetorProbabilidades[] = new double[numSalas];
				int soma = 0;
				if(k<numAgentes-numSalas) {
					//Para os primeiros 10 agentes, exigimos que a sala de destino seja diferente
					//da original e que eles nao vao para salas que ja tem 4 agentes ou mais
					for(int l=0;l<numSalas;l++) {
						if(salaDoAgente[k].equals(vetSalas[l])||numAgPorSalaPlanejado[l]>=capMax) {
							vetorProbabilidades[l] = 0;
						}
						else {
							vetorProbabilidades[l] = 1;
							soma++;
						}
					}					
				}
				else if(k==numAgentes-numSalas) {
					//Para o agente de numero 11, exigimos:
					// -que va para uma sala diferente da original;
					// -que nao va para uma sala que ja tem 4 agentes ou mais;
					// -que nao va para uma sala que tenha 2 agentes, se ela for a unica a ter 2
					//  agentes ou se houver duas salas com 3 agentes e apenas uma com 4 agentes;
					// -que nao va para uma sala que tenha 3 agentes se ja houver duas salas 
					//  com 4 agentes
					// -que VA para a ultima sala se nao houver nenhum agente alocado la ainda
					//  (pois os agentes que estao la neste momento precisam trocar de sala tbm)
					for(int n=0;n<numSalas;n++) {
						if(salaDoAgente[k].equals(vetSalas[n])||numAgPorSalaPlanejado[n]>=capMax||(numAgPorSalaPlanejado[n]==capMin&&(vetNumCap[0]<2||(vetNumCap[1]>1&&vetNumCap[2]<2)))||(numAgPorSalaPlanejado[n]==capMed&&vetNumCap[2]>=2)||(numAgPorSalaPlanejado[numSalas-1]==0&&n!=numSalas-1)){
							vetorProbabilidades[n] = 0;
						}
						else {
							vetorProbabilidades[n] = 1;
							soma++;
						}
					}
				}
				else if(k==numAgentes-numSalas+1) {
					//Para o agente de numero 12, exigimos:
					// -que va para uma sala diferente da original;
					// -que nao va para uma sala que ja tem 4 agentes ou mais;
					// -que nao va para uma sala que tenha 2 agentes, se ela for a unica a ter 2
					//  agentes ou se houver duas salas com 3 agentes e apenas uma com 4 agentes;
					// -que nao va para uma sala que tenha 3 agentes se ja houver duas salas 
					//  com 4 agentes
					// -que VA para a ultima sala se houver apenas um agente alocado la
					//  (pois os agentes que estao la neste momento precisam trocar de sala tbm)
					for(int n=0;n<numSalas;n++) {
						if(salaDoAgente[k].equals(vetSalas[n])||numAgPorSalaPlanejado[n]>=capMax||(numAgPorSalaPlanejado[n]==capMin&&(vetNumCap[0]<2||(vetNumCap[1]>1&&vetNumCap[2]<2)))||(numAgPorSalaPlanejado[n]==capMed&&vetNumCap[2]>=2)||(numAgPorSalaPlanejado[numSalas-1]==1&&n!=numSalas-1)){
							vetorProbabilidades[n] = 0;
						}
						else {
							vetorProbabilidades[n] = 1;
							soma++;
						}
					}
				}
				else {
					//Para os agentes de numero 13 a 15, exigimos:
					// -que va para uma sala diferente da original;
					// -que nao va para uma sala que ja tem 4 agentes ou mais;
					// -que nao va para uma sala que tenha 2 agentes, se ela for a unica a ter 2
					//  agentes ou se houver duas salas com 3 agentes e apenas uma com 4 agentes;
					// -que nao va para uma sala que tenha 3 agentes se ja houver duas salas 
					//  com 4 agentes					
					for(int n=0;n<numSalas;n++) {
						if(salaDoAgente[k].equals(vetSalas[n])||numAgPorSalaPlanejado[n]>=capMax||(numAgPorSalaPlanejado[n]==capMin&&(vetNumCap[0]<2||(vetNumCap[1]>1&&vetNumCap[2]<2)))||(numAgPorSalaPlanejado[n]==capMed&&vetNumCap[2]>=2)){
							vetorProbabilidades[n] = 0;
						}
						else {
							vetorProbabilidades[n] = 1;
							soma++;
						}
					}

				}
				for(int m=0;m<numSalas;m++) {
					vetorProbabilidades[m] = vetorProbabilidades[m]/soma; 
				}
				//Sorteio das salas possiveis, dadas as restricoes implementadas
				int indice = ExercicioPratico2.sorteia(vetorProbabilidades);
				sorteado[k] = vetSalas[indice];
				salaDestinoIDInterno[k] = indice;
				/*
				System.out.println(indice+"");
				System.out.println(vetSalas[salaDestinoIDInterno[k]]+"");
				System.out.println(idSalas[indice]+"");
				 */
				numAgPorSalaPlanejado[indice]++;
				Mensagens[2*k]="AGENTE "+vetAgentes[k]+" SAIU DA SALA "+vetSalas[salaDoAgenteIDInterno[k]];
				Mensagens[2*k+1]="AGENTE "+vetAgentes[k]+" ENTROU NA SALA "+vetSalas[salaDestinoIDInterno[k]];
				//Configura o plano de acoes do agente k: sair da sua sala original, mandar uma mensagem para os ocupantes dessa 
				//sala, ir para a sala de destino  recem-atribuida, enviar uma mensagem para os ocupantes dessa sala.
				ExercicioPratico2.configuraAcao(idAgentes[k], idSalas[indice], idSalas[salaDoAgenteIDInterno[k]], Mensagens[2*k], Mensagens[2*k+1]);
				for(int p=0;p<vetCap.length;p++) {
					vetNumCap[p] = 0;
					for(int q=0;q<numSalas;q++) {
						if(numAgPorSalaPlanejado[q]==vetCap[p]) {
							vetNumCap[p]++;
						}
					}
				}
			}
			//System.out.println("Numero planejado de agentes por sala ao final do processo:");
			//System.out.println(Arrays.toString(vetSalas));
			//System.out.println(Arrays.toString(numAgPorSalaPlanejado));
			//Agora, o mecanismo de controle vai determinar, de forma aleatoria, a ordem
			//em que os agentes serao movidos (sem move-los ainda)
			boolean[] movido = new boolean[numAgentes];
			double[] novoVetorProbabilidades = new double[numAgentes];	
			for(int i=0;i<numAgentes;i++) {
				movido[i] = false;
				novoVetorProbabilidades[i] = 1./numAgentes;
			}
			boolean tudoCerto = false;
			boolean deuProblema = false;			
			int[] ordem = new int[numAgentes];
			int[] ordemSalasDePartida = new int[numAgentes];
			int[] ordemSalasDeChegada = new int[numAgentes];
			while(!tudoCerto) {
				//Enquanto nao for encontrada uma sequencia que nao deixe nenhuma sala com mais
				//do que 4 ou menos do que 2 agentes a qualquer momento, repita:
				int numMovidos = 0;
				//Sorteio de um agente inicial (o primeiro a ser movido)
				int agenteSorteado = ExercicioPratico2.sorteia(novoVetorProbabilidades);
				int salaSelecionada = salaDoAgenteIDInterno[agenteSorteado];
				int salaDestino = salaDestinoIDInterno[agenteSorteado];
				numAgPorSala[salaSelecionada]--;
				numAgPorSala[salaDestino]++;			
				numMovidos++;
				movido[agenteSorteado] = true;
				ordem[0] = agenteSorteado;
				ordemSalasDePartida[0]=salaSelecionada;
				ordemSalasDeChegada[0]=salaDestino;
				int cont2 = 1;
				salaSelecionada = salaDestino;
				while(numMovidos<numAgentes) {
					double[] novissimoVetorProbabilidades = new double[razao];
					int soma2 = 0;		
					for(int i=0;i<razao;i++) {
						//Dado que o agente selecionado anteriormente chegou a sua sala de
						//destino, vamos dar uma chance para todos os agentes 
						//ocupantes desta sala que ainda nao foram movidos
						if (!movido[distOriginal[salaSelecionada][i]]) {
							soma2++;
							novissimoVetorProbabilidades[i]=1;
						}
						else {
							novissimoVetorProbabilidades[i]=0;
						}
					}
					if (soma2!=0) {
						for(int j=0;j<razao;j++) {
							novissimoVetorProbabilidades[j] = novissimoVetorProbabilidades[j]/soma2;
						}
						agenteSorteado = distOriginal[salaSelecionada][ExercicioPratico2.sorteia(novissimoVetorProbabilidades)];						
					} 
					else {
						//se todos os agentes na sala ja foram movidos, sorteamos um novo agente
						//para recomecar o processo dentre todos os que ainda nao foram movidos
						int soma3 = 0;					
						for(int k=0;k<numAgentes;k++) {
							if (!movido[k]&&numAgPorSala[salaDoAgenteIDInterno[k]]>capMin&&numAgPorSala[salaDestinoIDInterno[k]]<capMax) {
								novoVetorProbabilidades[k]=1;
								soma3++;
							}
							else {
								novoVetorProbabilidades[k]=0;
							}
						}
						if(soma3!=0) {
							for(int l=0;l<numAgentes;l++) {
								novoVetorProbabilidades[l]=novoVetorProbabilidades[l]/soma3;
								agenteSorteado = ExercicioPratico2.sorteia(novoVetorProbabilidades);								
							}
						}
						else {
							//se os agentes que ainda nao foram movidos nao podem ser
							//transferidos (por exemplo, porque deixariam uma sala com
							//menos ocupantes), a sequencia elaborada ate aqui e descartada
							//e uma nova sequencia comeca a ser sorteada (sai do while mais
							//interno)
							deuProblema = true;
							break;
						}
					}
					movido[agenteSorteado] = true;
					ordem[cont2] = agenteSorteado;
					salaSelecionada = salaDoAgenteIDInterno[agenteSorteado];
					salaDestino = salaDestinoIDInterno[agenteSorteado];
					ordemSalasDePartida[cont2]=salaSelecionada;
					ordemSalasDeChegada[cont2]=salaDestino;
					cont2++;
					numAgPorSala[salaSelecionada]--;
					numAgPorSala[salaDestino]++;
					//Se por algum motivo nao for detectado um problema na sequencia,
					//mas ele existir, os ifs a seguir identificam e o processo se encerra
					for(int i=0;i<numSalas;i++) {
						if(numAgPorSala[i]<capMin) {
							System.out.println("ERRO: sala com menos de 2 ocupantes");
							break;
						}
						if(numAgPorSala[i]>capMax) {
							System.out.println("ERRO: sala com mais de 4 ocupantes");
							break;
						}
					}

					salaSelecionada = salaDestino;
					numMovidos++;
				}
				if(!deuProblema&&numMovidos==numAgentes) {
					tudoCerto = true;
				}
			}

			//Registra o contexto inicial
			String filenameI = "contexto_inicial.txt";
			FileWriter writerI = new FileWriter("C:\\Users\\Daniel\\eclipse-workspace\\PCS\\src/"+filenameI);			
			for(int i=0;i<numSalas;i++) {
				writerI.write(ExercicioPratico2.consultaSala(idSalas[i]));
			}
			writerI.close();

			//Para cada agente i, seguindo a ordem pre-estabelecida para execucao dos planos:
			for(int i=0;i<ordem.length;i++) {
				//Executa o plano do agente
				ExercicioPratico2.executaPlano(idAgentes[ordem[i]]);
				Mensagens2[2*i]="AGENTE "+vetAgentes[ordem[i]]+" SAIU DA SALA "+vetSalas[salaDoAgenteIDInterno[ordem[i]]]+"\n";
				Mensagens2[2*i+1]="AGENTE "+vetAgentes[ordem[i]]+" ENTROU NA SALA "+vetSalas[salaDestinoIDInterno[ordem[i]]]+"\n";
			}

			//Imprime na tela o historico de mensagens (para checagem)
			System.out.println("Historico de mensagens (da mais antiga p/ mais nova):\n");
			System.out.println(Arrays.toString(Mensagens2));

			
			String filenameM = "mensagens.txt";
			//Cria uma lista vazia para armazenar as mensagens
			List<Mensagem> listaMensagens = new ArrayList<>();
			//Para cada agente i:
			for(int i=0;i<numAgentes;i++) {				
				ObjectMapper mapper = new ObjectMapper();
				//Recupera as mensagens recebidas pelo agente:
				List<Mensagem> mi2 = Arrays.asList(mapper.readValue(ExercicioPratico2.acessaMensagem(idAgentes[i]),Mensagem[].class));
				//Para cada mensagem recebida pelo agente: 
				for(int j=0;j<mi2.size();j++) {
					//Verifica se ja ha uma mensagem armazenada na lista de mensagens com o mesmo corpo:
					boolean igual = false;
					for(int k=0;k<listaMensagens.size();k++) {
						if (mi2.get(j).corpo.equals(listaMensagens.get(k).corpo)) {
							igual = true;
							break;
						}
					}
					//Se nao houver, inclui a mensagem em questao na lista de mensagens:
					if(!igual) {
						listaMensagens.add(mi2.get(j));
					}
				}
			}
			//Ordena as mensagens da mais antiga para a mais recente:
			List<Mensagem> novaListaMensagens = ExercicioPratico2.bubbleSort(listaMensagens);
			ObjectMapper om = new ObjectMapper();
			//Imprime as mensagens no arquivo "mensagents.txt"
			om.writerWithDefaultPrettyPrinter().writeValue(new File("C:\\Users\\Daniel\\eclipse-workspace\\PCS\\src/"+filenameM), novaListaMensagens);
			
			//Registra o contexto final
			String filenameF = "contexto_final.txt";
			FileWriter writerF = new FileWriter("C:\\Users\\Daniel\\eclipse-workspace\\PCS\\src/"+filenameF);
			for(int i=0;i<numSalas;i++) {
				writerF.write(ExercicioPratico2.consultaSala(idSalas[i]));
			}
			writerF.close();
		}		
		else {
			System.out.println("Erro: numero de agentes nao divisivel pelo numero de salas");
		}
	}
}