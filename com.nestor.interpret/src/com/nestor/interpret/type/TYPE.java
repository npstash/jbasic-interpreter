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

import java.io.Serializable;

import com.nestor.shared.Description;

public interface TYPE<D extends DATA> extends Description, Serializable
{
    /**
     * @param other
     * @return <code>true</code> if this and other {@link TYPE} are equal.
     */
    public boolean areEqual(TYPE other);

    /**
     * @return type name
     */
    public String getName();

    /**
     * Checks whether given {@link TYPE} is a sub type if this {@link TYPE} or if they are equal.
     * 
     * @param other
     * @return <code>true</code> when this {@link TYPE} is equal or super type of given
     *         {@link TYPE}
     */
    public boolean isAssignableFrom(TYPE other);

    /**
     * Checks wheter given {@link DATA} instance is represented by this {@link TYPE}
     * 
     * @param value
     * @return
     */
    public boolean isInstance(DATA value);

    /**
     * Create a new instance of data represented by this {@link TYPE}. It will be initialized to
     * default values, even if this is a complex type.
     * 
     * @return new {@link DATA} instance which type is this {@link TYPE}
     */
    public D newInstance();

    /**
     * Whether this type needs qualifiers
     * 
     * @return
     */
    public boolean isQualifiable();

    /**
     * Returns true if promotion is prossible.
     * 
     * @param toType
     * @return
     */
    public boolean canPromote(TYPE toType);

    /**
     * Returns instance of DATA of type passed in as argument, which has the same value as this
     * DATA. Promotion is only available between certain types, like from {@link INTEGER} to
     * {@link DOUBLE}.
     * 
     * @param
     * <P>
     * @param toType
     * @return
     */
    public <P extends DATA> P promote(D value, TYPE<P> toType);
}
