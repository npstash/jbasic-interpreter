/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-14
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import java.util.Map;

import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.lang.Stack.BlockKind;
import com.nestor.interpret.jbasic.statement.LocalDimStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;

final class DimParser extends StatementParser
{
    DimParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, RedeclarationException
    {
        // mandatory declaration statement
        if (scanner.matchAdvance(StatementTokens.DIM) == null)
            return null;

        // check if global or local
        boolean global = context.stack().getCurrentBlock().getType() == BlockKind.TOPLEVEL;

        VariableListFilter filter = new VLOnlyUndeclared(new VLAtLeastOne());
        VariableListParser varParser = new VariableListParser(context, filter);
        VariableList list = varParser.parse(scanner, context);

        BlockBuilder block = new BlockBuilder(false);
        for (Map.Entry<String, TYPE<? extends DATA>> pair : list.getVariables().entrySet())
            if (global)
                context.moduleBuilder().addVariable(new Variable(pair.getKey(), pair.getValue()));
            else
            {
                context.stack().addVariable(new Variable(pair.getKey(), pair.getValue()));
                LocalDimStatement dimAsStatement = new LocalDimStatement(pair.getKey(),
                        pair.getValue());
                block.addStatement(dimAsStatement);
            }
        // result can not be NULL!
        return block.getStatementNonNull();
    }
}
