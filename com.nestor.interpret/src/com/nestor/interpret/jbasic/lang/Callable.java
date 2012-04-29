/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-27
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import java.util.Arrays;
import java.util.List;

import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.statement.ReturnCode;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.DATA;

public abstract class Callable<T extends Callable> extends Statement
{
    private static final long serialVersionUID = -7887059123645838008L;

    private Statement code;

    private final String name;

    private final List<Parameter> params;

    public Callable(String name, List<Parameter> params)
    {
        if (name == null)
            throw new NullPointerException("name");
        if (params == null)
            throw new NullPointerException("params");
        this.name = name;
        this.params = params;
    }

    public Callable(String name, Parameter... params)
    {
        this(name, Arrays.asList(params));
    }

    public abstract Statement createCall(List<Value<? extends DATA>> arguments);

    @SuppressWarnings("unchecked")
    public boolean declares(Callable decl)
    {
        if (!decl.name.equals(this.name))
            return false;
        if (!Arrays.equals(decl.params.toArray(), decl.params.toArray()))
            return false;
        if (!getClass().isAssignableFrom(decl.getClass()))
            return false;
        return declaresSpecific((T) decl);
    }

    protected abstract boolean declaresSpecific(T decl);

    public String describe()
    {
        return this.name;
    }

    @Override
    protected ReturnCode executeImpl(RuntimeContext context) throws EvaluationException
    {
        return this.code.execute(context);
    }

    public String getName()
    {
        return this.name;
    }

    public List<Parameter> getParameters()
    {
        return this.params;
    }

    public void implement(Statement implementation)
    {
        if (this.code != null)
            throw new IllegalArgumentException("Cannot reimplement " + this.name);
        this.code = implementation;
    }

    public boolean isImplemented()
    {
        return this.code != null;
    }

    /**
     * This method attempts to find a predeclaration for given callable in current module, or
     * creates one if it does not exist. When given declaration re-declares previous
     * pre-declaration, an exception will be thrown.
     * 
     * @param context
     * @param declaration
     * @throws RedeclarationException -
     *             when redeclaration is caused
     * @return
     */
    public abstract Callable resolveDefinition(ParseContext context) throws DeclarationException;
}
