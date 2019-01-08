package amazons;

import org.junit.Test;

import static amazons.Piece.*;
import static org.junit.Assert.*;
import ucb.junit.textui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** The suite of all JUnit tests for the enigma package.
 *  @author edwardsun
 */
public class UnitTest {

    /** Run the JUnit tests in this package. Add xxxTest.class entries to
     *  the arguments of runClasses to run other JUnit tests. */
    public static void main(String[] ignored) {
        textui.runClasses(UnitTest.class);
        textui.runClasses(IteratorTests.class);
    }

    /** Tests basic correctness of put and get on the initialized board. */
    @Test
    public void testBasicPutGet() {
        Board b = new Board();
        b.put(BLACK, Square.sq(3, 5));
        assertEquals(b.get(3, 5), BLACK);
        b.put(WHITE, Square.sq(9, 9));
        assertEquals(b.get(9, 9), WHITE);
        b.put(EMPTY, Square.sq(3, 5));
        assertEquals(b.get(3, 5), EMPTY);
    }

    /** Tests proper identification of legal/illegal queen moves. */
    @Test
    public void testIsQueenMove() {
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(1, 5)));
        assertFalse(Square.sq(1, 5).isQueenMove(Square.sq(2, 7)));
        assertFalse(Square.sq(0, 0).isQueenMove(Square.sq(5, 1)));
        assertTrue(Square.sq(1, 1).isQueenMove(Square.sq(9, 9)));
        assertTrue(Square.sq(2, 7).isQueenMove(Square.sq(8, 7)));
        assertTrue(Square.sq(3, 0).isQueenMove(Square.sq(3, 4)));
        assertTrue(Square.sq(7, 9).isQueenMove(Square.sq(0, 2)));
    }

    @Test
    public void testmakeMove() {
        Board b = new Board();
        b.put(WHITE, Square.sq(3, 6));
        b.put(WHITE, Square.sq(7, 2));
        b.put(BLACK, Square.sq(0, 4));
        b.put(BLACK, Square.sq(4, 5));

        b.makeMove(Square.sq(3, 6), Square.sq(3, 2),  Square.sq(6, 5));
        assertEquals(b.get(3, 6), EMPTY);
        assertEquals(b.get(3, 2), WHITE);
        assertEquals(b.get(6, 5), SPEAR);
    }

    @Test
    public void testisLegal() {
        Board b = new Board();
        b.put(WHITE, Square.sq(3, 6));
        b.put(WHITE, Square.sq(7, 2));
        b.put(BLACK, Square.sq(0, 4));
        b.put(BLACK, Square.sq(4, 5));

        assertEquals(true, b.isLegal(Square.sq(3, 6),
                Square.sq(3, 2), Square.sq(6, 5)));

        b.put(SPEAR, Square.sq(3, 2));

        assertEquals(false, b.isLegal(Square.sq(3, 6),
                Square.sq(3, 2), Square.sq(6, 5)));
        assertEquals(false, b.isLegal(Square.sq(3, 6),
                Square.sq(4, 4), Square.sq(6, 5)));


        assertEquals(true, b.isLegal(Square.sq(7, 2),
                Square.sq(8, 3), Square.sq(5, 0)));
        assertEquals(true, b.isLegal(Square.sq(0, 4),
                Square.sq(1, 3), Square.sq(0, 4)));
    }


    @Test
    public void testDirection() {
        assertEquals(Square.sq(1, 5).direction(Square.sq(3, 7)), 1);
        assertEquals(Square.sq(3, 5).direction(Square.sq(3, 7)), 0);
        assertEquals(Square.sq(3, 5).direction(Square.sq(0, 2)), 5);
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testToString() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeSmile(b);
        assertEquals(SMILE, b.toString());
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testReachable() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeBlackLose(b);
        assertEquals(BLACK_LOSE, b.toString());

        Iterator<Square> reachable = b.reachableFrom(Square.sq(2, 7), null);
        assertFalse(reachable.hasNext());
    }

    /** Tests toString for initial board state and a smiling board state. :) */
    @Test
    public void testLegalMove() {
        Board b = new Board();
        assertEquals(INIT_BOARD_STATE, b.toString());
        makeBlackLose(b);
        assertEquals(BLACK_LOSE, b.toString());

        Iterator<Move> legal = b.legalMoves(BLACK);
        assertFalse(legal.hasNext());
    }


    private void makeBlackLose(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));

        b.put(BLACK, Square.sq(2, 7));
        b.put(BLACK, Square.sq(7, 7));
    }

    private void makeSmile(Board b) {
        b.put(EMPTY, Square.sq(0, 3));
        b.put(EMPTY, Square.sq(0, 6));
        b.put(EMPTY, Square.sq(9, 3));
        b.put(EMPTY, Square.sq(9, 6));
        b.put(EMPTY, Square.sq(3, 0));
        b.put(EMPTY, Square.sq(3, 9));
        b.put(EMPTY, Square.sq(6, 0));
        b.put(EMPTY, Square.sq(6, 9));
        for (int col = 1; col < 4; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(2, 7));
        for (int col = 6; col < 9; col += 1) {
            for (int row = 6; row < 9; row += 1) {
                b.put(SPEAR, Square.sq(col, row));
            }
        }
        b.put(EMPTY, Square.sq(7, 7));
        for (int lip = 3; lip < 7; lip += 1) {
            b.put(WHITE, Square.sq(lip, 2));
        }
        b.put(WHITE, Square.sq(2, 3));
        b.put(WHITE, Square.sq(7, 3));
    }

    static final String INIT_BOARD_STATE =
            "   - - - B - - B - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   B - - - - - - - - B\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   W - - - - - - - - W\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - W - - W - - -\n";

    static final String SMILE =
            "   - - - - - - - - - -\n"
            + "   - S S S - - S S S -\n"
            + "   - S - S - - S - S -\n"
            + "   - S S S - - S S S -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - W - - - - W - -\n"
            + "   - - - W W W W - - -\n"
            + "   - - - - - - - - - -\n"
            + "   - - - - - - - - - -\n";

    static final String BLACK_LOSE =
            "   - - - - - - - - - -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - S B S - - S B S -\n"
                    + "   - S S S - - S S S -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - W - - - - W - -\n"
                    + "   - - - W W W W - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n";

    static final String WHITELOSS =
            "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   B S S B - - - - - -\n"
                    + "   S S W B - - - - - -\n"
                    + "   S S S S - - - - - -\n"
                    + "   W S W S - - - - - -\n"
                    + "   S S S S B - - - - -\n"
                    + "   S S S - - - - - - -\n"
                    + "   W S - - - - - - - -\n";

    static final String SP3 =
            "   - - - B - - B - - -\n"
                    + "   - - - W - - S - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - B\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - - - - - -\n"
                    + "   W - - - - - - - - W\n"
                    + "   - - - - - - - - - -\n"
                    + "   - - - - - B - - - -\n"
                    + "   - - - - S W S - - -\n";

    @Test
     public void testReachableFromIterator() {
        Board b = new Board();
        assertEquals(20, testIterator(b, Square.sq("d1"), null));
        assertEquals(20, testIterator(b, Square.sq("g1"), Square.sq("g1")));
        assertEquals(20, testIterator(b, Square.sq("d10"), Square.sq("d10")));
        assertEquals(20, testIterator(b, Square.sq("g10"), Square.sq("g10")));
        assertEquals(20, testIterator(b, Square.sq("a4"), Square.sq("a4")));
        assertEquals(20, testIterator(b, Square.sq("a7"), Square.sq("a7")));
        assertEquals(20, testIterator(b, Square.sq("j4"), Square.sq("j4")));
        assertEquals(20, testIterator(b, Square.sq("j7"), Square.sq("j7")));
    }

    public int testIterator(Board b, Square from, Square asEmpty) {

        Iterator<Square> it = b.reachableFrom(from, asEmpty);
        int count = 0;
        while (it.hasNext()) {
            Square s = it.next();
            count++;
        }
        return count;
    }

    @Test
    public void testasEmpty() {
        Board b = new Board();
        assertEquals(24, testIterator(b, Square.sq("d1"), Square.sq("g1")));
        assertEquals(21, testIterator(b, Square.sq("a7"), Square.sq("j7")));
        assertEquals(21, testIterator(b, Square.sq("g10"), Square.sq("g1")));
        assertEquals(21, testIterator(b, Square.sq("j4"), Square.sq("a4")));
    }

    @Test
    public void testLegalMoves() {
        Board b = new Board();
        makeSmile(b);
        assertEquals(SMILE, b.toString());
        Square from = Square.sq(3, 2);
        int numLegals = 0;
        for (int i = b.SIZE * b.SIZE - 1; i >= 0; i--) {
            if (b.isLegal(from, Square.sq(i))) {
                numLegals++;
            }
        }
        assertEquals(15, numLegals);
    }

    @Test
    public void testSimpleThrows() {
        Board b = new Board();
        makeSmile(b);
        assertEquals(SMILE, b.toString());
        assertTrue(b.isLegal(Square.sq(3, 2), Square.sq(5, 4),
                Square.sq(2, 1)));
        assertTrue(b.isLegal(Square.sq(3, 2), Square.sq(5, 4),
                Square.sq(5, 7)));
        assertTrue(b.isLegal(Square.sq(3, 2), Square.sq(5, 4),
                Square.sq(3, 2)));
        assertFalse(b.isLegal(Square.sq(3, 2), Square.sq(5, 4),
                Square.sq(5, 2)));
    }

    @Test
    public void testLegalThrows() {
        Board b = new Board();
        makeSmile(b);
        assertEquals(SMILE, b.toString());
        Square from = Square.sq(3, 2);
        int numLegals = 0;
        for (int i = b.SIZE * b.SIZE - 1; i >= 0; i--) {
            for (int j = b.SIZE * b.SIZE - 1; j >= 0; j--) {
                if (b.isLegal(from, Square.sq(i), Square.sq(j))) {
                    numLegals++;
                }
            }
        }
        assertEquals(267, numLegals);
    }


    @Test
    public void testLegalMovesIterator() {
        Board b = new Board();
        List<String> ret = getList(b, WHITE);
        assertEquals(2176, ret.size());
    }

    private List<String> getList(Board b, Piece p) {
        List<String> ret = new ArrayList<>();
        Iterator<Move> it = b.legalMoves(p);
        while (it.hasNext()) {
            Move mv = it.next();
            assertTrue(b.isLegal(mv));
            ret.add(mv.toString());
        }
        return ret;
    }

    @Test
    public void testLegalMovesIterator2() {
        Board b = new Board();
        int numLegals = 0;
        Iterator<Move> it = b.legalMoves(WHITE);
        ArrayList<String> ret;
        while (it.hasNext()) {
            Move mv = it.next();
            assertTrue(b.isLegal(mv));
            Iterator<Move> it2 = b.legalMoves(WHITE);
            while (it2.hasNext()) {
                Move mv2 = it2.next();
                assertTrue(b.isLegal(mv2));
                numLegals++;
            }
        }
        assertEquals(2176 * 2176, numLegals);
    }

    @Test
    public void testLegalMovesIterator3() {
        Board b = new Board();
        int numLegals = 0;
        Iterator<Move> it = b.legalMoves(WHITE);
        ArrayList<String> ret;
        while (it.hasNext()) {
            Move mv = it.next();
            assertTrue(b.isLegal(mv));
            b.makeMove(mv);
            Iterator<Move> it2 = b.legalMoves(WHITE);
            while (it2.hasNext()) {
                Move mv2 = it2.next();
                assertTrue(b.isLegal(mv2));
                numLegals++;
            }
            b.undo();
        }
        assertEquals(4633196, numLegals);
    }

    static void readBoard(Board b, String pattern) {
        int index = 0;
        for (String i : pattern.split("[ \n]")) {
            if (i.length() == 0) {
                continue;
            }
            int col = index % b.SIZE;
            int row = b.SIZE - index / b.SIZE - 1;
            Square s = Square.sq(col, row);
            if (i.equals("B")) {
                b.put(BLACK, s);
            } else if (i.equals("W")) {
                b.put(WHITE, s);
            } else if (i.equals("S")) {
                b.put(SPEAR, s);
            } else if (i.equals("-")) {
                b.put(EMPTY, s);
            }
            index++;
        }
        assertEquals(pattern, b.toString());
    }

    @Test
    public void testInititalScore() {
        Board b = new Board();
        assertEquals(0, b.score());
    }

    @Test
    public void testInititalFreedom() {
        Board b = new Board();
        assertEquals(20, b.freedom(BLACK));
    }

    @Test
    public void testIfWhiteLoss() {
        Board b = new Board();
        readBoard(b, WHITELOSS);
        assertTrue(b.winner() == BLACK);
    }

    private Move _bestMove;

    private int maximizer(Board b, Piece turn) {
        _bestMove = null;
        Iterator<Move> it = b.legalMoves(turn);
        int bestSoFar = Integer.MIN_VALUE;
        while (it.hasNext()) {
            Move mv = it.next();
            b.makeMove(mv);
            int score = b.score();
            if (score > bestSoFar) {
                System.out.printf("BestSoFar=%d %s%n", score, mv);
                bestSoFar = score;
                _bestMove = mv;
            }
            b.undo();
        }
        return bestSoFar;
    }

    private int minimizer(Board b, Piece turn) {
        _bestMove = null;
        Iterator<Move> it = b.legalMoves(turn);
        int bestSoFar = Integer.MAX_VALUE;
        while (it.hasNext()) {
            Move mv = it.next();
            b.makeMove(mv);
            int score = b.score();
            if (score < bestSoFar) {
                System.out.printf("BestSoFar=%d %s%n", score, mv);
                bestSoFar = score;
                _bestMove = mv;
            }
            b.undo();
        }
        return bestSoFar;
    }

    private int play(Board b, Piece turn) {
        if (turn == WHITE) {
            return maximizer(b, turn);
        } else {
            return minimizer(b, turn);
        }
    }


    @Test
    public void testBestMoves() {
        assertEquals(4, play(new Board(), WHITE));
        assertEquals(Move.mv("d1-d2(d9)"), _bestMove);
        System.out.println();

        assertEquals(-4, play(new Board(), BLACK));
        assertEquals(Move.mv("a7-b8(h2)"), _bestMove);
        System.out.println();
    }

    @Test
    public void testBestMoveSP3() {
        Board b = new Board();
        readBoard(b, SP3);
        assertEquals(7, play(b, WHITE));
        assertEquals(Move.mv("f1-h3(h9)"), _bestMove);
        System.out.println();

        b = new Board();
        readBoard(b, SP3);
        assertEquals(-4, play(b, BLACK));
        assertEquals(Move.mv("g10-e8(i4)"), _bestMove);
        System.out.println();
    }


}


