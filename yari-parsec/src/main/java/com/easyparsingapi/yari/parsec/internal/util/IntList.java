/*
 * Copyright (c) 2025 Easy API
 * Website : https://easyparsingapi.com/
 * GitHub  : https://github.com/Easy-API-Style/yari-framework
 * Contact : easy.api.contact@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easyparsingapi.yari.parsec.internal.util;

import com.easyparsingapi.yari.parsec.internal.annotations.Private;

/**
 * A simple, efficient and dynamic int list.
 * 
 * <p>
 * Not thread-safe.
 */
public final class IntList {
    
    private int[] buf;
    private int len = 0;

    /**
     * Creates a {@code int[]} array containing all elements of this list.
     *
     * @return an array of all int values stored in this list
     */
    public int[] toArray() {
        int[] ret = new int[len];
        for (int i = 0; i < len; i++) {
            ret[i] = buf[i];
        }
        return ret;
    }

    /**
     * Creates an {@link IntList} with the given initial capacity.
     *
     * @param capacity the initial capacity of the internal buffer
     */
    public IntList(int capacity) {
        this.buf = new int[capacity];
    }

    /** Creates an empty {@link IntList} object. */
    public IntList() {
        this(10);
    }

    /**
     * Returns the number of int values stored in this list.
     *
     * @return the current size of this list
     */
    public int size() {
        return len;
    }

    private void checkIndex(int i) {
        if (i < 0 || i >= len) {
            throw new ArrayIndexOutOfBoundsException(i);
        }
    }

    /**
     * Gets the int value at a index {@code i}.
     * 
     * @param i the 0 - based index of the value.
     * @return the int value.
     * @throws ArrayIndexOutOfBoundsException if {@code i &lt; 0 or i >= size()}.
     */
    public int get(int i) {
        checkIndex(i);
        return buf[i];
    }

    /**
     * Sets the value at index {@code i} to {@code val}.
     * 
     * @param i   the 0 - based index.
     * @param val the new value.
     * @return the old value.
     * @throws ArrayIndexOutOfBoundsException if {@code i &lt; 0 or i >= size()}.
     */
    public int set(int i, int val) {
        checkIndex(i);
        int old = buf[i];
        buf[i] = val;
        return old;
    }

    @Private
    static int calcSize(int expectedSize, int factor) {
        int rem = expectedSize % factor;
        return expectedSize / factor * factor + (rem > 0 ? factor : 0);
    }

    /**
     * Ensures that there is at least {@code l} capacity.
     * 
     * @param capacity the minimal capacity.
     */
    public void ensureCapacity(int capacity) {
        if (capacity > buf.length) {
            int factor = buf.length / 2 + 1;
            grow(calcSize(capacity - buf.length, factor));
        }
    }

    private void grow(int l) {
        int[] nbuf = new int[buf.length + l];
        System.arraycopy(buf, 0, nbuf, 0, buf.length);
        buf = nbuf;
    }

    /**
     * Adds {@code i} into the array.
     * 
     * @param i the int value.
     * @return this object.
     */
    public IntList add(int i) {
        ensureCapacity(len + 1);
        buf[len++] = i;
        return this;
    }
    
}
