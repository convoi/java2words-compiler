package ce.payback.rainbow;

import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.tree.ValueTreeNode;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by stefan on 28.07.15.
 */
public class PathWalkerTest {

    @Test
    public void testGetTree() throws Exception {
        final PathWalker underTest = new PathWalker(0);

        underTest.addPath("src/main/java/Stefan.java", "#ffffff");
        underTest.addPath("src/main/test/Steven.java", "#000000");

        RootTreeNode result = underTest.getTree();

        assertEquals(1, result.getChildren().size());
        final ValueTreeNode stefan = (ValueTreeNode) result.getChildren().get("src").getChildren().get("main").getChildren().get("java").getChildren().get("Stefan.java");
        assertNotNull(stefan);
        assertNotNull(result.getChildren().get("src").getChildren().get("main").getChildren().get("test").getChildren().get("Steven.java"));

        assertEquals(stefan.getColor(), "#ffffff");
    }
}