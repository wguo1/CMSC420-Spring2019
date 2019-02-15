package projects.graph;

import projects.graph.utils.Neighbor;
import projects.graph.utils.NeighborList;

import java.util.HashSet;
import java.util.Set;
/**
 * <p>{@link AdjacencyListGraph} is a {@link Graph} implemented as an adjacency list, i.e a one-dimensional array of linked lists,
 * where A(i) is a linked list containing the neighbors of node i and the corresponding edges' weights. <b>The neighbors of a given node are defined as the nodes it  points to</b> (if any). </p>
 *
 * <p>Other implementations besides linked lists are possible (e.g BSTs over the weight of the edge), yet for this project we
 * will keep it simple and stick to that basic implementation. One of its advantages is that, because the lists do not need
 * to be sorted in any way, the insertion of a new edge is a O(1) operation (find the list corresponding to the source node in O(1)
 * and add the new list node up front.</p>
 *
 * @author --- YOUR NAME HERE! ---
 *
 * @see Graph
 * @see AdjacencyMatrixGraph
 * @see SparseAdjacencyMatrixGraph
 * @see NeighborList
 */
public class AdjacencyListGraph extends Graph {

    /* *********************************************************** */
    /* THE FOLLOWING DATA FIELD MAKES UP THE INNER REPRESENTATION */
    /* OF YOUR GRAPH. YOU SHOULD NOT CHANGE THIS DATA FIELD!      */
    /* *********************************************************  */

    private NeighborList[] list;

    /* ***************************************************** */
    /* PLACE ANY EXTRA PRIVATE DATA MEMBERS OR METHODS HERE: */
    /* ***************************************************** */
    private boolean nodeExists(int source, int dest) {
        return source >= 0 && source < list.length && dest >= 0 && dest < list.length;
    }
    /* ************************************************** */
    /* IMPLEMENT THE FOLLOWING PUBLIC METHODS. MAKE SURE  */
    /* YOU ERASE THE LINES THAT THROW EXCEPTIONS.         */
    /* ************************************************** */

    /**
     * A default (no-arg) constructor for {@link AdjacencyListGraph} <b>should</b> exist,
     * even if you don't do anything with it.
     */
    public AdjacencyListGraph(){
        list = new NeighborList[0];
    }

    @Override
    public void addNode() {
        NeighborList[] temp = new NeighborList[list.length + 1];
        System.arraycopy(list, 0, temp, 0, list.length);
        temp[list.length] = new NeighborList();
        list = temp;
    }

    @Override
    public void addEdge(int source, int dest, int weight) {
        if (weight < 0 || weight > INFINITY)
            throw new RuntimeException();
        else if (weight == 0)
            deleteEdge(source, dest);
        else if (nodeExists(source, dest))
        {
            if (list[source].containsNeighbor(dest))
                list[source].setWeight(dest, weight);
            else
                list[source].addBack(dest, weight);
        }
    }


    @Override
    public void deleteEdge(int source, int dest) {
        if (nodeExists(source, dest))
            if (list[source].containsNeighbor(dest))
                list[source].remove(dest);
    }

    @Override
    public boolean edgeBetween(int source, int dest) {
        boolean exists = false;

        if (nodeExists(source, dest))
            if (list[source].containsNeighbor(dest))
                exists = true;

        return exists;
    }

    @Override
    public int getEdgeWeight(int source, int dest) {
        int weight = 0;

        if (nodeExists(source, dest))
            if (list[source].containsNeighbor(dest))
                weight = list[source].getWeight(dest);

        return weight;
    }

    @Override
    public Set<Integer> getNeighbors(int node) {
        Set<Integer> neighbors = new HashSet<>(0);

        if (node >= list.length || node < 0)
            neighbors = null;
        else
            for (Neighbor neighbor : list[node])
                neighbors.add(neighbor.getNode());

        return neighbors;
    }

    @Override
    public int getNumNodes() {
        return list.length;
    }

    @Override
    public int getNumEdges() {
        int total = 0;

        for (NeighborList neighbors : list)
            total += neighbors.getCount();

        return total;
    }

    @Override
    public void clear() {
        list = new NeighborList[0];
    }

    /* Methods specific to this class follow. */

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     *   <b>cannot</b> implement it with the line of code toSparseAdjacencyMatrixGraph().toAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public AdjacencyMatrixGraph toAdjacencyMatrixGraph(){
        AdjacencyMatrixGraph matrix = new AdjacencyMatrixGraph();
        int cur = 0;

        for (NeighborList neighbors : list){
            matrix.addNode();
            for (Neighbor neighbor : neighbors)
                matrix.addEdge(cur, neighbor.getNode(), neighbor.getWeight());

            cur++;
        }

        return matrix;
    }

    /**
     * Transforms this into an instance of {@link AdjacencyMatrixGraph}. This is an O(E) operation.
     *
     * You are <b>not</b> allowed to implement this method by using other transformation methods. For example, you
     * <b>cannot</b> implement it with the line of code toAdjacencyMatrixGraph().toSparseAdjacencyMatrixGraph().
     *
     * @return An instance of {@link AdjacencyMatrixGraph}.
     */
    public SparseAdjacencyMatrixGraph toSparseAdjacencyMatrixGraph(){
        SparseAdjacencyMatrixGraph sGraph = new SparseAdjacencyMatrixGraph();
        int count = 0;

        for (int i = 0; i < list.length; i++)
            sGraph.addNode();

        for (NeighborList neighbors : list)
        {
            for (Neighbor neighbor : neighbors)
                sGraph.addEdge(count, neighbor.getNode(), neighbor.getWeight());

            count++;
        }

        return sGraph;
    }
}