package src;

class StackOverflowException extends RuntimeException {
    public StackOverflowException(String message) {
        super(message);
    }
}

class StackUnderflowException extends RuntimeException {
    public StackUnderflowException(String message) {
        super(message);
    }
}

class InvalidOperatorException extends RuntimeException {
    public InvalidOperatorException(String message) {
        super(message);
    }
}

public class TabStack {
    private String[] stack = new String[20];
    private int size = 0;

    public String pop() {
        if (size == 0) {
            throw new StackUnderflowException("Błąd: Próba zdjęcia elementu ze stosu, który jest pusty.");
        }
        size--;
        return stack[size];
    }

    public void push(String a) {
        if (size >= stack.length) {
            throw new StackOverflowException("Błąd: Przekroczono maksymalny rozmiar stosu.");
        }
        stack[size] = a;
        size++;
    }

    public String toString(){
        String tmp = "";
        for(int i = 0; i < size; i++)
            tmp += stack[i] + " ";
        return tmp;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int i) {
        size = i;
    }

    public String showValue(int i) {
        if (i < size)
            return stack[i];
        else return null;
    }
}