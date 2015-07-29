package ce.payback.rainbow;

import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.tree.TreeNode;
import ce.payback.rainbow.tree.TreeNodeType;
import ce.payback.rainbow.tree.ValueTreeNode;

import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * This class is used to create a tree out of path information.
 */
public class PathWalker {

  private final RootTreeNode root;
  private final Pattern pathSeparator;
  // TODO: different generated id sequence in DependencyExpander and
  // PathWalker.
  private int generatedIdSequence = Integer.MAX_VALUE - 100000;

  public PathWalker(final int id) {
    this.pathSeparator = Pattern.compile("/");
    this.root = new RootTreeNode(id);
  }

  public PathWalker(final int id, final String pathSeparator) {
    this.root = new RootTreeNode(id);
    this.pathSeparator = Pattern.compile("\\" + pathSeparator);
  }

  public RootTreeNode getTree() {
    return this.root;
  }

  public void addPath(final String path, final Integer colorReferenceNumber, final Map<String, Number> statistics, int maxClasses) {
    final int id = getNextSequence();
    final String[] names = this.pathSeparator.split(path);
    TreeNode currentNode = this.root;
    int chosenColor = colorReferenceNumber == null ? maxClasses + 1 : colorReferenceNumber +1;

    boolean isLastIndex;
    for (int i = 0; i < names.length; i = i + 1) {
      isLastIndex = (i == (names.length - 1));
      if (isLastIndex) {
        currentNode =
          this.getOrCreateChild(currentNode, id, names[i], TreeNodeType.TREE, chosenColor,
                  statistics);
      } else {
        currentNode = this.getOrCreateGeneratedChild(currentNode, names[i]);
      }
    }
  }

  private int getNextSequence() {
    this.generatedIdSequence = this.generatedIdSequence + 1;
    return this.generatedIdSequence;
  }

  private TreeNode getOrCreateChild(final TreeNode node, final Integer id, final String name, final TreeNodeType type,
    final Integer colorReferenceNumber, final Map<String, Number> statistics) {
    final Map<String, TreeNode> children = node.getChildren();
    if (children.containsKey(name)) {
      return children.get(name);
    }

    final TreeNode result =
      new ValueTreeNode(id, node, node.getDepth() + 1, type, name, colorReferenceNumber, statistics);

    node.addChildrenNode(name, result);

    return result;
  }

  private TreeNode getOrCreateGeneratedChild(final TreeNode node, final String name) {
    return this.getOrCreateChild(node, this.getNextSequence(), name, TreeNodeType.PATH_GENERATED, 0, Collections.EMPTY_MAP);
  }

}
