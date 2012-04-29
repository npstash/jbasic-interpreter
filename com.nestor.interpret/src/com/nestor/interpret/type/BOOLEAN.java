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

public final class BOOLEAN extends PRIMITIVE<BOOLEAN>
{
    private static final long serialVersionUID = -9125723566770799118L;

    public static final BOOLEAN FALSE = new BOOLEAN(false);

    public static final BOOLEAN TRUE = new BOOLEAN(true);

    private final boolean value;

    public BOOLEAN()
    {
        this.value = false;
    }

    public BOOLEAN(boolean value)
    {
        this.value = value;
    }

    public boolean booleanValue()
    {
        return this.value;
    }

    public String describe()
    {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equalsImpl(BOOLEAN t)
    {
        return t.value == this.value;
    }

    public TYPE<? extends BOOLEAN> getValueType()
    {
        return TYPES.BOOLEAN_TYPE;
    }
}
