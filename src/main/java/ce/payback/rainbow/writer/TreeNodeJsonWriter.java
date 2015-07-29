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
package ce.payback.rainbow.writer;

import ce.payback.rainbow.tree.RootTreeNode;
import ce.payback.rainbow.tree.TreeNode;
import ce.payback.rainbow.tree.ValueTreeNode;

import java.util.Map;

public class TreeNodeJsonWriter {

  public void transformRootTreeToJson(final JsonWriter jsonWriter, final RootTreeNode tree) throws WriterException {
    this.transformTreeToJson(jsonWriter, tree);
    jsonWriter.close();
  }

  private void transformTreeToJson(final JsonWriter jsonWriter, final TreeNode node) throws WriterException {
    jsonWriter.beginObject();

    jsonWriter.prop("id", node.getId());
    jsonWriter.prop("name", node.getName());
    jsonWriter.prop("isNode", node.isNode());
    optionalTransformMetricValues(jsonWriter, node);

    this.transformChildren(jsonWriter, node.getChildren());

    jsonWriter.endObject();
  }

  private void optionalTransformMetricValues(final JsonWriter jsonWriter, final TreeNode node) throws WriterException {
    if (node instanceof ValueTreeNode) {
      final ValueTreeNode valueNode = (ValueTreeNode) node;
      jsonWriter.prop("color", valueNode.getColorReferenceNumber());
      jsonWriter.name("statistics");
      jsonWriter.beginObject();
      for (Map.Entry<String, Number> entry : valueNode.getStatistics().entrySet()) {
          jsonWriter.prop(entry.getKey(), entry.getValue());
      }
      jsonWriter.endObject();
    }
  }

  private void transformChildren(final JsonWriter jsonWriter, final Map<String, TreeNode> children) throws WriterException {
    jsonWriter.name("children");
    jsonWriter.beginArray();

    // first folders
    for (final TreeNode child : children.values()) {
      if (child.isNode()) {
        this.transformTreeToJson(jsonWriter, child);
      }
    }

    // then files
    for (final TreeNode child : children.values()) {
      if (!child.isNode()) {
        this.transformTreeToJson(jsonWriter, child);
      }
    }

    jsonWriter.endArray();
  }

}
