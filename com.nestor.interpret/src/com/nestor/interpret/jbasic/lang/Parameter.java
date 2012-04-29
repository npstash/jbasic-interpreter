/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import java.io.Serializable;

import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

public final class Parameter implements Serializable, Description
{
    private static final long serialVersionUID = 3958241056148774818L;

    final String parameterName;

    final TYPE<? extends DATA> parameterType;

    public Parameter(String paramName, TYPE<? extends DATA> paramType)
    {
        if (paramName == null)
            throw new NullPointerException("paramName");
        if (paramType == null)
            throw new NullPointerException("paramType");
        this.parameterName = paramName;
        this.parameterType = paramType;
    }

    public String describe()
    {
        return this.parameterName + "[" + this.parameterType.describe() + "]";
    }

    public String getName()
    {
        return this.parameterName;
    }

    public TYPE<? extends DATA> getType()
    {
        return this.parameterType;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Parameter)
        {
            Parameter p2 = (Parameter) obj;
            if (!p2.parameterName.equals(this.parameterName))
                return false;
            return p2.parameterType.equals(this.parameterType);
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return this.parameterName.hashCode() + this.parameterType.hashCode();
    }
}