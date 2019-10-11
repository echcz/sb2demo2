package com.github.echcz.sb2demo2.model;

import java.util.Enumeration;

public class SingleEnumeration<E> implements Enumeration<E> {
    private E element;
    private boolean flag;

    public SingleEnumeration(E element) {
        this.element = element;
        flag = true;
    }
    @Override
    public boolean hasMoreElements() {
        return flag;
    }

    @Override
    public E nextElement() {
        flag = false;
        return element;
    }
}
