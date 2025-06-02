package pt.isec.pa.chess.model.data;

public enum PieceType {
    KING {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new King(color, col, row);
        }

        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new King(color, col, row, hasMoved);
        }
    },

    QUEEN {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new Queen(color,col,row);
        }

        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new Queen(color, col, row, hasMoved);
        }
    },

    BISHOP {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new Bishop(color,col,row);
        }

        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new Bishop(color, col, row, hasMoved);
        }
    },

    KNIGHT {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new Knight(color,col,row);
        }

        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new Knight(color, col, row, hasMoved);
        }
    },

    ROOK {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new Rook(color,col,row);
        }

        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new Rook(color, col, row, hasMoved);
        }
    },

    PAWN {
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row) {
            return new Pawn(color,col,row);
        }
        @Override
        public Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved) {
            return new Pawn(color, col, row, hasMoved);
        }
    };

    public abstract Piece createPiece(PieceType type, boolean color, int col, int row);

    public abstract Piece createPiece(PieceType type, boolean color, int col, int row, boolean hasMoved);

    public static Piece createPieceFromString(String representation) {
        if (representation == null || (representation.length() != 3 && representation.length() != 4))
            throw new IllegalArgumentException("Invalid representation: " + representation);

        representation = representation.trim();

        char pieceChar = representation.charAt(0);
        char colChar = representation.charAt(1);
        int rowChar = Character.getNumericValue(representation.charAt(2));

        boolean hasMoved = true; // Por default ainda não se moveu

        // Se existir 4º caracter, ele tem que ser '*'
        if (representation.length() == 4) {
            if (representation.charAt(3) != '*')
                throw new IllegalArgumentException("Invalid 4th character: " + representation);
            hasMoved = false;
        }

        boolean color;
        if (Character.isUpperCase(pieceChar)) {
            color = true;
        }else{
            color = false;
        }


        PieceType type;
        switch (Character.toUpperCase(pieceChar)) {
            case 'K' -> type = KING;
            case 'Q' -> type = QUEEN;
            case 'R' -> type = ROOK;
            case 'B' -> type = BISHOP;
            case 'N' -> type = KNIGHT;
            case 'P' -> type = PAWN;
            default -> throw new IllegalArgumentException("Unknown piece type: " + pieceChar);
        }

        int col = colChar - 'a';  // 'a' -> 0, 'b' -> 1, ...
        int row = rowChar - 1;    // '1' -> 0, '2' -> 1, ...

        return type.createPiece(type, color, col, row, hasMoved);
    }



}