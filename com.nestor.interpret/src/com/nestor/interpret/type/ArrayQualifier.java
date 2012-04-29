/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-19
 ****************************************************************************/

package com.nestor.interpret.type;

import java.util.ArrayList;
import java.util.List;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;

/**
 * TODO this qualifier only supports qualifying array elements, but not sub-arrays
 */
public final class ArrayQualifier extends ChainQualifier<ARRAY<DATA>>
{
    private static final long serialVersionUID = 1078988802944935701L;

    private final ARRAY_TYPE<? extends DATA> arrayType;

    private final List<Value<INTEGER>> indexes;

    public ArrayQualifier(ARRAY_TYPE<? extends DATA> arrayType, List<Value<INTEGER>> indexes,
            Qualifier chained)
    {
        super(chained);
        this.arrayType = arrayType;
        this.indexes = indexes;
    }

    public String describe()
    {
        String res = "[";
        for (int i = 0; i < this.indexes.size(); i++)
        {
            res += this.indexes.get(i).describe();
            if (i < this.indexes.size() - 1)
                res += ",";
        }
        res += "]";
        return res;
    }

    @Override
    protected DATA getChained(RuntimeEnv env, ARRAY<DATA> unqualified) throws EvaluationException
    {
        return unqualified.getElement(evaluateIndexes(env));
    }

    private List<Integer> evaluateIndexes(RuntimeEnv env) throws EvaluationException
    {
        List<Integer> evaluated = new ArrayList<Integer>(this.indexes.size());
        for (Value<INTEGER> index : this.indexes)
            evaluated.add(index.evaluate(env).intValue());
        return evaluated;
    }

    public TYPE<? extends DATA> getQualifiedType()
    {
        return this.arrayType.getElementType();
    }

    @Override
    protected void setChained(RuntimeEnv env, ARRAY<DATA> unqualified, DATA data)
            throws EvaluationException
    {
        unqualified.setElement(evaluateIndexes(env), data);
    }
}