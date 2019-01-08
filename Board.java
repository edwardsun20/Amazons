package amazons;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;

import static amazons.Piece.*;
import static amazons.Square.sq;


/** The state of an Amazons Game.
 *  @author edwardsun
 */
class Board {

    /** The number of squares on a side of the board. */
    static final int SIZE = 10;

    /** Initializes a game board with SIZE squares on a side in the
     *  initial position. */
    Board() {
        init();
    }

    /** Initializes a copy of MODEL. */
    Board(Board model) {
        copy(model);
    }

    /** Copies MODEL into me. */
    void copy(Board model) {
        this._map = new HashMap<Square, Piece>(model._map);
        this._turn = model._turn;
        this._winner = model._winner;
        this._history = new ArrayList<Move>(model._history);
    }

    /** Clears the board to the initial position. */
    void init() {
        _map = new HashMap<Square, Piece>();

        _map.put(sq(3, 0), WHITE);
        _map.put(sq(6, 0), WHITE);
        _map.put(sq(9, 3), WHITE);
        _map.put(sq(9, 6), BLACK);
        _map.put(sq(6, 9), BLACK);
        _map.put(sq(3, 9), BLACK);
        _map.put(sq(0, 6), BLACK);
        _map.put(sq(0, 3), WHITE);

        _turn = WHITE;
        _winner = null;
        _history = new ArrayList<Move>();
    }

    /** Return the Piece whose move it is (WHITE or BLACK). */
    Piece turn() {
        return _turn;
    }

    /** Return the number of moves (that have not been undone) for this
     *  board. */
    int numMoves() {
        return _history.size();
    }

    /** Return the winner in the current position, or null if the game is
     *  not yet finished. */
    Piece winner() {
        if (_winner == null) {
            Iterator<Move> legal = legalMoves(BLACK);
            if (!legal.hasNext()) {
                _winner = WHITE;
            }
        }

        if (_winner == null) {
            Iterator<Move> legal = legalMoves(WHITE);
            if (!legal.hasNext()) {
                _winner = BLACK;
            }
        }

        return _winner;
    }

    /** Returns map. */
    HashMap<Square, Piece> map() {
        return _map;
    }


    /** Return the contents the square at S. */
    final Piece get(Square s) {
        if (_map.containsKey(s)) {
            return _map.get(s);
        } else {
            return EMPTY;
        }
    }

    /** Return the contents of the square at (COL, ROW), where
     *  0 <= COL, ROW <= 9. */
    final Piece get(int col, int row) {
        Square s = sq(col, row);
        return get(s);
    }

    /** Return the contents of the square at COL ROW. */
    final Piece get(char col, char row) {
        return get(col - 'a', row - '1');
    }

    /** Set square S to P. */
    final void put(Piece p, Square s) {
        if (p == EMPTY) {
            _map.remove(s);
            return;
        }
        _map.put(s, p);
    }

    /** Set square (COL, ROW) to P. */
    final void put(Piece p, int col, int row) {
        put(p, sq(col, row));
        _winner = null;
    }

    /** Set square COL ROW to P. */
    final void put(Piece p, char col, char row) {
        put(p, col - 'a', row - '1');
    }

    /** Return true iff FROM - TO is an unblocked queen move on the current
     *  board, ignoring the contents of ASEMPTY, if it is encountered.
     *  For this to be true, FROM-TO must be a queen move and the
     *  squares along it, other than FROM and ASEMPTY, must be
     *  empty. ASEMPTY may be null, in which case it has no effect. */
    boolean isUnblockedMove(Square from, Square to, Square asEmpty) {
        if (to == asEmpty || !to.isQueenMove(asEmpty)) {
            return false;
        }
        Piece side = get(from);
        if (get(asEmpty) == side.opponent() || get(asEmpty) == Piece.SPEAR) {
            return false;
        }
        if (get(asEmpty) == side && (from != asEmpty)) {
            return false;
        }
        int dir = to.direction(asEmpty);
        for (int steps = 1; steps < SIZE; steps++) {
            Square next = to.queenMove(dir, steps);
            Piece p = get(next);
            if (next == from) {
                if (p == side.opponent()) {
                    return false;
                }
            } else if (p != Piece.EMPTY) {
                return false;
            }
            if (next == asEmpty) {
                break;
            }
        }
        return true;
    }

    /** Return true iff FROM is a valid starting square for a move. */
    boolean isLegal(Square from) {
        return (0 < from.col() && from.col() < 8 && 0 < from.row()
                && from.row() < 8);
    }

    /** Return true iff FROM-TO is a valid first part of move, ignoring
     *  spear throwing. */
    boolean isLegal(Square from, Square to) {
        if (!from.isQueenMove(to)) {
            return false;
        }
        while (from != to) {
            int dir = from.direction(to);
            Square a = from.queenMove(dir, 1);
            Piece p = get(a);
            if (p == EMPTY) {
                from = a;
            } else {
                return false;
            }
        }
        return true;
    }

    /** Return true iff FROM-TO(SPEAR) is a legal move in the current
     *  position. */
    boolean isLegal(Square from, Square to, Square spear) {
        return isLegal(from, to) && isUnblockedMove(from, to, spear);
    }

    /** Return true iff MOVE is a legal move in the current
     *  position. */
    boolean isLegal(Move move) {
        return get(move.from()) == _turn
                && isLegal(move.from(), move.to(), move.spear());
    }

    /** Move FROM-TO(SPEAR), assuming this is a legal move. */
    void makeMove(Square from, Square to, Square spear) {
        Piece p = get(from);
        if (p != BLACK && p != WHITE) {
            return;
        }
        put(EMPTY, from);
        put(p, to);
        put(SPEAR, spear);
    }

    /** Move according to MOVE, assuming it is a legal move. */
    void makeMove(Move move) {
        if (isLegal(move.from(), move.to(), move.spear())) {
            makeMove(move.from(), move.to(), move.spear());
            _history.add(move);
        }
    }

    /** Undo one move.  Has no effect on the initial board. */
    void undo() {
        Move m = _history.remove(_history.size() - 1);

        put(EMPTY, m.spear());
        Piece prev = get(m.to());
        put(prev, m.from());
        put(EMPTY, m.to());
    }

    /** Returns swap. */
    void swap() {
        _turn = _turn.opponent();
    }

    /** Return an Iterator over the Squares that are reachable by an
     *  unblocked queen move from FROM. Does not pay attention to what
     *  piece (if any) is on FROM, nor to whether the game is finished.
     *  Treats square ASEMPTY (if non-null) as if it were EMPTY.  (This
     *  feature is useful when looking for Moves, because after moving a
     *  piece, one wants to treat the Square it came from as empty for
     *  purposes of spear throwing.) */
    Iterator<Square> reachableFrom(Square from, Square asEmpty) {
        return new ReachableFromIterator(from, asEmpty);
    }

    /** Return an Iterator over all legal moves on the current board. */
    Iterator<Move> legalMoves() {
        return new LegalMoveIterator(_turn);
    }

    /** Return an Iterator over all legal moves on the current board for
     *  SIDE (regardless of whose turn it is). */
    Iterator<Move> legalMoves(Piece side) {
        return new LegalMoveIterator(side);
    }

    /** An iterator used by reachableFrom. */
    private class ReachableFromIterator implements Iterator<Square> {

        /** Iterator of all squares reachable by queen move from FROM,
         *  treating ASEMPTY as empty. */
        ReachableFromIterator(Square from, Square asEmpty) {
            _from = from;
            _dir = -1;
            _steps = 0;
            _asEmpty = asEmpty;
            toNext();
        }

        @Override
        public boolean hasNext() {
            return _dir < 8;
        }

        @Override
        public Square next() {
            Square current = _from.queenMove(_dir, _steps);
            toNext();
            return current;
        }

        /** Advance _dir and _steps, so that the next valid Square is
         *  _steps steps in direction _dir from _from. */
        private void toNext() {
            if (!hasNext()) {
                return;
            }

            if (_dir < 0) {
                _dir++;
            }

            _steps++;
            Square current = _from.queenMoveBound(_dir, _steps);

            while (current == null
                    || (current != _asEmpty
                    && Board.this.get(current) != EMPTY)) {
                _dir++;
                if (_dir >= 8) {
                    break;
                }
                _steps = 1;

                current = _from.queenMoveBound(_dir, _steps);
                if (current == null
                        || Board.this.get(_from.queenMoveBound(
                                _dir, _steps + 1)) != EMPTY) {
                    continue;
                }
            }
        }

        /** Starting square. */
        private Square _from;
        /** Current direction. */
        private int _dir;
        /** Current distance. */
        private int _steps;
        /** Square treated as empty. */
        private Square _asEmpty;
    }

    /** An iterator used by legalMoves. */
    private class LegalMoveIterator implements Iterator<Move> {

        /** All legal moves for SIDE (WHITE or BLACK). */
        LegalMoveIterator(Piece side) {
            _startingSquares = Square.iterator();
            _spearThrows = NO_SQUARES;
            _pieceMoves = NO_SQUARES;
            _fromPiece = side;
            toNext();
        }

        @Override
        public boolean hasNext() {
            boolean a = _startingSquares.hasNext();
            return a;
        }

        @Override
        public Move next() {

            Square spear = _spearThrows.next();
            Move a = Move.mv(_start, _nextSquare, spear);
            if (!_spearThrows.hasNext()) {
                toNext();
            }

            return a;
        }

        /** Advance so that the next valid Move is
         *  _start-_nextSquare(sp), where sp is the next value of
         *  _spearThrows. */
        private void toNext() {
            if (!_startingSquares.hasNext()) {
                return;
            }

            while (!_pieceMoves.hasNext()) {
                _start = _startingSquares.next();
                while (get(_start) != _fromPiece) {
                    if (!_startingSquares.hasNext()) {
                        return;
                    }
                    _start = _startingSquares.next();
                }
                _pieceMoves = Board.this.reachableFrom(_start, null);
                if (_pieceMoves.hasNext()) {
                    _nextSquare = _pieceMoves.next();
                    _spearThrows = Board.this.
                            reachableFrom(_nextSquare, _start);
                    return;
                }

            }

            if (_pieceMoves.hasNext()) {
                _nextSquare = _pieceMoves.next();
                _spearThrows = Board.this.reachableFrom(_nextSquare, _start);
            }

        }

        /** Color of side whose moves we are iterating. */
        private Piece _fromPiece;
        /** Current starting square. */
        private Square _start;
        /** Remaining starting squares to consider. */
        private Iterator<Square> _startingSquares;
        /** Current piece's new position. */
        private Square _nextSquare;
        /** Remaining moves from _start to consider. */
        private Iterator<Square> _pieceMoves;
        /** Remaining spear throws from _piece to consider. */
        private Iterator<Square> _spearThrows;
    }

    @Override
    /** Board String representation. */
    public String toString() {
        String rep = "";
        for (int i = SIZE - 1; i >= 0; i--) {
            rep += "   ";
            for (int j = 0; j < SIZE; j++) {
                rep += get(j, i).toString();
                if (j != SIZE - 1) {
                    rep += " ";
                }
            }
            rep += "\n";
        }
        return rep;
    }

    /** Board Score representation.
     * @return ret
     * */
    int score() {
        Piece p = WHITE;
        int f1 = freedom(p);
        int f2 = freedom(p.opponent());
        int ret = f1 - f2;

        return ret;
    }

    /** Board freedom.
     * @return total
     * @param side side
     * */
    int freedom(Piece side) {
        Iterator<Square> it = _map.keySet()
                .stream()
                .filter(i -> _map.get(i) == side)
                .iterator();
        int total = 0;
        while (it.hasNext()) {
            Square from = it.next();
            for (int dir = 0; dir < 8; dir++) {
                Square neighbor = from.queenMoveBound(dir, 1);
                if (neighbor == null) {
                    continue;
                }
                Piece p = get(neighbor);
                if (p == Piece.EMPTY) {
                    total += 1;
                }
            }
        }
        return total;
    }

    /** An empty iterator for initialization. */
    private static final Iterator<Square> NO_SQUARES =
        Collections.emptyIterator();

    /** Piece whose turn it is (BLACK or WHITE). */
    private Piece _turn;
    /** Cached value of winner on this board, or EMPTY if it has not been
     *  computed. */
    private Piece _winner;

    /** HashMap of Board. */
    private HashMap<Square, Piece> _map;

    /** Make a List to record all Move objects. */
    private List<Move> _history;
}
