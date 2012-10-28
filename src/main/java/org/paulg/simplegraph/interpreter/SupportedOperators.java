package org.paulg.simplegraph.interpreter;

public enum SupportedOperators {

	PLUS, TIMES, MINUS, DIV, POW;

	public static double evaluate(double val1, double val2, String el) {
		switch (el) {
		case "+":
			return val1 + val2;
		case "-":
			return val2 - val1;
		case "*":
			return val1 * val2;
		case "/":
			return val1 / val2;
		case "^":
			return Math.pow(val1, val2);
		default:
			return 0;
		}
	}
}
