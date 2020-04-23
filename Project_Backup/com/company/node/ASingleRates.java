/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class ASingleRates extends PRates
{
    private TTString _tString_;
    private TTEqual _tEqual_;
    private TTFloat _tFloat_;
    private TTSemicolon _tSemicolon_;

    public ASingleRates()
    {
        // Constructor
    }

    public ASingleRates(
        @SuppressWarnings("hiding") TTString _tString_,
        @SuppressWarnings("hiding") TTEqual _tEqual_,
        @SuppressWarnings("hiding") TTFloat _tFloat_,
        @SuppressWarnings("hiding") TTSemicolon _tSemicolon_)
    {
        // Constructor
        setTString(_tString_);

        setTEqual(_tEqual_);

        setTFloat(_tFloat_);

        setTSemicolon(_tSemicolon_);

    }

    @Override
    public Object clone()
    {
        return new ASingleRates(
            cloneNode(this._tString_),
            cloneNode(this._tEqual_),
            cloneNode(this._tFloat_),
            cloneNode(this._tSemicolon_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseASingleRates(this);
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

    public TTEqual getTEqual()
    {
        return this._tEqual_;
    }

    public void setTEqual(TTEqual node)
    {
        if(this._tEqual_ != null)
        {
            this._tEqual_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tEqual_ = node;
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

    public TTSemicolon getTSemicolon()
    {
        return this._tSemicolon_;
    }

    public void setTSemicolon(TTSemicolon node)
    {
        if(this._tSemicolon_ != null)
        {
            this._tSemicolon_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tSemicolon_ = node;
    }

    @Override
    public String toString()
    {
        return ""
            + toString(this._tString_)
            + toString(this._tEqual_)
            + toString(this._tFloat_)
            + toString(this._tSemicolon_);
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

        if(this._tEqual_ == child)
        {
            this._tEqual_ = null;
            return;
        }

        if(this._tFloat_ == child)
        {
            this._tFloat_ = null;
            return;
        }

        if(this._tSemicolon_ == child)
        {
            this._tSemicolon_ = null;
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

        if(this._tEqual_ == oldChild)
        {
            setTEqual((TTEqual) newChild);
            return;
        }

        if(this._tFloat_ == oldChild)
        {
            setTFloat((TTFloat) newChild);
            return;
        }

        if(this._tSemicolon_ == oldChild)
        {
            setTSemicolon((TTSemicolon) newChild);
            return;
        }

        throw new RuntimeException("Not a child.");
    }
}
