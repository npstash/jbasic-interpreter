/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-04-21
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.nestor.interpret.jbasic.lang.Callable;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.Function;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.RedeclarationException;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.lang.Sub;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.statement.FunctionEndStatement;
import com.nestor.interpret.jbasic.statement.NOP;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.jbasic.statement.SubEndStatement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.token.Token;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.TYPE;
import com.nestor.shared.Description;

final class CallableParser extends StatementParser
{
    CallableParser()
    {
        super(false);
    }

    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context)
            throws ParseException, DeclarationException
    {
        final Token callableTokenMatched;
        // find out what kind of 'callable' is this
        if (scanner.matchAdvance(StatementTokens.FUNCTION) != null)
            callableTokenMatched = StatementTokens.FUNCTION;
        else if (scanner.matchAdvance(StatementTokens.SUB) != null)
            callableTokenMatched = StatementTokens.SUB;
        else
            return null;

        String identifier = scanner.required(SyntaxTokens.IDENTIFIER);
        // is it a legal identifier?
        Description identified = context.identify(identifier);
        if (identified != null)
        {
            boolean preDeclared = false;
            // its known, which is either a clash or pre-declaration
            if (callableTokenMatched == StatementTokens.FUNCTION)
            {
                if (context.moduleBuilder().containsFunction(identifier))
                    preDeclared = true;
            } else if (context.moduleBuilder().containsSub(identifier))
                preDeclared = true;
            if (!preDeclared)
                throw new RedeclarationException(identifier, identified);
        }

        // return type - only for functions
        final TYPE<? extends DATA> retType;
        if (callableTokenMatched == StatementTokens.FUNCTION)
            retType = context.parseHelper().requireTypeDeclaration(scanner);
        else
            retType = null;

        // now parameters, same for all callable types
        LinkedHashMap<String, TYPE<? extends DATA>> args = new LinkedHashMap<String, TYPE<? extends DATA>>();
        if (scanner.matchAdvance(StandardTokens.LP) != null)
        {
            VariableListFilter filter = new VLOnlyUndeclared();
            VariableListParser varParser = new VariableListParser(context, filter);
            VariableList list = varParser.parse(scanner, context);
            args.putAll(list.getVariables());
            scanner.required(StandardTokens.RP);
        }
        List<Parameter> argList = new ArrayList<Parameter>(args.size());
        for (String varName : args.keySet())
            argList.add(new Parameter(varName, args.get(varName)));

        final Callable decl;
        if (callableTokenMatched == StatementTokens.FUNCTION)
            decl = new Function(identifier, retType, argList);
        else
            decl = new Sub(identifier, argList);

        final Callable callable;
        callable = decl.resolveDefinition(context);

        // TODO static?
        super.requireEOL(scanner);

        // create a new block
        if (callableTokenMatched == StatementTokens.FUNCTION)
            context.stack().new Frame(identifier, Stack.BlockKind.FUNCTION);
        else
            context.stack().new Frame(identifier, Stack.BlockKind.SUB);

        // add parameters to stack
        for (String varName : args.keySet())
            context.stack().addVariable(new Variable(varName, args.get(varName)));

        // parse body
        final Statement body;
        if (callableTokenMatched == StatementTokens.FUNCTION)
            body = context.parseHelper().requireStatementBlock(scanner, FunctionEndStatement.class);
        else
            body = context.parseHelper().requireStatementBlock(scanner, SubEndStatement.class);
        callable.implement(body);

        // clean up variables declared for the time of parsing
        context.stack().popFrame();

        // actually it is a no-op itself
        return NOP.INSTANCE;
    }
}