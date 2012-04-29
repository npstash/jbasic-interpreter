/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-31
 ****************************************************************************/

package com.nestor.interpret.token;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexToken implements Token
{
    private final Pattern pattern;

    private final String description;

    public RegexToken(String regex)
    {
        this.description = null;
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public RegexToken(String description, String regex)
    {
        this.description = description;
        this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    }

    public RegexToken(String regex, boolean caseSensitive)
    {
        this.description = null;
        if (!caseSensitive)
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        else
            this.pattern = Pattern.compile(regex);
    }

    public RegexToken(String description, String regex, boolean caseSensitive)
    {
        this.description = description;
        if (!caseSensitive)
            this.pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        else
            this.pattern = Pattern.compile(regex);
    }

    public final String match(String input, int offset)
    {
        Matcher matcher = this.pattern.matcher(input.substring(offset));
        if (matcher.lookingAt())
            return matcher.group();
        return null;
    }

    public final boolean isSame(Token obj)
    {
        if (obj instanceof RegexToken)
            return ((RegexToken) obj).pattern.pattern().equals(this.pattern.pattern());
        return false;
    }

    public String describe()
    {
        if (this.description != null)
            return this.description;
        return this.pattern.toString();
    }

    public final boolean isShadowing(Token other)
    {
        if (other instanceof SimpleToken)
        {
            SimpleToken st = (SimpleToken) other;
            // shadowing happens if this token always
            // matches the other token
            Matcher matcher = this.pattern.matcher(st.getLex());
            return matcher.matches();
        }
        if (other instanceof RegexToken)
        {
            RegexToken rt = (RegexToken) other;
            // shadowing must happen if this'
            // tokens pattern is a subset of the
            // other token's pattern from index 0
            return rt.pattern.pattern().indexOf(this.pattern.pattern()) == 0;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return describe();
    }
}
