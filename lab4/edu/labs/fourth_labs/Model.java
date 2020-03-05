package edu.labs.fourth_labs;

import java.text.MessageFormat;

import org.mariuszgromada.math.mxparser.*;

import javax.swing.*;

class Model {
    private final MessageFormat answerdForm = new MessageFormat("------------\n{0}\n\t{1}\n");
    private final MessageFormat msgFrom = new MessageFormat("------------\n{0}\n");
    private DefaultListModel<ListItem> listElements;
    private volatile double answer = 0.D;

    Model() {
        listElements = new DefaultListModel<>();
        listElements.addElement(new ListItem("Sinus","sin()",4));
        listElements.addElement(new ListItem("Cosinus","cos()",4));
        listElements.addElement(new ListItem("Tangent","tg()",4));
        listElements.addElement(new ListItem("Square root","sqrt()",5));
        listElements.addElement(new ListItem("Degree to radian","rad()",4));
        listElements.addElement(new ListItem("Last Result","last_result",0));
        listElements.addElement(new ListItem("\u03C0","pi",2));
        listElements.addElement(new ListItem("Euler's number","e",1));
        listElements.addElement(new ListItem("Golden ratio","[phi]",5));
        listElements.addElement(new ListItem("Exponentiation"," ^ ",0));
        listElements.addElement(new ListItem("Factorial"," ! ",0));
        listElements.addElement(new ListItem("Modulo"," # ",0));

    }

    public void setAnswer(double answer) {
        this.answer = answer;
    }

    public DefaultListModel<ListItem> getListElements() {
        return listElements;
    }

    public String formattedAnswer(String equation) {
        Object[] toForm = {equation, answer};
        return answerdForm.format(toForm);
    }

    public String formattedMessage(String message) {
        Object[] toForm = {message};
        return msgFrom.format(toForm);

    }

    public void evalueate(String equation) throws ArithmeticException {
        Expression expression = new Expression(equation);
        if(!expression.checkSyntax()) throw new ArithmeticException(expression.getErrorMessage());
        answer = expression.calculate();
    }

    public double getAnswer() {
        return answer;
    }


}
