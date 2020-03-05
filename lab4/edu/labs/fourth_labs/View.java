package edu.labs.fourth_labs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class View extends JFrame {
    private JPanel mainPanel;
    private JPanel listPanel;
    private JList<ListItem> functionsList;
    private JButton execCalcButton;
    private JTextField formulaTextField;
    private JTextArea historyArea;
    private JMenu menuOptions;
    private JMenuBar menuBar;
    private JMenuItem menuExit;
    private JMenuItem menuReset;

    View(Model guiModel) {
        //setting information about window
        setTitle("Calculator");
        setSize(450,450);
        setMinimumSize(new Dimension(400,330));

        //creating menu
        menuBar = new JMenuBar();
        menuOptions = new JMenu("Options");
        menuExit = new JMenuItem("Exit");
        menuReset = new JMenuItem("Reset");

        //adding elements to menu
        menuBar.add(menuOptions);
        menuOptions.add(menuReset);
        menuOptions.add(menuExit);
        functionsList.setModel(guiModel.getListElements());
        //setting all frame
        setJMenuBar(menuBar);
        add(mainPanel,BorderLayout.CENTER);
        pack();
    }

    public void insertInFormulaField(int cursorMove, String toInsert) {
        int caretPosition = formulaTextField.getCaretPosition();
        StringBuilder currentText = new StringBuilder(formulaTextField.getText());
        currentText.insert(caretPosition,toInsert);
        caretPosition += cursorMove;
        formulaTextField.setText(currentText.toString());
        formulaTextField.setCaretPosition(caretPosition);
    }

    public void setFocusToFormulaField() {
        formulaTextField.requestFocusInWindow();
    }

    public void addEvaluateActionListener(ActionListener listener) {
        this.execCalcButton.addActionListener(listener);
    }

    public void addListMouseListener(MouseListener listener) {
        this.functionsList.addMouseListener(listener);
    }

    public void addFormulaKeyListener(KeyAdapter listener) {
        this.formulaTextField.addKeyListener(listener);
    }

    public void addMenuExitActionListener(ActionListener listener) {
        this.menuExit.addActionListener(listener);
    }

    public void addMenuResetActionListener(ActionListener listener) {
        this.menuReset.addActionListener(listener);
    }

    public void setEvaluationState(boolean isEvaluating) {
        if(isEvaluating) {
            execCalcButton.setText("Stop");
        } else {
            execCalcButton.setText("Evaluate");
        }
    };

    public void clearHistoryArea() {
        historyArea.setText("");
        formulaTextField.setText("");
    }

    public String getFormulaString() {
        return formulaTextField.getText();
    }

    public void appendHistoryField(String field) {
        this.historyArea.append(field);
    }

    public void clearFormulaArea() {
        formulaTextField.setText("");
    }


}
