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

import com.nestor.interpret.jbasic.parse.InterpretException;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public final class ModuleBuilder
{
    private final CheckedContainer<Constant> constants;

    private final CheckedContainer<Function> functions;

    private final String name;

    private final CheckedContainer<Sub> subs;

    private final CheckedContainer<TYPE<? extends DATA>> types;

    private final CheckedContainer<Variable> variables;

    public ModuleBuilder(String name, Configurator config)
    {
        this.name = name;
        boolean caseSensitive = config.identifiersCaseSensitive();
        this.constants = new CheckedContainer<Constant>("constant", caseSensitive);
        this.functions = new CheckedContainer<Function>("function", caseSensitive);
        this.subs = new CheckedContainer<Sub>("sub", caseSensitive);
        this.types = new CheckedContainer<TYPE<? extends DATA>>("type", caseSensitive);
        this.variables = new CheckedContainer<Variable>("variable", caseSensitive);
    }

    public void addConstant(Constant element) throws RedeclarationException
    {
        this.constants.add(element.getName(), element);
    }

    public void addFunction(Function element) throws RedeclarationException
    {
        this.functions.add(element.getName(), element);
    }

    public void addSub(Sub element) throws RedeclarationException
    {
        this.subs.add(element.getName(), element);
    }

    public void addType(TYPE<? extends DATA> type) throws RedeclarationException
    {
        this.types.add(type.getName(), type);
    }

    public void addVariable(Variable element) throws RedeclarationException
    {
        this.variables.add(element.getName(), element);
    }

    public Module buildModule(Statement mainBody) throws InterpretException
    {
        // do module verification
        // 1. each function / sub must be implemented
        for (Sub sub : this.subs)
            if (!sub.isImplemented())
                // TODO specialize for definition exception
                throw new InterpretException("Sub '" + sub.describe() + "' is not implemented!");
        for (Function function : this.functions)
            if (!function.isImplemented())
                throw new InterpretException("Function '" + function.describe()
                        + "' is not implemented!");

        return new Module(this.name, this.subs, this.functions, this.types, this.variables,
                mainBody);
    }

    public final boolean containsConstant(String identifier)
    {
        return this.constants.contains(identifier);
    }

    public final boolean containsFunction(String identifier)
    {
        return this.functions.contains(identifier);
    }

    public final boolean containsSub(String identifier)
    {
        return this.subs.contains(identifier);
    }

    public final boolean containsType(String identifier)
    {
        return this.types.contains(identifier);
    }

    public final boolean containsVariable(String identifier)
    {
        return this.variables.contains(identifier);
    }

    public Constant getConstant(String identifier) throws UndeclaredIdentifierException
    {
        return this.constants.get(identifier);
    }

    public Function getFunction(String identifier) throws UndeclaredIdentifierException
    {
        return this.functions.get(identifier);
    }

    public Sub getSub(String identifier) throws UndeclaredIdentifierException
    {
        return this.subs.get(identifier);
    }

    public TYPE<? extends DATA> getType(String identifier) throws UndeclaredIdentifierException
    {
        return this.types.get(identifier);
    }

    public Variable getVariable(String identifier) throws UndeclaredIdentifierException
    {
        return this.variables.get(identifier);
    }

    @Override
    public final String toString()
    {
        return this.name;
    }
}
