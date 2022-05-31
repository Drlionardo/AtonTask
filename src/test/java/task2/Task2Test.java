package task2;

import org.junit.jupiter.api.*;
import task2.NaiveSolution;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class Task2Test {
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    private final String[][] lyrics = {
            {"Cher", "They say we're young and we don't know \nWe won't find out until we grow"},
            {"Sonny", "Well I don't know if all that's true \n'Cause you got me, and baby I got you"},
            {"Sonny", "Babe"},
            {"Sonny, Cher", "I got you babe \nI got you babe"},
            {"Cher", "They say our love won't pay the rent \nBefore it's earned, our money's all been spent"},
            {"Sonny", "I guess that's so, we don't have a pot \nBut at least I'm sure of all the things we got"},
            {"Sonny", "Babe"},
            {"Sonny, Cher", "I got you babe \nI got you babe"},
            {"Sonny", "I got flowers in the spring \nI got you to wear my ring"},
            {"Cher", "And when I'm sad, you're a clown \nAnd if I get scared, you're always around"},
            {"Cher", "So let them say your hair's too long \n'Cause I don't care, with you I can't go wrong"},
            {"Sonny", "Then put your little hand in mine \nThere ain't no hill or mountain we can't climb"},
            {"Sonny", "Babe"},
            {"Sonny, Cher", "I got you babe \nI got you babe"},
            {"Sonny", "I got you to hold my hand"},
            {"Cher", "I got you to understand"},
            {"Sonny", "I got you to walk with me"},
            {"Cher", "I got you to talk with me"},
            {"Sonny", "I got you to kiss goodnight"},
            {"Cher", "I got you to hold me tight"},
            {"Sonny", "I got you, I won't let go"},
            {"Cher", "I got you to love me so"},
            {"Sonny, Cher", "I got you babe \nI got you babe \nI got you babe \nI got you babe \nI got you babe"}
    };

    @Test
    void singTest() throws InterruptedException {
        NaiveSolution solution = new NaiveSolution(lyrics);
        Thread cherThread = new Thread(solution::sing);
        cherThread.setName("Cher");
        Thread sonnyThread = new Thread(solution::sing);
        sonnyThread.setName("Sonny");

        sonnyThread.start();
        cherThread.start();

        cherThread.join();
        sonnyThread.join();
        String expectedpart1 = """
                Cher: They say we're young and we don't know\s
                We won't find out until we grow
                Sonny: Well I don't know if all that's true\s
                'Cause you got me, and baby I got you
                Sonny: Babe
                """;
        String chorusPart1 = """
                Sonny: I got you babe\s
                I got you babe
                """;
        String chorusPart2 = """
                Cher: I got you babe\s
                I got you babe
                """;
        String expected2 = """
                Cher: They say our love won't pay the rent\s
                Before it's earned, our money's all been spent
                Sonny: I guess that's so, we don't have a pot\s
                But at least I'm sure of all the things we got
                Sonny: Babe
                """;
        String expected3 = """
                Sonny: I got flowers in the spring\s
                I got you to wear my ring
                Cher: And when I'm sad, you're a clown\s
                And if I get scared, you're always around
                Cher: So let them say your hair's too long\s
                'Cause I don't care, with you I can't go wrong
                Sonny: Then put your little hand in mine\s
                There ain't no hill or mountain we can't climb
                Sonny: Babe
                """;
        String expected4 = """
                Sonny: I got you to hold my hand
                Cher: I got you to understand
                Sonny: I got you to walk with me
                Cher: I got you to talk with me
                Sonny: I got you to kiss goodnight
                Cher: I got you to hold me tight
                Sonny: I got you, I won't let go
                Cher: I got you to love me so
                """;
        String finalPart1 = """
                Sonny: I got you babe\s
                I got you babe\s
                I got you babe\s
                I got you babe\s
                I got you babe
                """;
        String finalPart2 = """
                Cher: I got you babe\s
                I got you babe\s
                I got you babe\s
                I got you babe\s
                I got you babe
                """;
        var resultLines = outContent.toString().split("\n");

        assertEquals(expectedpart1, getLines(resultLines, 0, 5));

        var actualChorus1 = getLines(resultLines, 5, 9);
        assertTrue(equalsIgnoreOrder(actualChorus1, chorusPart1, chorusPart2));

        assertEquals(expected2, getLines(resultLines, 9, 14));

        var actualChorus2 = getLines(resultLines, 14, 18);
        assertTrue(equalsIgnoreOrder(actualChorus2, chorusPart1, chorusPart2));

        assertEquals(expected3, getLines(resultLines, 18, 27));

        var actualChorus3 = getLines(resultLines, 27, 31);
        assertTrue(equalsIgnoreOrder(actualChorus3, chorusPart1, chorusPart2));
        assertEquals(expected4, getLines(resultLines, 31, 39));

        var actualFinalPart = getLines(resultLines, 39, 49);
        assertTrue(equalsIgnoreOrder(actualFinalPart, finalPart1, finalPart2));
    }

    private boolean equalsIgnoreOrder(String expected, String part1, String part2) {
        return expected.equals(part1 + part2) || expected.equals(part2 + part1);
    }
    
    private String getLines(String[] lyrics, int from, int to) {
        String[] subarray = Arrays.copyOfRange(lyrics, from, to);
        return String.join("\n", subarray) + "\n";
    }
}