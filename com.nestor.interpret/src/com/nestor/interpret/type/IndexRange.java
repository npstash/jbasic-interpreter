/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-02
 ****************************************************************************/

package com.nestor.interpret.type;

import java.io.Serializable;

public final class IndexRange implements Serializable
{
    private static final long serialVersionUID = -659437743897496387L;

    private final int low, high;

    /**
     * Default mode, invalid range.
     */
    public IndexRange()
    {
        this.low = 0;
        this.high = -1;
    }

    public IndexRange(int low, int high)
    {
        if (low > high)
            throw new IllegalArgumentException("low > high");
        this.low = low;
        this.high = high;
    }

    /**
     * Translated range-based index to a zero-based index.
     * 
     * @param index
     * @return
     */
    public int translate(int index)
    {
        if (index < this.low || index > this.high)
            throw new IndexOutOfBoundsException("Index " + index + " is out of bounds: " + this);
        return index - this.low;
    }

    public int size()
    {
        return this.high - this.low + 1;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof IndexRange)
            return ((IndexRange) obj).low == this.low && ((IndexRange) obj).high == this.high;
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.high + this.low << 16;
    }

    @Override
    public String toString()
    {
        if (this.low > this.high)
            return "";
        return this.low + " TO " + this.high;
    }
}
