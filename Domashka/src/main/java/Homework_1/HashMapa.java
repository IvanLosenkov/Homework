package Homework_1;

import java.util.Objects;

public class HashMapa<K, V> {

    private Node<K, V>[] nodes;
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    private int size;

    public HashMapa() {
        nodes = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new NullPointerException("Ключ не должен быть null");
        }
        if (size >= nodes.length * LOAD_FACTOR) {
            resize();
        }

        int index = getIndex(key);
        Node<K, V> node = nodes[index];

        while (node != null) {
            if (node.key.equals(key)) {
                node.value = value;
                return;
            }
            node = node.next;
        }

        nodes[index] = new Node<>(key, value, nodes[index]);
        size++;
    }

    public V get(K key) {
        if (key == null) {
            throw new NullPointerException("Ключ не должен быть null");
        }

        int index = getIndex(key);
        Node<K, V> node = nodes[index];

        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException("Ключ не должен быть null");
        }

        int index = getIndex(key);
        Node<K, V> node = nodes[index];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.key.equals(key)) {
                if (prev == null) {
                    nodes[index] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return node.value;
            }
            prev = node;
            node = node.next;
        }
        return null;
    }

    private int getIndex(K key) {
        return (key.hashCode() & 0x7fffffff) % nodes.length;
    }

    private void resize() {
        Node<K, V>[] oldTable = nodes;
        nodes = (Node<K, V>[]) new Node[oldTable.length * 2];
        size = 0;

        for (Node<K, V> node : oldTable) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Node<K, V> node : nodes) {
            while (node != null) {
                sb.append(" ").append(node.key).append(" - ").append(node.value).append("\n");
                node = node.next;
            }
        }
        return sb.toString();
    }
}