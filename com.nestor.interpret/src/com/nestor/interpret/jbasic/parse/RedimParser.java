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

import java.util.Map.Entry;

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.statement.LocalDimStatement;
import com.nestor.interpret.jbasic.statement.RedimStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.ARRAY_TYPE;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

public final class RedimParser extends StatementParser
{
    public RedimParser()
    {
        super(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, InterpretException
    {
        if (scanner.matchAdvance(StatementTokens.REDIM) == null)
            return null;

        boolean global = context.stack().getCurrentBlock().getType() == Stack.BlockKind.TOPLEVEL;

        VLRestrictedType arraysOnly = new VLRestrictedType(new ARRAY_TYPE(),
                VLRestrictedType.Relation.SUPER_OR_EQUAL);
        VariableListFilter filter = new VLAtLeastOne(arraysOnly);

        VariableListParser varParser = new VariableListParser(context, filter);
        VariableList list = varParser.parse(scanner, context);
        BlockBuilder block = new BlockBuilder(false);
        for (Entry<String, TYPE<? extends DATA>> entry : list.getVariables().entrySet())
        {
            // if identifier is known, redim it; dim otherwise
            Variable var = null;
            String name = entry.getKey();
            if (context.stack().containsVariable(name))
                var = context.stack().getVariable(name);
            else if (context.moduleBuilder().containsVariable(name))
                var = context.moduleBuilder().getVariable(name);
            if (var != null)
            {
                ARRAY_TYPE<? extends DATA> varType = (ARRAY_TYPE<? extends DATA>) var.getValueType();
                ARRAY_TYPE<? extends DATA> newType = (ARRAY_TYPE<? extends DATA>) entry.getValue();
                varType.redim(newType.getDimensions());
                block.addStatement(new RedimStatement(name, newType));
            } else // not yet declared
            if (global)
                context.moduleBuilder().addVariable(new Variable(name, entry.getValue()));
            else
            {
                context.stack().addVariable(new Variable(name, entry.getValue()));
                LocalDimStatement dimAsStatement = new LocalDimStatement(name, entry.getValue());
                block.addStatement(dimAsStatement);
            }
        }
        return block.getStatementNonNull();
    }
}
