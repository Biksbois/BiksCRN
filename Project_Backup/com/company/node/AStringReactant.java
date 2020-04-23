/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AStringReactant extends PReactant
{
    private TTString _tString_;

    public AStringReactant()
    {
        // Constructor
    }

    public AStringReactant(
        @SuppressWarnings("hiding") TTString _tString_)
    {
        // Constructor
        setTString(_tString_);

    }

    @Override
    public Object clone()
    {
        return new AStringReactant(
            cloneNode(this._tString_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAStringReactant(this);
    }

    public TTString getTString()
    {
        return this._tString_;
    }

    public void setTString(TTString node)
    {
        if(this._tString_ != null)
        {
            this._tString_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tString_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tString_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tString_ == child)
        {
            this._tString_ = null;
            return;
        }

        throw new RuntimeException("Not a child.");
    }

    @Override
    void replaceChild(@SuppressWarnings("unused") Node oldChild, @SuppressWarnings("unused") Node newChild)
    {
        // Replace child
        if(this._tString_ == oldChild)
        {
            setTString((TTString) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
