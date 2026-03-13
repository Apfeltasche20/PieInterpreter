package main.input;

public class DataStream
{
    private String data;
    private int pointer;

    public DataStream(String data)
    {
        this.data = data;
        this.pointer = 0;
    }

    public boolean isAtEnd()
    {
        return pointer >= data.length();
    }

    public String getData()
    {
        return data;
    }

    public String getUntilNextLineToken()
    {
        if(pointer >= data.length())
            return null;

        StringBuilder output = new StringBuilder();
        boolean inQuotes = false;
        while(pointer < data.length())
        {
            char c = data.charAt(pointer++);
            switch (c)
            {
                case ';', '{', '}' -> {
                    output.append(c);
                    if(!inQuotes)
                        return output.toString().trim();
                }
                case '"' -> {
                    output.append(c);
                    inQuotes = !inQuotes;
                }
                default -> {
                    output.append(c);
                }
            }
        }
        return output.toString().trim();
    }

    public String getNextToken()
    {
        if(pointer >= data.length())
            return null;

        StringBuilder output = new StringBuilder();
        boolean inQuotes = false;
        while(pointer < data.length())
        {
            char c = data.charAt(pointer++);
            switch (c)
            {
                case '(', ')', ',', '=', '+', '-', '*', '/', ';', '.', '[', ']' -> {
                    if(output.isEmpty())
                    {
                        output.append(c);
                        return output.toString().trim();
                    }
                    else
                    {
                        if(!inQuotes)
                        {
                            pointer--;
                            return output.toString().trim();
                        }
                        else
                        {
                            output.append(c);
                        }
                    }
                }
                case ' ' -> {
                    if(output.isEmpty())
                        continue;
                    if(!inQuotes)
                        return output.toString().trim();
                    else
                        output.append(c);
                }
                case '"' -> {
                    output.append(c);
                    inQuotes = !inQuotes;
                }
                default -> {
                    output.append(c);
                }
            }
        }
        return output.toString().trim();
    }

    public String peakNextToken()
    {
        int startPointer = pointer;

        if(pointer >= data.length())
            return null;

        StringBuilder output = new StringBuilder();
        boolean inQuotes = false;
        while(pointer < data.length())
        {
            char c = data.charAt(pointer++);
            switch (c)
            {
                case '(', ')', ',', '=', '+', '-', '*', '/', ';', '.' -> {
                    if(output.isEmpty())
                    {
                        output.append(c);
                        pointer = startPointer;
                        return output.toString().trim();
                    }
                    else
                    {
                        if(!inQuotes)
                        {
                            pointer--;
                            pointer = startPointer;
                            return output.toString().trim();
                        }
                        else
                        {
                            output.append(c);
                        }
                    }
                }
                case ' ' -> {
                    if(output.isEmpty())
                        continue;
                    if(!inQuotes)
                    {
                        pointer = startPointer;
                        return output.toString().trim();
                    }
                    else
                        output.append(c);
                }
                case '"' -> {
                    output.append(c);
                    inQuotes = !inQuotes;
                }
                default -> {
                    output.append(c);
                }
            }
        }
        pointer = startPointer;
        return output.toString().trim();
    }
}
