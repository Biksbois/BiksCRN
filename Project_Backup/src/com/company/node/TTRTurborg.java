/* This file was generated by SableCC (http://www.sablecc.org/). */

package com.company.node;

import com.company.analysis.*;

@SuppressWarnings("nls")
public final class TTRTurborg extends Token
{
    public TTRTurborg(String text)
    {
        setText(text);
    }

    public TTRTurborg(String text, int line, int pos)
    {
        setText(text);
        setLine(line);
        setPos(pos);
    }

    @Override
    public Object clone()
    {
      return new TTRTurborg(getText(), getLine(), getPos());
    }

    @Override
    public void apply(Switch sw)
    {
        ((Analysis) sw).caseTTRTurborg(this);
    }
}
