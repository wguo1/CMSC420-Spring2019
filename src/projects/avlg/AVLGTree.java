package projects.avlg;

import projects.avlg.exceptions.EmptyTreeException;
import projects.avlg.exceptions.InvalidBalanceException;

import java.util.List;

/** <p>An <tt>AVL-G Tree</tt> is an AVL Tree with a relaxed balance condition. Its constructor receives a strictly
=======
/** <p>An AVL-G Tree is an AVL Tree with a relaxed balance condition. Its constructor receives a strictly
>>>>>>> upstream/master
 * positive parameter which controls the <b>maximum</b> imbalance allowed on any subtree of the tree which
 * it creates. So, for example:</p>
 *  <ul>
 *      <li>An AVL-1 tree is a classic AVL tree, which only allows for perfectly balanced binary
 *      subtrees (imbalance of 0 everywhere), or subtrees with a maximum imbalance of 1 (somewhere). </li>
 *      <li>An AVL-2 tree relaxes the criteria of AVL-1 trees, by also allowing for subtrees
 *      that have an imbalance of 2.</li>
 *      <li>AVL-3 trees allow an imbalance of 3</li>
 *      <li>...</li>
 *  </ul>
 *
 *  <p>The idea behind AVL-G trees is that rotations cost time, so maybe we would be willing to
 *  accept bad search performance now and then if it would mean less rotations.</p>
 *
 * @author <a href="https://github.com/JasonFil">Jason Filippou</a>
 */
public class AVLGTree<T extends Comparable<T>> {


    private static RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /* *************************************************************************
     ************** PLACE YOUR PRIVATE METHODS AND FIELDS HERE: ****************
     ***************************************************************************/

    private final int imbalance;
    private Node root;
    private int count = 0;

    private class Node
    {
        private int height;
        private Node left;
        private Node right;
        private T value;

        private Node(T key)
        {
            height = 0;
            value = key;
        }
    }

    private void updateHeights(Node cur)
    {
        if (cur != null)
            cur.height = Math.max(converter(cur.right), converter(cur.left)) + 1;
    }

    private int converter(Node cur)
    {
        return (cur == null) ? -1 : cur.height;
    }

    private Node rotateR(Node cur)
    {
        Node temp = cur.left;
        cur.left = temp.right;
        temp.right = cur;

        updateHeights(cur);
        updateHeights(temp);

        return temp;
    }

    private Node rotateL(Node cur)
    {
        Node temp = cur.right;
        cur.right = temp.left;
        temp.left = cur;

        updateHeights(cur);
        updateHeights(temp);

        return temp;
    }

    private Node rotateLR(Node cur) {
        cur.left = rotateL(cur.left);
        return rotateR(cur);
    }

    private Node rotateRL(Node cur) {
        cur.right = rotateR(cur.right);
        return rotateL(cur);
    }

    private Node inOrderDelete(T val, Node cur, Node indicator)
    {
        if (cur == null)
            indicator.value = null;
        else if (val.compareTo(cur.value) < 0) {
            cur.left = inOrderDelete(val, cur.left, indicator);
            if(converter(cur.left) - converter(cur.right) < (-1 * getMaxImbalance()))
                cur = (converter(cur.right.left) > converter(cur.right.right)) ? rotateRL(cur) : rotateL(cur);
        }
        else if (val.compareTo(cur.value) > 0) {
            cur.right = inOrderDelete(val ,cur.right, indicator);
            if(converter(cur.left) - converter(cur.right) > getMaxImbalance())
                cur = (converter(cur.left.left) < converter(cur.left.right)) ? rotateLR(cur) : rotateR(cur);
        }
        else
        {
            if (cur.right == null && cur.left == null)
                cur = null;
            else if (cur.left == null)
                cur = cur.right;
            else if (cur.right == null)
                cur = cur.left;
            else {
                Node temp = cur.left;
                while(temp.right != null)
                    temp = temp.right;

                cur.value = temp.value;
                cur.left = inOrderDelete(temp.value, cur.left, indicator);
                if(converter(cur.left) - converter(cur.right) < (-1 * getMaxImbalance()))
                    cur = (converter(cur.right.left) > converter(cur.right.right)) ? rotateRL(cur) : rotateL(cur);
            }
        }

        updateHeights(cur);
        return cur;
    }

    private Node avlSearch(Node cur, T key) {
        if (cur != null)
        {
            if (key.compareTo(cur.value) < 0)
                cur = avlSearch(cur.left, key);
            else if (key.compareTo(cur.value) > 0)
                cur = avlSearch(cur.right, key);
        }

        return cur;
    }

    private Node insertNode(T key, Node cur){
        if (cur == null) {
            cur = new Node(key);
            count++;
        }
        else if (key.compareTo(cur.value) < 0) {
            cur.left = insertNode(key, cur.left);
            if (converter(cur.left) - converter(cur.right) > getMaxImbalance())
                cur = (key.compareTo(cur.left.value) < 0) ? rotateR(cur) : rotateLR(cur);
        }
        else if (key.compareTo(cur.value) > 0) {
            cur.right = insertNode(key, cur.right);
            if (converter(cur.left) - converter(cur.right) < (-1 * getMaxImbalance()))
                cur = (key.compareTo(cur.right.value) > 0) ? rotateL(cur) : rotateRL(cur);
        }

        updateHeights(cur);
        return cur;
    }

    private boolean isBSTHelper(Node cur) {
        if (cur == null || (cur.left == null && cur.right == null))
            return true;
        else if (cur.left == null)
            return (cur.value.compareTo(cur.right.value) < 0) && isBSTHelper(cur.right);
        else if (cur.right == null)
            return (cur.value.compareTo(cur.left.value) > 0) && isBSTHelper(cur.left);
        else
            return (cur.value.compareTo(cur.right.value) < 0) && (cur.value.compareTo(cur.left.value) > 0) &&
                    isBSTHelper(cur.right) && isBSTHelper(cur.left);
    }

    private boolean balanced(Node cur) {
        boolean result = true;
        if (cur != null)
            result = (Math.abs(converter(cur.right) - converter(cur.left)) <= getMaxImbalance()) &&
                    balanced(cur.left) && balanced(cur.right);

        return result;
    }
    /* *********************************************************************
     ************************* PUBLIC (INTERFACE) METHODS *******************
     **********************************************************************/

    /**
     * The class constructor provides the tree with its maximum maxImbalance allowed.
     * @param maxImbalance The maximum maxImbalance allowed by the AVL-G Tree.
     * @throws InvalidBalanceException if maxImbalance is a value smaller than 1.
     */
    public AVLGTree(int maxImbalance) throws InvalidBalanceException {
        if (maxImbalance < 1)
            throw new InvalidBalanceException("Constructor is broken");
        else
            imbalance = maxImbalance;
    }

    /**
     * Insert key in the tree.
     * @param key The key to insert in the tree.
     */

    public void insert(T key) {
        root = insertNode(key, root);
    }

    /**
     * Delete the key from the data structure and return it to the caller.
     * @param key The key to delete from the structure.
     * @return The key that was removed, or null if the key was not found.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T delete(T key) throws EmptyTreeException {
        if (root == null)
            throw new EmptyTreeException("Delete");
        else {
            Node indicator = new Node(key);
            root = inOrderDelete(key, root, indicator);

            if (indicator.value != null)
                count--;

            return indicator.value;
        }
    }

    /**
     * <p>Search for key in the tree. Return a reference to it if it's in there,
     * or null otherwise.</p>
     * @param key The key to search for.
     * @return key if key is in the tree, or null otherwise.
     * @throws EmptyTreeException if the tree is empty.
     */
    public T search(T key) throws EmptyTreeException {
        if (root == null)
            throw new EmptyTreeException("Empty Tree");
        else
        {
            Node node = avlSearch(root, key);
            return (node == null) ? null : node.value;
        }
    }


    /**
     * Retrieves the maximum imbalance parameter.
     * @return The maximum imbalance parameter provided as a constructor parameter.
     */
    public int getMaxImbalance(){
        return imbalance;
    }


    /**
     * <p>Return the height of the tree. The height of the tree is defined as the length of the
     * longest path between the root and the leaf level. By definition of path length, a
     * stub tree has a height of 0, and we define an empty tree to have a height of -1.</p>
     * @return The height of the tree. If the tree is empty, returns -1.
     */
    public int getHeight() {
        return (root == null) ? -1 : root.height;
    }

    /**
     * Query the tree for emptiness. A tree is empty iff it has zero keys stored.
     * @return true if the tree is empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Return the key at the tree's root nodes.
     * @return The key at the tree's root nodes.
     * @throws  EmptyTreeException if the tree is empty.
     */
    public T getRoot() throws EmptyTreeException{
        if (root == null)
            throw new EmptyTreeException("getRoot");
        else
            return root.value;
    }

    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the BST condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return true if the tree satisfies the Binary Search Tree property,
     * false otherwise.
     */
    public boolean isBST() {
        return isBSTHelper(root);
    }


    /**
     * <p>Establishes whether the AVL-G tree <em>globally</em> satisfies the AVL-G condition. This method is
     * <b>terrifically useful for testing!</b></p>
     * @return true if the tree satisfies the Binary Search Tree property,
     * false otherwise.
     */
    public boolean isAVLGBalanced() {
        return isBST() && balanced(root);
    }



    /**
     * <p>Empties the AVLGTree of all its elements. After a call to this method, the
     * tree should have <b>0</b> elements.</p>
     */
    public void clear(){
        root = null;
        count = 0;
    }


    /**
     * <p>Return the number of elements in the tree.</p>
     * @return  The number of elements in the tree.
     */
    public int getCount(){
        return count;
    }

    public void pr(){System.out.println(inOrder(root));}

    private String inOrder(Node cur)
    {
        if (cur == null)
            return "";
        return inOrder(cur.left) + " " + cur.value.toString() + "H:" + cur.height + " " + inOrder(cur.right);
    }
}
