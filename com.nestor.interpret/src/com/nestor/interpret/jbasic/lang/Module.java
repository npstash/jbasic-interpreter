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

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

public final class Module extends Statement implements Description
{
    private static final long serialVersionUID = 8389787146570637823L;

    private final CheckedContainer<Function> functions;

    private final Statement main;

    private final String name;

    private final CheckedContainer<Sub> subs;

    private final CheckedContainer<TYPE<? extends DATA>> types;

    private final CheckedContainer<Variable> variables;

    public Module(String name, CheckedContainer<Sub> subs, CheckedContainer<Function> functions,
            CheckedContainer<TYPE<? extends DATA>> types, CheckedContainer<Variable> variables,
            Statement main)
    {
        if (name == null)
            throw new NullPointerException("name");
        if (subs == null)
            throw new NullPointerException("subs");
        if (functions == null)
            throw new NullPointerException("functions");
        if (types == null)
            throw new NullPointerException("types");
        if (variables == null)
            throw new NullPointerException("variables");
        if (main == null)
            throw new NullPointerException("main");
        this.name = name;
        this.functions = functions;
        this.subs = subs;
        this.types = types;
        this.variables = variables;
        this.main = main;
    }

    public String describe()
    {
        return this.name;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        return this.main.execute(context);
    }

    public Function getFunction(String identifier)
    {
        return this.functions.getUnchecked(identifier);
    }

    public Sub getSub(String identifier)
    {
        return this.subs.getUnchecked(identifier);
    }

    public TYPE<? extends DATA> getType(String identifier)
    {
        return this.types.getUnchecked(identifier);
    }

    public Variable getVariable(String identifier)
    {
        return this.variables.getUnchecked(identifier);
    }
}