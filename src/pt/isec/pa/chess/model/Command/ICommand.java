package pt.isec.pa.chess.model.Command;

public interface ICommand {
    boolean execute();
    boolean undo();
}
