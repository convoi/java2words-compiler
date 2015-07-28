package ce.payback.rainbow;

import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.tree.ValueTreeNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit test of the PathWalker class.
 */
public class PathWalkerTest {

    final private static Integer WHITE = 0;
    final private static Integer BLACK = 20;

    @Test
    public void testGetTree() throws Exception {

        final PathWalker underTest = new PathWalker(0);

        underTest.addPath("src/main/java/Stefan.java", WHITE);
        underTest.addPath("src/main/test/Steven.java", BLACK);

        RootTreeNode result = underTest.getTree();

        assertEquals(1, result.getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().size());
        assertEquals(2, result.getChildren().get("src").getChildren().get("main").getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().size());
        assertEquals(1, result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().size());

        final ValueTreeNode stefan = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().get("Stefan.java");
        assertNotNull(stefan);
        assertEquals(stefan.getColorReferenceNumber(), WHITE);

        final ValueTreeNode steven = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().get("Steven.java");
        assertNotNull(steven);
        assertEquals(steven.getColorReferenceNumber(), BLACK);
    }
}
