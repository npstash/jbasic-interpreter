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

class SIMPLE_TYPE<D extends DATA> extends DATA_TYPE<D>
{
    private static final long serialVersionUID = 8212223004453889188L;

    private final Class<D> dataClass;

    SIMPLE_TYPE(final Class<D> dataClass)
    {
        this.dataClass = dataClass;
    }

    @Override
    public boolean areEqual(TYPE other)
    {
        if (other instanceof SIMPLE_TYPE)
            return this.dataClass.equals(((SIMPLE_TYPE) other).dataClass);
        return false;
    }

    @Override
    public String describe()
    {
        return this.dataClass.getSimpleName();
    }

    @Override
    public String getName()
    {
        return this.dataClass.getSimpleName();
    }

    @Override
    public boolean canPromote(TYPE toType)
    {
        return false;
    }

    @Override
    public <P extends DATA> P promote(D value, TYPE<P> toType)
    {
        return null;
    }

    @Override
    public boolean isAssignableFrom(TYPE other)
    {
        if (other instanceof SIMPLE_TYPE)
            return this.dataClass.isAssignableFrom(((SIMPLE_TYPE) other).dataClass);
        return false;
    }

    @Override
    public boolean isInstance(DATA value)
    {
        return this.dataClass.isInstance(value);
    }

    @Override
    public boolean isQualifiable()
    {
        return false;
    }

    @Override
    public D newInstance()
    {
        try
        {
            return this.dataClass.newInstance();
        } catch (Exception e)
        {
            throw new IllegalArgumentException("cannot instantiate data");
        }
    }
}
