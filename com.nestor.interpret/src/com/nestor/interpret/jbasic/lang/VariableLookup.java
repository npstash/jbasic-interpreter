/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-20
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import java.io.Serializable;

import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

public abstract class VariableLookup implements Description, Serializable
{
    private static final long serialVersionUID = 1532031714278720392L;

    public enum LookupKind
    {
        GLOBAL, IMPLICIT, LOCAL
    }

    public static VariableLookup createFnReturnVarLookup(ParseContext context, Function function)
    {
        TYPE<? extends DATA> type = function.getReturnType();
        String name = function.getName();
        return createLocalLookupImpl(name, type);
    }

    public static VariableLookup createGlobalLookup(ParseContext context, String name)
            throws UndeclaredIdentifierException
    {
        TYPE<? extends DATA> type = context.moduleBuilder().getVariable(name).getValueType();
        return new VariableLookup(name, type, LookupKind.GLOBAL)
        {
            private static final long serialVersionUID = -982300510667537851L;

            @Override
            public Variable lookup(RuntimeContext ctx)
            {
                return ctx.module().getVariable(super.name);
            }
        };
    }

    public static VariableLookup createImplicitLookup(ParseContext context, String name,
            final TYPE<? extends DATA> type) throws RedeclarationException
    {
        // add to stack now
        context.stack().addVariable(new Variable(name, type));

        return new VariableLookup(name, type, LookupKind.IMPLICIT)
        {
            private static final long serialVersionUID = -1755468449199148447L;

            @Override
            public Variable lookup(RuntimeContext ctx1)
            {
                // already on stack?
                if (ctx1.stack().getVariable(this.name) == null)
                {
                    Variable var = new Variable(this.name, type);
                    ctx1.stack().addVariable(var);
                    return var;
                }
                return ctx1.stack().getVariable(super.name);
            }
        };
    }

    public static VariableLookup createLocalLookup(ParseContext context, String name)
            throws UndeclaredIdentifierException
    {
        TYPE<? extends DATA> type = context.stack().getVariable(name).getValueType();
        return createLocalLookupImpl(name, type);
    }

    private static VariableLookup createLocalLookupImpl(String name, TYPE<? extends DATA> type)
    {
        return new VariableLookup(name, type, LookupKind.LOCAL)
        {
            private static final long serialVersionUID = 9090607447568637311L;

            @Override
            public Variable lookup(RuntimeContext ctx)
            {
                return ctx.stack().getVariable(super.name);
            }
        };
    }

    public static VariableLookup createLookup(ParseContext context, String identifier,
            TYPE<? extends DATA> defaultType) throws DeclarationException
    {
        if (context.stack().containsVariable(identifier))
        {
            defaultType = context.stack().getVariable(identifier).getValueType();
            return createLocalLookup(context, identifier);
        } else if (context.moduleBuilder().containsVariable(identifier))
        {
            defaultType = context.moduleBuilder().getVariable(identifier).getValueType();
            return createGlobalLookup(context, identifier);
        } else if (context.identify(identifier) != null)
            throw new RedeclarationException(identifier, context.identify(identifier));
        else
            return createImplicitLookup(context, identifier, defaultType);
    }

    private final LookupKind lookupKind;

    protected final String name;

    private TYPE<? extends DATA> type;

    VariableLookup(String name, TYPE<? extends DATA> type, LookupKind lookupKind)
    {
        this.name = name;
        this.type = type;
        this.lookupKind = lookupKind;
    }

    public String describe()
    {
        return this.lookupKind + " " + this.name;
    }

    public LookupKind getLookupKind()
    {
        return this.lookupKind;
    }

    public final String getName()
    {
        return this.name;
    }

    public TYPE<? extends DATA> getValueType()
    {
        return this.type;
    }

    public abstract Variable lookup(RuntimeContext context);

    @Override
    public String toString()
    {
        return describe();
    }
}
