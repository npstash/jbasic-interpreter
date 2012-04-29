/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-30
 ****************************************************************************/

package com.nestor.interpret.jbasic.libs;

import com.nestor.interpret.jbasic.lang.Constant;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.type.DATA;

public abstract class AbstractLibrary
{
    protected void addConstant(ModuleBuilder program, String name, DATA<? extends DATA> value)
            throws DeclarationException
    {
        program.addConstant(new Constant(name, value));
    }

    public abstract void load(ModuleBuilder program) throws DeclarationException;
}
