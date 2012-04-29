/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import com.nestor.shared.Description;

public final class CheckedContainer<E extends Description> implements Serializable, Iterable<E>
{
    private static final long serialVersionUID = 2928996982350167471L;

    private final String elementDescription;

    private final boolean caseSensitive;

    private HashMap<String, E> elements = new HashMap<String, E>();

    public CheckedContainer(String elementDescription, boolean caseSensitive)
    {
        this.elementDescription = elementDescription;
        this.caseSensitive = caseSensitive;
    }

    public void add(String identifier, E element) throws RedeclarationException
    {
        if (!this.caseSensitive)
            identifier = identifier.toUpperCase();
        if (this.elements.containsKey(identifier))
            throw new RedeclarationException(this.elementDescription, element);
        this.elements.put(identifier, element);
    }

    /**
     * To use when identifier is assuer to not exist (ignores exception).
     * 
     * @param identifier
     * @param element
     */
    public void addUnchecked(String identifier, E element)
    {
        if (!this.caseSensitive)
            identifier = identifier.toUpperCase();
        if (!this.elements.containsKey(identifier))
            this.elements.put(identifier, element);
    }

    public boolean contains(String identifier)
    {
        if (!this.caseSensitive)
            identifier = identifier.toUpperCase();
        return this.elements.containsKey(identifier);
    }

    public E get(String identifier) throws UndeclaredIdentifierException
    {
        if (!this.caseSensitive)
            identifier = identifier.toUpperCase();
        if (!this.elements.containsKey(identifier))
            throw new UndeclaredIdentifierException(this.elementDescription);
        return this.elements.get(identifier);
    }

    public E getUnchecked(String identifier)
    {
        if (!this.caseSensitive)
            identifier = identifier.toUpperCase();
        return this.elements.get(identifier);
    }

    public Iterator<E> iterator()
    {
        final Iterator<E> valueIterator = this.elements.values().iterator();
        return new Iterator<E>()
        {
            private Iterator<E> iterator;
            {
                this.iterator = valueIterator;
            }

            public boolean hasNext()
            {
                return this.iterator.hasNext();
            }

            public E next()
            {
                return this.iterator.next();
            }

            public void remove()
            {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString()
    {
        return this.elements.toString();
    }
}
