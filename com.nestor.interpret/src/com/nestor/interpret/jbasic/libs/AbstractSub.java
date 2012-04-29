/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-04
 ****************************************************************************/

package com.nestor.interpret.jbasic.libs;

import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.statement.Statement;

public abstract class AbstractSub extends Statement
{
    private static final long serialVersionUID = 1L;
    private final String name;

    public AbstractSub(Sub sub)
    {
        this.name = sub.getName();
    }

    public String describe()
    {
        return this.name;
    }
}