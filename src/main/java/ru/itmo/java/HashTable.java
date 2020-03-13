package ru.itmo.java;

public class HashTable {
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private final double loadFactor;
    private int threshold;
    private Entry[] elements;
    private int size = 0;

    public HashTable(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        }
        if (Double.isNaN(loadFactor) || loadFactor < 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " + loadFactor);
        }
        this.elements = new Entry[initialCapacity];
        this.loadFactor = loadFactor;
        this.threshold = (int) (initialCapacity * loadFactor);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTable() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    Object put(Object key, Object value) {
        int hash = Math.abs(key.hashCode()) % elements.length;

        for (int i = hash; i < elements.length; ++i) {
            if (elements[i] == null || elements[i].deleted && elements[i].key.equals(key)) {
                this.elements[i] = new Entry(key, value);
                this.size++;
                if (this.size > this.threshold) {
                    this.resize();
                }
                return null;
            }
            if (elements[i].key.equals(key)) {
                Object oldValue = this.elements[i].value;
                this.elements[i].value = value;
                return oldValue;
            }
            if (i == elements.length - 1) {
                i = 0;
            }
        }

        return null;
    }

    private void resize() {
        Entry[] oldElements = this.elements;
        this.elements = new Entry[this.elements.length * 2];
        this.threshold = (int) (this.elements.length * loadFactor);
        this.size = 0;
        for (Entry entry : oldElements) {
            if (entry != null && !entry.deleted) {
                put(entry.key, entry.value);
            }
        }
    }

    Object get(Object key) {
        int hash = Math.abs(key.hashCode()) % elements.length;

        for (int i = hash; i < elements.length; ++i) {
            if (elements[i] == null) {
                return null;
            }
            if (elements[i].key.equals(key) && !elements[i].deleted) {
                return elements[i].value;
            }
            if (i == elements.length - 1) {
                i = 0;
            }
        }
        return null;
    }

    Object remove(Object key) {
        int hash = Math.abs(key.hashCode()) % elements.length;

        for (int i = hash; i < elements.length; ++i) {
            if (elements[i] == null) {
                return null;
            }
            if (elements[i].key.equals(key) && !elements[i].deleted) {
                this.elements[i].deleted = true;
                this.size--;
                return this.elements[i].value;
            }
            if (i == elements.length - 1) {
                i = 0;
            }
        }
        return null;
    }

    int size() {
        return size;
    }

    static class Entry {
        private Object key, value;
        private boolean deleted = false;

        Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

    }

}
