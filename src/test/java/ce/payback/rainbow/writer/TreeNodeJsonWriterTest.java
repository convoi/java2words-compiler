package ce.payback.rainbow.writer;

import ce.payback.rainbow.PathWalker;
import ce.payback.rainbow.tree.RootTreeNode;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Created by stefan on 28.07.15.
 */
public class TreeNodeJsonWriterTest {

  @Test
  public void testTransformRootTreeToJson() throws Exception, WriterException {
    final StringWriter stringWriter = new StringWriter();
    final JsonWriter jsonWriter = JsonWriter.of(stringWriter);

    final TreeNodeJsonWriter underTest = new TreeNodeJsonWriter();

    final RootTreeNode tree = createSampleTree();

    underTest.transformRootTreeToJson(jsonWriter, tree);

    final String expectedStringResult = "{\"id\":0,\"name\":\"root\",\"isNode\":true,\"children\":[{\"id\":2147383649,\"name\":\"src\",\"isNode\":true,\"color\":0,\"children\":[{\"id\":2147383650,\"name\":\"main\",\"isNode\":true,\"color\":0,\"children\":[{\"id\":2147383651,\"name\":\"java\",\"isNode\":true,\"color\":0,\"children\":[{\"id\":2147383648,\"name\":\"Stefan.java\",\"isNode\":false,\"color\":20,\"children\":[]}]},{\"id\":2147383655,\"name\":\"test\",\"isNode\":true,\"color\":0,\"children\":[{\"id\":2147383652,\"name\":\"Steven.java\",\"isNode\":false,\"color\":0,\"children\":[]}]}]}]}]}";

    assertEquals(expectedStringResult, stringWriter.toString());
  }

  private RootTreeNode createSampleTree() {
    final PathWalker underTest = new PathWalker(0);

    underTest.addPath("src/main/java/Stefan.java", 20);
    underTest.addPath("src/main/test/Steven.java", 0);

    return underTest.getTree();
  }
}
