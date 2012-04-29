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

package com.nestor.interpret.jbasic.profile;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.token.TokenContainer;
import com.nestor.interpret.type.DATA;

public interface Configurator
{
    public void addStatementParsers(ChoiceParser<Statement> parsers);

    public void addTokens(TokenContainer tokens);

    public void addTypeValueParsers(ChoiceParser<Value<? extends DATA>> parsers);

    public LangConstants getLangConstants();

    public void prepareModule(ModuleBuilder moduleBuilder) throws DeclarationException;

    public boolean identifiersCaseSensitive();
}
