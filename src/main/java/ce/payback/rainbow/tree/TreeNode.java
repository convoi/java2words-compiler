/*
 * SoftVis3D Sonar plugin
 * Copyright (C) 2014 Stefan Rinderle
 * stefan@rinderle.info
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package ce.payback.rainbow.tree;

import java.util.*;

public class TreeNode {

  private final Integer id;
  private String name;

  private TreeNode parent;
  private int depth;
  private final TreeNodeType type;

  private final Map<String, TreeNode> children = new TreeMap<String, TreeNode>();

  public TreeNode(final Integer id, final TreeNode parent, final int depth, final TreeNodeType type, final String name) {
    this.id = id;
    this.parent = parent;
    this.depth = depth;
    this.name = name;
    this.type = type;
  }

  public Map<String, TreeNode> getChildren() {
    return this.children;
  }

  public boolean isNode() {
    return !this.children.isEmpty();
  }

  public Integer getId() {
    return this.id;
  }

  public TreeNode getParent() {
    return this.parent;
  }

  public void setParent(final TreeNode parent) {
    this.parent = parent;
  }

  public Integer getDepth() {
    return this.depth;
  }

  public void setDepth(final Integer depth) {
    this.depth = depth;
  }

  public TreeNodeType getType() {
    return this.type;
  }

  public String getName() {
    return this.name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public int getAllChildrenNodesSize() {
    int result = 0;

    result += this.getChildrenNodes().size();
    for (final TreeNode node : this.getChildrenNodes()) {
      result += node.getAllChildrenNodesSize();
    }

    return result;
  }

  /**
   * TODO: Could be placed somewhere else
   */
  private TreeNode recursiveSearch(final Integer id, final TreeNode treeNode) {
    if (treeNode.getId().equals(id)) {
      /**
       * check if there is a child treeNode with the same id. This is to parse long paths and get the last
       * treeNode of the chain with the same id.
       */
      for (final TreeNode child : treeNode.getChildren().values()) {
        if (child.getId().equals(id)) {
          return this.recursiveSearch(id, child);
        }
      }

      return treeNode;
    }

    final Map<String, TreeNode> nodeChildren = treeNode.getChildren();
    TreeNode temp;
    if (!nodeChildren.isEmpty()) {
      for (final TreeNode child : nodeChildren.values()) {
        temp = this.recursiveSearch(id, child);
        if (temp != null) {
          return temp;
        }
      }
    }

    return null;
  }

  public List<TreeNode> getChildrenNodes() {
    final List<TreeNode> result = new ArrayList<TreeNode>();

    for (final TreeNode child : children.values()) {
      if (!child.getChildren().isEmpty()) {
        result.add(child);
      }
    }

    return result;
  }

  public List<TreeNode> getChildrenLeaves() {
    final List<TreeNode> result = new ArrayList<TreeNode>();

    for (final TreeNode child : children.values()) {
      if (child.getChildren().isEmpty()) {
        result.add(child);
      }
    }

    return result;
  }

  public void addChildrenNode(final String name, final TreeNode child) {
    children.put(name, child);
  }
}
