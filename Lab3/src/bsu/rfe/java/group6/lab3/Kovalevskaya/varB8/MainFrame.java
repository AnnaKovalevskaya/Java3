package bsu.rfe.java.group6.lab3.Kovalevskaya.varB8;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")

public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
 // Массив коэффициентов многочлена
    private Double[] coefficients;
 // Объект диалогового окна для выбора файлов
 // Компонент не создаѐтся изначально, т.к. может и не понадобиться
 // пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;
 // Элементы меню вынесены в поля данных класса, так как ими необходимо
 // манипулировать из разных мест
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
 // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    
    private Box hBoxResult;
    
    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();
 // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new
            GornerTableCellRenderer();
 // Модель данных с результатами вычислений
    private GornerTableModel data;

	@SuppressWarnings("deprecation")
	public MainFrame(Double[] coefficients) {
		// Обязательный вызов конструктора предка
        super("Табулирование многочлена на отрезке по схеме Горнера");
     // Обязательный вызов конструктора предка
        this.coefficients = coefficients;
     // Установить размеры окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
     // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2,
                (kit.getScreenSize().height - HEIGHT) / 2);

     // Создать меню
        JMenuBar menuBar = new JMenuBar();
        // Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
     // Добавить в меню пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
     // Добавить его в главное меню
        menuBar.add(fileMenu);
     // Создать пункт меню "Таблица"
        JMenu tableMenu = new JMenu("Таблица");
     // Добавить его в главное меню
        menuBar.add(tableMenu);
        JMenu infMenu = new JMenu("Справка");
        menuBar.add(infMenu);
        infMenu.add( new AbstractAction("О программе")
        {
            public void actionPerformed(ActionEvent event)
            {
                JOptionPane.showMessageDialog(MainFrame.this,"Автор:\nКовалевская\n6-я группа",
                        "Информация",JOptionPane.QUESTION_MESSAGE);
            }
        });
        
     // Создать новое "действие" по сохранению в текстовый файл
        saveToTextMenuItem = fileMenu.add(new AbstractAction("Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                	// Если экземпляр диалогового окна "Открыть файл" ещѐ не создан,
                	// то создать его
                    fileChooser = new JFileChooser();
                 // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
             // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                	// Если результат его показа успешный,
                	// сохранить данные в текстовый файл
                    saveToTextFile(fileChooser.getSelectedFile());
            }
        });
     // По умолчанию пункт меню является недоступным (данных ещѐ нет)
        saveToTextMenuItem.setEnabled(false);
     // Добавить соответствующий пункт подменю в меню "Файл"
     // Создать новое "действие" по сохранению в текстовый файл
        saveToGraphicsMenuItem = fileMenu.add(new AbstractAction("Сохранить данные для построения графика") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                	// Если экземпляр диалогового окна
                	// "Открыть файл" ещѐ не создан,
                	// то создать его
                    fileChooser = new JFileChooser();
                 // и инициализировать текущей директорией
                    fileChooser.setCurrentDirectory(new File("."));
                }
             // Показать диалоговое окно
                if (fileChooser.showSaveDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION) ;
             // Если результат его показа успешный,
             // сохранить данные в двоичный файл
                saveToGraphicsFile(fileChooser.getSelectedFile());
            }
        });
     // По умолчанию пункт меню является недоступным(данных ещѐ нет)
        saveToGraphicsMenuItem.setEnabled(false);
     // Добавить соответствующий пункт подменю в меню "Файл"
     // Создать новое действие по поиску значений многочлена
        searchValueMenuItem = tableMenu.add(new AbstractAction("Найти значение многочлена") {
            public void actionPerformed(ActionEvent event) {
            	// Запросить пользователя ввести искомую строку
                String value =
                        JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска",
                                "Поиск значения", JOptionPane.QUESTION_MESSAGE);
             // Установить введенное значение в качестве иголки
                renderer.setNeedle(value);
             // Обновить таблицу
                getContentPane().repaint();
            }
        });
     // По умолчанию пункт меню является недоступным (данных ещѐ нет)
        searchValueMenuItem.setEnabled(false);
     // Создать область с полями ввода для границ отрезка и шага
     // Создать подпись для ввода левой границы отрезка
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
     // Создать текстовое поле для ввода значения длиной в 10 символов
     // со значением по умолчанию 0.0
        textFieldFrom = new JTextField("0.0", 10);
     // Установить максимальный размер равный предпочтительному, чтобы
     // предотвратить увеличение размера поля ввода
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
     // Создать подпись для ввода левой границы отрезка
        JLabel labelForTo = new JLabel("до:");
     // Создать текстовое поле для ввода значения длиной в 10 символов
     // со значением по умолчанию 1.0
        textFieldTo = new JTextField("1.0", 10);
     // Установить максимальный размер равный предпочтительному, чтобы
     // предотвратить увеличение размера поля ввода
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
     // Создать подпись для ввода шага табулирования
        JLabel labelForStep = new JLabel("с шагом:");
     // Создать текстовое поле для ввода значения длиной в 10 символов
     // со значением по умолчанию 1.0
        textFieldStep = new JTextField("0.1", 10);
     // Установить максимальный размер равный предпочтительному, чтобы
     // предотвратить увеличение размера поля ввода
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
     // Создать контейнер 1 типа "коробка с горизонтальной укладкой"
        Box hboxRange = Box.createHorizontalBox();
     // Задать для контейнера тип рамки "объѐмная"
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
     // Задать для контейнера тип рамки "объѐмная"
        hboxRange.add(Box.createHorizontalGlue());
     // Добавить подпись "От"
        hboxRange.add(labelForFrom);
        // Добавить "распорку" C1-H2
        hboxRange.add(Box.createHorizontalStrut(10));
     // Добавить поле ввода "От"
        hboxRange.add(textFieldFrom);
     // Добавить "распорку" C1-H3
        hboxRange.add(Box.createHorizontalStrut(20));
     // Добавить подпись "До"
        hboxRange.add(labelForTo);
     // Добавить "распорку" C1-H4
        hboxRange.add(Box.createHorizontalStrut(10));
     // Добавить поле ввода "До"
        hboxRange.add(textFieldTo);
     // Добавить "распорку" C1-H5
        hboxRange.add(Box.createHorizontalStrut(20));
     // Добавить подпись "с шагом"
        hboxRange.add(labelForStep);
        // Добавить "клей" C1-H6
        hboxRange.add(Box.createHorizontalStrut(10));
        // Добавить поле для ввода шага табулирования
        hboxRange.add(textFieldStep);
     // Добавить "клей" C1-H7
        hboxRange.add(Box.createHorizontalGlue());
     // Установить предпочтительный размер области равным удвоенному
     // минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(
                new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue() * 2));
     // Установить область в верхнюю (северную) часть компоновки
        getContentPane().add(hboxRange, BorderLayout.NORTH);
        
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    Double from =
                            Double.parseDouble(textFieldFrom.getText());
                    Double to =
                            Double.parseDouble(textFieldTo.getText());
                    Double step =
                            Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step,
                            MainFrame.this.coefficients);
                    JTable table = new JTable(data);
                    table.setDefaultRenderer(Double.class,
                            renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    getContentPane().validate();
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.add(new JPanel());
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                getContentPane().validate();
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxRange.setPreferredSize(new Dimension(
        		new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
        		new Double(hboxRange.getMinimumSize().getHeight()).intValue()*2));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToGraphicsFile(File selectedFile) {
        try (DataOutputStream out = new DataOutputStream(new
                FileOutputStream(selectedFile))) {
            for (int i = 0; i < data.getRowCount(); i++) {
                out.writeDouble((Double) data.getValueAt(i, 0));
                out.writeDouble((Double) data.getValueAt(i, 1));
            }
        } catch (Exception e) {
            //NOP
        }
    }

    protected void saveToTextFile(File selectedFile) {
        try (PrintStream out = new PrintStream(selectedFile)) {
            out.println("Результаты табулирования многочлена по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length - i - 1));
                if (i != coefficients.length - 1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
            for (int i = 0; i < data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i, 0)
                        + " равно " + data.getValueAt(i, 1));
            }
        } catch (FileNotFoundException e) {
            //NOP
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg : args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        } catch (NumberFormatException ex) {
            System.err.println("Ошибка преобразования строки '" +
                    args[i] + "' в число типа Double");
            System.exit(-2);
        }
        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

	public DecimalFormat getFormatter() {
		return formatter;
	}

	public void setFormatter(DecimalFormat formatter) {
		this.formatter = formatter;
	}
}
