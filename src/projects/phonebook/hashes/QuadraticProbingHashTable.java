package projects.phonebook.hashes;

import projects.phonebook.utils.KVPair;
import projects.phonebook.utils.PrimeGenerator;

import java.util.HashSet;

/**
 * <p>{@link QuadraticProbingHashTable} is an Openly Addressed {@link HashTable} which uses <b>Quadratic
 * Probing</b> as its collision resolution strategy. Quadratic Probing differs from <b>Linear</b> Probing
 * in that collisions are resolved by taking &quot; jumps &quot; on the hash table, the length of which
 * determined by an increasing polynomial factor. For example, during a key insertion which generates
 * several collisions, the first collision will be resolved by moving 1^2 + 1 = 2 positions over from
 * the originally hashed address (like Linear Probing), the second one will be resolved by moving
 * 2^2 + 2= 6 positions over from our hashed address, the third one by moving 3^2 + 3 = 12 positions over, etc.
 * </p>
 *
 * <p>By using this collision resolution technique, {@link QuadraticProbingHashTable} aims to get rid of the
 * &quot;key clustering &quot; problem that {@link LinearProbingHashTable} suffers from. Leaving more
 * space in between memory probes allows other keys to be inserted without many collisions. The tradeoff
 * is that, in doing so, {@link QuadraticProbingHashTable} sacrifices <em>cache locality</em>.</p>
 *
 * @author YOUR NAME HERE!
 *
 * @see HashTable
 * @see SeparateChainingHashTable
 * @see LinearProbingHashTable
 * @see CollisionResolver
 */
public class QuadraticProbingHashTable implements HashTable{

    /* *******************************************************************/
    /* ***** PRIVATE FIELDS / METHODS PROVIDED TO YOU: DO NOT EDIT! ******/
    /* ****************************************************** ***********/

    private static final RuntimeException UNIMPL_METHOD = new RuntimeException("Implement this method!");
    private KVPair[] table;
    private PrimeGenerator primeGenerator;
    private int count = 0;
    private int hash(String key){
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    /*  YOU SHOULD ALSO IMPLEMENT THE FOLLOWING METHOD ACCORDING TO THE SPECS
     * PROVIDED IN THE PROJECT WRITEUP, BUT KEEP IT PRIVATE!  */

    private void enlarge(){
        KVPair[] tester = table;
        table = new KVPair[primeGenerator.getNextPrime()];
        for (int i = 0; i < tester.length; i++)
        {
            if (tester[i] != null)
            {
                int oCode = hash(tester[i].getKey());
                int hashCode = hash(tester[i].getKey());
                int jump = 1;
                while (table[hashCode] != null)
                {
                    jump++;
                    hashCode = (oCode + (jump - 1) + (jump - 1) * (jump - 1)) % table.length;
                }

                table[hashCode] = tester[i];
            }
        }
    }


    /* ******************/
    /*  PUBLIC METHODS: */
    /* ******************/

    /**
     *  Default constructor. Initializes the internal storage with a size equal to the default of {@link PrimeGenerator}.
     *  This constructor is <b>given to you: DO NOT EDIT IT.</b>
     */
    public QuadraticProbingHashTable(){
        primeGenerator = new PrimeGenerator();
        table = new KVPair[primeGenerator.getCurrPrime()];
        count = 0;
    }

    /**
     * Inserts the pair &lt;key, value&gt; into this. The container should <b>not</b> allow for null
     * keys and values, and we <b>will</b> test if you are throwing a {@link IllegalArgumentException} from your code
     * if this method is given null arguments! It is important that we establish that no null entries
     * can exist in our database because the semantics of {@link #get(String)} and {@link #remove(String)} are that they
     * return null if, and only if, their key parameter is null. This method is expected to run in <em>amortized
     * constant time</em>.
     *
     * Instances of {@link QuadraticProbingHashTable} will follow the writeup's guidelines about how to internally resize
     * the hash table when the capacity exceeds 50&#37;
     * @param key The record's key.
     * @throws IllegalArgumentException if either argument is null.
     */
    @Override
    public void put(String key, String value) {
        if (key != null && value != null) {
            if (count++ > new Double(table.length) / 2.0)
                enlarge();

            int oCode = hash(key);
            int hashCode = hash(key);
            int jump = 1;
            while (table[hashCode] != null)
            {
                jump++;
                hashCode = (oCode + (jump - 1) + (jump - 1) * (jump - 1)) % table.length;
            }

            table[hashCode] = new KVPair(key, value);
        }
    }


    @Override
    public String get(String key) {
        if (key != null) {
            int oCode = hash(key);
            int hashCode = hash(key);
            int jump = 1;
            HashSet<String> visited = new HashSet<>();
            while (visited.size() <= count && table[hashCode] != null && !table[hashCode].getKey().equals(key))
            {
                visited.add(table[hashCode].getKey());
                jump++;
                hashCode = oCode + (jump - 1) + (jump - 1) * (jump - 1) % table.length;
            }

            if (table[hashCode] != null && table[hashCode].getKey().equals(key))
                return table[hashCode].getValue();
        }

        return null;
    }


    /**
     * <b>Return</b> and <b>remove</b> the value associated with key in the {@link HashTable}. If key does not exist in the database
     * or if key = null, this method returns null. This method is expected to run in <em>amortized constant time</em>.
     *
     * @param key The key to search for.
     * @return The associated value if key is non-null <b>and</b> exists in our database, null
     * otherwise.
     */
    @Override
    public String remove(String key) {
        if (key != null) {
            int oCode = hash(key);
            int hashCode = hash(key);
            int jump = 1;
            HashSet<String> visited = new HashSet<>();
            while (visited.size() <= count && table[hashCode] != null &&
                    !table[hashCode].getKey().equals(key))
            {
                visited.add(table[hashCode].getKey());
                jump++;
                hashCode = oCode + (jump - 1) + (jump - 1) * (jump - 1) % table.length;
            }

            if (table[hashCode] != null && table[hashCode].getKey().equals(key))
            {
                String value = table[hashCode].getValue();
                count--;
                table[hashCode] = null;
                return value;
            }
        }

        return null;
    }


    @Override
    public boolean containsKey(String key) {
        boolean returner = false;

        if (key != null) {
            int oCode = hash(key);
            int hashCode = hash(key);
            int jump = 1;
            HashSet<String> visited = new HashSet<>();
            while (visited.size() <= count && !table[hashCode].getKey().equals(key))
            {
                visited.add(table[hashCode].getKey());
                jump++;
                hashCode = oCode + (jump - 1) + (jump - 1) * (jump - 1) % table.length;
            }

            returner = table[hashCode].getKey().equals(key);
        }

        return returner;
    }

    @Override
    public boolean containsValue(String value) {
        boolean returner = false;

        if (value != null) {
            int oCode = hash(value);
            int hashCode = hash(value);
            int jump = 1;
            HashSet<String> visited = new HashSet<>();
            while (visited.size() <= count && !table[hashCode].getValue().equals(value))
            {
                visited.add(table[hashCode].getKey());
                jump++;
                hashCode = oCode + (jump - 1) + (jump - 1) * (jump - 1) % table.length;
            }

            returner = table[hashCode].getKey().equals(value);
        }

        return returner;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public int capacity() {
        return table.length;
    }
}