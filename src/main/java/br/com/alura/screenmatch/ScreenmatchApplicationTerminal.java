//package br.com.alura.screenmatch;
//
//import br.com.alura.screenmatch.principal.Principal;
//import br.com.alura.screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
////		 String url = "http://www.omdbapi.com/?apikey=d1ef19b0&t=" + busca;
////		json = consumoAPI.obterDados("https://coffee.alexflipnote.dev/random.json");
////		System.out.println(json);
//
//@SpringBootApplication
//public class ScreenmatchApplicationTerminal implements CommandLineRunner {
//	@Autowired
//	private SerieRepository repositorio;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplicationTerminal.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		Principal principal = new Principal(repositorio);
//		principal.exibeMenu();
//	}
//}
