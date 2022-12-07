package year2022.days;

import year2022.Day2022;

import java.util.*;

public class Day07 extends Day2022 {
    public static class Trie {
        public class TrieNode {
            public HashMap<String, TrieNode> children;
            public boolean isFile;
            public long totalSize;

            public TrieNode() {
                children = new HashMap<>();
                totalSize = 0;
            }

            public boolean hasChild(String item) {
                return children.containsKey(item);
            }

            public TrieNode getChild(String item) {
                return children.get(item);
            }

            public void setChild(String item, TrieNode child) {
                children.put(item, child);
            }
        }

        private TrieNode root;

        public Trie() {
            root = new TrieNode();
        }

        public void insert(Deque<String> path, long fileSize) {
            TrieNode curr = root;
            root.totalSize += fileSize;
            for (String part : path) {
                if (!curr.hasChild(part)) {
                    curr.setChild(part, new TrieNode());
                }

                curr = curr.getChild(part);
                curr.totalSize += fileSize;
            }
        }

        public long getTotalSize(long limit) {
            long size = root.totalSize <= limit ? root.totalSize : 0;
            for (TrieNode child : root.children.values()) {
                size += getTotalSize(child, limit);
            }
            return size;
        }

        public long getCeiling(long limit) {
            return getCeiling(root, Long.MAX_VALUE, limit);
        }

        private long getCeiling(TrieNode curr, long currCeil, long limit) {
            long ceil = currCeil;
            if (curr.totalSize >= limit) {
                if (curr.totalSize - limit < currCeil - limit) {
                    ceil = curr.totalSize;
                }
            }

            for (TrieNode child : curr.children.values()) {
                ceil = Math.min(getCeiling(child, ceil, limit), ceil);
            }
            return ceil;
        }
        private long getTotalSize(TrieNode curr, long limit) {
            long size = 0;
            for (TrieNode child : curr.children.values()) {
                if (child.totalSize <= limit) {
                    size += child.totalSize;
                }
                size += getTotalSize(child, limit);
            }
            return size;
        }
    }

    public Day07() {
        super(7);
    }

    @Override
    public Object solvePart1() {
        Scanner scanner = getInputScanner();

        Trie trie = buildTrie(scanner);
        return trie.getTotalSize(100_000);
    }

    @Override
    public Object solvePart2() {
        Scanner scanner = getInputScanner();

        Trie trie = buildTrie(scanner);
        long totalSpace = 70_000_000;
        long occupied = trie.root.totalSize;
        long required = 30_000_000 - totalSpace + occupied;
        return trie.getCeiling(required);
    }

    private static Trie buildTrie(Scanner scanner) {
        Trie trie = new Trie();
        Deque<String> path = new LinkedList<>();
        while (scanner.hasNextLine()) {
            String marker = scanner.next();
            if ("$".equals(marker)) {
                String cmd = scanner.next();
                if (cmd.equals("cd")) {
                    String dir = scanner.next();
                    if ("..".equals(dir)) {
                        path.removeLast();
                    } else if ("/".equals(dir)) {
                        path.clear();
                        path.offerLast("/");
                    } else {
                        path.offerLast(dir);
                    }
                }
            } else {
                if (!"dir".equals(marker)) {
                    long size = Long.parseLong(marker);
                    trie.insert(path, size);
                }
                scanner.next();
            }
        }
        return trie;
    }
}
