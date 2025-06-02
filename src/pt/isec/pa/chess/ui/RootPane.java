package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;
import pt.isec.pa.chess.model.ModelLog;
import pt.isec.pa.chess.ui.res.SoundManager;
import java.io.File;

public class RootPane extends BorderPane {

    ChessGameManager chessGame;

    ChessBoardJFX chessBoard;

    // Menus
    Menu mnGame, mnMode, mnSound;
    MenuItem mnNew, mnOpen, mnSave, mnImport, mnExport, mnQuit, mnUndo, mnRedo;
    CheckMenuItem mnLearning, mnShowPM, mnSoundToggle;

    private final PlayersInfoPane playersInfoPane;

    public RootPane(ChessGameManager data) {
        this.chessGame = data;
        this.chessBoard = new ChessBoardJFX(chessGame);
        this.playersInfoPane = new PlayersInfoPane(chessGame);
        chessBoard.setPlayersInfoPane(playersInfoPane);

        createViews();
        registerHandlers();

        update();
    }

    private void createViews() {
        setTop(createMenu());
        mnRedo.setDisable(true);
        mnShowPM.setDisable(true);
        mnUndo.setDisable(true);


        Pane centerPane = new Pane(chessBoard);
        chessBoard.widthProperty().bind(centerPane.widthProperty());
        chessBoard.heightProperty().bind(centerPane.heightProperty());

        setCenter(centerPane);
        setLeft(playersInfoPane);
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnGame = new Menu("Game");
        mnNew = new MenuItem("New");
        mnOpen = new MenuItem("Open");
        mnSave = new MenuItem("Save");
        mnImport = new MenuItem("Import");
        mnExport = new MenuItem("Export");
        mnQuit = new MenuItem("E_xit");
        mnGame.getItems().addAll(mnNew,mnOpen,mnSave,mnImport, mnExport,new SeparatorMenuItem(),mnQuit);

        mnLearning = new CheckMenuItem("Learning Mode");
        mnShowPM = new CheckMenuItem("Show Possible Moves");
        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");

        mnMode = new Menu("Mode");
        mnMode.getItems().addAll(mnLearning, new SeparatorMenuItem(), mnShowPM, mnUndo, mnRedo);

        mnSound = new Menu("Settings"); // para quando for preciso fazer mais coisas
        mnSoundToggle = new CheckMenuItem("Toggle Sound");
        mnSoundToggle.setSelected(true);
        mnSound.getItems().add(mnSoundToggle);

        mb.getMenus().addAll(mnGame,mnMode, mnSound);
        return mb;
    }

    private void registerHandlers() {
        mnNew.setOnAction(actionEvent -> {
            AskName askName = new AskName(chessGame);
            askName.showAndWait();
            if(askName.isConfirmed()){
                chessGame.resetGame();
                ModelLog.getInstance().clear();
                ModelLog.getInstance().log("Jogo iniciado!");
            }
        });

        mnQuit.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mnLearning.setOnAction(actionEvent -> {
            boolean isSelected = mnLearning.isSelected();
            mnRedo.setDisable(!isSelected);
            mnShowPM.setDisable(!isSelected);
            if(!isSelected){
                mnShowPM.setSelected(false);
            }
            mnUndo.setDisable(!isSelected);
            chessBoard.setLearningMode(isSelected);

            ModelLog.getInstance().log("Learning mode " + (isSelected ? "enabled" : "disabled"));
        });

        mnShowPM.setOnAction(actionEvent -> {
            boolean show = mnShowPM.isSelected();
            chessBoard.setShowPossibleMoves(show);
            chessBoard.update();
            ModelLog.getInstance().log("Show possible moves " + (show ? "enabled" : "disabled"));
        });

        mnImport.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Game from CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                AskName askName = new AskName(chessGame);
                askName.showAndWait();
                if(askName.isConfirmed()){
                    chessGame.importGameCsv(file.getAbsolutePath());
                    ModelLog.getInstance().log("Jogo importado!");
                    ModelLog.getInstance().getLogs();
                }
            }
        });

        mnExport.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export Game to CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null) {
                chessGame.exportGameCsv(file.getAbsolutePath());
            }
        });

        mnOpen.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Saved Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Saved Games", "*.dat"));
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                AskName askName = new AskName(chessGame);
                askName.showAndWait();
                if(askName.isConfirmed()){
                    chessGame.loadGame(file.getAbsolutePath());
                    ModelLog.getInstance().log("Jogo carregado!");
                    ModelLog.getInstance().getLogs();
                }
            }
        });

        mnSave.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Game");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Saved Games", "*.dat"));
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (file != null) {
                chessGame.saveGame(file.getAbsolutePath());
            }
        });

        mnSoundToggle.setOnAction(actionEvent -> {
            if (mnSoundToggle.isSelected()) {
                System.out.println("Som Ativado");
                chessBoard.setSoundOn(true);
            } else {
                if(SoundManager.isPlaying())
                    SoundManager.stop();
                chessBoard.setSoundOn(false);
                System.out.println("Som Desativado");
            }
        });

        mnRedo.setOnAction(actionEvent -> {
            chessGame.redo();
        });

        mnUndo.setOnAction(actionEvent -> {
            chessGame.undo();
        });
    }

    private void update() {

    }
}