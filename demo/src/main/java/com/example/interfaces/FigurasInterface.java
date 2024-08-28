package com.example.interfaces;

import java.awt.Graphics;

public interface FigurasInterface 
{
    @FunctionalInterface
    public interface Drawable 
    {
        void draw(Graphics g);
    }
    
}
