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

public abstract class NUMBER<D extends NUMBER> extends PRIMITIVE<D>
{
    private static final long serialVersionUID = 7921644446492813497L;

    public abstract double doubleValue();

    public abstract int intValue();

    public static TYPE<? extends NUMBER> getBestBaseType(TYPE<? extends NUMBER> left,
            TYPE<? extends NUMBER> right)
    {
        if (TYPES.INTEGER_TYPE.isAssignableFrom(left) && TYPES.INTEGER_TYPE.isAssignableFrom(right))
            return TYPES.INTEGER_TYPE;
        return TYPES.DOUBLE_TYPE;
    }
}
