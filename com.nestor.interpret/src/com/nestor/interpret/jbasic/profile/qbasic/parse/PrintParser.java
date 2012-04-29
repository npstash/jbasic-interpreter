/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-01-29
 ****************************************************************************/

package com.nestor.interpret.jbasic.profile.qbasic.parse;

import java.util.ArrayList;

import com.nestor.interpret.expression.Value;
import com.nestor.interpret.expression.string.Concatenation;
import com.nestor.interpret.expression.string.StringValueAdapter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.parse.StatementParser;
import com.nestor.interpret.jbasic.profile.qbasic.QBasicTokens;
import com.nestor.interpret.jbasic.profile.qbasic.statement.PrintStatement;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.MultiToken;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.STRING;
import com.nestor.interpret.type.TYPES;

public final class PrintParser extends StatementParser
{
    public PrintParser()
    {
        super(true);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Statement parseStatement(Scanner scanner, ParseContext context) throws ParseException
    {
        if (scanner.matchAdvance(QBasicTokens.PRINT) == null)
            return null;
        MultiToken terminators = new MultiToken(StandardTokens.COMMA, StandardTokens.SEMICOLON);
        ArrayList<Value<STRING>> values = new ArrayList<Value<STRING>>();
        while (true)
        {
            if (scanner.matchAdvance(StandardTokens.COMMA) != null)
            {
                values.add(new STRING("\t"));
                continue;
            }
            if (scanner.matchAdvance(StandardTokens.SEMICOLON) != null)
            {
                values.add(new STRING(" "));
                continue;
            }
            if (super.endOfStatementReached(scanner))
                break;
            // nothing matched, expecting a value
            Value<DATA> value = context.parseHelper().requireValue(scanner, TYPES.DATA_TYPE);
            if (TYPES.STRING_TYPE.isAssignableFrom(value.getValueType()))
            {
                Value<STRING> s = (Value<STRING>) ((Object) value);
                values.add(s);
            } else
                values.add(new StringValueAdapter(value));
            // after matching a value, comma or semicolon is required to continue
            if (terminators.match(scanner) == null)
                break;
        }
        return new PrintStatement(new Concatenation(values));
    }
}
