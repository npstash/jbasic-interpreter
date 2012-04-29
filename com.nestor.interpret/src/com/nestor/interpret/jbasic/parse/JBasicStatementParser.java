/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-02-03
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;

public final class JBasicStatementParser implements Parser<Statement>
{
    private final Parser<Statement> codeParser;

    public JBasicStatementParser(Configurator configurator)
    {
        ChoiceParser<Statement> statements = new ChoiceParser<Statement>();

        // empty-line and comment parsers first to improve performance
        statements.addParser(new NOPParser());
        statements.addParser(new CommentParser());

        // functions and subs, properly distinguish them from variables,
        // which can be created at the time of use
        statements.addParser(new CallParser());

        // IF and Control
        statements.addParser(new IfParser());
        statements.addParser(new EndParser());

        // LOOPS
        statements.addParser(new ForParser());
        statements.addParser(new ForNextParser());
        statements.addParser(new DoWhileParser());
        statements.addParser(new DoWhileLoopParser());
        statements.addParser(new ExitParser());

        // DECLARATIONS
        statements.addParser(new DimParser());
        statements.addParser(new RedimParser());
        statements.addParser(new ConstantParser());
        statements.addParser(new LetParser());
        statements.addParser(new TypeParser());

        // LANG-SPECIFIC
        configurator.addStatementParsers(statements);

        this.codeParser = statements;
    }

    public String describe()
    {
        return StatementParser.STATEMENT_DESCRIPTION;
    }

    public Statement parse(Scanner scanner, ParseEnv env) throws ParseException
    {
        return this.codeParser.parse(scanner, env);
    }
}
