/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 09/08/2006
 ****************************************************************************/

package com.nestor.shared.util;

public class ImmutableList<T> implements Traversable<T>
{
    private ImmutableList<T> next = null;

    private T content = null;

    public ImmutableList(ImmutableList<T> disp, T listener)
    {
        if (listener == null)
        {
            throw new NullPointerException();
        }
        this.content = listener;
        this.next = disp;
    }

    public ImmutableList<T> remove(T listener)
    {
        // first check if it exists in list
        ImmutableList<T> current = this;
        ImmutableList<T> found = null;
        while (current != null)
        {
            if (current.content == listener)
            {
                found = current;
                break;
            }
        }
        // if not, return the original list
        if (found == null)
        {
            return this;
        }

        // go through list again to create a copy
        current = this;
        ImmutableList<T> copy = null;
        while (current != found)
        {
            copy = new ImmutableList<T>(copy, current.content);
            current = current.next;
        }
        if (copy != null)
        {
            while (copy.next != null)
            {
                copy = copy.next;
            }
            // append the not-changed part to list
            copy.next = current.next;
        } else
        {
            copy = current.next;
        }
        return copy;
    }

    /**
     * If fact, this iterator is a visitor
     * 
     * @param visitor
     */
    public void traverse(PassiveIterator<T> visitor)
    {
        visitor.next(this.content);
        if (this.next != null)
        {
            this.next.traverse(visitor);
        }
    }

}
