package UP_Beat.Parser;

import UP_Beat.Error.ErrorEval;
import UP_Beat.Error.NoSuchElementException;
import UP_Beat.Error.SyntaxError;
import UP_Beat.Expression.Expression;
import UP_Beat.Land.Cell;
import UP_Beat.Land.CityCrew;
import UP_Beat.Land.Region;
import UP_Beat.Player.Player;
import UP_Beat.Tokenizer.ProcessTokenizer;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class ParseExpressionTest {

    @Test
    void testSimpleAddition() throws SyntaxError, NoSuchElementException, ErrorEval {
        ProcessTokenizer tokenizer = new ProcessTokenizer("1 + 2");
        CityCrew cityCrew = new CityCrew(new Player("TestPlayer", new Region(5, 5)), new Region(5, 5), new Cell(0, 0));
        ParseExpression parser = new ParseExpression(tokenizer, cityCrew, new HashMap<>());

        Expression result = parser.parse();

        assertEquals("(1 + 2)", result.toString());
    }

    @Test
    void testComplexExpression() throws SyntaxError, NoSuchElementException, ErrorEval {
        ProcessTokenizer tokenizer = new ProcessTokenizer("3 * (4 + 2) / 2 ^ 2");
        CityCrew cityCrew = new CityCrew(new Player("TestPlayer", new Region(5, 5)), new Region(5, 5), new Cell(0, 0));
        ParseExpression parser = new ParseExpression(tokenizer, cityCrew, new HashMap<>());

        Expression result = parser.parse();

        assertEquals("((3 * (4 + 2)) / (2 ^ 2))", result.toString());
    }

    @Test
    void testInfoExpression() throws SyntaxError, NoSuchElementException, ErrorEval {
        ProcessTokenizer tokenizer = new ProcessTokenizer("opponent");
        CityCrew cityCrew = new CityCrew(new Player("TestPlayer", new Region(5, 5)), new Region(5, 5), new Cell(0, 0));
        ParseExpression parser = new ParseExpression(tokenizer, cityCrew, new HashMap<>());

        Expression result = parser.parse();

        assertEquals("opponent", result.toString());
    }

    @Test
    void testNearbyInfoExpression() throws SyntaxError, NoSuchElementException, ErrorEval {
        ProcessTokenizer tokenizer = new ProcessTokenizer("nearby up");
        CityCrew cityCrew = new CityCrew(new Player("TestPlayer", new Region(5, 5)), new Region(5, 5), new Cell(0, 0));
        ParseExpression parser = new ParseExpression(tokenizer, cityCrew, new HashMap<>());

        Expression result = parser.parse();

        assertEquals("nearby(up)", result.toString());
    }

    @Test
    void testInvalidExpression() throws ErrorEval, SyntaxError {
        ProcessTokenizer tokenizer = new ProcessTokenizer("1 + * 2");
        CityCrew cityCrew = new CityCrew(new Player("TestPlayer", new Region(5, 5)), new Region(5, 5), new Cell(0, 0));
        ParseExpression parser = new ParseExpression(tokenizer, cityCrew, new HashMap<>());

        assertThrows(SyntaxError.class, parser::parse);
    }
}