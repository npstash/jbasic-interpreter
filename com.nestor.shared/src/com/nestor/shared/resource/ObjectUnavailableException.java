/*****************************************************************************
 * Copyright (C) 2006 Nestor Pawlowski
 * All Rights Reserved
 * 
 * This file may not be copied, photocopied, reproduced, translated, or 
 * reduced to any electronic medium or machine readable form, either in 
 * whole or in part, without prior written agreement from Nestor Pawlowski
 *
 * Created: 2006-12-09
 ****************************************************************************/

package com.nestor.shared.resource;

class ObjectUnavailableException extends RuntimeException
{
    private static final long serialVersionUID = -6248541260161517818L;

    ObjectUnavailableException(Object obj)
    {
        super("Access to object " + obj + " has been revoked.");
    }
}