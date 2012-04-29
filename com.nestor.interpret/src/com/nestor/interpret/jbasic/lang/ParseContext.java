/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-28
 ****************************************************************************/

package com.nestor.interpret.jbasic.lang;

import com.nestor.interpret.jbasic.parse.JBasicParser;
import com.nestor.interpret.jbasic.parse.JBasicValueParser;
import com.nestor.interpret.jbasic.parse.ParsingHelper;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.profile.LangConstants;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.Parser;
import com.nestor.shared.Description;

public final class ParseContext implements ParseEnv
{
    private final ParseEnv env;

    private LangConstants langConstants;

    private final ModuleBuilder module;

    private final ParsingHelper parseHelper = new ParsingHelper(this);

    private final Stack stack;

    private final JBasicParser topLevelParser;

    private JBasicValueParser typeParser;

    public ParseContext(ModuleBuilder module, ParseEnv env, Configurator configurator)
    {
        this.module = module;
        this.env = env;
        this.stack = new Stack(configurator.identifiersCaseSensitive());
        this.topLevelParser = new JBasicParser(configurator);
        this.typeParser = new JBasicValueParser(configurator);
        this.langConstants = configurator.getLangConstants();
    }

    public void error(String msg)
    {
        this.env.error(msg);
    }

    public final LangConstants getLangConstants()
    {
        return this.langConstants;
    }

    public Description identify(String identifier)
    {
        try
        {
            // function
            if (this.module.containsFunction(identifier))
                return this.module.getFunction(identifier);
            // sub
            if (this.module.containsSub(identifier))
                return this.module.getSub(identifier);
            // type
            if (this.module.containsType(identifier))
                return this.module.getType(identifier);
            // global var
            if (this.module.containsVariable(identifier))
                return this.module.getVariable(identifier);
            // constant
            if (this.module.containsConstant(identifier))
                return this.module.getConstant(identifier);
            // local var
            if (this.stack.containsVariable(identifier))
                return this.stack.getVariable(identifier);
        } catch (UndeclaredIdentifierException e)
        {
            throw new RuntimeException();
        }
        return null;
    }

    public void log(String msg)
    {
        this.env.log(msg);
    }

    public ModuleBuilder moduleBuilder()
    {
        return this.module;
    }

    public ParsingHelper parseHelper()
    {
        return this.parseHelper;
    }

    public Stack stack()
    {
        return this.stack;
    }

    public Parser<Statement> statementParser()
    {
        return this.topLevelParser.getStatementParser();
    }

    public Parser<Statement> topLevelParser()
    {
        return this.topLevelParser;
    }

    public final JBasicValueParser typeParser()
    {
        return this.typeParser;
    }

    public void warning(String msg)
    {
        this.env.warning(msg);
    }
}
