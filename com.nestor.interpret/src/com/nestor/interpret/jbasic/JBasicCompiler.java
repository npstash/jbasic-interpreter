/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-28
 ****************************************************************************/

package com.nestor.interpret.jbasic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import com.nestor.interpret.jbasic.lang.ModuleBuilder;
import com.nestor.interpret.jbasic.lang.ModuleFactory;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.Stack;
import com.nestor.interpret.jbasic.parse.BlockBuilder;
import com.nestor.interpret.jbasic.parse.InterpretException;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;

public final class JBasicCompiler
{
    private final Configurator configurator;

    public JBasicCompiler(Configurator configurator)
    {
        this.configurator = configurator;
    }

    private Statement compile(String name, Scanner scanner, ParseContext context)
            throws ParseException
    {
        context.log("Parsing " + name);
        context.stack().new Frame("MAIN-PARSING", Stack.BlockKind.TOPLEVEL);
        BlockBuilder block = new BlockBuilder(false);
        while (true)
        {
            Statement statement = context.topLevelParser().parse(scanner, context);
            if (statement != null)
                block.addStatement(statement);
            else if (!scanner.end())
                throw scanner.expected(context.topLevelParser());
            else
                break;
        }
        context.stack().popFrame();
        return block.getStatementNonNull();
    }

    public void compile(String name, Scanner scanner, ParseEnv env, OutputStream output)
            throws IOException, ParseException, InterpretException
    {
         JBasicProgram program = compile(name, scanner, env);
        env.log("Serializing " + name);
        ObjectOutputStream oos;
        if (output instanceof ObjectOutputStream)
            oos = (ObjectOutputStream) output;
        else
            oos = new ObjectOutputStream(output);
        oos.writeObject(program);
    }

    public JBasicProgram compile(String name, Scanner scanner, ParseEnv env) throws ParseException,
            InterpretException
    {
        // new context and frame for the time of parsing
        ModuleBuilder moduleBuilder = ModuleFactory.prepareModuleBuilder(name, this.configurator);
        ParseContext context = new ParseContext(moduleBuilder, env, this.configurator);

        Statement body = compile(name, scanner, context);
        return new JBasicProgram(moduleBuilder.buildModule(body));
    }
}