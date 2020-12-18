package bsu.rfe.java.group6.lab3.Kovalevskaya.varB8;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class GornerTableModel extends AbstractTableModel {

    private Double[] coefficients;
    private Double from;
    private Double to;
    private Double step;

    public GornerTableModel(Double from, Double to, Double step,
    		Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    public int getColumnCount() {
        return 3;
    }

    @SuppressWarnings("deprecation")
    public int getRowCount() {
    	return new Double(Math.ceil((to-from)/step)).intValue()+1;
    	}



    public Object getValueAt(int row, int col) {
    	 double result = 0.0;
        double x = from + step * row;
        if (col == 1) {
            for (int i = 0; i < coefficients.length; i++)
                result += Math.pow(x, coefficients.length - 1 - i) * coefficients[i];
        } else if (col == 0){
        	return x;
        }
        return result;
    }

    public String getColumnName(int col) {
        if (col == 2)
        	return "Взаимно простые?";
        else if (col == 1)
        	return "Значение многочлена";
        else
        	return "Значение X";
    }

    public Class<?> getColumnClass(int col) {
    	return Double.class;
    }
}