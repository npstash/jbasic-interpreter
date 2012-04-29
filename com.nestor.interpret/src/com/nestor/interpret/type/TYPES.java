/*****************************************************************************
 * Copyright (C) 2007 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2007-05-01
 ****************************************************************************/

package com.nestor.interpret.type;

public final class TYPES
{
    public static final TYPE<BOOLEAN> BOOLEAN_TYPE = new SIMPLE_TYPE<BOOLEAN>(BOOLEAN.class);

    public static final TYPE<DATA> DATA_TYPE = new DATA_TYPE<DATA>();

    public static final TYPE<DOUBLE> DOUBLE_TYPE = new SIMPLE_TYPE<DOUBLE>(DOUBLE.class)
    {
        private static final long serialVersionUID = 2821972995402948186L;

        @Override
        public boolean canPromote(TYPE toType)
        {
            return INTEGER_TYPE.areEqual(toType);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <P extends DATA> P promote(DOUBLE value, com.nestor.interpret.type.TYPE<P> toType)
        {
            if (INTEGER_TYPE.areEqual(toType))
                // cast here is safe due to check
                return (P) new INTEGER(value.intValue());
            return null;
        }
    };

    public static final TYPE<INTEGER> INTEGER_TYPE = new SIMPLE_TYPE<INTEGER>(INTEGER.class)
    {
        private static final long serialVersionUID = -4185086311021619955L;

        @Override
        public boolean canPromote(TYPE toType)
        {
            return DOUBLE_TYPE.areEqual(toType);
        }

        @Override
        @SuppressWarnings("unchecked")
        public <P extends DATA> P promote(INTEGER value, TYPE<P> toType)
        {
            if (DOUBLE_TYPE.areEqual(toType))
                // cast here is safe due to check
                return (P) new DOUBLE(value.doubleValue());
            return null;
        }
    };

    public static final TYPE<NUMBER> NUMBER_TYPE = new SIMPLE_TYPE<NUMBER>(NUMBER.class);

    public static final TYPE<PRIMITIVE> PRIMITIVE_TYPE = new SIMPLE_TYPE<PRIMITIVE>(PRIMITIVE.class);

    public static final TYPE<STRING> STRING_TYPE = new SIMPLE_TYPE<STRING>(STRING.class);

    private TYPES()
    {
        /* Empty */
    }
}
