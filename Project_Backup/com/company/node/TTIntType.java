/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class TTIntType extends Token
{
    public TTIntType(String text)
    {
        setText(text);
    }

    public TTIntType(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TTIntType(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTTIntType(this);
    }
}
