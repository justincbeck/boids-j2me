// Copyright 2002 Nokia Corporation.
//
// THIS SOURCE CODE IS PROVIDED 'AS IS', WITH NO WARRANTIES WHATSOEVER,
// EXPRESS OR IMPLIED, INCLUDING ANY WARRANTY OF MERCHANTABILITY, FITNESS
// FOR ANY PARTICULAR PURPOSE, OR ARISING FROM A COURSE OF DEALING, USAGE
// OR TRADE PRACTICE, RELATING TO THE SOURCE CODE OR ANY WARRANTY OTHERWISE
// ARISING OUT OF ANY PROPOSAL, SPECIFICATION, OR SAMPLE AND WITH NO
// OBLIGATION OF NOKIA TO PROVIDE THE LICENSEE WITH ANY MAINTENANCE OR
// SUPPORT. FURTHERMORE, NOKIA MAKES NO WARRANTY THAT EXERCISE OF THE
// RIGHTS GRANTED HEREUNDER DOES NOT INFRINGE OR MAY NOT CAUSE INFRINGEMENT
// OF ANY PATENT OR OTHER INTELLECTUAL PROPERTY RIGHTS OWNED OR CONTROLLED
// BY THIRD PARTIES
//
// Furthermore, information provided in this source code is preliminary,
// and may be changed substantially prior to final release. Nokia Corporation
// retains the right to make changes to this source code at
// any time, without notice. This source code is provided for informational
// purposes only.
//
// Nokia and Nokia Connecting People are registered trademarks of Nokia
// Corporation.
// Java and all Java-based marks are trademarks or registered trademarks of
// Sun Microsystems, Inc.
// Other product and company names mentioned herein may be trademarks or
// trade names of their respective owners.
//
// A non-exclusive, non-transferable, worldwide, limited license is hereby
// granted to the Licensee to download, print, reproduce and modify the
// source code. The licensee has the right to market, sell, distribute and
// make available the source code in original or modified form only when
// incorporated into the programs developed by the Licensee. No other
// license, express or implied, by estoppel or otherwise, to any other
// intellectual property rights is granted herein.


package com.beckproduct.boids;

import javax.microedition.lcdui.*;


class BoidsCanvas
        extends Canvas
        implements CommandListener, Runnable, BoidsConstants
{
    private static final int MILLIS_PER_TICK = 15;
    private static final int NUM_BOIDS = 3;

    private final BoidsMIDlet midlet;
    private final boolean useColor;
    private final Command exitCommand;
    private volatile Thread animationThread = null;
    private final Boid[] boids = new Boid[NUM_BOIDS];
    private static final int[][] colors =
            {
                    {255, 0, 0},  // red
                    {0, 255, 0},  // green
                    {0, 0, 255},  // blue
                    {255, 255, 0},  // yellow  -- can't see on white background
                    {255, 0, 255},  // magenta
                    {0, 255, 255}   // cyan
            };


    public BoidsCanvas(BoidsMIDlet midlet, Display display)
    {
        this.midlet = midlet;

        useColor = display.isColor();

        for (int i = 0; i < boids.length; ++i)
        {
            int red;
            int green;
            int blue;
            if (useColor)
            {
                int index = i % (colors.length);
                red = colors[index][0];
                green = colors[index][1];
                blue = colors[index][2];
            }
            else
            {
                red = 0;
                green = 0;
                blue = 0;
            }
            boids[i] = new Boid(
                    BoidsUtils.random(5) << FIXED_POINT_SHIFT,
                    BoidsUtils.random(5) << FIXED_POINT_SHIFT,
                    BoidsUtils.random(5) << FIXED_POINT_SHIFT,
                    BoidsUtils.random(5) << FIXED_POINT_SHIFT,
                    red, green, blue,
                    this);
        }

        setCommandListener(this);

        exitCommand = new Command("Exit", Command.EXIT, 1);
        addCommand(exitCommand);
    }


    public synchronized void start()
    {
        animationThread = new Thread(this);
        animationThread.start();
    }


    public synchronized void stop()
    {
        animationThread = null;
    }

    private long fps = 0;
    private long frames = 0;

    public void run()
    {
        Thread currentThread = Thread.currentThread();

        try
        {
            // This ends when animationThread is set to null, or when
            // it is subsequently set to a new thread; either way, the
            // current thread should terminate

            System.gc();

            while (currentThread == animationThread)
            {
                long startTime = System.currentTimeMillis();

                tickBoids();
                repaint(0, 0, getWidth(), getHeight());
                serviceRepaints();
                long timeTaken = System.currentTimeMillis() - startTime;

                if (timeTaken < MILLIS_PER_TICK)
                {
                    synchronized (this)
                    {
                        wait(MILLIS_PER_TICK - timeTaken);
                    }
                }
                else
                {
                    currentThread.yield();
                }

                try
                {
                    fps = 1000 / timeTaken;
                }
                catch (RuntimeException e)
                {
                    System.exit(0);
                }

                currentThread.yield();

                frames++;
            }
        }
        catch (InterruptedException e)
        {
            System.exit(0);
        }
    }


    public void paint(Graphics g)
    {
        int width = getWidth();
        int height = getHeight();
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, width, height);
        g.setColor(0, 0, 0);
        g.drawRect(0, 0, width - 1, height - 1);
        g.setClip(1, 1, width - 2, height - 2);
        drawBoids(g);
        g.drawString(String.valueOf(fps), 10, 5, 0);
    }


    public void commandAction(Command c, Displayable d)
    {
        if (c == exitCommand)
        {
            midlet.exitRequested();
        }
    }


    void applyRule(BoidRule rule, Boid notThisOne)
    {
        for (int i = 0; i < boids.length; ++i)
        {
            if (boids[i] != notThisOne)
            {
                rule.applyTo(boids[i]);
            }
        }
    }


    private synchronized void tickBoids()
    {
        for (int i = 0; i < boids.length; ++i)
        {
            boids[i].tick();
        }
    }


    private synchronized void drawBoids(Graphics g)
    {
        for (int i = 0; i < boids.length; ++i)
        {
            boids[i].draw(g);
        }
    }
}
