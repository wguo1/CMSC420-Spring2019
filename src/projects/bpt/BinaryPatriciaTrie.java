package projects.bpt;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * <p>{@link BinaryPatriciaTrie} is a Patricia Trie over the binary alphabet &#123;	 0, 1 &#125;. By restricting themselves
 * to this small but terrifically useful alphabet, Binary Patricia Tries combine all the positive
 * aspects of Patricia Tries while shedding the storage cost typically associated with Tries that
 * deal with huge alphabets.</p>
 *
 * @author ---- YOUR NAME HERE! -----
 */
public class BinaryPatriciaTrie {
    private Node root;
    private int count;

    private class Node {
        private String data;
        private boolean valid;
        private Node left, right;

        private Node(String d) {
            data = d;
            left = right = null;
        }

        private void set(boolean condition) {
            valid = condition;
        }

        private boolean searchNode(String key) {
            if (data.equals("") || (key.length() >= data.length() && key.substring(0, data.length()).equals(data)))
            {
                if (key.length() == data.length())
                    return valid;
                else if (key.charAt(data.length()) == '0' && left != null)
                    return left.searchNode(key.substring(data.length()));
                else if (key.charAt(data.length()) == '1' && right != null)
                    return right.searchNode(key.substring(data.length()));
            }

            return false;
        }

        private Node insertNode (String key)
        {
            if (data.equals(key)) {
                if (!valid)
                    count++;
                set(true);
            }
            else if (data.length() < key.length() && key.substring(0, data.length()).equals(data)) {
                if (key.charAt(data.length()) == '0') {
                    if (left == null) {
                        left = new Node(key.substring(data.length()));
                        left.set(true);
                        count++;
                    }
                    else
                        left = left.insertNode(key.substring(data.length()));
                }
                else {
                    if (right == null) {
                        right = new Node(key.substring(data.length()));
                        right.set(true);
                        count++;
                    }
                    else
                        right = right.insertNode(key.substring(data.length()));
                }
            }
            else if (data.length() > key.length() && data.substring(0, key.length()).equals(key)) {
                Node parent = new Node(key);
                parent.set(true);
                data = data.substring(key.length());

                if (data.charAt(0) == '0')
                    parent.left = this;
                else
                    parent.right = this;

                count++;
                return parent;
            }
            else {
                String d;
                String k;
                if (key.length() <= data.length()) {
                    d = data;
                    k = key;
                }
                else {
                    d = key;
                    k = data;
                }

                int pos = 0;
                while (pos < d.length() && k.charAt(pos) == d.charAt(pos))
                    pos++;

                Node parent = new Node(d.substring(0, pos));
                parent.set(false);

                Node child;
                if (data.equals(d)) {
                    data = d.substring(pos);
                    child = new Node(k.substring(pos));
                }
                else {
                    data = k.substring(pos);
                    child = new Node(d.substring(pos));
                }

                child.set(true);
                set(true);
                count++;

                if (data.charAt(0) == '0') {
                    parent.left = this;
                    parent.right = child;
                }
                else {
                    parent.left = child;
                    parent.right = this;
                }
                return parent;
            }
            return this;
        }

        private String longest() {
            String leftLong = (left == null) ? "0" : left.longest();
            String rightLong = (right == null) ? "0" : right.longest();

            if (leftLong.equals("0") && rightLong.equals("0")) {
                if (valid)
                    return data;
            }
            else if (Integer.parseInt(leftLong, 2) >= Integer.parseInt(rightLong, 2))
                return data + leftLong;
            else if (Integer.parseInt(leftLong, 2) < Integer.parseInt(rightLong, 2))
                return data + rightLong;

            return "";
        }

        private boolean junkFree() {
            boolean result = valid;
            if (result) {
                if (left == null) {
                    if (right != null)
                        result = right.junkFree();
                }
                else
                    result = (right == null) ? left.junkFree() : left.junkFree() && right.junkFree();
            }
            else
                result = (left != null && right != null) ? left.junkFree() &&
                          right.junkFree() : (left == null && right == null);

            return result;
        }

        private ArrayList<String> iterate() {
            ArrayList<String> leftList = new ArrayList<>();
            ArrayList<String> rightList = new ArrayList<>();

            if (left != null)
                for (String ele : left.iterate())
                    leftList.add(data + ele);

            if (valid)
                leftList.add(data);

            if (right != null)
                for (String ele : right.iterate())
                    rightList.add(data + ele);

            leftList.addAll(rightList);

            return leftList;
        }

        private Node deleteNode(String key) {
            if (key.length() == data.length()) {
                if (key.equals(data) && valid) {
                    set(false);
                    count--;
                }
            }
            else if (key.length() > data.length() && key.substring(0, data.length()).equals(data))
            {
                if (key.charAt(data.length()) == '0' && left != null)
                    left = left.deleteNode(key.substring(data.length()));
                else if (key.charAt(data.length()) == '1' && right != null)
                    right = right.deleteNode(key.substring(data.length()));
            }

            if (!valid && !data.equals("")) {
                if (right == null) {
                    if (left == null)
                        return null;
                    else {
                        left.data = data + left.data;
                        return left;
                    }
                }
                else if (left == null) {
                    right.data = data + right.data;
                    return right;
                }
            }

            return this;
        }
    }

    /* **************************************************************************************************  */
    /* ********************* PLACE YOUR PRIVATE MEMBERS AND METHODS BELOW: ******************************  */
    /*  ************************************************************************************************** */

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");

    /* ************************************************************************************************** */
    /* ***********************   IMPLEMENT THE FOLLOWING PUBLIC METHODS:   ******************************** */
    /* ************************************************************************************************** */

    /**
     * Simple constructor that will initialize the internals of this.
     */
    public BinaryPatriciaTrie() {
        root = new Node("");
        root.set(false);
        count = 0;
    }

    /**
     * Searches the trie for a given key.
     *
     * @param key The input String key.
     * @return true if and only if key is in the trie, false otherwise.
     */
    public boolean search(String key) {
        if (key != null)
            return root.searchNode(key);
        else
            return false;
    }


    /**
     * Inserts key into the trie.
     *
     * @param key The input String key.
     * @return true if and only if the key was not already in the trie, false otherwise.
     */
    public boolean insert(String key) {
        if (key != null) {
            int curCount = count;
            root = root.insertNode(key);
            return curCount != count;
        }
        else
            return false;
    }

    /**
     * Deletes key from the trie.
     *
     * @param key The String key to be deleted.
     * @return True if and only if key was contained by the trie before we attempted deletion, false otherwise.
     */
    public boolean delete(String key) {
        if (key != null) {
            int curCount = count;
            root = root.deleteNode(key);
            return curCount != count;
        }
        else
            return false;
    }

    /**
     * Queries the trie for emptiness.
     *
     * @return true if and only if {@link #getSize()} == 0, false otherwise.
     */
    public boolean isEmpty() {
        return getSize() == 0;
    }

    /**
     * Returns the number of keys in the tree.
     *
     * @return The number of keys in the tree.
     */
    public int getSize() {
        return count;
    }

    /**
     * <p>Performs an <i>inorder (symmetric) traversal</i> of the Binary Patricia Trie. Remember from lecture that inorder
     * traversal in tries is NOT sorted traversal, unless all the stored keys have the same length. This
     * is of course not required by your implementation, so you should make sure that in your tests you
     * are not expecting this method to return keys in lexicographic order. We put this method in the
     * interface because it helps us test your submission thoroughly and it helps you debug your code! </p>
     *
     * <p>We <b>neither require nor test </b> whether the {@link Iterator} returned by this method is fail-safe or fail-fast.
     * This means that you  do <b>not</b> need to test for thrown {@link java.util.ConcurrentModificationException}s and we do
     * <b>not</b> test your code for the possible occurrence of concurrent modifications.</p>
     *
     * <p>We also assume that the {@link Iterator} is <em>immutable</em>, i,e we do <b>not</b> test for the behavior
     * of {@link Iterator#remove()}. You can handle it any way you want for your own application, yet <b>we</b> will
     * <b>not</b> test for it.</p>
     *
     * @return An {@link Iterator} over the {@link String} keys stored in the trie, exposing the elements in <i>symmetric
     * order</i>.
     */
    public Iterator<String> inorderTraversal() {
        return root.iterate().iterator();
    }


    /**
     * Finds the longest {@link String} stored in the Binary Patricia Trie.
     *
     * @return <p>The longest {@link String} stored in this. If the trie is empty, the empty string &quot;&quot; should be
     * returned. Careful: the empty string &quot;&quot;is <b>not</b> the same string as &quot; &quot;; the latter is a string
     * consisting of a single <b>space character</b>! It is also <b>not the same as the</b> null <b>reference</b>!</p>
     *
     * <p>Ties should be broken in terms of <b>value</b> of the bit string. For example, if our trie contained
     * only the binary strings 01 and 11, <b>11</b> would be the longest string. If our trie contained
     * only 001 and 010, <b>010</b> would be the longest string.</p>
     */
    public String getLongest() {
        if (isEmpty())
            return "";
        else
            return root.longest();
    }

    /**
     * Makes sure that your trie doesn't have splitter nodes with a single child. In a Patricia trie, those nodes should
     * be pruned. Be careful with the implementation of this method, since our tests call it to make sure your deletions work
     * correctly! That is to say, if your deletions work well, but you have made an error in this (far easier) method,
     * you will <b>still</b> not be passing our tests!
     *
     * @return true iff all nodes in the trie either denote stored strings or split into two subtrees, false otherwise.
     */
    public boolean isJunkFree(){
        boolean result = true;
        if (root.left != null)
            result = root.left.junkFree();
        if (root.right != null)
            result = result && root.right.junkFree();

        return result;
        //return root.junkFree();
    }
}
