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

package com.nestor.interpret.jbasic.lang;

import java.util.List;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;

public final class Sub extends Callable<Sub>
{
    private static final long serialVersionUID = 657393536723937340L;

    public Sub(String name, List<Parameter> params)
    {
        super(name, params);
    }

    public Sub(String name, Parameter... params)
    {
        super(name, params);
    }

    @Override
    public SubCall createCall(List<Value<? extends DATA>> arguments)
    {
        return new SubCall(this, arguments);
    }

    @Override
    public boolean declaresSpecific(Sub decl)
    {
        return true;
    }

    @Override
    public Callable resolveDefinition(ParseContext context) throws DeclarationException
    {
        Sub sub = null;
        if (context.moduleBuilder().containsSub(getName()))
        {
            // function could have been pre-declared
            sub = context.moduleBuilder().getSub(getName());
            // check if declaration is consistent with previous one
            if (declares(sub))
                return sub;
        }
        context.moduleBuilder().addSub(this);
        return this;
    }
}
