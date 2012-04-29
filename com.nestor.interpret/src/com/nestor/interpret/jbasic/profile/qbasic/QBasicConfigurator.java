/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 6 Dec 2007
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.profile.LangConstants;
import com.nestor.interpret.jbasic.profile.qbasic.libs.Graphics;
import com.nestor.interpret.jbasic.profile.qbasic.libs.Terminal;
import com.nestor.interpret.jbasic.profile.qbasic.parse.LineInputParser;
import com.nestor.interpret.jbasic.profile.qbasic.parse.PrintParser;
import com.nestor.interpret.jbasic.profile.qbasic.parse.QNumberValueParser;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.token.TokenContainer;
import com.nestor.interpret.type.DATA;

public class QBasicConfigurator implements Configurator
{
    public void addStatementParsers(ChoiceParser<Statement> parsers)
    {
        parsers.addParser(new PrintParser());
        parsers.addParser(new LineInputParser());
    }

    public void addTokens(TokenContainer tokens)
    {
        tokens.addTokens(QBasicTokens.class);
        tokens.addToken(QNumberValueParser.QINTEGER_TOKEN);
        tokens.addToken(QNumberValueParser.QDOUBLE_TOKEN);
    }

    public void addTypeValueParsers(ChoiceParser<Value<? extends DATA>> parsers)
    {
        parsers.addParser(QNumberValueParser.PARSER);
    }

    public boolean identifiersCaseSensitive()
    {
        return false;
    }

    public LangConstants getLangConstants()
    {
        return QBasicConstants.getInstance();
    }

    public void prepareModule(ModuleBuilder moduleBuilder) throws DeclarationException
    {
        new Terminal().load(moduleBuilder);
        new Graphics().load(moduleBuilder);
    }
}
