package ce.payback.rainbow.tree;

import java.util.Map;

public class ValueTreeNode extends TreeNode {

  private final Integer colorReferenceNumber;
  private final Map<String, Number> statistics;

  public ValueTreeNode(final Integer id, final TreeNode parent, final int depth, final TreeNodeType type,
    final String name, final Integer colorReferenceNumber, final Map<String, Number> statistics) {
    super(id, parent, depth, type, name);

    this.colorReferenceNumber = colorReferenceNumber;
    this.statistics = statistics;
  }

  public Integer getColorReferenceNumber() {
    return colorReferenceNumber;
  }

  public Map<String, Number> getStatistics() {
    return statistics;
  }

}
