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

package com.nestor.interpret.jbasic;

import java.io.Reader;
import java.lang.ref.SoftReference;

import com.nestor.interpret.expression.arithmetic.ArithmeticTokens;
import com.nestor.interpret.expression.arithmetic.NumberValueParser;
import com.nestor.interpret.expression.bool.BooleanTokens;
import com.nestor.interpret.expression.bool.BooleanValueParser;
import com.nestor.interpret.expression.relational.RelationalTokens;
import com.nestor.interpret.expression.string.StringValueParser;
import com.nestor.interpret.jbasic.parse.StatementParser;
import com.nestor.interpret.jbasic.parse.StatementTokens;
import com.nestor.interpret.jbasic.parse.SyntaxTokens;
import com.nestor.interpret.jbasic.profile.Configurator;
import com.nestor.interpret.parse.LineScanner;
import com.nestor.interpret.parse.ParseEnv;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.parse.Skip;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.token.TokenContainer;

/**
 * Creates qbasic scanners for given input - strings and readers. It adds standard tokens to the
 * {@link LineScanner}. Those include: boolean tokens ({@link BooleanTokens}), arithmetic tokens ({@link ArithmeticTokens}),
 * relational tokens ({@link RelationalTokens}), standard punctation tokens, quick basic statement
 * tokens and some basic tokens like numbers, booleans, strings and EOL.
 * 
 * Created {@link LineScanner} is using a {@link Skip} which skips spaces and tabs only.
 */
public class JBasicScannerFactory
{
    private final Configurator configurator;

    private SoftReference<TokenContainer> tokensRef = null;

    private final ParseEnv env;

    public JBasicScannerFactory(Configurator configurator, ParseEnv env)
    {
        this.configurator = configurator;
        this.env = env;
    }

    public Scanner createQBasicScanner(Reader reader)
    {
        return new LineScanner(this.env, getScannerSkip(), getTokenCollection(), reader);
    }

    public Scanner createQBasicScanner(String input)
    {
        return new LineScanner(this.env, getScannerSkip(), getTokenCollection(), input);
    }

    private static Skip getScannerSkip()
    {
        return new Skip()
        {
            public boolean skip(char c)
            {
                return c == ' ' || c == '\t';
            }
        };
    }

    private synchronized TokenContainer getTokenCollection()
    {
        if (this.tokensRef == null || this.tokensRef.get() == null)
        {
            TokenContainer tokens = new TokenContainer();

            tokens.addTokens(BooleanTokens.class);
            tokens.addTokens(ArithmeticTokens.class);
            tokens.addTokens(RelationalTokens.class);
            tokens.addTokens(StandardTokens.class);
            tokens.addTokens(StatementTokens.class);

            tokens.addToken(NumberValueParser.DOUBLE_TOKEN);
            tokens.addToken(NumberValueParser.INTEGER_TOKEN);
            tokens.addToken(BooleanValueParser.BOOLEAN_TOKEN);
            tokens.addToken(StringValueParser.STRING_TOKEN);

            this.configurator.addTokens(tokens);

            // SyntaxTokens contain identifier token which matches all words!
            tokens.addTokens(SyntaxTokens.class);
            // carefully with this one - make sure that lines cannot be split
            // and it is not skipped by scanner
            tokens.addToken(StatementParser.EOL);

            return (this.tokensRef = new SoftReference<TokenContainer>(tokens)).get();
        }
        return this.tokensRef.get();
    }
}
