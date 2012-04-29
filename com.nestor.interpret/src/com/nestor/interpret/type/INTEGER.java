/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-27
 ****************************************************************************/

package com.nestor.interpret.type;

public final class INTEGER extends NUMBER<INTEGER>
{
    private static final long serialVersionUID = -2753222211666265908L;

    private final int value;

    public INTEGER()
    {
        this.value = 0;
    }

    public INTEGER(int value)
    {
        this.value = value;
    }

    public String describe()
    {
        return String.valueOf(this.value);
    }

    @Override
    public double doubleValue()
    {
        return this.value;
    }

    @Override
    public boolean equalsImpl(INTEGER t)
    {
        return t.value == this.value;
    }

    public TYPE<? extends INTEGER> getValueType()
    {
        return TYPES.INTEGER_TYPE;
    }

    @Override
    public int intValue()
    {
        return this.value;
    }
}
