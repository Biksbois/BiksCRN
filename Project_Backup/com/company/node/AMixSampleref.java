/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class AMixSampleref extends PSampleref
{
    private TTEqual _tEqual_;
    private TTMixdcl _tMixdcl_;
    private TTLParen _tLParen_;
    private TTString _tString_;
    private TTComma _tComma_;
    private PProtocolparam _protocolparam_;
    private TTRParen _tRParen_;
    private TTSemicolon _tSemicolon_;

    public AMixSampleref()
    {
        // Constructor
    }

    public AMixSampleref(
        @SuppressWarnings("hiding") TTEqual _tEqual_,
        @SuppressWarnings("hiding") TTMixdcl _tMixdcl_,
        @SuppressWarnings("hiding") TTLParen _tLParen_,
        @SuppressWarnings("hiding") TTString _tString_,
        @SuppressWarnings("hiding") TTComma _tComma_,
        @SuppressWarnings("hiding") PProtocolparam _protocolparam_,
        @SuppressWarnings("hiding") TTRParen _tRParen_,
        @SuppressWarnings("hiding") TTSemicolon _tSemicolon_)
    {
        // Constructor
        setTEqual(_tEqual_);

        setTMixdcl(_tMixdcl_);

        setTLParen(_tLParen_);

        setTString(_tString_);

        setTComma(_tComma_);

        setProtocolparam(_protocolparam_);

        setTRParen(_tRParen_);

        setTSemicolon(_tSemicolon_);

    }

    @Override
    public Object clone()
    {
        return new AMixSampleref(
            cloneNode(this._tEqual_),
            cloneNode(this._tMixdcl_),
            cloneNode(this._tLParen_),
            cloneNode(this._tString_),
            cloneNode(this._tComma_),
            cloneNode(this._protocolparam_),
            cloneNode(this._tRParen_),
            cloneNode(this._tSemicolon_));
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseAMixSampleref(this);
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

    public TTMixdcl getTMixdcl()
    {
        return this._tMixdcl_;
    }

    public void setTMixdcl(TTMixdcl node)
    {
        if(this._tMixdcl_ != null)
        {
            this._tMixdcl_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tMixdcl_ = node;
    }

    public TTLParen getTLParen()
    {
        return this._tLParen_;
    }

    public void setTLParen(TTLParen node)
    {
        if(this._tLParen_ != null)
        {
            this._tLParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tLParen_ = node;
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

    public PProtocolparam getProtocolparam()
    {
        return this._protocolparam_;
    }

    public void setProtocolparam(PProtocolparam node)
    {
        if(this._protocolparam_ != null)
        {
            this._protocolparam_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._protocolparam_ = node;
    }

    public TTRParen getTRParen()
    {
        return this._tRParen_;
    }

    public void setTRParen(TTRParen node)
    {
        if(this._tRParen_ != null)
        {
            this._tRParen_.parent(null);
        }

        if(node != null)
        {
            if(node.parent() != null)
            {
                node.parent().removeChild(node);
            }

            node.parent(this);
        }

        this._tRParen_ = node;
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
            + toString(this._tEqual_)
            + toString(this._tMixdcl_)
            + toString(this._tLParen_)
            + toString(this._tString_)
            + toString(this._tComma_)
            + toString(this._protocolparam_)
            + toString(this._tRParen_)
            + toString(this._tSemicolon_);
    }

    @Override
    void removeChild(@SuppressWarnings("unused") Node child)
    {
        // Remove child
        if(this._tEqual_ == child)
        {
            this._tEqual_ = null;
            return;
        }

        if(this._tMixdcl_ == child)
        {
            this._tMixdcl_ = null;
            return;
        }

        if(this._tLParen_ == child)
        {
            this._tLParen_ = null;
            return;
        }

        if(this._tString_ == child)
        {
            this._tString_ = null;
            return;
        }

        if(this._tComma_ == child)
        {
            this._tComma_ = null;
            return;
        }

        if(this._protocolparam_ == child)
        {
            this._protocolparam_ = null;
            return;
        }

        if(this._tRParen_ == child)
        {
            this._tRParen_ = null;
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
        if(this._tEqual_ == oldChild)
        {
            setTEqual((TTEqual) newChild);
            return;
        }

        if(this._tMixdcl_ == oldChild)
        {
            setTMixdcl((TTMixdcl) newChild);
            return;
        }

        if(this._tLParen_ == oldChild)
        {
            setTLParen((TTLParen) newChild);
            return;
        }

        if(this._tString_ == oldChild)
        {
            setTString((TTString) newChild);
            return;
        }

        if(this._tComma_ == oldChild)
        {
            setTComma((TTComma) newChild);
            return;
        }

        if(this._protocolparam_ == oldChild)
        {
            setProtocolparam((PProtocolparam) newChild);
            return;
        }

        if(this._tRParen_ == oldChild)
        {
            setTRParen((TTRParen) newChild);
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
