import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import pt.isec.pa.chess.model.data.*;
import static org.junit.jupiter.api.Assertions.*;


public class ChessGameTest {

    private ChessGame game;

    @BeforeEach
    void setUp() {
        game = new ChessGame();
    }

    @Test
    @DisplayName("Teste 1: Operações básicas do tabuleiro")
    void testBoardOperations() {

        Piece testPiece = new Queen(true, 4, 4, false);
        game.setPiece(testPiece, 4, 4);

        Piece retrievedPiece = game.getPiece(4, 4);
        assertNotNull(retrievedPiece, "Peça deve ser recuperada corretamente");
        assertTrue(retrievedPiece instanceof Queen, "Peça deve ser uma Rainha");
        assertTrue(retrievedPiece.isWhite(), "Peça deve ser branca");

        game.removePiece(4, 4);
        assertNull(game.getPiece(4, 4), "Posição deve estar vazia após remoção");

        assertNull(game.getPiece(-1, 0), "Posição inválida deve retornar null");
        assertNull(game.getPiece(8, 0), "Posição inválida deve retornar null");
        assertNull(game.getPiece(0, -1), "Posição inválida deve retornar null");
        assertNull(game.getPiece(0, 8), "Posição inválida deve retornar null");

    }


    @Test
    @DisplayName("Teste 2: Detecção de xeque e xeque-mate")
    void testCheckAndCheckmate() {
        // Verificar que não há xeque na posição inicial
        assertFalse(game.isCheck(true), "Brancas não devem estar em xeque inicialmente");
        assertFalse(game.isCheck(false), "Pretas não devem estar em xeque inicialmente");

        // Verificar que não há xeque-mate na posição inicial
        assertFalse(game.isCheckMate(true), "Brancas não devem estar em xeque-mate inicialmente");
        assertFalse(game.isCheckMate(false), "Pretas não devem estar em xeque-mate inicialmente");
        assertEquals(-1, game.getWinner(), "Não deve haver vencedor inicialmente");

        // Criar uma situação de xeque simples
        // Limpar o tabuleiro e colocar apenas reis e uma torre
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (!(game.getPiece(col, row) instanceof King)) {
                    game.removePiece(col, row);
                }
            }
        }

        Piece blackRook = new Rook(false, 4, 0, false);
        game.setPiece(blackRook, 4, 0);

        game.removePiece(4, 7);
        Piece whiteKing = new King(true, 4, 6, false);
        game.setPiece(whiteKing, 4, 6);

        assertTrue(game.isCheck(true), "Rei branco deve estar em xeque");

    }

    @Test
    @DisplayName("Teste 3: Condições de empate - Regra dos 50 movimentos")
    void testDrawConditions() {
        // Testar regra dos 50 movimentos - início
        assertFalse(game.isFiftyMoveRule(), "Regra dos 50 movimentos não deve estar ativa no início");
        assertFalse(game.isDraw(), "Não deve ser empate no início");

        // Simular 99 meio-movimentos
        for (int i = 0; i < 99; i++) {
            game.updateGameHistory(false); // Movimento sem captura ou peão
        }
        assertFalse(game.isFiftyMoveRule(), "99 meio-movimentos ainda não devem ativar a regra");

        // 100º meio-movimento
        game.updateGameHistory(false);
        assertTrue(game.isFiftyMoveRule(), "100 meio-movimentos devem ativar a regra dos 50 movimentos");
        assertTrue(game.isDraw(), "Deve ser empate pela regra dos 50 movimentos");

        // Testar reset com captura
        game.resetGame();
        for (int i = 0; i < 50; i++) {
            game.updateGameHistory(false);
        }
        game.updateGameHistory(true); // Captura reseta contador
        for (int i = 0; i < 99; i++) {
            game.updateGameHistory(false);
        }
        assertFalse(game.isFiftyMoveRule(), "Captura deve resetar contador");

    }

    @Test
    @DisplayName("Teste 4: Material insuficiente para mate")
    void testInsufficientMaterial() {

        assertFalse(game.isInsufficientMaterial(), "Posição inicial deve ter material suficiente");

        // Remover todas as peças exceto reis
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getPiece(col, row);
                if (piece != null && !(piece instanceof King)) {
                    game.removePiece(col, row);
                }
            }
        }

        assertTrue(game.isInsufficientMaterial(), "Apenas reis devem resultar em material insuficiente");
        assertTrue(game.isDraw(), "Material insuficiente deve resultar em empate");


        game.resetGame();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = game.getPiece(col, row);
                if (piece != null && !(piece instanceof King)) {
                    if (!(piece instanceof Bishop && piece.isWhite() && col == 2 && row == 7)) {
                        game.removePiece(col, row);
                    }
                }
            }
        }

        assertTrue(game.isInsufficientMaterial(), "Rei + Bispo vs Rei deve ser material insuficiente");

    }

    @Test
    @DisplayName("Teste 5: Promoção de peão")
    void testPawnPromotion() {

        game.removePiece(4, 6);
        Pawn promotionPawn = new Pawn(true, 4, 1, true); // Peão na 7ª linha
        game.setPiece(promotionPawn, 4, 1);

        game.promotePawn(4, 1, "queen", true);
        Piece promotedPiece = game.getPiece(4, 1);
        assertNotNull(promotedPiece, "Deve haver uma peça após promoção");
        assertTrue(promotedPiece instanceof Queen, "Peça promovida deve ser Rainha");
        assertTrue(promotedPiece.isWhite(), "Peça promovida deve manter a cor");

        game.setPiece(new Pawn(false, 3, 6, true), 3, 6);
        game.promotePawn(3, 6, "rook", false);
        assertTrue(game.getPiece(3, 6) instanceof Rook, "Deve promover para Torre");

        game.setPiece(new Pawn(true, 5, 1, true), 5, 1);
        game.promotePawn(5, 1, "bishop", true);
        assertTrue(game.getPiece(5, 1) instanceof Bishop, "Deve promover para Bispo");

        game.setPiece(new Pawn(false, 6, 6, true), 6, 6);
        game.promotePawn(6, 6, "knight", false);
        assertTrue(game.getPiece(6, 6) instanceof Knight, "Deve promover para Cavalo");

        System.out.println("✅ Teste 9 - Promoção de peão: PASSOU");
    }


    @Test
    @DisplayName("Teste 6: Detecção de Stalemate")
    void testStalemate() {

        assertFalse(game.isStalemate(true), "Brancas não devem estar em stalemate inicialmente");
        assertFalse(game.isStalemate(false), "Pretas não devem estar em stalemate inicialmente");

        assertFalse(game.isCheck(true), "Brancas não devem estar em xeque");
        assertFalse(game.isStalemate(true), "Com movimentos válidos, não é stalemate");

    }


    @Test
    @DisplayName("Teste 7: Repetição tripla de posições")
    void testThreefoldRepetition() {

        game.updateGameHistory(false);
        game.updateGameHistory(false);
        assertFalse(game.isThreefoldRepetition(), "Duas repetições ainda não são suficientes");


    }

}