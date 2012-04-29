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

public class Stack
{
    public class Block
    {
        final BlockKind type;

        final CheckedContainer<Variable> variables;

        public Block(BlockKind type)
        {
            this.type = type;
            this.variables = new CheckedContainer<Variable>("variable", Stack.this.caseSensitive);
            Stack.this.frames.peek().blocks.push(this);
        }

        public final BlockKind getType()
        {
            return this.type;
        }

        @Override
        public final String toString()
        {
            return this.variables.toString();
        }
    }

    public enum BlockKind
    {
        CODE, FOR, FUNCTION, IF, SUB, TOPLEVEL, DOWHILE
    }

    public final class ForBlock extends Block
    {
        private final String counterVariableName;

        public ForBlock(String counterVariableName)
        {
            super(BlockKind.FOR);
            if (counterVariableName == null)
                throw new NullPointerException("counterVariableName");
            this.counterVariableName = counterVariableName;
        }

        public String getCounterVariableName()
        {
            return this.counterVariableName;
        }
    }

    public final class Frame
    {
        final java.util.Stack<Block> blocks = new java.util.Stack<Block>();

        final String callName;

        /**
         * @param callName -
         *            is a function or sub name which is currently executed.
         * @param withBlock
         */
        public Frame(String callName, BlockKind blockType)
        {
            if (callName == null)
                throw new IllegalArgumentException("callName");
            if (blockType == null)
                throw new IllegalArgumentException("blockType");
            this.callName = callName;
            Stack.this.frames.push(this);
            Stack.this.new Block(blockType);
        }

        @Override
        public String toString()
        {
            return this.blocks.toString();
        }
    }

    final java.util.Stack<Frame> frames = new java.util.Stack<Frame>();

    final boolean caseSensitive;

    public Stack(boolean caseSensitive)
    {
        this.caseSensitive = caseSensitive;
    }

    /**
     * Leave current block and remove it from current frame.
     * 
     * @return
     */
    public final Block blockEnd()
    {
        return this.frames.peek().blocks.pop();
    }

    public final boolean containsVariable(String identifier)
    {
        java.util.Stack<Block> blocks = this.frames.peek().blocks;
        for (Block block : blocks)
            if (block.variables.contains(identifier))
                return true;
        return false;
    }

    /**
     * Find and return block of given type within one, current frame. Lookup is done from the top of
     * the block stack, i.e. most recent first.
     * 
     * @return
     */
    public final Block findBlock(BlockKind bt)
    {
        java.util.Stack<Block> blocks = this.frames.peek().blocks;
        for (int i = blocks.size() - 1; i >= 0; i--)
        {
            Block block = blocks.get(i);
            if (block.type == bt)
                return block;
            else if (bt.getClass().isInstance(block.type))
                return block;
        }
        return null;
    }

    final Block findBlockContainingVariable(String identifier)
    {
        java.util.Stack<Block> blocks = this.frames.peek().blocks;
        for (Block block : blocks)
            if (block.variables.contains(identifier))
                return block;
        return null;
    }

    /**
     * Returns the topmost block of the topmost frame.
     * 
     * @return type of current block.
     */
    public final Block getCurrentBlock()
    {
        return this.frames.peek().blocks.peek();
    }

    public final String getCurrentFrameName()
    {
        return this.frames.peek().callName;
    }

    /**
     * Leave current frame.
     */
    public final void popFrame()
    {
        this.frames.pop();
    }

    public void addVariable(Variable variable) throws RedeclarationException
    {
        getCurrentBlock().variables.add(variable.getName(), variable);
    }

    public Variable getVariable(String identifier) throws UndeclaredIdentifierException
    {
        Block block2 = findBlockContainingVariable(identifier);
        if (block2 != null)
            return block2.variables.get(identifier);
        return null;
    }
}
