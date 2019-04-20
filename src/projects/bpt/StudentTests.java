package projects.bpt;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

/**
 * A jUnit test suite for {@link BinaryPatriciaTrie}. You should extend this test suite with more tests of your own!
 *
 * @author --- YOUR NAME HERE! ----.
 */
public class StudentTests {


    @Test public void testEmptyTrie() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue("Trie should be empty",trie.isEmpty());
        assertTrue("Trie size should be 0",trie.getSize() == 0);

        assertFalse("No string inserted so search should fail", trie.search("0101"));
    }

    @Test public void testFewInsertionsWithSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        assertTrue("String should be inserted successfully",trie.insert("00000"));
        assertTrue("String should be inserted successfully",trie.insert("00011"));
        assertFalse("Search should fail as string does not exist",trie.search("000"));
    }

    @Test public void testInsertionsWithSearch() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        assertTrue(trie.isJunkFree());
        assertTrue("String should be inserted successfully",trie.insert("00100"));
        assertTrue(trie.isJunkFree());
        assertTrue("String should be inserted successfully",trie.insert("001"));
        assertTrue("String should be inserted successfully",trie.insert("1101"));
        assertTrue("String should be inserted successfully",trie.insert("1111000"));
        assertTrue("String should be inserted successfully",trie.insert("11"));
        assertTrue("String should be inserted successfully",trie.insert("111100110001"));
        assertTrue(trie.insert("111"));
        assertTrue(trie.insert("010"));
        assertTrue(trie.insert("010010"));
        assertTrue(trie.insert("100000000001"));
        assertTrue(trie.insert("1000000000"));
        assertTrue(trie.insert("010111"));

        assertTrue(trie.search("00100"));
        assertTrue(trie.search("001"));
        assertTrue(trie.search("11"));
        assertTrue(trie.search("1101"));
        assertTrue(trie.search("1111000"));
        assertTrue(trie.search("111100110001"));
        assertTrue(trie.search("1000000000"));
        assertTrue(trie.search("010010"));
        assertTrue(trie.search("111"));
        assertTrue(trie.delete("010010"));
        assertTrue(trie.search("010111"));
        assertTrue(trie.delete("1101"));
        assertFalse(trie.search("1101"));
        assertTrue(trie.search("111"));

        assertTrue("String should be inserted successfully",trie.insert("11011"));
        assertTrue(trie.search("11011"));
        assertTrue(trie.delete("11011"));
        assertTrue(trie.isJunkFree());

        assertTrue("String should be inserted successfully",trie.insert("111100"));
        assertTrue(trie.search("111100"));
        assertTrue(trie.delete("111100"));
        assertTrue(trie.isJunkFree());

        assertTrue("String should be inserted successfully",trie.insert("0010"));
        assertTrue(trie.search("0010"));
        assertTrue(trie.delete("0010"));
        assertTrue(trie.isJunkFree());

        assertTrue("String should be inserted successfully",trie.insert("111100110010"));
        assertTrue(trie.search("111100110010"));
        assertTrue(trie.delete("111100110010"));
        assertTrue(trie.isJunkFree());

        assertFalse(trie.delete("001110"));
        assertTrue(trie.isJunkFree());
        assertFalse(trie.delete("110"));
        assertFalse(trie.delete("000"));

        assertTrue(trie.delete("11"));
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("11"));
        assertTrue(trie.search("11"));

        assertTrue(trie.delete("001"));
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("001"));
        assertTrue(trie.search("001"));

        assertTrue(trie.delete("111100110001"));
        assertTrue(trie.isJunkFree());
        assertTrue(trie.insert("111100110001"));
        assertTrue(trie.search("111100110001"));

        assertTrue(trie.search("001"));
        assertTrue(trie.search("00100"));
        assertTrue(trie.search("11"));
        //assertTrue(trie.search("1101"));
        assertFalse(trie.search("111100"));
        assertTrue(trie.search("1111000"));
        assertTrue(trie.search("111100110001"));
        assertFalse(trie.search(""));

        assertEquals("111100110001", trie.getLongest());

        /*Iterator<String> list = trie.inorderTraversal();

        while (list.hasNext())
            System.out.println(list.next());*/
    }


    //testing isEmpty function
    @Test public void testFewInsertionsWithDeletion() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        trie.insert("000");
        assertEquals("After inserting one string, the trie should report one string stored.", 1, trie.getSize());
        trie.insert("001");
        assertEquals("After inserting two strings, the trie should report two strings stored.", 2, trie.getSize());
        trie.insert("011");
        assertEquals("After inserting three strings, the trie should report three strings stored.", 3, trie.getSize());
        trie.insert("1001");
        assertEquals("After inserting four strings, the trie should report four strings stored.", 4, trie.getSize());
        trie.insert("1");

        assertFalse("After inserting five strings, the trie should not be considered empty!", trie.isEmpty());
        assertEquals("After inserting five strings, the trie should report five strings stored.", 5, trie.getSize());

        assertTrue(trie.search("011"));
        //trie.delete("0"); // Failed deletion; should affect exactly nothing.
        assertEquals(5, trie.getSize());
        assertTrue("After inserting five strings and requesting the deletion of one not in the trie, the trie had some junk in it!",
                trie.isJunkFree());

        trie.delete("011"); // Successful deletion
        assertEquals("After inserting five strings and deleting one of them, the trie should report 4 strings.", 4, trie.getSize());
        assertTrue("After inserting five strings and deleting one of them, the trie had some junk in it!",
                trie.isJunkFree());
    }

    @Test public void test5() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();

        assertTrue(trie.insert("1101"));
        assertTrue(trie.insert("010"));
        assertTrue(trie.insert("11101"));
        assertTrue(trie.insert("011110"));
        assertTrue(trie.insert("01110"));
        assertTrue(trie.insert("111"));



        assertTrue(trie.search("1101"));
        assertTrue(trie.search("010"));
        assertTrue(trie.search("11101"));
        assertTrue(trie.search("011110"));
        assertTrue(trie.search("01110"));
        assertTrue(trie.search("111"));

        assertTrue(trie.delete("1101"));
        assertTrue(trie.delete("010"));
        assertTrue(trie.delete("11101"));
        assertTrue(trie.delete("011110"));
        assertTrue(trie.delete("01110"));
        assertTrue(trie.delete("111"));

        assertTrue(trie.insert("0"));
    }

    @Test public void test6() {
        BinaryPatriciaTrie trie = new BinaryPatriciaTrie();
        assertTrue(trie.insert("1111111"));
        assertTrue(trie.insert("1111"));
        assertTrue(trie.insert("1"));
        assertTrue(trie.insert("0000"));
        assertTrue(trie.insert("010"));
        assertTrue(trie.insert("000"));
        assertTrue(trie.insert("00000001"));
        assertTrue(trie.insert("11"));
        assertTrue(trie.insert("111"));
        assertTrue(trie.insert("1110"));


        assertTrue(trie.search("1111111"));
        assertTrue(trie.search("1111"));
        assertTrue(trie.search("1"));
        assertTrue(trie.search("0000"));
        assertTrue(trie.search("010"));
        assertTrue(trie.search("000"));
        assertTrue(trie.search("00000001"));
        assertTrue(trie.search("11"));
        assertTrue(trie.search("111"));
        assertTrue(trie.search("1110"));

        assertTrue(trie.delete("111"));
        assertTrue(trie.search("1110"));
        assertTrue(trie.search("1111"));

        Iterator<String> list = trie.inorderTraversal();

        while (list.hasNext())
            System.out.println(list.next());
    }
}
