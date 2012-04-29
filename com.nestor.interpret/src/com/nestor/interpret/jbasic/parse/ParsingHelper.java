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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import com.nestor.interpret.expression.DummyRuntimeEnv;
import com.nestor.interpret.expression.EvaluationException;
import com.nestor.interpret.expression.RuntimeEnv;
import com.nestor.interpret.expression.Value;
import com.nestor.interpret.jbasic.lang.DeclarationException;
import com.nestor.interpret.jbasic.lang.Parameter;
import com.nestor.interpret.jbasic.lang.ParseContext;
import com.nestor.interpret.jbasic.lang.UndeclaredIdentifierException;
import com.nestor.interpret.jbasic.lang.Variable;
import com.nestor.interpret.jbasic.lang.VariableAccessor;
import com.nestor.interpret.jbasic.lang.VariableLookup;
import com.nestor.interpret.jbasic.statement.Statement;
import com.nestor.interpret.parse.ChoiceParser;
import com.nestor.interpret.parse.ParseException;
import com.nestor.interpret.parse.Scanner;
import com.nestor.interpret.token.StandardTokens;
import com.nestor.interpret.type.ARRAY_TYPE;
import com.nestor.interpret.type.ArrayQualifier;
import com.nestor.interpret.type.DATA;
import com.nestor.interpret.type.INTEGER;
import com.nestor.interpret.type.IndexRange;
import com.nestor.interpret.type.Qualifier;
import com.nestor.interpret.type.STRUCT_TYPE;
import com.nestor.interpret.type.StructQualifier;
import com.nestor.interpret.type.TYPE;
import com.nestor.interpret.type.TYPES;

public final class ParsingHelper
{
    private final ParseContext context;

    public ParsingHelper(ParseContext context)
    {
        this.context = context;
    }

    public ArrayQualifier parseArrayQualifier(Scanner scanner, ARRAY_TYPE<? extends DATA> arType,
            Qualifier previous) throws ParseException
    {
        // parse brackets
        if (scanner.matchAdvance(StandardTokens.LP) == null)
            // qualifier not present
            return null;
        // and indexes
        List<Value<INTEGER>> indexList = new ArrayList<Value<INTEGER>>();

        // see if there is any index
        Value<INTEGER> index = parseValue(scanner, TYPES.INTEGER_TYPE);
        if (index != null)
        {
            indexList.add(index);
            for (int i = 1; i < arType.getDimensions().size(); i++)
            {
                scanner.required(StandardTokens.COMMA);
                indexList.add(requireValue(scanner, TYPES.INTEGER_TYPE));
            }
        }
        scanner.required(StandardTokens.RP);
        if (indexList.isEmpty())
            return null;
        return new ArrayQualifier(arType, indexList, previous);
    }

    public <T extends DATA> T parseConstantValue(Scanner scanner, TYPE<T> type)
            throws ParseException
    {
        ChoiceParser<Value> valueParser = new ChoiceParser<Value>();

        // basic types
        valueParser.addParser(this.context.typeParser());

        ExpressionValueParser<T> expressionValueParser = new ExpressionValueParser<T>(type,
                valueParser);
        Value<T> value = expressionValueParser.parse(scanner, this.context);

        if (value == null)
            return null;
        // need a new runtime env now...
        RuntimeEnv env = new DummyRuntimeEnv();
        try
        {
            return value.evaluate(env);
        } catch (EvaluationException e)
        {
            throw scanner.parseFailure(e);
        }
    }

    /**
     * @param scanner
     * @param typeFound
     * @return <code>null</code> when no qualifiers were parsed
     * @throws ParseException
     */
    @SuppressWarnings("unchecked")
    public Qualifier parseQualifiers(Scanner scanner, TYPE<? extends DATA> typeFound)
            throws ParseException
    {
        TYPE currentType = typeFound;
        Qualifier currentQualifier = null;
        while (currentType.isQualifiable())
        {
            // keep currentQualifier non-null whenever possible
            Qualifier nextQualifier = null;
            // TODO get rid of IF's
            if (currentType instanceof ARRAY_TYPE)
            {
                ARRAY_TYPE<? extends DATA> arType = (ARRAY_TYPE<? extends DATA>) currentType;
                nextQualifier = parseArrayQualifier(scanner, arType, currentQualifier);
            } else if (currentType instanceof STRUCT_TYPE)
                nextQualifier = parseStructQualifier(scanner, (STRUCT_TYPE) currentType,
                        currentQualifier);
            else
                throw new InternalError("not implemented for type " + currentType);
            if (nextQualifier == null)
                // no qualifier parsed - return null and let other decide what to do
                return currentQualifier;
            currentQualifier = nextQualifier;
            currentType = currentQualifier.getQualifiedType();
        }
        return currentQualifier;
    }

    private Qualifier parseStructQualifier(Scanner scanner, STRUCT_TYPE structType,
            Qualifier previous) throws ParseException
    {
        // parse dot
        if (scanner.matchAdvance(StandardTokens.DOT) == null)
            return null;

        String fieldName = scanner.required(SyntaxTokens.IDENTIFIER);
        if (structType.getFieldType(fieldName) == null)
            throw scanner.expected("Qualifier for " + structType.describe());

        return new StructQualifier(structType, fieldName, previous);
    }

    /**
     * Require to parse value of given type. The value will be parsed using function, and
     * {@link ExpressionValueParser}, which also parses variables.
     * 
     * @param <T>
     * @param requestedType
     * @param scanner
     * @return
     * @throws ParseException
     */
    public <T extends DATA> Value<T> parseValue(Scanner scanner, TYPE<T> requestedType)
            throws ParseException
    {
        ChoiceParser<Value> valueParser = new ChoiceParser<Value>();
        // first function parser, as the variable parser creates variables for
        // unknown identifiers
        valueParser.addParser(new FunctionValueParser(this.context));
        valueParser.addParser(new ConstantValueParser(this.context));
        // variable parser MUST be last, because when it finds an undeclared
        // identifier, it will declare it!
        valueParser.addParser(new VariableParser(this.context));

        // now basic types
        valueParser.addParser(this.context.typeParser());

        ExpressionValueParser<T> expressionValueParser = new ExpressionValueParser<T>(
                requestedType, valueParser);
        return expressionValueParser.parse(scanner, this.context);
    }

    public VariableAccessor parseVariableAccess(Scanner scanner, String identifier,
            TYPE<? extends DATA> defaultType) throws ParseException, DeclarationException
    {
        VariableLookup lookup;
        lookup = VariableLookup.createLookup(this.context, identifier, defaultType);

        TYPE<? extends DATA> valueType = lookup.getValueType();
        // read qualifiers if this is a complex type
        Qualifier qualifiers = null;
        if (valueType.isQualifiable())
            qualifiers = this.context.parseHelper().parseQualifiers(scanner, valueType);
        // note qualifiers returned may by null
        return new VariableAccessor(lookup, qualifiers);
    }

    public <T extends DATA> T requireConstantValue(Scanner scanner, TYPE<T> type)
            throws ParseException
    {
        T constantValue = parseConstantValue(scanner, type);
        if (constantValue == null)
            throw scanner.expected(type);
        return constantValue;
    }

    /**
     * @param scanner
     * @param parameters
     * @param subCall -
     *            flag indicates that callable is called as a SUB, which means that parenthesis is
     *            not required, unless profile enforces it
     * @return
     * @throws ParseException
     */
    public List<Value<? extends DATA>> requireParameterValueList(Scanner scanner,
            Collection<Parameter> parameters, boolean subCall) throws ParseException
    {
        // if parent
        boolean parenthesisRequired;
        if (this.context.getLangConstants().isParenthesisRequiredForCallableArgs())
            parenthesisRequired = true;
        else if (subCall)
            parenthesisRequired = false;
        else
            parenthesisRequired = parameters.size() > 0;
        if (parenthesisRequired)
            scanner.required(StandardTokens.LP);
        else
            parenthesisRequired = false;// scanner.matchAdvance(StandardTokens.LP) != null;
        // list to store argument values
        LinkedList<Value<? extends DATA>> arguments = new LinkedList<Value<? extends DATA>>();
        int count = 1;
        for (Parameter param : parameters)
        {
            Value<? extends DATA> argument = requireValue(scanner, param.getType());

            // variable are comma separated
            if (count++ < parameters.size())
                scanner.required(StandardTokens.COMMA);
            arguments.add(argument);
        }
        if (parenthesisRequired)
            scanner.required(StandardTokens.RP);
        return arguments;
    }

    /**
     * Read a statements until statement of given class is read. This method does not create a new
     * block on stack.
     * 
     * @param scanner
     * @param context
     * @param until -
     *            parsing ends when statement of context class is parsed (inclusive)
     * @return
     * @throws ParseException
     */
    public Statement requireStatementBlock(Scanner scanner, Class<? extends Statement> until)
            throws ParseException
    {
        BlockBuilder blockBody = new BlockBuilder(true);
        while (true)
        {
            Statement next = this.context.statementParser().parse(scanner, this.context);
            blockBody.addStatement(next);
            if (until.isInstance(next))
                break;
            else if (next == null && scanner.end())
                throw scanner.expected("Unexpected end of file");
            else if (next == null)
                throw scanner.expected(StatementParser.STATEMENT_DESCRIPTION);
        }
        return blockBody.getStatementNonNull();
    }

    /**
     * Request type. Does not parse the 'AS' token. Also does not parse arrays.
     * 
     * @param scanner
     * @param context
     * @return
     * @throws ParseException
     */
    private TYPE<? extends DATA> requireType(Scanner scanner) throws ParseException
    {
        String typeName = scanner.required(SyntaxTokens.IDENTIFIER, "type");
        TYPE<? extends DATA> type;
        try
        {
            type = this.context.moduleBuilder().getType(typeName);
        } catch (UndeclaredIdentifierException e)
        {
            throw scanner.parseFailure(e.getMessage());
        }
        if (type == null)
            throw scanner.expected("type");
        return type;
    }

    /**
     * Request type. Parses the 'AS' token and arrays.
     * 
     * @param scanner
     * @return
     * @throws ParseException
     */
    public TYPE<? extends DATA> requireTypeDeclaration(Scanner scanner) throws ParseException
    {
        // try array
        List<IndexRange> dimensions = null;
        if (scanner.matchAdvance(StandardTokens.LP) != null)
        {
            int low = 0, high = 0;

            // read dimension list
            dimensions = new ArrayList<IndexRange>();
            do
            {
                // a number is mandatory after a comma; otherwise it might an unbound
                // array decl.
                if (dimensions.size() > 0)
                    high = requireConstantValue(scanner, TYPES.INTEGER_TYPE).intValue();
                else
                {
                    INTEGER ihigh = parseConstantValue(scanner, TYPES.INTEGER_TYPE);
                    if (ihigh == null)
                    {
                        // unbound array decl.
                        dimensions.add(new IndexRange());
                        break;
                    }
                    high = ihigh.intValue();
                }
                low = 0;
                if (scanner.matchAdvance(StatementTokens.TO) != null)
                {
                    low = high;
                    high = requireConstantValue(scanner, TYPES.INTEGER_TYPE).intValue();
                }
                dimensions.add(new IndexRange(low, high));
            } while (scanner.matchAdvance(StandardTokens.COMMA) != null);
            scanner.required(StandardTokens.RP);
        }

        // optionally match the AS token
        TYPE<? extends DATA> variableType;
        if (scanner.matchAdvance(StatementTokens.AS) != null)
            // match variable type
            variableType = requireType(scanner);
        else
            // get default type
            variableType = Variable.DEFAULT_TYPE;
        // variable is ready
        if (dimensions == null)
            // not an array
            return variableType;
        return new ARRAY_TYPE<DATA>(dimensions, variableType);
    }

    public <T extends DATA> Value<T> requireValue(Scanner scanner, TYPE<T> requestedType)
            throws ParseException
    {
        Value<T> value = parseValue(scanner, requestedType);
        if (value == null)
            throw scanner.expected(requestedType);
        return value;
    }
}
