package UP_Beat.Parser;

import UP_Beat.Error.ErrorEval;
import UP_Beat.Error.NoSuchElementException;
import UP_Beat.Error.SyntaxError;
import UP_Beat.Expression.Expression;
import UP_Beat.Land.Cell;
import UP_Beat.Land.CityCrew;
import UP_Beat.Eval.*;
import UP_Beat.Land.Region;
import UP_Beat.Player.Player;
import UP_Beat.Tokenizer.ProcessTokenizer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ParsePlanTest {

    private CityCrew cityCrew;
    private Map<String, Integer> bindings;
    private ProcessTokenizer tokenizer;

    @BeforeEach
    void setUp() {
        Region region;
        try {
            region = new Region(5, 5);
            Player player = new Player("TestPlayer", region);
            cityCrew = new CityCrew(player, region, region.getRandomEmptyCell());
        } catch (Exception e) {
            fail("Error setting up the test: " + e.getMessage());
        } catch (ErrorEval e) {
            throw new RuntimeException(e);
        }
        bindings = new HashMap<>();
    }

    @Test
    void testParseMoveCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
        tokenizer = new ProcessTokenizer("move up");
        simulateUserInput("move up");
        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);

        parsePlan.parsePlan();

        assertEquals(1, cityCrew.getCurrentCell().getRow());
    }

    @Test
    void testParseInvestCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
        tokenizer = new ProcessTokenizer("invest 50");
        simulateUserInput("invest 50");
        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);

        parsePlan.parsePlan();

        assertEquals(50, cityCrew.getRegion().getCell(2, 2).getDeposit().getCurrentDeposit());
    }

//    @Test
//    void testParseCollectCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
//        tokenizer = new ProcessTokenizer("collect 30;");
//        simulateUserInput("collect 30;");
//        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);
//
//        parsePlan.parsePlan();
//
//        assertEquals(30, cityCrew.getBudget());
//    }

//    @Test
//    void testParseShootCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
//        tokenizer = new ProcessTokenizer("shoot up opponent;");
//        simulateUserInput("shoot up opponent;");
//        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);
//
//        parsePlan.parsePlan();
//
//        Assertions.assertNotNull(cityCrew.getCurrentCell().getOpponent());
//        Assertions.assertTrue(cityCrew.getCurrentCell().getOpponent().isAlive());
//    }

    @Test
    void testParseRelocateCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
        tokenizer = new ProcessTokenizer("relocate;");
        simulateUserInput("relocate;");
        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);

        parsePlan.parsePlan();

        Assertions.assertNotEquals(cityCrew.getCurrentCell(), cityCrew.getRegion().getCell(2, 2));
    }

    @Test
    void testParseDoneCommand() throws SyntaxError, NoSuchElementException, ErrorEval {
        tokenizer = new ProcessTokenizer("done");
        simulateUserInput("done");
        ParsePlan parsePlan = new ParsePlan(tokenizer, cityCrew, bindings);

        parsePlan.parsePlan();

        Assertions.assertFalse(cityCrew.getPlayer().getStatus());
    }

    private void simulateUserInput(String input) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
    }
}
