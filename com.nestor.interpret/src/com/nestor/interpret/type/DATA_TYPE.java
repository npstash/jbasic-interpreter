/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-20
 ****************************************************************************/

package com.nestor.interpret.type;

/**
 * All types should inherit from this one.
 * 
 * @param <D>
 */
public class DATA_TYPE<D extends DATA> implements TYPE<D>
{
    private static final long serialVersionUID = 1L;

    public boolean areEqual(TYPE other)
    {
        return this.getClass().equals(other.getClass());
    }

    public boolean canPromote(TYPE toType)
    {
        return false;
    }

    public String describe()
    {
        return getName();
    }

    public String getName()
    {
        return DATA.class.getSimpleName();
    }

    public boolean isAssignableFrom(TYPE other)
    {
        if (other == null)
            return false;
        return this.getClass().isAssignableFrom(other.getClass());
    }

    public boolean isInstance(DATA value)
    {
        if (value == null)
            return true;
        return isAssignableFrom(value.getValueType());
    }

    public boolean isQualifiable()
    {
        return false;
    }

    public D newInstance()
    {
        throw new UnsupportedOperationException();
    }

    public <P extends DATA> P promote(D value, TYPE<P> toType)
    {
        return null;
    }

    @Override
    public String toString()
    {
        return this.describe();
    }
}
