package edu.grinnell.csc207.balancing;

public class AVLTree <T extends Comparable<? super T>> {
    private static class Node <T extends Comparable<? super T>> {
        public T data;
        public Node<T> left;
        public Node<T> right;

        private Node (T data, Node<T> left, Node<T> right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }

        public Node (T data) { this(data, null, null); }
    }

    private Node<T> root;

    public AVLTree () { root = null; }

    private int sizeH(Node<T> root) {
        if (root == null) {
            return 0;
        } else {
            return 1 + sizeH(root.left) + sizeH(root.right);
        }
    }

    public int size() { return sizeH(root); }

    private boolean containsH (T data, Node<T> root) {
        if (root == null) {
            return false;
        } else if (data.compareTo(root.data) < 0) {
            return containsH(data, root.left);
        } else if (data.compareTo(root.data) > 0) {
            return containsH(data, root.right);
        } else {
            return true;
        }
    }

    public boolean contains (T data) { return containsH(data, root); }

    public int heightH(Node<T> cur) {
        if (cur != null) {
            return 1 + Math.max(heightH(cur.left), heightH(cur.right));
        } else {
            return 0;
        }
    }

    public int height() {
        if (root != null) {
            return heightH(root);
        } else {
            return 0;
        }
    }

    

    private int balanceFactor (Node<T> root) {
        if (root != null) {
            return heightH(root.left) - heightH(root.right);
        } else {
            return 0;
        }
    }

    private Node<T> insertH(T data, Node<T> root) {
        if (root == null) {
            return new Node<>(data);
        } else if (data.compareTo(root.data) < 0) {
            root.left = insertH(data, root.left);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertH(data, root.right);
        }
        return root;
    }

    public void insert(T data) {
        root = insertH(data, root);
    }

    private record FindAndDeleteResult<T extends Comparable <? super T>>(T data, Node<T> newRoot) {}

    private FindAndDeleteResult<T> findAndDeleteMax(Node<T> root) {
        if (root == null) {
            throw new IllegalStateException("findAndDeleteMin expects a non-empty tree");
        } else if (root.right == null) {
            return new FindAndDeleteResult<>(root.data, root.left);
        } else {
            FindAndDeleteResult<T> result = findAndDeleteMax(root.right);
            root.right = result.newRoot;
            return new FindAndDeleteResult<>(result.data, root);
        }
    }

    private Node<T> deleteH(T data, Node<T> root) {
        if (root == null) {
            return null;
        } else if (data.compareTo(root.data) < 0) {
            root.left = deleteH(data, root.left);
        } else if (data.compareTo(root.data) > 0) {
            root.right = deleteH(data, root.right);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            } else {
                FindAndDeleteResult<T> result = findAndDeleteMax(root.left);
                root.left = result.newRoot;
                root.data = result.data;
                return root;
            }
        }
        return root;
    }

    private void delete(T data) {
        root = deleteH(data, root);
    }

    private static void printStatistics(AVLTree<Integer> tree) {
        System.out.println("Size: " + tree.size());
        System.out.println("Height: " + tree.height());
        System.out.println("Balance factor: " + tree.balanceFactor(tree.root));
    }

    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        tree.insert(5);
        tree.insert(1);
        tree.insert(0);
        tree.insert(3);
        tree.insert(2);
        tree.insert(7);
        tree.insert(6);
        //
        //        5
        //       / \
        //      /   \
        //     /     \
        //    1       7
        //   / \     /
        //  0   3   6
        //     /
        //    2
        //
        System.out.println("Initial insertions");
        System.out.println("==================");
        printStatistics(tree);
        System.out.println("Contains 4? " + tree.contains(4));
        System.out.println("Contains 5? " + tree.contains(5));
        System.out.println("Contains 6? " + tree.contains(6));
        System.out.println();
        tree.delete(5);
        System.out.println("After deletion");
        System.out.println("==============");
        printStatistics(tree);
        System.out.println("Contains 4? " + tree.contains(4));
        System.out.println("Contains 5? " + tree.contains(5));
        System.out.println("Contains 6? " + tree.contains(6));

    }
}
