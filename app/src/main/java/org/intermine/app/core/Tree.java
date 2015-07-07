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
import java.util.Comparator;
import java.util.LinkedList;

public class Tree<T> {
    private final Node<T> rootElement;

    public Tree() {
        this(null, null);
    }

    public Tree(final Comparator<? super T> comparator, final T rootValue) {
        this.rootElement = new Node<T>(rootValue, null);
    }

    public Tree(final T rootValue) {
        this.rootElement = new Node<T>(rootValue, null);
    }

    private static <T> boolean doVisit(final Node<T> node, final NodeVisitor<T> visitor) {
        boolean result = visitor.visit(node);
        if (result) {
            for (final Node<T> subNode : node.children) {
                if (!doVisit(subNode, visitor)) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public void visitNodes(final NodeVisitor<T> visitor) {
        doVisit(rootElement, visitor);
    }

    public Node<T> getRootElement() {
        return rootElement;
    }

    public interface NodeVisitor<T> {

        boolean visit(Node<T> node);
    }
    private static final class NodeComparator<T> implements Comparator<Node<T>> {

        private final Comparator<T> wrapped;

        public NodeComparator(final Comparator<T> wrappedComparator) {
            this.wrapped = wrappedComparator;
        }

        @Override
        public int compare(final Node<T> o1, final Node<T> o2) {
            return wrapped.compare(o1.value, o2.value);
        }

    }
    public static class Node<T> {

        private final LinkedList<Node<T>> children;

        private final Node<T> parent;
        private T value;

        @SuppressWarnings("unchecked")
        Node(final T value, final Node<T> parent) {
            this.value = value;
            this.parent = parent;
            children = new LinkedList<Node<T>>();
        }

        public ArrayList<Node<T>> getChildren() {
            return new ArrayList<Node<T>>(children);
        }

        public Node<T> getParent() {
            return parent;
        }

        public T getValue() {
            return value;
        }

        public void setValue(final T value) {
            this.value = value;
        }

        public Node<T> addChild(final T value) {
            for (Node child : children) {
                if (child.getValue().equals(value)) {
                    return child;
                }
            }
            final Node<T> node = new Node<T>(value, this);
            return children.add(node) ? node : null;
        }
    }
}