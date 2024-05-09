package br.com.alura.screenmatch.model;

public enum Categoria {
    ACAO("Action", "Ação"),
    AVENTURA("Adventure", "Aventura"),
    ANIMACAO("Animation", "Animação"),
    BIOGRAFIA("Biography", "Biografia"),
    COMEDIA("Comedy", "Comédia"),
    CRIME("Crime", "Crime"),
    DOCUMENTARIO("Documentary", "Documentário"),
    DRAMA("Drama", "Drama"),
    FAMILIA("Family", "Família"),
    FANTASIA("Fantasy", "Fantasia"),
    NOIR("Film Noir", "Noir"),
    HISTORIA("History", "História"),
    TERROR("Horror", "Terror"),
    MUSICA("Music", "Música"),
    MUSICAL("Musical", "Musical"),
    MISTERIO("Mystery", "Mistério"),
    ROMANCE("Romance", "Romance"),
    SCI_FI("Sci-Fi", "Ficção científica"),
    CURTA("Short", "Curta"),
    ESPORTE("Sport", "Esporte"),
    SUPERHEROI("Superhero", "Super-herói"),
    SUSPENSE("Thriller", "Suspense"),
    GUERRA("War", "Guerra"),
    FAROESTE("Western", "Faroeste");

    private String categoriaOmdb;

    private String categoriaPortugues;

    Categoria (String categoriaOmdb, String categoriaPortugues){
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
    public static Categoria fromPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}

