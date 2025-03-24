package src;

public class ONP {
    private TabStack stack = new TabStack();

    boolean czyPoprawneRownanie(String rownanie) {
        if (!rownanie.endsWith("=")) {
            throw new IllegalArgumentException("Błąd: równanie musi kończyć się znakiem '='");
        }
        return true;
    }

    public String obliczOnp(String rownanie) {

        czyPoprawneRownanie(rownanie);

        stack.setSize(0);
        String wynik = "";
        Double a, b = 0.0;

        for (int i = 0; i < rownanie.length(); i++) {
            char znak = rownanie.charAt(i);

            if (Character.isDigit(znak)) {
                wynik += znak;
                if (!(rownanie.charAt(i + 1) >= '0' && rownanie.charAt(i + 1) <= '9')) {
                    stack.push(wynik);
                    wynik = "";
                }
            } else if (znak == '=') {
                return stack.pop();
            } else if (znak != ' ') {
                try {
                    if (znak != '!') {
                        b = Double.parseDouble(stack.pop());
                        a = Double.parseDouble(stack.pop());
                    } else {
                        a = Double.parseDouble(stack.pop());
                    }
                } catch (Exception e) {
                    throw new IllegalStateException("Błąd: Za mało operantów dla operatora '" + znak + "'");
                }

                switch (znak) {
                    case '+':
                        stack.push((a + b) + "");
                        break;
                    case '-':
                        stack.push((a - b) + "");
                        break;
                    case '*':
                    case 'x':
                        stack.push((a * b) + "");
                        break;
                    case '/':
                        if (b == 0) throw new ArithmeticException("Błąd: Dzielenie przez zero.");
                        stack.push((a / b) + "");
                        break;
                    case '^':
                        stack.push(Math.pow(a, b) + "");
                        break;
                    case 'p': // Pierwiastek kwadratowy
                        if (a < 0) throw new ArithmeticException("Błąd: Pierwiastek z liczby ujemnej.");
                        stack.push(Math.sqrt(a) + "");
                        break;
                    case '%': // Modulo
                        stack.push((a % b) + "");
                        break;
                    case '!': // Silnia
                        if (a.intValue() < 0)
                            throw new ArithmeticException("Błąd: Silnia z liczby ujemnej.");
                        else {
                            stack.push(silnia(a.intValue()) + "");
                        }
                        break;
                    default:
                        throw new InvalidOperatorException("Błąd: Nieobsługiwany operator '" + znak + "'");
                }
            }
        }
        return "0.0";
    }


    private int silnia(int n) {
        int wynik = 1;
        for (int i = 2; i <= n; i++) {
            wynik *= i;
        }
        return wynik;
    }

    public String przeksztalcNaOnp(String rownanie) {
        if (!czyPoprawneRownanie(rownanie))
            return "null";

        String wynik = "";
        for (int i = 0; i < rownanie.length(); i++) {
            char znak = rownanie.charAt(i);
            if (znak >= '0' && znak <= '9') {
                wynik += znak;
                if (!(rownanie.charAt(i + 1) >= '0' && rownanie.charAt(i + 1) <= '9'))
                    wynik += " ";
            } else {
                switch (znak) {
                    case '+':
                    case '-':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.push(String.valueOf(znak));
                        break;
                    case '*':
                    case '/':
                    case '%':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")
                                && !stack.showValue(stack.getSize() - 1).equals("+")
                                && !stack.showValue(stack.getSize() - 1).equals("-")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.push(String.valueOf(znak));
                        break;
                    case '^':
                        stack.push(String.valueOf(znak));
                        break;
                    case '(':
                        stack.push(String.valueOf(znak));
                        break;
                    case ')':
                        while (stack.getSize() > 0 && !stack.showValue(stack.getSize() - 1).equals("(")) {
                            wynik += stack.pop() + " ";
                        }
                        stack.pop();
                        break;
                    case '!':
                        wynik += "! ";
                        break;
                    case '=':
                        while (stack.getSize() > 0) {
                            wynik += stack.pop() + " ";
                        }
                        wynik += "=";
                        break;
                }
            }
        }

        return wynik;
    }

    public static void main(String[] args) {
        // 7 1 + 4 2 - 2 ^ * =
        String tmp = "";
        if (args.length == 0) {
            tmp = "(2+3)*(6-2)^2=";
        } else {
            for (int i = 0; i < args.length; i++) {
                tmp += args[i];
            }
        }
        ONP onp = new ONP();
        System.out.print(tmp + " ");
        try {
            String rownanieOnp = onp.przeksztalcNaOnp(tmp);
            System.out.print(rownanieOnp);
            String wynik = onp.obliczOnp(rownanieOnp);
            System.out.println(" " + wynik);
        } catch (ArithmeticException e) {
            System.out.println("Błąd matematyczny: " + e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Błąd składniowy: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Nieznany błąd: " + e.getMessage());
        }
    }
}