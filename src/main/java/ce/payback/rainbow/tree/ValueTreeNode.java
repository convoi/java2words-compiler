package ce.payback.rainbow.tree;

public class ValueTreeNode extends TreeNode {

  private final Integer colorReferenceNumber;

  public ValueTreeNode(final Integer id, final TreeNode parent, final int depth, final TreeNodeType type,
    final String name, final Integer colorReferenceNumber) {
    super(id, parent, depth, type, name);

    this.colorReferenceNumber = colorReferenceNumber;
  }

  public Integer getColorReferenceNumber() {
    return colorReferenceNumber;
  }
}
