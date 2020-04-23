/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ASingleProtoexstend extends PProtoexstend
{
    private TTComma _tComma_;
    private TTString _tString_;

    public ASingleProtoexstend()
    {
        // Constructor
    }

    public ASingleProtoexstend(
        @SuppressWarnings("hiding") TTComma _tComma_,
        @SuppressWarnings("hiding") TTString _tString_)
    {
        // Constructor
        setTComma(_tComma_);

        setTString(_tString_);

    }

    @Override
    public Object clone()
    {
        return new ASingleProtoexstend(
            cloneNode(this._tComma_),
            cloneNode(this._tString_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleProtoexstend(this);
    }

    public TTComma getTComma()
    {
        return this._tComma_;
    }

    public void setTComma(TTComma node)
    {
        if(this._tComma_ != null)
        {
            this._tComma_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tComma_ = node;
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
            + toString(this._tComma_)
            + toString(this._tString_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tComma_ == child)
        {
            this._tComma_ = null;
            return;
        }

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
        if(this._tComma_ == oldChild)
        {
            setTComma((TTComma) newChild);
            return;
        }

        if(this._tString_ == oldChild)
        {
            setTString((TTString) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
