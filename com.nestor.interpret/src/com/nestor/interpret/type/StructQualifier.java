/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-21
 ****************************************************************************/

package com.nestor.interpret.type;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;

public final class StructQualifier extends ChainQualifier<STRUCT>
{
    private static final long serialVersionUID = 1078988802944935701L;

    private final String fieldName;

    private final STRUCT_TYPE structType;

    public StructQualifier(STRUCT_TYPE structType, String fieldName, Qualifier chained)
    {
        super(chained);
        if (structType.getFieldType(fieldName) == null)
            throw new IllegalArgumentException("Invalid qualfier");
        this.structType = structType;
        this.fieldName = fieldName;
    }

    public String describe()
    {
        return this.fieldName;
    }

    @Override
    protected DATA getChained(RuntimeEnv env, STRUCT unqualified) throws EvaluationException
    {
        return unqualified.getField(this.fieldName);
    }

    public TYPE<? extends DATA> getQualifiedType()
    {
        return this.structType.getFieldType(this.fieldName);
    }

    @Override
    protected void setChained(RuntimeEnv env, STRUCT unqualified, DATA data)
            throws EvaluationException
    {
        unqualified.setField(this.fieldName, data);
    }
}