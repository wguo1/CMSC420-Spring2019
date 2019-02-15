package projects.graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>{@link SparseAdjacencyMatrixGraph} is a {@link Graph} implemented as a <b>sparse adjacency matrix</b>, i.e a
 * linked list of the matrix's non-null (or, in this case, non-zero) elements.</p>
 *
 * <p>Inserting a new node has no influence in the inner storage of this class, because the only storage it provides
 * is for <b>edges</b>. This does not mean that you should neglect to store information about a new node having
 * been inserted: it just means that a new node needs to be connected to an existing one if it is to appear as a new
 * node <b>in the list</b>. Think about the (dense) adjacency matrix representation: there, if we add a new node,
 * we have to pay O(V^2) to re-allocate the space of the old array to the new array and add a new row and column
 * of zeroes. There is no <b>novel</b> assignment in the matrix! In the case of the sparsified representation, the inner
 * representation does not have to change at all! But you will <b>still</b> need to make note of the fact that a new node was
 * inserted. After all, we want {@link #getNumNodes()} to work correctly, don't we? </p>
 *
 *
 *<p>{@link #edgeBetween(int, int)}, {@link #deleteEdge(int, int)}, {@link #getEdgeWeight(int, int)} and
 * {@link #getNeighbors(int)} all run in O(E) time.</p>
 *
 * <p>The space occupied by an instance of {@link SparseAdjacencyMatrixGraph} is O(E). </p>
 *
 * @author --- YOUR NAME HERE! ---
 *
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class SparseAdjacencyMatrixGraph extends Graph {

    /* ******************************************************************************* */
    /* THE FOLLOWING CLASS DECLARATION AND DATA FIELD MAKE UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!                           */
    /* ******************************************************************************  */

    private class EdgeData {
        int source;
        int dest;
        int weight;

        /* A three-arg constructor for easy initialization of fields */
        EdgeData(int source, int dest, int weight){
            this.source = source;
            this.dest = dest;
            this.weight = weight;
        }

        @Override
        public boolean equals(Object o){
            if(o == null || o.getClass() != this.getClass())
                return  false;
            EdgeData oCasted = null;
            try {
                oCasted = (EdgeData)o;
            } catch(ClassCastException ignored){
                return false;
            }
            return  (oCasted.source == source) &&
                    (oCasted.dest == dest) &&
                    (oCasted.weight == weight);
        }
    }

    private List<EdgeData> list; // This is a standard and safe usage of Java's generics.

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */

    private int index = -1;

    private boolean nodeExists(int source, int dest) {
        return source >= 0 && source <= index && dest >= 0 && dest <= index;
    }

    private EdgeData search(int source, int dest){
        EdgeData temp = null;

        if (nodeExists(source, dest))
        {
            for (EdgeData edge : list)
            {
                if (edge.source >= source)
                {
                    if (edge.source == source)
                    {
                        if (edge.dest >= dest)
                        {
                            if (edge.dest == dest)
                                temp = edge;
                            break;
                        }
                    }
                    else break;
                }
            }
        }

        return temp;
    }

    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link SparseAdjacencyMatrixGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public SparseAdjacencyMatrixGraph(){
        list = new LinkedList<EdgeData>();
    }

    @Override
    public void addNode() {
        index++;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        if (weight < 0 || weight > INFINITY)
            throw new RuntimeException();
        else if (weight == 0)
            deleteEdge(source, dest);
        else if (nodeExists(source, dest))
        {
            int cur = 0;
            for (EdgeData edge : list)
            {
                if (edge.source >= source)
                {
                    if (edge.source == source)
                    {
                        if (edge.dest >= dest)
                        {
                            if (edge.dest == dest)
                                edge.weight = weight;
                            else
                                list.add(cur, new EdgeData(source, dest, weight));

                            break;
                        }
                    }
                    else
                    {
                        list.add(cur, new EdgeData(source, dest, weight));
                        break;
                    }
                }

                cur++;
            }

            if (cur == list.size())
                list.add(new EdgeData(source, dest, weight));
        }
    }

    @Override
    public void deleteEdge(int source, int dest) {
        EdgeData temp = search(source, dest);

        if (temp != null)
            list.remove(temp);
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        return search(source, dest) != null;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        EdgeData temp = search(source, dest);

        if (temp != null)
            return temp.weight;
        else
            return 0;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> neighbors = new HashSet<Integer>(0);

        for (EdgeData edge : list)
        {
            if (edge.source == node)
                neighbors.add(edge.dest);
            else if (edge.source > node)
                break;
        }

        return neighbors;
    }

    @Override
    public int getNumNodes() {
        return index + 1;
    }

    @Override
    public int getNumEdges() {
        return list.size();
    }

    @Override
    public void clear() {
        list = new LinkedList<EdgeData>();
        index = -1;
    }

    /* Methods specific to this class follow. */

    /**
     * Creates the dense representation of the matrix, corresponding to an instance of {@link AdjacencyMatrixGraph}. The adjacency
     * matrix will occupy O(V^2) space. Since every node in the list that makes up the sparsified representation containsKVPair a triple (i, j, weight), simply
     * looping through the list in O(E) and assigning the corresponding cell of the matrix to the appropriate weight is sufficient. For this
     * reason, it is not important whether the list is sorted in any way, <b>except for the special case of having been immediately created
     * by {@link AdjacencyMatrixGraph#toSparseAdjacencyMatrixGraph()}, where we want you to create the adjacency list following row-major
     * order.</b> However, because of this simple iterative algorithm we just explained, addEdge() in this class is an O(1) operation: we can just insert
     * a new node as the "head" of the list, and we are done.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *  <b>cannot</b> implement it with the line of code toAdjacencyListGraph().toAdjacencyMatrixGraph().
     *
     * @return An {@link AdjacencyMatrixGraph} instance.
     */
    public AdjacencyMatrixGraph toAdjacencyMatrixGraph(){
        AdjacencyMatrixGraph matrix = new AdjacencyMatrixGraph();

        for (int i = 0; i < index; i++)
            matrix.addNode();

        for (EdgeData edge : list)
            matrix.addEdge(edge.source, edge.dest, edge.weight);

        return matrix;
    }

    /**
     * Transforms this to an {@link AdjacencyListGraph}instance.  Remember that an {@link AdjacencyListGraph}
     * is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *     <b>cannot</b> implement it with the line of code toAdjacencyMatixGraph().toAdjacencyListGraph().
     *
     * @return An {@link AdjacencyListGraph} instance.
     */
    public AdjacencyListGraph toAdjacencyListGraph(){
        AdjacencyListGraph newList = new AdjacencyListGraph();

        for (int i = 0; i < index; i++)
            newList.addNode();

        for (EdgeData edge : list)
            newList.addEdge(edge.source, edge.dest, edge.weight);

        return newList;
    }
}