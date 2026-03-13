package main.input;

import java.util.ArrayList;
import java.util.List;

public class CodeStream
{
    private DataStream dataStream;

    public CodeStream(String code)
    {
        this.dataStream = new DataStream(code);
    }

    public boolean isFinished()
    {
        return dataStream.isAtEnd();
    }

    public String getCode()
    {
        return dataStream.getData();
    }

    public String getNextLine()
    {
        return dataStream.getUntilNextLineToken();
    }

    public String getNextToken()
    {
        return dataStream.getNextToken();
    }

    public String peakNextToken()
    {
        return dataStream.peakNextToken();
    }

    public String getLine()
    {
        return dataStream.getData();
    }

    public String getStringUntilClosingBrackets()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int openBrackets = 1;
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals("("))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(")"))
            {
                openBrackets--;
                if(openBrackets == 0)
                    return stringBuilder.toString();
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        System.err.println("No Closing Bracket Pair Found!");
        return "";
    }

    public CodeStream getCodeStreamUntilOpeningAndClosingBrackets()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int openBrackets = 0;
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals("("))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(")"))
            {
                openBrackets--;
                if(openBrackets == 0)
                    return new CodeStream(stringBuilder.toString());
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        System.err.println("No Closing Bracket Pair Found!");
        return new CodeStream("");
    }

    public CodeStream getCodeStreamUntilClosingBrackets()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int openBrackets = 1;
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals("("))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(")"))
            {
                openBrackets--;
                if(openBrackets == 0)
                    return new CodeStream(stringBuilder.toString());
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        System.err.println("No Closing Bracket Pair Found!");
        return new CodeStream("");
    }

    public CodeStream getCodeStreamUntilClosingCurlyBrackets()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int openBrackets = 1;
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals("{"))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals("}"))
            {
                openBrackets--;
                if(openBrackets == 0)
                    return new CodeStream(stringBuilder.toString());
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        System.err.println("No Closing Bracket Pair Found!");
        return new CodeStream("");
    }

    public CodeStream getCodeStreamUntilOpenAndClosingCurlyBrackets()
    {
        StringBuilder stringBuilder = new StringBuilder();
        int openBrackets = 0;
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals("{"))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals("}"))
            {
                openBrackets--;
                if(openBrackets == 0)
                    return new CodeStream(stringBuilder.toString());
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        System.err.println("No Closing Bracket Pair Found!");
        return new CodeStream("");
    }

    public CodeStream getCodeStreamUntilTopLevelToken(String token)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String nextToken = getNextToken();
        int openBrackets = 1;
        while (nextToken != null)
        {
            if(nextToken.equals("{"))
            {
                openBrackets++;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals("}"))
            {
                openBrackets--;
                stringBuilder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(token))
            {
                if(openBrackets == 1)
                    return new CodeStream(stringBuilder.toString().trim());
                else
                    stringBuilder.append(nextToken).append(" ");
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        return new CodeStream(stringBuilder.toString().trim());
    }

    public CodeStream getCodeStreamUntilToken(String token)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals(token))
            {
                return new CodeStream(stringBuilder.toString().trim());
            }
            else
            {
                stringBuilder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }

        return new CodeStream(stringBuilder.toString().trim());
    }

    public List<CodeStream> splitOnTopLevelToken(String split)
    {
        List<CodeStream> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        String nextToken = getNextToken();
        int openBrackets = 1;
        while (nextToken != null)
        {
            if(nextToken.equals("("))
            {
                openBrackets++;
                builder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(")"))
            {
                openBrackets--;
                builder.append(nextToken).append(" ");
            }
            else if(nextToken.equals(split) && (openBrackets == 1))
            {
                result.add(new CodeStream(builder.toString().trim()));
                builder = new StringBuilder();
            }
            else
            {
                builder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }
        if(!builder.isEmpty())
        {
            result.add(new CodeStream(builder.toString().trim()));
        }

        return result;
    }

    public List<CodeStream> splitOnToken(String split)
    {
        List<CodeStream> result = new ArrayList<>();
        StringBuilder builder = new StringBuilder();

        String nextToken = getNextToken();
        while (nextToken != null)
        {
            if(nextToken.equals(split))
            {
                result.add(new CodeStream(builder.toString().trim()));
                builder = new StringBuilder();
            }
            else
            {
                builder.append(nextToken).append(" ");
            }
            nextToken = getNextToken();
        }
        if(!builder.isEmpty())
        {
            result.add(new CodeStream(builder.toString().trim()));
        }

        return result;
    }
}
