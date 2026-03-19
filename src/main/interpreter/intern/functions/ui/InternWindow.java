package main.interpreter.intern.functions.ui;

import main.interpreter.Interpreter;
import main.interpreter.action.NewClassObjectAction;
import main.interpreter.action.NumberAction;
import main.interpreter.function.Function;
import main.interpreter.intern.ClassStorage;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.VariableNumber;
import main.interpreter.variable.VariableObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class InternWindow
{
    private Interpreter interpreter;

    private JFrame window;
    private WindowClass contentPanel;

    public InternWindow(Interpreter interpreter)
    {
        this.interpreter = interpreter;

        window = new JFrame();
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        contentPanel = new WindowClass();
        window.setContentPane(contentPanel);
    }

    public void setVisible(boolean visible)
    {
        window.setVisible(visible);
    }

    public void setSize(int width, int height)
    {
        window.setSize(width, height);
    }

    public void setTitle(String name)
    {
        window.setTitle(name);
    }

    public void addDrawHandler(Function function)
    {
        contentPanel.setDrawHandler(function);
    }

    public void centerOnScreen()
    {
        window.setLocationRelativeTo(null);
    }

    public class WindowClass extends JComponent
    {
        //private Clazz canvasClazz;
        //private Scope canvasObject;

        private VariableNumber width;
        private VariableNumber height;
        private VariableObject canvas;

        private Function drawHandler;

        private Graphics2D currentGraphics;

        public WindowClass()
        {
            //this.canvasClazz = interpreter.getCodeFromCacheOrLoad("ui").getGlobalScope().getClassByName("canvas");
            canvas = (VariableObject) (new NewClassObjectAction("canvas", List.of(new NumberAction(ClassStorage.storeObject(this)))).evaluate(interpreter, interpreter.getCodeFromCacheOrLoad("ui").getGlobalScope()));
            canvas.setName("canvas");

            width = new VariableNumber("width", 0);
            height = new VariableNumber("height", 0);
            //canvas = new VariableObject("canvas", canvasObject, canvasClazz);
        }

        public void setDrawHandler(Function drawHandler)
        {
            this.drawHandler = drawHandler;
            drawHandler.getFunctionScope().addVariable(width);
            drawHandler.getFunctionScope().addVariable(height);
            drawHandler.getFunctionScope().addVariable(canvas);
        }

        @Override
        public void paint(Graphics g)
        {
            //super.paint(g);
            Dimension dimension = getSize();

            currentGraphics = (Graphics2D) g;

            width.setValue(dimension.width);
            height.setValue(dimension.height);
            interpreter.executeScope(drawHandler.getFunctionScope(), ScopeType.FUNCTION);
        }

        private Rectangle getStringBounds(String str, float x, float y)
        {
            FontRenderContext frc = currentGraphics.getFontRenderContext();
            GlyphVector gv = currentGraphics.getFont().createGlyphVector(frc, str);
            return gv.getPixelBounds(null, x, y);
        }

        public void drawText(String string, int x, int y)
        {
            currentGraphics.setColor(Color.BLACK);
            currentGraphics.drawString(string, x, y + getStringBounds(string, x, y).height);
            //currentGraphics.drawRect(20, 20, 20, 20);
        }

        public void drawImage(BufferedImage image, int x, int y, int width, int height)
        {
            if(width == 0)
                width = image.getWidth();
            if(height == 0)
                height = image.getHeight();

            currentGraphics.drawImage(image, x, y, width, height, null);
        }

        public void drawImageBytes(byte[] image, int x, int y)
        {
            try
            {
                currentGraphics.drawImage(ImageIO.read(new ByteArrayInputStream(image)), x, y, null);
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
