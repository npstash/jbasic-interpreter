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

import java.util.List;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.shared.Description;

public final class ARRAY<D extends DATA> extends DATA<ARRAY<D>>
{
    private static final long serialVersionUID = 7330143092081329559L;

    private static String describeRecursive(Object[] root)
    {
        String res = "[";
        for (Object object : root)
            if (object.getClass().isArray())
                res += describeRecursive((Object[]) object);
            else
            {
                if (res != "[")
                    res += ",";
                res += ((Description) object).describe();
            }
        return res + "]";
    }

    private static void eraseRecursive(Object[] root, TYPE elementType)
    {
        for (int i = 0; i < root.length; i++)
            if (root[i].getClass().isArray())
                eraseRecursive((Object[]) root[i], elementType);
            else
                root[i] = elementType.newInstance();
    }

    /**
     * Elements of this array are either deeper arrays, or stored data.
     */
    private Object[] data;

    private ARRAY_TYPE<D> declaredType;

    public ARRAY(ARRAY_TYPE<D> arrayType)
    {
        this.declaredType = arrayType;
        createLists(this.data = new Object[arrayType.getDimensions().get(0).size()], 0);
    }

    private void createLists(Object[] root, int depth)
    {
        IndexRange range = this.declaredType.getDimensions().get(depth);
        for (int i = 0; i < range.size(); i++)
            if (depth + 1 < this.declaredType.getDimensions().size())
            {
                Object[] nlist = new Object[this.declaredType.getDimensions().get(depth + 1).size()];
                root[i] = nlist;
                createLists(nlist, depth + 1);
            } else
                // last level, dont add arrays, but just elements with default values
                root[i] = this.declaredType.getElementType().newInstance();
    }

    public String describe()
    {
        return describeRecursive(this.data);
    }

    @Override
    public boolean equalsImpl(ARRAY<D> t)
    {
        for (int i = 0; i < this.data.length; i++)
            if (!t.data[i].equals(this.data[i]))
                return false;
        return true;
    }

    public void erase()
    {
        eraseRecursive(this.data, this.declaredType.getElementType());
    }

    @SuppressWarnings("unchecked")
    public D getElement(List<Integer> indexes) throws EvaluationException
    {
        List<Integer> translated = this.declaredType.getTranslatedIndexes(indexes);
        try
        {
            Object[] lookup = getLastLevelArray(translated);
            return (D) lookup[translated.get(translated.size() - 1)];
        } catch (IndexOutOfBoundsException e)
        {
            throw new EvaluationException(e.getMessage());
        }
    }

    private Object[] getLastLevelArray(List<Integer> translated)
    {
        Object[] lookup = this.data;
        for (int depth = 0; depth < translated.size() - 1; depth++)
            lookup = (Object[]) lookup[translated.get(depth)];
        return lookup;
    }

    public ARRAY_TYPE<D> getValueType()
    {
        return this.declaredType;
    }

    public void redim(ARRAY_TYPE<D> newType)
    {
        if (!newType.getElementType().areEqual(this.declaredType.getElementType()))
            throw new IllegalArgumentException("Trying to redefine array element type");
        if (!(newType.getDimensions().size() != this.declaredType.getDimensions().size()))
            throw new IllegalArgumentException("Trying to redefine array dimension");
        this.declaredType = newType;
        createLists(this.data = new Object[newType.getDimensions().get(0).size()], 0);
    }

    public void setElement(List<Integer> indexes, D element)
    {
        List<Integer> translated = this.declaredType.getTranslatedIndexes(indexes);
        Object[] lookup = getLastLevelArray(translated);
        lookup[translated.get(translated.size() - 1)] = element;
    }
}