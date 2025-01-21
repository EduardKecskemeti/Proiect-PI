// src/main/java/datastructures/RedBlackTree.java
package datastructures;

public class RedBlackTree<T extends Comparable<T>> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private class Node {
        T data;
        Node left, right, parent;
        boolean color;

        Node(T data) {
            this.data = data;
            this.color = RED;
        }
    }

    private Node root;

    public void insert(T data) {
        Node newNode = new Node(data);
        root = insert(root, newNode);
        fixInsert(newNode);
    }

    private Node insert(Node root, Node node) {
        if (root == null) {
            return node;
        }
        if (node.data.compareTo(root.data) < 0) {
            root.left = insert(root.left, node);
            root.left.parent = root;
        } else if (node.data.compareTo(root.data) > 0) {
            root.right = insert(root.right, node);
            root.right.parent = root;
        }
        return root;
    }

    private void fixInsert(Node node) {
        Node parent = null;
        Node grandParent = null;
        while (node != root && node.color != BLACK && node.parent.color == RED) {
            parent = node.parent;
            grandParent = parent.parent;
            if (parent == grandParent.left) {
                Node uncle = grandParent.right;
                if (uncle != null && uncle.color == RED) {
                    grandParent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.right) {
                        rotateLeft(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateRight(grandParent);
                    boolean tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            } else {
                Node uncle = grandParent.left;
                if (uncle != null && uncle.color == RED) {
                    grandParent.color = RED;
                    parent.color = BLACK;
                    uncle.color = BLACK;
                    node = grandParent;
                } else {
                    if (node == parent.left) {
                        rotateRight(parent);
                        node = parent;
                        parent = node.parent;
                    }
                    rotateLeft(grandParent);
                    boolean tempColor = parent.color;
                    parent.color = grandParent.color;
                    grandParent.color = tempColor;
                    node = parent;
                }
            }
        }
        root.color = BLACK;
    }

    private void rotateLeft(Node node) {
        Node rightChild = node.right;
        node.right = rightChild.left;
        if (node.right != null) {
            node.right.parent = node;
        }
        rightChild.parent = node.parent;
        if (node.parent == null) {
            root = rightChild;
        } else if (node == node.parent.left) {
            node.parent.left = rightChild;
        } else {
            node.parent.right = rightChild;
        }
        rightChild.left = node;
        node.parent = rightChild;
    }

    private void rotateRight(Node node) {
        Node leftChild = node.left;
        node.left = leftChild.right;
        if (node.left != null) {
            node.left.parent = node;
        }
        leftChild.parent = node.parent;
        if (node.parent == null) {
            root = leftChild;
        } else if (node == node.parent.left) {
            node.parent.left = leftChild;
        } else {
            node.parent.right = leftChild;
        }
        leftChild.right = node;
        node.parent = leftChild;
    }

    public boolean search(T data) {
        return search(root, data) != null;
    }

    private Node search(Node root, T data) {
        if (root == null || root.data.equals(data)) {
            return root;
        }
        if (data.compareTo(root.data) < 0) {
            return search(root.left, data);
        }
        return search(root.right, data);
    }

    public void delete(T data) {
        Node node = search(root, data);
        if (node == null) {
            return;
        }
        delete(node);
    }

    private void delete(Node node) {
        Node replacement = node;
        Node child;
        boolean originalColor = replacement.color;
        if (node.left == null) {
            child = node.right;
            transplant(node, node.right);
        } else if (node.right == null) {
            child = node.left;
            transplant(node, node.left);
        } else {
            replacement = minimum(node.right);
            originalColor = replacement.color;
            child = replacement.right;
            if (replacement.parent == node) {
                if (child != null) {
                    child.parent = replacement;
                }
            } else {
                transplant(replacement, replacement.right);
                replacement.right = node.right;
                replacement.right.parent = replacement;
            }
            transplant(node, replacement);
            replacement.left = node.left;
            replacement.left.parent = replacement;
            replacement.color = node.color;
        }
        if (originalColor == BLACK) {
            fixDelete(child);
        }
    }

    private void fixDelete(Node node) {
        while (node != root && (node == null || node.color == BLACK)) {
            if (node == node.parent.left) {
                Node sibling = node.parent.right;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateLeft(node.parent);
                    sibling = node.parent.right;
                }
                if ((sibling.left == null || sibling.left.color == BLACK) &&
                        (sibling.right == null || sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling.right == null || sibling.right.color == BLACK) {
                        if (sibling.left != null) {
                            sibling.left.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateRight(sibling);
                        sibling = node.parent.right;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    if (sibling.right != null) {
                        sibling.right.color = BLACK;
                    }
                    rotateLeft(node.parent);
                    node = root;
                }
            } else {
                Node sibling = node.parent.left;
                if (sibling.color == RED) {
                    sibling.color = BLACK;
                    node.parent.color = RED;
                    rotateRight(node.parent);
                    sibling = node.parent.left;
                }
                if ((sibling.left == null || sibling.left.color == BLACK) &&
                        (sibling.right == null || sibling.right.color == BLACK)) {
                    sibling.color = RED;
                    node = node.parent;
                } else {
                    if (sibling.left == null || sibling.left.color == BLACK) {
                        if (sibling.right != null) {
                            sibling.right.color = BLACK;
                        }
                        sibling.color = RED;
                        rotateLeft(sibling);
                        sibling = node.parent.left;
                    }
                    sibling.color = node.parent.color;
                    node.parent.color = BLACK;
                    if (sibling.left != null) {
                        sibling.left.color = BLACK;
                    }
                    rotateRight(node.parent);
                    node = root;
                }
            }
        }
        if (node != null) {
            node.color = BLACK;
        }
    }

    private void transplant(Node target, Node with) {
        if (target.parent == null) {
            root = with;
        } else if (target == target.parent.left) {
            target.parent.left = with;
        } else {
            target.parent.right = with;
        }
        if (with != null) {
            with.parent = target.parent;
        }
    }

    private Node minimum(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }
}