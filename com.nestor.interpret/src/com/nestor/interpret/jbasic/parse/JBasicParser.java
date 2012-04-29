/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-06-11
 ****************************************************************************/

package com.nestor.interpret.jbasic.parse;

import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Parser;
import com.nestor.interpret.parse.Scanner;

public final class JBasicParser implements Parser<Statement>
{
    private final Parser<Statement> codeParser;

    private JBasicStatementParser statementParser;

    public JBasicParser(Configurator configurator)
    {
        ChoiceParser<Statement> statements = new ChoiceParser<Statement>();

        // empty-line and comment parsers first to improve performance
        statements.addParser(new NOPParser());
        statements.addParser(new CommentParser());

        statements.addParser(new CallableDefParser());
        statements.addParser(new CallableParser());

        this.statementParser = new JBasicStatementParser(configurator);
        statements.addParser(this.statementParser);

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

    public final JBasicStatementParser getStatementParser()
    {
        return this.statementParser;
    }
}
