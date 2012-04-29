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

public final class DOUBLE extends NUMBER<DOUBLE>
{
    private static final long serialVersionUID = 8745080286559897204L;

    private final double value;

    public DOUBLE()
    {
        this.value = 0;
    }

    public DOUBLE(double value)
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
    public boolean equalsImpl(DOUBLE t)
    {
        return t.value == this.value;
    }

    public TYPE<? extends DOUBLE> getValueType()
    {
        return TYPES.DOUBLE_TYPE;
    }

    @Override
    public int intValue()
    {
        return (int) this.value;
    }
}
