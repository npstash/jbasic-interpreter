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

package com.nestor.interpret.expression;

import com.nestor.interpret.type.DATA;

public abstract class AbstractOperator<VAL extends DATA, ARG extends DATA> implements
        Operator<VAL, ARG>
{
    private static final long serialVersionUID = 1L;
    private final String description;

    public AbstractOperator(String description)
    {
        this.description = description;
    }

    public final String describe()
    {
        return this.description;
    }
}
