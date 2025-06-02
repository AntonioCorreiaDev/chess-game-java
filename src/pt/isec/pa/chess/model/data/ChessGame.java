package pt.isec.pa.chess.model.data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principal que lida com os dados e a logica do jogo.
 * Controla o tabuleiro e as peças, o turno do jogador,
 * executar movimentos e verificar condições como xeque e xeque-mate.
 *
 * @author Afonso Reis, António Correia, Pedro Vieira
 */
public class ChessGame implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Contador para a regra dos 50 movimentos
     */
    private int halfMoveClock = 0;

    /**
     * Lista para guardar o histórico das jogadas
     */
    private List<String> positionHistory;

    /**
     * Indica se é a vez das peças brancas ou pretas
     */
    boolean isWhiteTurn;

    /**
     * Representação do tabuleiro de xadrez
     */
    Board board;

    /**
     * Nome do jogador 1 (brancas) e jogador 2 (pretas)
     */
    String player1, player2;
    /**
     * Estado de vitória: 0 = preto ganhou, 1 = branco ganhou, 2 = xeque, 3 = empate,
     * de resto todos os numeros indicam que o jogo continua a decorrer.
     */
    int winner = -1; //

    Piece pieceEnPassantVulnerable = null;

    /**
     * Construtor por omissão que inicia o jogo com o tabuleiro padrão e vez das brancas
     */
    public ChessGame(){
        isWhiteTurn = true;
        board = new Board();
        halfMoveClock = 0;
        positionHistory = new ArrayList<>();
    }

    /**
     * Construtor que carrega o estado de jogo a partir de um ficheiro CSV
     * @param gameState Caminho para o ficheiro CSV
     */
    public ChessGame(String gameState) {
        importCsv(gameState);
        halfMoveClock = 0;           // Adicionar
        positionHistory = new ArrayList<>();
    }

    /**
     * Verifica todos os tipos de empates possiveis
     */
    public boolean isDraw() {
        return isStalemate(isWhiteTurn)||
                isFiftyMoveRule() ||
                isInsufficientMaterial() ||
                isThreefoldRepetition();
    }

    /**
     * Verifica se há empate pelo stale mate
     */
    public boolean isStalemate(boolean isWhite) {
        if (isCheck(isWhite)) {
            return false;
        }

        return !hasValidMoves(isWhite);
    }

    /**
     * Verifica se há empate pela regra dos 50 movimentos
     */
    public boolean isFiftyMoveRule() {
        return halfMoveClock >= 100; // 100 meio-movimentos = 50 movimentos completos
    }

    /**
     * Verifica se há empate por 3 posições iguais
     */
    public boolean isThreefoldRepetition() {
        String currentPosition = board.getAllBoardText() + (isWhiteTurn ? "W" : "B");

        int count = 0;
        for (String position : positionHistory) {
            if (position.equals(currentPosition)) {
                count++;
            }
        }

        return count >= 3;
    }

    /**
     * Verifica se há empate pela quantidade e tipo de peças disponiveis
     */
    public boolean isInsufficientMaterial() {
        // Contadores para peças brancas
        int whiteQueens = 0, whiteRooks = 0;
        int whiteBishops = 0, whiteKnights = 0, whitePawns = 0;

        // Contadores para peças pretas
        int blackQueens = 0, blackRooks = 0;
        int blackBishops = 0, blackKnights = 0, blackPawns = 0;

        // Contar todas as peças do tabuleiro
        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                Piece piece = board.getPiece(col, row);
                if (piece != null) {
                    if (piece.isWhite()) {
                        if (piece instanceof Queen) whiteQueens++;
                        else if (piece instanceof Rook) whiteRooks++;
                        else if (piece instanceof Bishop) whiteBishops++;
                        else if (piece instanceof Knight) whiteKnights++;
                        else if (piece instanceof Pawn) whitePawns++;
                    } else {
                        if (piece instanceof Queen) blackQueens++;
                        else if (piece instanceof Rook) blackRooks++;
                        else if (piece instanceof Bishop) blackBishops++;
                        else if (piece instanceof Knight) blackKnights++;
                        else if (piece instanceof Pawn) blackPawns++;
                    }
                }
            }
        }

        // Verificar se ambos os lados têm material insuficiente
        return isInsufficientMaterialForSide(whiteQueens, whiteRooks,
                whiteBishops, whiteKnights, whitePawns) &&
                isInsufficientMaterialForSide(blackQueens, blackRooks,
                        blackBishops, blackKnights, blackPawns);
    }

    /**
     * verifica se há ou não material suficiente para continuar o jogo
     * @param queens numero de rainhas
     * @param rooks numero de torres
     * @param bishops numero de bispos
     * @param knights numero de cavalos
     * @param pawns numero de peões
     * @return true ou false dependendo das quantidades de peças necessarias
     */
    private boolean isInsufficientMaterialForSide(int queens, int rooks,
                                                  int bishops, int knights, int pawns) {
        // Se há rainhas, torres ou peões, há material suficiente
        if (queens > 0 || rooks > 0 || pawns > 0) {
            return false;
        }

        // Total de peças (excluindo o rei)
        int totalPieces = queens + rooks + bishops + knights + pawns;

        // Apenas rei
        if (totalPieces == 0) {
            return true;
        }

        // Rei + 1 bispo
        if (totalPieces == 1 && bishops == 1) {
            return true;
        }

        // Rei + 1 cavalo
        if (totalPieces == 1 && knights == 1) {
            return true;
        }

        // Rei + 2 cavalos (tecnicamente possível dar mate mas muito raro)
        if (totalPieces == 2 && knights == 2) {
            return true;
        }

        // Se chegou aqui, há material suficiente
        return false;
    }

    /**
     * Caso haja alguma peça capturada coloca o contador das peças capturadas a 0,
     * usado para ver se há empate ou não
     * @param wasCaptureOrPawnMove indica se alguma peça foi capturada ou se houve movimentos de peões
     */
    public void updateGameHistory(boolean wasCaptureOrPawnMove) {
        // Resetar contador se houve captura ou movimento de peão
        if (wasCaptureOrPawnMove) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }

        String currentPosition = board.getAllBoardText() + (isWhiteTurn ? "W" : "B");
        positionHistory.add(currentPosition);
    }
    /**
     * Devolve a peça na posição indicada
     * @param row Linha
     * @param col Coluna
     * @return peça na posição, ou null se não existir
     */
    public Piece getPiece(int row, int col){
        return board.getPiece(row, col);
    }

    /**
     * Devolve o nome da imagem associada à peça na posição
     * @param row Linha
     * @param col Coluna
     * @return string com o nome da imagem
     */
    public String getPieceImageString(int row, int col){
        return board.getPieceImageString(row, col);
    }

    /**
     * Devolve o nome do som associado à peça na posição
     * @param row linha
     * @param col coluna
     * @return string com o nome do som
     */
    public String getPieceSoundString(int row, int col){
        return board.getPieceSoundString(row, col);
    }

    /**
     * Verifica se a peça está vulnerável a en passant
     * @param row Linha
     * @param col Coluna
     * @return true se vulnerável, false caso contrário
     */
    public Boolean getPieceEnPassantVulnerable(int row, int col){
        return board.getPieceEnPassantVulnerable(row, col);
    }

    /**
     * Devolve o tamanho do tabuleiro
     * @return tamanho do tabuleiro
     */
    public int getBoardSize(){
        return board.getBoardSize();
    }

    /**
     * Define os nomes dos jogadores
     * @param n1 nome do jogador 1
     * @param n2 nome do jogador 2
     */
    public void setPlayersNames(String n1, String n2){
        player1 = n1;
        player2 = n2;
        System.out.println("WHITE PLAYER: " + player1);
        System.out.println("BLACK PLAYER: " + player2);
    }

    /**
     * Devolve o nome do jogador 1.
     * @return nome do jogador 1.
     */
    public String getPlayer1(){
        return player1;
    }

    /**
     * Devolve o nome do jogador 2.
     * @return nome do jogador 2.
     */
    public String getPlayer2(){
        return player2;
    }

    /**
     * Devolve o estado do vencedor.
     * @return 0 = preto ganhou, 1 = branco ganhou, 2 = xeque, 3 = empate ou null caso nao exista.
     */
    public int getWinner(){
        return winner;
    }

    /**
     * Devolve os movimentos válidos da peça na posição indicada
     * @param col Coluna
     * @param row Linha
     * @return lista de coordenadas possíveis [col, row]
     */
    public List<int[]> getValidMoves(int col, int row) {
        Piece piece = board.getPiece(col, row);
        if (piece == null) {
            return List.of();
        }

        // Obter todos os movimentos possíveis da peça
        List<int[]> allMoves = piece.getPossibleMoves(board);
        List<int[]> validMoves = new ArrayList<>();

        // Filtrar todos os movimentos para garantir que não deixem o rei em xeque
        for (int[] move : allMoves) {
            if (isMoveValidAndSafe(piece, col, row, move[0], move[1], isWhiteTurn)) {
                validMoves.add(move);
            }
        }

        return validMoves;
    }

    /**
     * Reinicia o estado do jogo
     */
    public void resetGame() {
        board = new Board();
        isWhiteTurn = true;
        winner = -1;
        halfMoveClock = 0;
        positionHistory.clear();
    }


    /**
     * Devolve o nome do jogador atual
     * @return "WHITE" ou "BLACK"
     */
    public String getCurrentPlayer(){
        if (isWhiteTurn) return "WHITE";
        return "BLACK";
    }

    /**
     * Muda para o jogador seguinte
     */
    public void changeCurrentPlayer(){
        isWhiteTurn = !isWhiteTurn;
    }

    /**
     * Devolve o estado do jogo como uma string formatada que nos dá uma noção do jogo atual,
     * permitindo assim ver se houve alterações no mesmo
     * @return estado do jogo
     */
    public String getQueryState(){ // Query the board state
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentPlayer());
        sb.append(",\n");
        sb.append(board.getAllBoardText());

        return sb.toString();
    }

    /**
     * Exporta o estdo atual do jogo para um ficheiro CSV
     * @param filename nome do ficheiro de destino
     */
    public void exportCsv(String filename){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(getQueryState());
            System.out.println("Estado do jogo salvo em: " + filename);
        } catch (IOException e) {
            System.err.println("Erro ao salvar o estado do jogo: " + e.getMessage());
        }
    }

    /**
     * Importa o estado do jogo a partir de um ficheiro CSV
     * @param filename nome do ficheiro CSV
     */
    public void importCsv(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            StringBuilder fullText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fullText.append(line.trim()).append(" ");
            }

            String[] parts = fullText.toString().trim().split(",");

            if (parts.length == 0)
                throw new IOException("Ficheiro CSV vazio ou inválido.");

            String currentPlayer = parts[0].trim().toUpperCase();
            this.isWhiteTurn = currentPlayer.startsWith("WHITE");

            List<String> pieces = new ArrayList<>();
            for (int i = 1; i < parts.length; i++) {
                String piece = parts[i].trim();
                if (!piece.isEmpty()) {
                    pieces.add(piece);
                }
            }

            this.board = new Board(pieces);
        } catch (IOException e) {
            System.err.println("Erro ao importar o estado do jogo: " + e.getMessage());
        }
    }

    /**
     * Verifica se o jogador especificado está em check
     * @param isWhite
     * @return
     */
    public boolean isCheck(boolean isWhite) {

        int[] kingPos = board.findKingPosition(isWhite);
        if (kingPos == null) return false;

        return board.isSquareUnderAttack(kingPos[0], kingPos[1], !isWhite);
    }

    /**
     * Verifica se um movimento é válido e deixa o rei seguro
     * @param piece peça a mover
     * @param fromCol coluna inicial
     * @param fromRow linha inicial
     * @param toCol coluna destino
     * @param toRow linha destino
     * @param kingIsWhite cor do rei a proteger
     * @return true se o movimento é válido e seguro
     */
    private boolean isMoveValidAndSafe(Piece piece, int fromCol, int fromRow, int toCol, int toRow, boolean kingIsWhite) {
        // Guardar a peça que pode ser capturada
        Piece capturedPiece = board.getPiece(toCol, toRow);

        // Executar o movimento temporariamente
        board.removePiece(fromCol, fromRow);
        board.setPiece(piece, toCol, toRow);

        // Verificar se o rei ainda está em xeque
        boolean inCheck = isCheck(kingIsWhite);

        // Desfazer o movimento
        board.removePiece(toCol, toRow);
        board.setPiece(piece, fromCol, fromRow);
        if (capturedPiece != null) {
            board.setPiece(capturedPiece, toCol, toRow);
        }

        return !inCheck;
    }

    /**
     * Verifica se há movimentos válidos disponíveis para o jogador
     * @param isWhite true se for o jogador branco
     * @return true se há pelo menos um movimento válido
     */
    private boolean hasValidMoves(boolean isWhite) {
        for (int row = 0; row < board.getBoardSize(); row++) {
            for (int col = 0; col < board.getBoardSize(); col++) {
                Piece piece = board.getPiece(col, row);
                if (piece != null && piece.isWhite() == isWhite) {
                    List<int[]> validMoves = getValidMoves(col, row);
                    if (!validMoves.isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Verifica se o jogador está em cheque-mate
     * @param isWhite true se o jogador for branco
     * @return true se for cheque-mate
     */
    public boolean isCheckMate(boolean isWhite){
        // Se não está em xeque, não pode ser xeque-mate
        if (!isCheck(isWhite)) {
            return false;
        }

        // Se está em xeque mas tem movimentos válidos, não é xeque-mate
        if (hasValidMoves(isWhite)) {
            return false;
        }

        // Se está em xeque e não tem movimentos válidos, é xeque-mate
        if(isWhite){
            winner = 0;
        } else {
            winner = 1;
        }
        return true;
    }

    /**
     * Verificar se existe uma peça e se esta no turno certo para ser movida
     * @param col Coluna
     * @param row Linha
     * @return true se a peça pertence ao jogador atual
     */
    public boolean checkPieceLegal(int col, int row) {
        Piece piece = board.getPiece(col, row);
         if (piece == null) {
            return false;
        }
        if (piece.isWhite() != isWhiteTurn) {
            return false;
        }
        return true;
    }

    /**
     * Remove uma peça na posição indicada
     * @param col
     * @param row
     */
    public void removePiece(int col, int row) {
        board.removePiece(col, row);
    }

    /**
     * Coloca uma peça na posição indicada
     * @param piece peça a colocar
     * @param col Coluna
     * @param row Linha
     */
    public void setPiece(Piece piece, int col, int row) {
        board.setPiece(piece, col, row);
    }

    /**
     * Promove um peão para outra peça, ,com base na escolha do jogador
     * @param col coluna do peão
     * @param row linha do peão
     * @param evolution tipo de peça para promover (queen, rook, bishop, knight)
     * @param white true se o peão for branco
     */
    public void promotePawn(int col, int row, String evolution, boolean white) {
        board.removePiece(col, row);

        if(evolution.equals("queen")){
            Piece newQueen = new Queen(white, col, row, true);
            board.setPiece(newQueen, col, row);

        }else if(evolution.equals("rook")){
            Piece newRook = new Rook(white, col, row, true);
            board.setPiece(newRook, col, row);

        }else if(evolution.equals("bishop")){
            Piece newBishop = new Bishop(white, col, row, true);
            board.setPiece(newBishop, col, row);

        }else if(evolution.equals("knight")){
            Piece newKnight = new Knight(white, col, row, true);
            board.setPiece(newKnight, col, row);
        }
    }

    /**
     * Executa um movimento, caso seja válido,
     * alterando o winner seguindo a logica explicada em cima
     * @param startCol coluna inicial
     * @param startRow linha incial
     * @param endCol coluna destino
     * @param endRow linha destino
     * @return código do resultado: -1 sem movimento, 0 movimento invalido, 1 movimento normal, 2 check, 3 checkmate, 4 empate
     */
    public int executeMove(int startCol, int startRow, int endCol, int endRow) {
        Piece piece = board.getPiece(startCol, startRow);

        if(piece == null || piece.isWhite() != isWhiteTurn){
            return -1;
        }

        // Verificar se o movimento está na lista de movimentos válidos
        List<int[]> validMoves = getValidMoves(startCol, startRow);
        boolean isValidMove = false;
        for (int[] move : validMoves) {
            if (move[0] == endCol && move[1] == endRow) {
                isValidMove = true;
                break;
            }
        }

        if (!isValidMove) {
            System.out.println("Movimento inválido!");
            return 0;
        }

        // Verificar se houve captura ou movimento de peão
        Piece capturedPiece = board.getPiece(endCol, endRow);
        boolean wasCaptureOrPawnMove = (capturedPiece != null) || (piece instanceof Pawn);

        // Executar o movimento
        board.removePiece(startCol, startRow);
        piece.setPosition(endCol, endRow);
        board.setPiece(piece, endCol, endRow);

        // Lógica de roque e en passant (mantém igual)
        if (piece instanceof King && Math.abs(startCol - endCol) == 2) {
            //rooque pequeno (lado do rei)
            if (endCol > startCol) {
                Piece rook = board.getPiece(7, startRow);
                board.removePiece(7, startRow);
                rook.setPosition(5, startRow);
                board.setPiece(rook, 5, startRow);
            } else {
                Piece rook = board.getPiece(0, startRow);
                board.removePiece(0, startRow);
                rook.setPosition(3, startRow);
                board.setPiece(rook, 3, startRow);
            }
        }


        // Verificar en passant
        if(piece instanceof Pawn){
            int direction = piece.isWhite() ? 1 : -1;
            Piece pawnEnPassantCaptured = board.getPiece(endCol, endRow - direction);
            if(pawnEnPassantCaptured instanceof Pawn
                    && pawnEnPassantCaptured.isWhite() != piece.isWhite()
                    && pawnEnPassantCaptured.isEnPassantVulnerable()){
                board.removePiece(endCol, endRow - direction);
                wasCaptureOrPawnMove = true;
            }
        }

        if(pieceEnPassantVulnerable != null && pieceEnPassantVulnerable instanceof Pawn){
            ((Pawn) pieceEnPassantVulnerable).setEnPassantVulnerable(false);
            pieceEnPassantVulnerable = null;
        }

        // Lógica de roque e en passant (mantém igual)
        if(piece instanceof Pawn && (startRow == endRow -2 || startRow == endRow +2)) {
            pieceEnPassantVulnerable = piece;
        }

        isWhiteTurn = !isWhiteTurn;
        updateGameHistory(wasCaptureOrPawnMove);

        // Verificar condições de fim de jogo
       if (isCheck(isWhiteTurn)) {
            if(isCheckMate(isWhiteTurn)){
                System.out.println("Xeque-mate!");
                winner = isWhiteTurn ? 0 : 1;
                return 3;
            } else {
                System.out.println("Xeque!");
                winner = 2;
                return 2;
            }
        } else if (isDraw()) {
            System.out.println("Empate!");
            winner = 3;
            return 4;
        }

        winner = -1;
        return 1;
    }

    /**
     * Mét0do main para possíveis teste locais
     * @param args argumentos da linha de comandos
     */
    public static void main(String[] args) {

    }
}