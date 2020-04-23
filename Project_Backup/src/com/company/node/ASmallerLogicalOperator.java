/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ASmallerLogicalOperator extends PLogicalOperator
{
    private TTSmaller _tSmaller_;

    public ASmallerLogicalOperator()
    {
        // Constructor
    }

    public ASmallerLogicalOperator(
        @SuppressWarnings("hiding") TTSmaller _tSmaller_)
    {
        // Constructor
        setTSmaller(_tSmaller_);

    }

    @Override
    public Object clone()
    {
        return new ASmallerLogicalOperator(
            cloneNode(this._tSmaller_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASmallerLogicalOperator(this);
    }

    public TTSmaller getTSmaller()
    {
        return this._tSmaller_;
    }

    public void setTSmaller(TTSmaller node)
    {
        if(this._tSmaller_ != null)
        {
            this._tSmaller_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tSmaller_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tSmaller_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tSmaller_ == child)
        {
            this._tSmaller_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tSmaller_ == oldChild)
        {
            setTSmaller((TTSmaller) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
