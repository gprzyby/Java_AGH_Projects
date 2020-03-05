package edu.labs.fourth_labs;

import org.mariuszgromada.math.mxparser.Expression;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.*;

class Controller {
    private View appGUI;
    private Model appModel;
    private boolean isEvaluating = false;

    private Controller(View guiView,Model model) {
        this.appGUI = guiView;
        this.appModel = model;
        EvaluateController evaluateController = new EvaluateController();
        //adding listeners to view
        appGUI.addMenuExitActionListener(e -> System.exit(0));

        appGUI.addMenuResetActionListener(e -> {
            appGUI.clearFormulaArea();
            appGUI.clearHistoryArea();
        });

        appGUI.addEvaluateActionListener(evaluateController);

        appGUI.addFormulaKeyListener( new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    evaluateController.actionPerformed(null);
                } else if (e.getKeyCode() == KeyEvent.VK_UP){
                    String lastResultInString = Double.toString(appModel.getAnswer());
                    appGUI.insertInFormulaField(lastResultInString.length(),lastResultInString);
                }
            }
        });

        appGUI.addListMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<? extends ListItem> eventSource = (JList<ListItem>) e.getSource();
                if(e.getClickCount() == 2) {
                    ListItem selected = eventSource.getSelectedValue();
                    if(selected.getFunction() == "last_result") {
                        String lastResultInString = Double.toString(appModel.getAnswer());
                        appGUI.insertInFormulaField(lastResultInString.length(),lastResultInString);
                    } else {
                        appGUI.insertInFormulaField(selected.getCaretMove(), selected.getFunction());
                    }
                    appGUI.setFocusToFormulaField();
                }
            }
        });
    }

    public static Controller setApplication() {
        Model model = new Model();
        View view = new View(model);
        Controller controller = new Controller(view,model);
        return controller;
    }

    public void run() {
        EventQueue.invokeLater(() -> {
            appGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            appGUI.setVisible(true);
        });
    }

    private class EvaluateController implements ActionListener {
        private CalculationThread threadFunct = new CalculationThread("0");
        private ExecutorService thread = new ExecutorWithExceptionHandling();
        private Future<?> currentTask;

        @Override
        public void actionPerformed(ActionEvent e) {
            if(!isEvaluating) {
                appGUI.setEvaluationState(true);
                threadFunct.setEquation(appGUI.getFormulaString());
                currentTask = thread.submit(threadFunct);

                isEvaluating = true;
            } else {
                isEvaluating = false;
                if(!currentTask.isDone()) {
                    currentTask.cancel(true);
                }
                appGUI.setEvaluationState(false);
            }

        }
    }

    private class CalculationThread implements Runnable {
        private String equation;

        CalculationThread(String equation) {
            this.equation = equation;
        }

        public void setEquation(String equation) {
            this.equation = equation;
        }

        //executing expression in thread
        @Override
        public void run() throws ArithmeticException, CancellationException {
            //creating expression handler
            System.out.println(Thread.currentThread().toString() + " with id: " + Thread.currentThread().getId());
            Expression expression = new Expression(equation);

            //calculating answer if syntax is OK, unless throw exception with expression error message
            if(expression.checkSyntax()) {
                final double answer = expression.calculate();
                if(Double.isNaN(answer)) throw new ArithmeticException("Math error");
                //updating answer to gui
                SwingUtilities.invokeLater(() -> {
                    isEvaluating = false;
                    appGUI.setEvaluationState(false);
                    appModel.setAnswer(answer);
                    appGUI.appendHistoryField(appModel.formattedAnswer(equation));
                });

            } else {
                throw new ArithmeticException(expression.getErrorMessage());
            }
        }

    }

    private class ExecutorWithExceptionHandling extends ThreadPoolExecutor {
        ExecutorWithExceptionHandling() {
            super(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            super.afterExecute(r, t);
            if(r instanceof Future<?>) {
                Future<?> future = (Future<?>)r;
                try {
                    future.get();
                } catch (CancellationException | InterruptedException e) {
                    SwingUtilities.invokeLater(() -> {
                        isEvaluating = false;
                        appGUI.setEvaluationState(false);
                        appGUI.appendHistoryField(appModel.formattedMessage("Execution has been cancelled"));
                    });
                } catch (ExecutionException e) {
                    if(e.getCause() instanceof ArithmeticException) {
                        SwingUtilities.invokeLater(() -> {
                            isEvaluating = false;
                            appGUI.setEvaluationState(false);
                            JOptionPane.showMessageDialog(null,
                                    appModel.formattedMessage("Arithmetic error: \n" + ((ArithmeticException)e.getCause()).getMessage() +"\n"),
                                    "Error",JOptionPane.ERROR_MESSAGE);
                        });
                    }
                    else {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
