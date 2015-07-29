package ce.payback.rainbow;

import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.tree.ValueTreeNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test of the PathWalker class.
 */
public class PathWalkerTest {

    final private static int WHITE = 0;
    final private static int BLACK = 20;
    final private static String  SIZE_KEY = "size";
    final private static Integer ONE = 1;
    final private static Integer FIVE = 5;

    @Test
    public void testGetTree() throws Exception {

        final PathWalker underTest = new PathWalker(0);

        underTest.addPath("src/main/java/Stefan.java", WHITE, createStatistics(SIZE_KEY, FIVE), 5);
        underTest.addPath("src/main/test/Steven.java", BLACK, createStatistics(SIZE_KEY, ONE), 5);

        RootTreeNode result = underTest.getTree();

        assertEquals("testGetTree structure check 1", 1, result.getChildren().size());
        assertEquals("testGetTree structure check 2", 1, result.getChildren().get("src").getChildren().size());
        assertEquals("testGetTree structure check 3", 2, result.getChildren().get("src").getChildren().get("main").getChildren().size());
        assertEquals("testGetTree structure check 4", 1, result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().size());
        assertEquals("testGetTree structure check 5", 1, result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().size());

        final ValueTreeNode stefan = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().get("Stefan.java");
        assertNotNull("testGetTree node stefan check not null", stefan);
        assertEquals("testGetTree node stefan check colorReferenceNumber", WHITE, stefan.getColorReferenceNumber()-1);
        assertEquals("testGetTree node stefan check statistic size", FIVE,stefan.getStatistics().get(SIZE_KEY));

        final ValueTreeNode steven = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().get("Steven.java");
        assertNotNull("testGetTree node steven check not null", steven);
        assertEquals("testGetTree node steven check colorReferenceNumber", BLACK, steven.getColorReferenceNumber()-1);
        assertEquals("testGetTree node steven check statistic size", ONE, steven.getStatistics().get(SIZE_KEY));
    }

    private Map<String, Number> createStatistics(final String key, final Number value) {
        final Map<String, Number> statistics = new HashMap<>();
        statistics.putIfAbsent(key, value);
        return statistics;
    }


}
