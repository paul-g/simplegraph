package org.paulg.simplegraph.interpreter;

import java.util.Stack;

public class Function {

	private static final int[] left_asoc = { 1, 1, 1, 1, 0 };
	private static final int[] right_asoc = { 1, 1, 1, 1, 0 };
	private static final int prec[] = { 1, 1, 10, 10, 100 };
	private static final String operatori = "+-*/^";
	private static final String functions = "sin,sqrt,cos,arcsin,arctg,arccos,tg,tan,ln,floor,abs";

	private boolean error = false;
	public String law;

	private Stack<String> output = new Stack<String>();
	public Function(String s) {
		law = s;
		toRPN();
	}
	


	private void toRPN() {
		Stack<String> stiva = new Stack<String>();
		int i = 0;
		char c;
		String nr = "";
		int len = law.length();
		String function_name;
		int impins_nr = 0, impins_f = 0;

		while (i < len && !error) {
			c = law.charAt(i);
			if (Character.isDigit(c)) {
				while (Character.isDigit(c) || (c == '.')) {
					nr += c;
					if (i + 1 < len
							&& (Character.isDigit(law.charAt(i + 1)) || (law
									.charAt(i + 1) == '.')))
						c = law.charAt(++i);
					else
						break;
				}
				output.push(nr);
				impins_nr = 1;
				impins_f = 0;
				nr = "";
			} else if (operatori.indexOf(c) != -1) {
				if (left_asoc[operatori.indexOf(c)] == 1) {
					while (stiva.size() > 0
							&& stiva.peek().isEmpty()
							&& operatori.indexOf(stiva.peek()) != -1
							&& prec[operatori.indexOf(c)] <= prec[operatori
									.indexOf(stiva.peek())])
						output.push(stiva.pop());

				} else if (right_asoc[operatori.indexOf(c)] == 1) {
					while (stiva.peek().isEmpty()
							&& operatori.indexOf(stiva.peek()) != -1
							&& prec[operatori.indexOf(c)] < prec[operatori
									.indexOf(stiva.peek())])
						output.push(stiva.pop());
				}
				stiva.push("" + c);
				impins_nr = 0;
				impins_f = 0;
			} else if (c == '(' || c == '[') {
				if (impins_nr == 1) {
					stiva.push("*");
					impins_nr = 0;
				}
				if (i + 1 < len && law.charAt(i + 1) == '-')
					output.push("0");
				if (c == '[')
					stiva.push("floor");
				stiva.push("(");
			} else if (c == ')' || c == ']') {
				while (!stiva.peek().equals("(") && !stiva.peek().equals(""))
					output.push(stiva.pop());
				if (stiva.peek().isEmpty()) {
					// / TODO: add exception for wrong bracketing
					error = true;
				} else {
					stiva.pop();
					if (!stiva.isEmpty() && !stiva.peek().isEmpty()
							&& functions.indexOf(stiva.peek()) != -1) {
						output.push(stiva.pop());
						impins_f = 1;
						impins_nr = 1;
					}
				}
				impins_nr = 0;
			} else if (c == 'x' || c == 'y') {
				output.push("" + c);
				if (impins_nr == 1) {
					stiva.push("*");
					impins_nr = 0;
				}
			} else if (Character.isLetter(c)) {
				function_name = ""; // handle functions
				while (Character.isLetter(c)) {
					function_name += c;
					if (i + 1 < len && Character.isLetter(law.charAt(i + 1)))
						c = law.charAt(++i);
					else
						break;

				}
				if (impins_nr == 1)
					stiva.push("*");
				if (function_name.equals("e"))
					output.push(Double.toString(Math.E));
				else if (function_name.equals("pi"))
					output.push(Double.toString(Math.PI));
				else {
					if (impins_f == 1)
						stiva.push("*");
					stiva.push(function_name);
					impins_nr = 0;
					impins_f = 0;
				}

			}
			i++;
			// stiva.display();
			// output.display();
		}

		while (stiva.size() > 0 && !stiva.peek().isEmpty()) {
			if (stiva.peek().equals("(")) {
				// textEroare = "Paranteze gresite";
				// TODO add exceptions for wrong bracketing
				error = true;
			} else
				output.push(stiva.pop());
		}

		if (error == true)
			output.clear();

	}

	public double evaluate(double x) {
		Stack<Double> stiva = new Stack<Double>();
		int i = 0;
		String el;

		while (i < output.size()) {
			el = output.get(i);
			if (!el.equals("") && Character.isDigit(el.charAt(0))) {
				stiva.push(Double.parseDouble(el));
			} else if (operatori.indexOf(el) != -1) {
				double res = SupportedOperators.evaluate(stiva.pop(), stiva.pop(), el);
				stiva.push(res);
			} else if (el.equals("x"))
				stiva.push(x);
			else {
				stiva.push(SupportedFunctions.evaluate(el, stiva.pop()));
			}
			i++;
		}

		return stiva.pop();
	}
}