/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-01
 ****************************************************************************/

package com.nestor.interpret.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ARRAY_TYPE<D extends DATA> extends DATA_TYPE<ARRAY<D>>
{
    private static final long serialVersionUID = -5198709842965932445L;

    private final List<IndexRange> dimensions;

    private final TYPE<? extends D> elementType;

    /**
     * Unbound array, general type.
     */
    @SuppressWarnings("unchecked")
    public ARRAY_TYPE()
    {
        this(Collections.EMPTY_LIST, (TYPE<? extends D>) TYPES.DATA_TYPE);
    }

    public ARRAY_TYPE(List<IndexRange> dimensions, TYPE<? extends D> elementType)
    {
        this.dimensions = new ArrayList<IndexRange>(dimensions);
        this.elementType = elementType;
    }

    @Override
    public boolean areEqual(TYPE other)
    {
        if (other instanceof ARRAY_TYPE)
        {
            ARRAY_TYPE arrType = (ARRAY_TYPE) other;
            if (!arrType.elementType.areEqual(this.elementType))
                return false;
            return this.dimensions.size() == arrType.dimensions.size();
        }
        return false;
    }

    @Override
    public String describe()
    {
        return this.elementType.describe() + Arrays.toString(this.dimensions.toArray());
    }

    public List<IndexRange> getDimensions()
    {
        return this.dimensions;
    }

    public final TYPE<? extends D> getElementType()
    {
        return this.elementType;
    }

    @Override
    public String getName()
    {
        return describe();
    }

    /**
     * Returns a translated index - zero based index relative to the index given. Thows an
     * {@link IndexOutOfBoundsException} if index invalid.
     * 
     * @param index
     * @return (index - low_range), for each index
     */
    public List<Integer> getTranslatedIndexes(List<Integer> indexes)
    {
        if (indexes.size() != this.dimensions.size())
            throw new IllegalArgumentException("Invalid qualifiers");
        List<Integer> translated = new ArrayList<Integer>(indexes.size());
        for (int i = 0; i < indexes.size(); i++)
            translated.add(this.dimensions.get(i).translate(indexes.get(i)));
        return translated;
    }

    @Override
    public boolean canPromote(TYPE toType)
    {
        return false;
    }

    @Override
    public <P extends DATA> P promote(ARRAY<D> value, TYPE<P> toType)
    {
        return null;
    }

    @Override
    public boolean isAssignableFrom(TYPE other)
    {
        if (other instanceof ARRAY_TYPE)
        {
            ARRAY_TYPE arrType = (ARRAY_TYPE) other;
            if (!this.elementType.isAssignableFrom(arrType.elementType))
                return false;
            if (this.dimensions.size() == 0)
                // unbound
                return true;
            return this.dimensions.size() == arrType.dimensions.size();
        }
        return false;
    }

    @Override
    public boolean isInstance(DATA value)
    {
        return this.isAssignableFrom(value.getValueType());
    }

    @Override
    public boolean isQualifiable()
    {
        return true;
    }

    @Override
    public ARRAY<D> newInstance()
    {
        return new ARRAY<D>(this);
    }

    public void redim(List<IndexRange> newDimenstions)
    {
        if (newDimenstions.size() != this.dimensions.size())
            throw new IllegalArgumentException("Uncompatible redim");
        for (int i = 0; i < this.dimensions.size(); i++)
            this.dimensions.set(i, newDimenstions.get(i));
    }
}
