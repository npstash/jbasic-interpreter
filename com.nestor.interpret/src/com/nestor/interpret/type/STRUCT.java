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

import java.util.HashMap;
import java.util.Map;

public final class STRUCT extends DATA<STRUCT>
{
    private static final long serialVersionUID = 2804450471823139813L;

    private final STRUCT_TYPE structType;

    private final Map<String, DATA> values;

    public STRUCT(STRUCT_TYPE structType)
    {
        this.structType = structType;
        this.values = new HashMap<String, DATA>();
        this.structType.initializeValue(this.values);
    }

    public String describe()
    {
        return this.structType.describe(this.values);
    }

    @Override
    public boolean equalsImpl(STRUCT t)
    {
        for (Map.Entry<String, DATA> entry : this.values.entrySet())
        {
            DATA othData = t.values.get(entry.getKey());
            if (othData == null)
                return false;
            if (!othData.equals(entry.getValue()))
                return false;
        }
        return true;
    }

    public DATA getField(String fieldName)
    {
        return this.values.get(fieldName);
    }

    public STRUCT_TYPE getValueType()
    {
        return this.structType;
    }

    public void setField(String fieldName, DATA newValue)
    {
        this.values.put(fieldName, newValue);
    }
}