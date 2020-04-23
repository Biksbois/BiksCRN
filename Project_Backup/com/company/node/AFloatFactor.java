/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AFloatFactor extends PFactor
{
    private TTFloat _tFloat_;

    public AFloatFactor()
    {
        // Constructor
    }

    public AFloatFactor(
        @SuppressWarnings("hiding") TTFloat _tFloat_)
    {
        // Constructor
        setTFloat(_tFloat_);

    }

    @Override
    public Object clone()
    {
        return new AFloatFactor(
            cloneNode(this._tFloat_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAFloatFactor(this);
    }

    public TTFloat getTFloat()
    {
        return this._tFloat_;
    }

    public void setTFloat(TTFloat node)
    {
        if(this._tFloat_ != null)
        {
            this._tFloat_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tFloat_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tFloat_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tFloat_ == child)
        {
            this._tFloat_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tFloat_ == oldChild)
        {
            setTFloat((TTFloat) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
