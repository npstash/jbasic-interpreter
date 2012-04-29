/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-31
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

final class CallableDefParser extends StatementParser
{
    CallableDefParser()
    {
        super(true);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, InterpretException
    {
        if (scanner.matchAdvance(StatementTokens.DECLARE) == null)
            return null;

        final Token callableType;
        if (scanner.matchAdvance(StatementTokens.FUNCTION) != null)
            callableType = StatementTokens.FUNCTION;
        else if (scanner.matchAdvance(StatementTokens.SUB) != null)
            callableType = StatementTokens.SUB;
        else
            throw scanner.expected(StatementTokens.FUNCTION);

        final String identifier = scanner.required(SyntaxTokens.IDENTIFIER);
        Description identified = context.identify(identifier);
        if (identified != null)
            scanner.parseFailure(new RedeclarationException(identifier, identified));

        // return type - only for functions
        final TYPE<? extends DATA> retType;
        if (callableType == StatementTokens.FUNCTION)
            retType = context.parseHelper().requireTypeDeclaration(scanner);
        else
            retType = null;

        // now parameters, same for all callable types
        LinkedHashMap<String, TYPE<? extends DATA>> args = new LinkedHashMap<String, TYPE<? extends DATA>>();
        if (scanner.matchAdvance(StandardTokens.LP) != null)
        {
            // parse variables; filter is any variable count, but they must not be previously
            // declared
            VariableListFilter filter = new VLOnlyUndeclared();
            VariableListParser varParser = new VariableListParser(context, filter);
            VariableList list = varParser.parse(scanner, context);
            args.putAll(list.getVariables());
            scanner.required(StandardTokens.RP);
        }
        final List<Parameter> argList = new ArrayList<Parameter>(args.size());
        for (String varName : args.keySet())
            argList.add(new Parameter(varName, args.get(varName)));

        if (callableType == StatementTokens.FUNCTION)
            context.moduleBuilder().addFunction(new Function(identifier, retType, argList));
        else
            context.moduleBuilder().addSub(new Sub(identifier, argList));

        // but it does nothing at runtime
        return NOP.INSTANCE;
    }
}
