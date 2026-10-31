import java.awt.*;
import java.awt.event.*;

public class Calculator extends Frame implements ActionListener {
    TextField display;
    double first = 0;
    String operator = "";
    boolean newNumber = true;

    Calculator() {
        setTitle("Simple Calculator");
        setSize(350, 450);
        setLayout(new BorderLayout(5, 5));
        setBackground(Color.DARK_GRAY);

        display = new TextField("0");
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setBackground(Color.WHITE);
        display.setForeground(Color.BLACK);
        add(display, BorderLayout.NORTH);

        Panel panel = new Panel();
        panel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "C", "DEL", "%", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "+/-", "0", ".", "="
        };

        for (String text : buttons) {
            Button b = new Button(text);
            b.setFont(new Font("Arial", Font.BOLD, 20));
            if ("+-*/%=".contains(text) && text.length() == 1) {
                b.setBackground(new Color(255, 165, 0));
            } else if (text.equals("C") || text.equals("DEL")) {
                b.setBackground(Color.LIGHT_GRAY);
            } else {
                b.setBackground(Color.WHITE);
            }
            b.addActionListener(this);
            panel.add(b);
        }

        add(panel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        String text = display.getText();

        try {
            if (cmd.matches("[0-9]")) {
                if (newNumber || text.equals("0")) {
                    display.setText(cmd);
                } else {
                    display.setText(text + cmd);
                }
                newNumber = false;
            } else if (cmd.equals(".")) {
                if (newNumber) {
                    display.setText("0.");
                    newNumber = false;
                } else if (!text.contains(".")) {
                    display.setText(text + ".");
                }
            } else if (cmd.equals("C")) {
                display.setText("0");
                first = 0;
                operator = "";
                newNumber = true;
            } else if (cmd.equals("DEL")) {
                if (text.length() > 1) {
                    display.setText(text.substring(0, text.length() - 1));
                } else {
                    display.setText("0");
                }
            } else if (cmd.equals("+/-")) {
                display.setText(format(-Double.parseDouble(text)));
            } else if (cmd.equals("%")) {
                display.setText(format(Double.parseDouble(text) / 100));
                newNumber = true;
            } else if ("+-*/".contains(cmd) && cmd.length() == 1) {
                first = Double.parseDouble(text);
                operator = cmd;
                newNumber = true;
            } else if (cmd.equals("=")) {
                double second = Double.parseDouble(text);
                double result = 0;
                switch (operator) {
                    case "+": result = first + second; break;
                    case "-": result = first - second; break;
                    case "*": result = first * second; break;
                    case "/":
                        if (second == 0) {
                            display.setText("Cannot divide by zero");
                            newNumber = true;
                            return;
                        }
                        result = first / second;
                        break;
                }
                display.setText(format(result));
                operator = "";
                newNumber = true;
            }
        } catch (Exception ex) {
            display.setText("Error");
            newNumber = true;
        }
    }

    private String format(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}

