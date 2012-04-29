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

public final class STRING extends PRIMITIVE<STRING>
{
    private static final long serialVersionUID = 521820753471246765L;

    private final String value;

    public STRING()
    {
        this.value = "";
    }

    public STRING(String initial)
    {
        this.value = initial;
    }

    public String describe()
    {
        return this.value;
    }

    @Override
    public boolean equalsImpl(STRING t)
    {
        return t.value.equals(this.value);
    }

    public TYPE<? extends STRING> getValueType()
    {
        return TYPES.STRING_TYPE;
    }

    public String stringValue()
    {
        return this.value;
    }
}
