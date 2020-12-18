package bsu.rfe.java.group6.lab3.Kovalevskaya.varB8;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;


public class GornerTableCellRenderer implements TableCellRenderer {
    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();
 // Ищем ячейки, строковое представление которых совпадает с needle
 // (иголкой). Применяется аналогия поиска иголки в стоге сена, в роли
 // стога сена - таблица
    private String needle = null;
    
    private DecimalFormat formatter =
            (DecimalFormat) NumberFormat.getInstance();

    public GornerTableCellRenderer() {
    	// Показывать только 5 знаков после запятой
        formatter.setMaximumFractionDigits(5);
     // Не использовать группировку (т.е. не отделять тысячи
     // ни запятыми, ни пробелами), т.е. показывать число как "1000",
     // а не "1 000" или "1,000"
        formatter.setGroupingUsed(false);
     // Установить в качестве разделителя дробной части точку, а не
     // запятую. По умолчанию, в региональных настройках
     // Россия/Беларусь дробная часть отделяется запятой
        DecimalFormatSymbols dottedDouble =
                formatter.getDecimalFormatSymbols();
        dottedDouble.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(dottedDouble);
     // Разместить надпись внутри панели
        panel.add(label);
     // Установить выравнивание надписи по левому краю панели
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
    }


    public Component getTableCellRendererComponent(JTable table,
    Object value, boolean isSelected, boolean hasFocus, int row, int col) {
    	// Преобразовать double в строку с помощью форматировщика
        String formattedDouble = formatter.format(value);
     // Установить текст надписи равным строковому представлению числа
        label.setText(formattedDouble);
        if ( col == 1) {
            double col1 = (Double) table.getValueAt(row, col);
            int Col1 = (int) col1;
            col1 -= Col1;
            String COL1 =  formatter.format(col1);
            if (COL1.length() - 2 <= 3) panel.setBackground(Color.GREEN);
            else panel.setBackground(Color.YELLOW);
        }
        
        if(col==2){
            double x = (Double) table.getValueAt(row ,0);
            double y = (Double) table.getValueAt(row ,1);
            int X = (int) x;
            int Y = (int) y;
            boolean check;
            if (X == 0 || Y == 0) check = true;
            else {
                while (Math.abs(X) != Math.abs(Y)) {
                    if (Math.abs(X) > Y) X -= Y;
                    else Y -= Math.abs(X);
                }
                if (X == 1) check = true;
                else check = false;
            }
            panel.remove(0);
            JCheckBox jCheckBox = new JCheckBox();
            jCheckBox.setSelected(check);
            panel.add(jCheckBox);
            panel.setBackground(Color.WHITE);
            return panel;
        }else{
            panel.remove(0);
            panel.add(label);
        }
        if(col == 0) {
            if (needle != null && needle.equals(formattedDouble)) {
                panel.setBackground(Color.RED);
            }
        }
        return panel;
    }

    public void setNeedle(String needle) {
        this.needle = needle;
    }

}