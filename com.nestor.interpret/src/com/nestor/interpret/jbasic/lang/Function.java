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

import java.util.Arrays;
import java.util.List;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public final class Function extends Callable<Function>
{
    private static final long serialVersionUID = 8972762777729928202L;

    private final TYPE<? extends DATA> returnType;

    public Function(String name, TYPE<? extends DATA> returnType, List<Parameter> params)
    {
        super(name, params);
        if (returnType == null)
            throw new NullPointerException("returnType");
        this.returnType = returnType;
    }

    public Function(String name, TYPE<? extends DATA> returnType, Parameter... params)
    {
        this(name, returnType, Arrays.asList(params));
    }

    @Override
    public FunctionCall createCall(List<Value<? extends DATA>> arguments)
    {
        return new FunctionCall(this, arguments);
    }

    @Override
    public boolean declaresSpecific(Function decl)
    {
        return decl.returnType.equals(this.returnType);
    }

    public TYPE<? extends DATA> getReturnType()
    {
        return this.returnType;
    }

    @Override
    public Callable resolveDefinition(ParseContext context) throws DeclarationException
    {
        Function function = null;
        if (context.moduleBuilder().containsFunction(getName()))
        {
            // function could have been pre-declared
            function = context.moduleBuilder().getFunction(getName());
            // check if declaration is consistent with previous one
            if (declares(function))
                return function;
        }
        context.moduleBuilder().addFunction(this);
        return this;
    }
}
