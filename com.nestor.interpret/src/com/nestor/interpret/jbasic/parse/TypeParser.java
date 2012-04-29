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

package com.nestor.interpret.jbasic.parse;

import java.util.HashMap;
import java.util.Map;

import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.STRUCT_TYPE;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

final class TypeParser extends StatementParser
{
    TypeParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, DeclarationException
    {
        if (scanner.matchAdvance(StatementTokens.TYPE) == null)
            return null;

        // we must be in top-level block, not code body
        if (context.stack().getCurrentBlock().getType() != Stack.BlockKind.TOPLEVEL)
            throw scanner.parseFailure("Must be in top level block to declare types");

        String typeName = scanner.required(SyntaxTokens.IDENTIFIER);
        Description identified = context.identify(typeName);
        if (identified != null)
            scanner.parseFailure(new RedeclarationException(typeName, identified));

        // go to next line, parse elements
        Map<String, TYPE<? extends DATA>> elements = new HashMap<String, TYPE<? extends DATA>>();
        super.requireEOL(scanner);
        while (scanner.matchAdvance(StatementTokens.END) == null)
        {
            String elementName = scanner.required(SyntaxTokens.IDENTIFIER);
            if (elements.containsKey(elementName))
                throw scanner.parseFailure("Duplicate identifier");

            TYPE<? extends DATA> type = context.parseHelper().requireTypeDeclaration(scanner);
            super.requireEOL(scanner);

            elements.put(elementName, type);
        }
        scanner.required(StatementTokens.TYPE);

        // create a new type and add it to the module
        STRUCT_TYPE newType = new STRUCT_TYPE(typeName, elements);
        context.moduleBuilder().addType(newType);

        // END TYPE parsed, required EOL is parsed by StatementParser
        return NOP.INSTANCE;
    }
}
