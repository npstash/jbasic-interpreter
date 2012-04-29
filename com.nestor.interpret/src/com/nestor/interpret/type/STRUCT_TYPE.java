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

import java.util.Map;

import com.nestor.interpret.jbasic.parse.StatementTokens;

public final class STRUCT_TYPE extends DATA_TYPE<STRUCT>
{
    private static final long serialVersionUID = -7385469599041240251L;

    private final String name;

    private final Map<String, TYPE<? extends DATA>> namedContent;

    public STRUCT_TYPE(String name, Map<String, TYPE<? extends DATA>> namedContent)
    {
        this.name = name;
        this.namedContent = namedContent;
    }

    @Override
    public boolean areEqual(TYPE other)
    {
        if (other instanceof STRUCT_TYPE)
        {
            STRUCT_TYPE ost = (STRUCT_TYPE) other;
            if (ost.namedContent.size() != this.namedContent.size())
                return false;
            for (Map.Entry<String, TYPE<? extends DATA>> entry : this.namedContent.entrySet())
            {
                TYPE<? extends DATA> othType = ost.namedContent.get(entry.getKey());
                if (othType == null)
                    return false;
                if (!othType.areEqual(entry.getValue()))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public String describe()
    {
        String res = this.name + "{";
        for (Map.Entry<String, TYPE<? extends DATA>> entry : this.namedContent.entrySet())
        {
            res += entry.getKey() + " " + StatementTokens.AS.describe();
            res += " " + entry.getValue().describe() + ";";
        }
        return res + "}";
    }

    public String describe(Map<String, DATA> valuesRef)
    {
        String res = this.name + "{";
        for (Map.Entry<String, TYPE<? extends DATA>> entry : this.namedContent.entrySet())
            res += entry.getKey() + " = " + valuesRef.get(entry.getKey()) + ";";
        return res + "}";
    }

    /**
     * Returns type of filed of given name or <code>null</code> if its invalid for this struct,
     * 
     * @param fieldName
     * @return
     */
    public TYPE<? extends DATA> getFieldType(String fieldName)
    {
        return this.namedContent.get(fieldName);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public void initializeValue(Map<String, DATA> valuesRef)
    {
        // initialize field values
        for (Map.Entry<String, TYPE<? extends DATA>> entry : this.namedContent.entrySet())
            valuesRef.put(entry.getKey(), entry.getValue().newInstance());
    }

    @Override
    public boolean canPromote(TYPE toType)
    {
        return false;
    }

    @Override
    public <P extends DATA> P promote(STRUCT value, TYPE<P> toType)
    {
        return null;
    }

    @Override
    public boolean isAssignableFrom(TYPE other)
    {
        // no inheritance
        return areEqual(other);
    }

    @Override
    public boolean isInstance(DATA value)
    {
        return value.getValueType().areEqual(this);
    }

    @Override
    public boolean isQualifiable()
    {
        return true;
    }

    @Override
    public STRUCT newInstance()
    {
        return new STRUCT(this);
    }
}