package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoAPI;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d1ef19b0";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    @Autowired
    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }


    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar séries por gênero
                    8 - Buscar Séries boas e curtas
                    9 - Buscar Episódio por Trecho de nome
                    10 - Top 5 Episódios por série
                    11 - Episódios a partir de uma data
                    
                    0 - Sair
                    """;
            System.out.println(menu);

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    buscarSeriesBoasCurtas();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    buscarTop5Episodios();
                    break;
                case 11:
                    buscarEpisodiosAposData();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;


                default:
                    System.out.println("Opção inválida");
            }
        }
    }



    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
//        dadosSeries.add(dados);
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.toLowerCase().replace(' ', '+') + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Digite qual serie deseja visualizar os episódios:");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json =
                        consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().toLowerCase().replace(' ', '+') + API_KEY +
                                "&season=" + i);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                            .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite qual serie deseja visualizar os episódios:");
        var nomeSerie = sc.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBusca.isPresent()) {
            System.out.println("Dados da Série: " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Digite o nome do ator para buscar: ");
        var nomeAtor = sc.nextLine();
        System.out.println("Digite a avaliação mínima das séries que deseja pesquisar: ");
        var avaliacaoMinima = sc.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacaoMinima);
        System.out.println("Série em que " + nomeAtor + " trabalhou:");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s -> System.out.println(s.getTitulo() + " " + s.getAvaliacao()));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Digite a categoria que deseja buscar: ");
        var nomeGenero = sc.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriesBoasCurtas() {
        System.out.println("Digite o número máximo de temporadas: ");
        var numeroTemporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Digite a avaliação mínima das séries que deseja pesquisar: ");
        var avaliacaoMinima = sc.nextDouble();
        sc.nextLine();
//        List<Serie> seriesBoas = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(numeroTemporadas, avaliacaoMinima);
        List<Serie> seriesBoas = repositorio.seriesPorTemporadaEAvaliacao(numeroTemporadas, avaliacaoMinima);
        System.out.println("Séries com no máximo " + numeroTemporadas + " temporadas e nota maior que " + avaliacaoMinima);
        seriesBoas.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Digite o trecho do nome do episódio que deseja encontrar: ");
        var trechoEpisodio = sc.nextLine();
        List<Episodio> episodioEncontrados  = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodioEncontrados.forEach(e -> System.out.printf("Série: %s - Temporada %s - Episódio %s - %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()));

    }

    private void buscarTop5Episodios() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s - Temporada %s - Episódio %s - %s  - Avaliação: %s\n", e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    private void buscarEpisodiosAposData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            System.out.println("Digite o ano limite de lançamento: ");
            var anoLancamento = sc.nextInt();
            sc.nextLine();
//            Serie serie = serieBusca.get();
            List<Episodio> episodiosAno = repositorio.episodioPorSerieEAno(serieBusca.get(), anoLancamento);
            episodiosAno.forEach(e -> System.out.printf("Série: %s - Temporada %s - Episódio %s - %s  - Avaliação: %s" +
                            " - Data de lançamento: %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo(),
                    e.getAvaliacao(), e.getDataLancamento()));
        }
    }

}
