package pt.isec.pa.chess.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import pt.isec.pa.chess.model.ChessGameManager;

import java.io.File;

public class RootPane extends BorderPane {

    ChessGameManager chessGame;
    ChessBoardJFX chessBoard;
    Menu mnGame, mnMode;
    MenuItem mnNew, mnOpen, mnSave, mnImport, mnExport, mnQuit, mnNormal, mnLearning,
            mnShowPM, mnUndo, mnRedo;
    private PlayersInfoPane playersInfoPane;



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
        setCenter(chessBoard);
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

        mnNormal = new MenuItem("Normal Mode");
        mnLearning = new MenuItem("Learning Mode");
        mnShowPM = new MenuItem("Show Possible Moves");
        mnUndo = new MenuItem("Undo");
        mnRedo = new MenuItem("Redo");

        mnMode = new Menu("Mode");
        mnMode.getItems().addAll(mnNormal,mnLearning, new SeparatorMenuItem(), mnShowPM, mnUndo, mnRedo);

        mb.getMenus().addAll(mnGame,mnMode);
        return mb;
    }

    private void registerHandlers() {
        mnNew.setOnAction(actionEvent -> {
            AskName askName = new AskName(chessGame);
            askName.showAndWait();
            chessGame.resetGame();
            update();
            chessBoard.update();
            playersInfoPane.reset();
        });

        mnQuit.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mnLearning.setOnAction(actionEvent -> {
            mnRedo.setDisable(false);
            mnShowPM.setDisable(false);
            mnUndo.setDisable(false);
        });

        mnNormal.setOnAction(actionEvent -> {
            mnRedo.setDisable(true);
            mnShowPM.setDisable(true);
            mnUndo.setDisable(true);
        });

        mnImport.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Game from CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (file != null) {
                chessGame.importGameCsv(file.getAbsolutePath());
                AskName askName = new AskName(chessGame);
                askName.showAndWait();
                update();
                chessBoard.update();
                playersInfoPane.reset();
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
                chessGame.loadGame(file.getAbsolutePath());
                chessBoard.update();
                playersInfoPane.reset();
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
    }


    private void update() {
        playersInfoPane.update();
    }

}

