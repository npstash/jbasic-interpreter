/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-30
 ****************************************************************************/

package com.nestor.interpret.jbasic;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.nestor.interpret.jbasic.parse.InterpretException;
import com.nestor.interpret.jbasic.profile.qbasic.QBasicConfigurator;
import com.nestor.interpret.jbasic.profile.qbasic.services.simple.SimpleRuntimeServiceProvider;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.parse.SimpleParseEnv;

public class JBasicStarter
{
    public static void main(String[] args)
    {
        new JBasicStarter(args);
    }

    private static void printUsage()
    {
        System.out.println("Usage {-compile | -run} <file-name>");
    }

    private boolean compile = false;

    private boolean run = false;

    private String runFile = null;

    private String sourceFile = null;

    public JBasicStarter(String[] args)
    {
        try
        {
            parseOptions(args);
        } catch (IllegalArgumentException e1)
        {
            printUsage();
            System.exit(1);
        }

        if (this.compile)
        {
            QBasicConfigurator qbasicConfigurator = new QBasicConfigurator();
            SimpleParseEnv env = new SimpleParseEnv();
            try
            {
                JBasicScannerFactory scannerFactory = new JBasicScannerFactory(qbasicConfigurator,
                        env);
                Scanner scanner = scannerFactory.createQBasicScanner(new FileReader(this.sourceFile));

                ByteArrayOutputStream compilierOutput = new ByteArrayOutputStream();
                JBasicCompiler interpreter = new JBasicCompiler(qbasicConfigurator);
                interpreter.compile(this.sourceFile, scanner, env, compilierOutput);

                compilierOutput.close();
                FileOutputStream fileOutput = new FileOutputStream(this.runFile);
                fileOutput.write(compilierOutput.toByteArray());
                fileOutput.close();
            } catch (IOException e)
            {
                printError(e);
            } catch (ParseException e)
            {
                printError(e);
            } catch (InterpretException e)
            {
                printError(e);
            }
        }
        if (this.run)
            try
            {
                ObjectInputStream oos = new ObjectInputStream(new FileInputStream(this.runFile));
                JBasicProgram program = (JBasicProgram) oos.readObject();
                oos.close();
                program.run(new SimpleRuntimeServiceProvider());
            } catch (FileNotFoundException e)
            {
                printError(e);
            } catch (IOException e)
            {
                printError(e);
            } catch (ClassNotFoundException e)
            {
                printError(e);
            } catch (RuntimeException e)
            {
                printException(e);
            }
    }

    private void parseOptions(String[] args) throws IllegalArgumentException
    {
        for (int i = 0; i < args.length; i++)
        {
            String arg = args[i];
            if (arg.equals("-compile"))
            {
                this.compile = true;
                continue;
            }
            if (arg.endsWith("-run"))
            {
                this.run = true;
                continue;
            }
            if (i != args.length - 1)
                throw new IllegalArgumentException("File name should be last element");
            // last argument
            if (this.compile)
            {
                this.sourceFile = arg;
                if (this.run)
                {
                    int idx = arg.lastIndexOf('.');
                    if (idx < 0)
                        this.runFile = arg + ".ser";
                    else
                        this.runFile = arg.substring(0, idx) + ".ser";
                }
            } else if (this.run)
                this.runFile = arg;
        }
        if (!this.compile && !this.run)
            throw new IllegalArgumentException("Specify either 'compile' or 'run'");
        if (this.sourceFile == null && this.runFile == null)
            throw new IllegalArgumentException("Specify file name");
    }

    private void printException(Throwable e)
    {
        System.out.println("Exception occured: " + e.getMessage());
        e.printStackTrace();
    }

    private void printError(Throwable e)
    {
        // System.out.println(e.getMessage());
        System.out.println("\n=====================================================\n");
        // StackTraceElement[] stackTrace = e.getStackTrace();
        // for (StackTraceElement element : stackTrace)
        // {
        // System.out.println(element);
        // }
        e.printStackTrace();
        System.exit(2);
    }
}