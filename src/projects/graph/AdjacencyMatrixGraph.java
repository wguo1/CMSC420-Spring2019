package projects.graph;

import java.util.HashSet;
import java.util.Set;

/**
 * <p>{@link AdjacencyMatrixGraph} is a {@link Graph} implemented as an <b>adjacency matrix</b>. An adjacency matrix
 * is a V x V matrix where M(i, j) is the weight of the edge from i to j. If there is no edge between i and j,
 * the weight should be zero. </p>
 *
 * <p>Adjacency matrices answer {@link #edgeBetween(int, int)} in O(1) time. Insertion and deletion of edges, as well
 * as retrieval of the weight of a given edge  are all O(1) operations as well. Retrieval of all neighbors of a given node
 * happens in O(V) time. </p>
 *
 * <p>The main drawbacks of adjacency matrices are: </p>
 *  <ol>
 *      <li>They occupy O(V^2) <b>contiguous</b> memory space, which for sparse graphs can be a significant memory footprint. </li>
 *      <li>addNode() runs in O(V^2) time, since new array storage needs to be allocated for the extra row and column,
 *      and the old data need be copied to the new array. </li>
 *  </ol>
 *
 * @author --- YOUR NAME HERE! ---
 * @see Graph
 * @see SparseAdjacencyMatrixGraph
 * @see AdjacencyListGraph
 */
public class AdjacencyMatrixGraph extends Graph {

    /* ****************************************************** */
    /* THE FOLLOWING DATA FIELD IS THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD! */
    /* ******************************************************  */

    private int[][] matrix;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */
    private boolean nodeExists(int source, int dest) {
        return source >= 0 && source < matrix.length && dest >= 0 && dest < matrix.length;
    }
    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyMatrixGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyMatrixGraph(){
        matrix = new int[0][0];
    }

    @Override
    public void addNode() {
        int[][] newMatrix = new int[matrix.length + 1][matrix.length + 1];

        for (int i = 0; i < matrix.length; i++)
            System.arraycopy(matrix[i], 0, newMatrix[i], 0, matrix.length);

        matrix = newMatrix;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        // I throw an AssertionError if either node isn't within parameters. Behavior open to implementation according to docs.
        if (weight < 0 || weight > INFINITY)
            throw new RuntimeException();
        else if (weight == 0)
            deleteEdge(source, dest);
        else if (nodeExists(source, dest))
            matrix[source][dest] = weight;
    }

    @Override
    public void deleteEdge(int source, int dest) {
        if (nodeExists(source, dest))
            matrix[source][dest] = 0;
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        boolean found = false;
        if (nodeExists(source, dest))
            if (matrix[source][dest] != 0)
                found = true;

        return found;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        int weight = 0;
        if (nodeExists(source, dest))
            weight = matrix[source][dest];

        return weight;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> neighbors = new HashSet<>(0);

        if (node >= matrix.length || node < 0)
            neighbors = null;
        else
            for (int i = 0; i < matrix.length; i++)
                if (matrix[node][i] != 0)
                    neighbors.add(i);

        return neighbors;
    }

    @Override
    public int getNumNodes() {
        return matrix.length;
    }

    @Override
    public int getNumEdges() {
        int edges = 0;

        for (int i = 0; i < matrix.length; i++)
            for (int k = 0; k < matrix.length; k++)
                if (matrix[i][k] != 0)
                    edges++;

        return edges;
    }

    @Override
    public void clear() {
        matrix = new int[0][0];
    }


    /* Methods specific to this class follow. */

    /**
     * Returns a sparsified representation of the adjacency matrix, i.e a linked list of its non-null elements (non-zero,
     * in this case) and their coordinates. The matrix should be scanned in <b>row-major order</b> to populate
     * the elements of the list (<b>and we test for proper element insertion order!</b>).
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyListGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return A {@link SparseAdjacencyMatrixGraph} instance.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph sGraph = new SparseAdjacencyMatrixGraph();

        for (int i = 0; i < matrix.length; i++)
            sGraph.addNode();

        for (int i = 0; i < matrix.length; i++)
            for (int k = 0; k < matrix.length; k++)
                if (matrix[i][k] != 0)
                    sGraph.addEdge(i, k, matrix[i][k]);

        return sGraph;
    }

    /**
     * Returns a representation of the {@link Graph} as an {@link AdjacencyListGraph}. Remember that an {@link AdjacencyListGraph}
     * is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.  Remember that an
     * {@link AdjacencyListGraph} is implemented as an array of linked lists, where A(i) is a linked list containing the neighbors of node i.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *    <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyListGraph().
     *
     * @return  An {@link AdjacencyListGraph} instance.
     */
    public AdjacencyListGraph toAdjacencyListGraph(){
        AdjacencyListGraph list = new AdjacencyListGraph();

        for (int i = 0; i < matrix.length; i++)
        {
            list.addNode();

            for (int k = 0; k < matrix.length; k++)
                if (matrix[i][k] != 0)
                    list.addEdge(i, k, matrix[i][k]);

        }

        return list;
    }
}