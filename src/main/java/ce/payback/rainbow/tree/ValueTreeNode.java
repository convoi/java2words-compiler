package ce.payback.rainbow.tree;

public class ValueTreeNode extends TreeNode {

  private final Integer colorReferenceNumber;
  private final Integer size;

  public ValueTreeNode(final Integer id, final TreeNode parent, final int depth, final TreeNodeType type,
    final String name, final Integer colorReferenceNumber, final Integer size) {
    super(id, parent, depth, type, name);

    this.colorReferenceNumber = colorReferenceNumber;
    this.size = size;
  }

  public Integer getColorReferenceNumber() {
    return colorReferenceNumber;
  }

  public Integer getSize() { return size; }
}
