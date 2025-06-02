package pt.isec.pa.chess.ui.res;

import javafx.scene.image.Image;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Classe utilitária responsável pela gestão de imagens utilizadas na interface gráfica do jogo de xadrez.
 * Utiliza cache para evitar carregamentos repetidos e melhorar o desempenho.
 *
 * @author Afonso Reis, António Correia, Pedro Vieira
 */
public class ImageManager {

    // Construtor privado para impedir instanciação da classe utilitária.
    private ImageManager() { }

    /**
     * Cache das imagens carregadas, associadas ao nome do ficheiro.
     */
    private static final HashMap<String, Image> images = new HashMap<>();

    /**
     * Obtém uma imagem dos recursos internos da aplicação (diretório {@code images/pieces/}).
     * Se a imagem já tiver sido carregada anteriormente, é retornada a versão em cache.
     *
     * @param filename nome do ficheiro da imagem (ex: "white_pawn.png")
     * @return objeto {@link Image} correspondente, ou {@code null} se não for possível carregar a imagem
     */
    public static Image getImage(String filename) {
        Image image = images.get(filename);
        if (image == null)
            try (InputStream is = ImageManager.class.getResourceAsStream("images/pieces/"+filename)) {
                image = new Image(is);
                images.put(filename,image);
            } catch (Exception e) { return null; }
        return image;
    }

    /**
     * Obtém uma imagem a partir de um caminho externo (por exemplo, uma URL ou caminho absoluto).
     * Se a imagem já tiver sido carregada anteriormente, é retornada a versão em cache.
     *
     * @param filename caminho externo da imagem
     * @return objeto {@link Image} correspondente, ou {@code null} se não for possível carregar a imagem
     */
    public static Image getExternalImage(String filename) {
        Image image = images.get(filename);
        if (image == null)
            try {
                image = new Image(filename);
                images.put(filename,image);
            } catch (Exception e) { return null; }
        return image;
    }

    /**
     * Remove uma imagem da cache, caso exista.
     * Utilizado para libertar memória ou forçar recarregamento.
     *
     * @param filename nome do ficheiro ou caminho usado como chave
     */
    public static void purgeImage(String filename) { images.remove(filename); }
}
