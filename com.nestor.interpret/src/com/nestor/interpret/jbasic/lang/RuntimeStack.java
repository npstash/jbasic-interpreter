/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

public final class RuntimeStack extends Stack
{
    public RuntimeStack()
    {
        // no-case-sensivity matters only at parse time. If at parse time case
        // sensivity was enabled, lets keep it here.
        super(true);
    }

    @Override
    public void addVariable(Variable variable)
    {
        getCurrentBlock().variables.addUnchecked(variable.getName(), variable);
    }

    @Override
    public Variable getVariable(String identifier)
    {
        Block block2 = findBlockContainingVariable(identifier);
        if (block2 != null)
            return block2.variables.getUnchecked(identifier);
        return null;
    }
}
