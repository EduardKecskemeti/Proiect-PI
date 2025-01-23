
package datastructures;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Trie {
    private static final int ALPHABET_SIZE = 26;

    private class TrieNode {
        TrieNode[] children = new TrieNode[ALPHABET_SIZE];
        boolean isEndOfWord;
        String originalWord;
    }

    private final TrieNode root;
    private final Map<String, String> originalWordsMap;

    public Trie() {
        root = new TrieNode();
        originalWordsMap = new HashMap<>();
    }

    public void insert(String word) {
        String lowerCaseWord = word.toLowerCase();
        originalWordsMap.put(lowerCaseWord, word);
        TrieNode node = root;
        for (char c : lowerCaseWord.toCharArray()) {
            if (c < 'a' || c > 'z') {
                continue;
            }
            int index = c - 'a';
            if (node.children[index] == null) {
                node.children[index] = new TrieNode();
            }
            node = node.children[index];
        }
        node.isEndOfWord = true;
        node.originalWord = word;
    }

    public List<String> search(String query) {
        query = query.toLowerCase();
        List<String> results = new ArrayList<>();
        searchHelper(root, new StringBuilder(), query, results);
        return results;
    }

    private void searchHelper(TrieNode node, StringBuilder currentWord, String query, List<String> results) {
        if (node.isEndOfWord && currentWord.toString().contains(query)) {
            results.add(node.originalWord);
        }
        for (char c = 'a'; c <= 'z'; c++) {
            int index = c - 'a';
            if (node.children[index] != null) {
                currentWord.append(c);
                searchHelper(node.children[index], currentWord, query, results);
                currentWord.deleteCharAt(currentWord.length() - 1);
            }
        }
    }
}