package pt.isec.pa.chess.model.data;
import java.io.*;

public class ChessGameSerialization {

    private ChessGameSerialization() {
        // Impede a instanciação da classe
        throw new AssertionError("This class cannot be instantiated");
    }

    public static void serialize(ChessGame game, String filePath) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(game);
        }
    }

    public static ChessGame deserialize(String filePath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (ChessGame) ois.readObject();
        }
    }
}
