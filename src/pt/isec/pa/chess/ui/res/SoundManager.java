package pt.isec.pa.chess.ui.res;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class SoundManager {

    // Construtor privado para evitar instanciação.
    private SoundManager() { }

    private static MediaPlayer mp;

    public static boolean play(String filename) {
        try {
            var url = SoundManager.class.getResource("/pt/isec/pa/chess/ui/res/sounds/en/" + filename);
            if (url == null) {
                System.err.println("Som não encontrado: " + filename);
                return false;
            }
            String path = url.toExternalForm();
            Media music = new Media(path);
            mp = new MediaPlayer(music);
            mp.setStartTime(Duration.ZERO);
            mp.setStopTime(music.getDuration());
            mp.setAutoPlay(true);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isPlaying() {
        return mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static void stop() {
        if (mp != null && mp.getStatus() == MediaPlayer.Status.PLAYING)
            mp.stop();
    }

    public static List<String> getSoundList() {
        File soundsDir = new File(SoundManager.class.getResource("sounds/").getFile());
        return Arrays.stream(soundsDir.listFiles()).map(x -> x.getName()).toList();
    }

    public static void playMultiple(String... filenames) {
        if (filenames == null || filenames.length == 0)
            return;

        playSequence(Arrays.asList(filenames), 0);
    }

    private static void playSequence(List<String> files, int index) {
        if (index >= files.size())
            return;

        try {
            var url = SoundManager.class.getResource("/pt/isec/pa/chess/ui/res/sounds/en/" + files.get(index));
            if (url == null) {
                System.err.println("Som não encontrado: " + files.get(index));
                return;
            }

            Media media = new Media(url.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);

            player.setOnEndOfMedia(() -> playSequence(files, index + 1));

            mp = player;
            mp.play();
        } catch (Exception e) {
            System.err.println("Erro ao tocar som: " + files.get(index));
        }
    }
}
