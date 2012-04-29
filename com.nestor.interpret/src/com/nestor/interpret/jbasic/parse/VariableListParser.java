/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-07-16
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import java.util.LinkedHashMap;

import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.parse.Match;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

final class VariableList implements Match
{
    private static final long serialVersionUID = -8454338546910282670L;

    private final LinkedHashMap<String, TYPE<? extends DATA>> vars;

    public VariableList(LinkedHashMap<String, TYPE<? extends DATA>> vars)
    {
        this.vars = vars;
    }

    public String describe()
    {
        return this.vars.toString();
    }

    public final LinkedHashMap<String, TYPE<? extends DATA>> getVariables()
    {
        return this.vars;
    }
}

abstract class VariableListFilter
{
    private final VariableListFilter chain;

    public VariableListFilter()
    {
        this.chain = null;
    }

    public VariableListFilter(VariableListFilter chain)
    {
        this.chain = chain;
    }

    public abstract boolean acceptNoVariables();

    final boolean acceptNoVariablesChained()
    {
        if (this.chain != null)
            if (!this.chain.acceptNoVariables())
                return false;
        return acceptNoVariables();
    }

    public abstract void verify(ParseContext context, String identifier, TYPE<? extends DATA> type)
            throws DeclarationException;

    final void verifyChained(ParseContext context, String identifier, TYPE<? extends DATA> type)
            throws DeclarationException
    {
        if (this.chain != null)
            this.chain.verify(context, identifier, type);
        verify(context, identifier, type);
    }
}

final class VariableListParser implements Parser<VariableList>
{
    private final ParseContext context;

    private final VariableListFilter filter;

    public VariableListParser(ParseContext context, VariableListFilter filter)
    {
        this.context = context;
        this.filter = filter;
    }

    public String describe()
    {
        return "variable list parser";
    }

    public VariableList parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        LinkedHashMap<String, TYPE<? extends DATA>> vars = new LinkedHashMap<String, TYPE<? extends DATA>>();
        boolean comma = false;
        do
        {
            // get the identifier name
            String identifier;
            if (comma || !(comma || this.filter.acceptNoVariablesChained()))
                identifier = scanner.required(SyntaxTokens.IDENTIFIER);
            else
            {
                identifier = scanner.matchAdvance(SyntaxTokens.IDENTIFIER);
                if (identifier == null)
                    break;
            }

            // read data type
            TYPE<? extends DATA> variableType = this.context.parseHelper().requireTypeDeclaration(
                    scanner);

            // if its known, then it must be a variable, obviously
            Description identified = this.context.identify(identifier);
            if (identified != null)
                if (!this.context.moduleBuilder().containsVariable(identifier)
                        && !this.context.stack().containsVariable(identifier))
                    throw scanner.parseFailure(new RedeclarationException(identifier, identified));

            try
            {
                this.filter.verifyChained(this.context, identifier, variableType);
            } catch (DeclarationException e)
            {
                throw scanner.parseFailure(e);
            }

            vars.put(identifier, variableType);
            // check if there are more variables after a comma
            comma = scanner.matchAdvance(StandardTokens.COMMA) != null;
        } while (comma);
        return new VariableList(vars);
    }
}

final class VLAtLeastOne extends VariableListFilter
{
    public VLAtLeastOne()
    {
        /* Empty */
    }

    public VLAtLeastOne(VariableListFilter chain)
    {
        super(chain);
    }

    @Override
    public boolean acceptNoVariables()
    {
        return false;
    }

    @Override
    public void verify(ParseContext context, String identifier, TYPE<? extends DATA> type)
            throws DeclarationException
    {
        /* Empty */
    }
}

final class VLOnlyUndeclared extends VariableListFilter
{

    public VLOnlyUndeclared()
    {
        /* Empty */
    }

    public VLOnlyUndeclared(VariableListFilter chain)
    {
        super(chain);
    }

    @Override
    public boolean acceptNoVariables()
    {
        return true;
    }

    @Override
    public void verify(ParseContext context, String identifier, TYPE<? extends DATA> type)
            throws DeclarationException
    {
        Description identified = context.identify(identifier);
        if (identified != null)
            throw new RedeclarationException(identifier, identified);
    }
}

final class VLRestrictedType extends VariableListFilter
{

    enum Relation
    {
        EQUAL, SUB_OR_EQUAL, SUPER_OR_EQUAL
    }

    private final Relation relation;

    private final TYPE<? extends DATA> type;

    public VLRestrictedType(TYPE<? extends DATA> type, Relation relation)
    {
        this.type = type;
        this.relation = relation;
    }

    public VLRestrictedType(VariableListFilter chain, TYPE<? extends DATA> type, Relation relation)
    {
        super(chain);
        this.type = type;
        this.relation = relation;
    }

    @Override
    public boolean acceptNoVariables()
    {
        return true;
    }

    @Override
    public void verify(ParseContext context, String identifier, TYPE<? extends DATA> type1)
            throws DeclarationException
    {
        switch (this.relation) {
        case EQUAL:
            if (!type1.areEqual(this.type))
                throw new DeclarationException(identifier + " must be of type " + type1);
            break;
        case SUB_OR_EQUAL:
            if (!type1.isAssignableFrom(this.type))
                throw new DeclarationException(identifier + " must be a super type of " + this.type);
            break;
        case SUPER_OR_EQUAL:
            if (!this.type.isAssignableFrom(type1))
                throw new DeclarationException(identifier + " must be a sub type of " + this.type);
            break;
        }
    }
}
