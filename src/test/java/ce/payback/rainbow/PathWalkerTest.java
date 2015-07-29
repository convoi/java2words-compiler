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

    final private static Integer WHITE = 0;
    final private static Integer BLACK = 20;
    final private static String  SIZE_KEY = "size";
    final private static Integer ONE = 1;
    final private static Integer FIVE = 5;

    @Test
    public void testGetTree() throws Exception {

        final PathWalker underTest = new PathWalker(0);

        underTest.addPath("src/main/java/Stefan.java", WHITE, createStatistics(SIZE_KEY, FIVE));
        underTest.addPath("src/main/test/Steven.java", BLACK, createStatistics(SIZE_KEY, ONE));

        RootTreeNode result = underTest.getTree();

        assertEquals(1, result.getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().size());
        assertEquals(2, result.getChildren().get("src").getChildren().get("main").getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().size());

        final ValueTreeNode stefan = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().get("Stefan.java");
        assertNotNull(stefan);
        assertEquals(stefan.getColorReferenceNumber(), WHITE);
        assertEquals(stefan.getStatistics().get(SIZE_KEY), FIVE);

        final ValueTreeNode steven = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().get("Steven.java");
        assertNotNull(steven);
        assertEquals(steven.getColorReferenceNumber(), BLACK);
        assertEquals(steven.getStatistics().get(SIZE_KEY), ONE);
    }

    private Map<String, Number> createStatistics(final String key, final Number value) {
        final Map<String, Number> statistics = new HashMap<>();
        statistics.putIfAbsent(key, value);
        return statistics;
    }


}
