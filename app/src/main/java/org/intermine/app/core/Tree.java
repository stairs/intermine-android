package org.intermine.app.core; 
/*
 * Copyright (C) 2015 InterMine
 *
 * This code may be freely distributed and modified under the
 * terms of the GNU Lesser General Public Licence.  This should
 * be distributed with the code.  See the LICENSE file for more
 * information or http://www.gnu.org/copyleft/lesser.html.
 *
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Tree {
    private final Node rootElement;

    public Tree() {
        this(null);
    }

    public Tree(final String rootValue) {
        this.rootElement = new Node(rootValue, null);
    }

    private static <T> boolean doVisit(final Node node, final NodeVisitor visitor) {
        boolean result = visitor.visit(node);
        if (result) {
            for (final Node subNode : node.children) {
                if (!doVisit(subNode, visitor)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public void visitNodes(final NodeVisitor visitor) {
        doVisit(rootElement, visitor);
    }

    public void compact(Node node) {
        LinkedList<Node> children = node.children;

        if (!children.isEmpty()) {
            for (Node subNode : children) {
                compact(subNode);
            }

            if (null != node.getValue() && 1 == children.size()) {
                Node onlyChild = children.get(0);

                if (!onlyChild.children.isEmpty()) {
                    node.removeChild(onlyChild);
                    node.setValue(node.getValue() + " > " + onlyChild.getValue());
                    node.addChildren(onlyChild.children);
                }
            }
        }
    }

    public void computeDepthOfEachNode(Node node) {
        LinkedList<Node> children = node.children;

        if (children.isEmpty()) {
            node.depth = 0;
        } else {
            for (Node subNode : children) {
                computeDepthOfEachNode(subNode);
            }
            Collections.sort(children, new Comparator<Node>() {
                @Override
                public int compare(Node lhs, Node rhs) {
                    return lhs.depth - rhs.depth;
                }
            });
            node.depth = node.children.getLast().depth + 1;
        }
    }

    public Node getRootElement() {
        return rootElement;
    }

    public interface NodeVisitor {

        boolean visit(Node node);
    }

    public static class Node {
        private final LinkedList<Node> children;
        private Node parent;
        private String value;
        private int depth;
        private int number;

        @SuppressWarnings("unchecked")
        Node(final String value, final Node parent) {
            this.value = value;
            this.parent = parent;
            children = new LinkedList<>();
        }

        public ArrayList<Node> getChildren() {
            return new ArrayList<>(children);
        }

        public Node getParent() {
            return parent;
        }

        public String getValue() {
            return value;
        }

        public int getDepth() {
            return depth;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public Node addChild(final String value) {
            for (Node child : children) {
                if (child.getValue().equals(value)) {
                    return child;
                }
            }
            final Node node = new Node(value, this);
            return children.add(node) ? node : null;
        }

        public void addChildren(final Collection<Node> children) {
            for (Node node : children) {
                node.parent = this;
                this.children.add(node);
            }
        }

        public void removeChild(final Node node) {
            children.remove(node);
        }
    }
}