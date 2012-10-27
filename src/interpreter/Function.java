package interpreter;
import java.util.Stack;

/*
 * @author Paul Grigoras
 */

public class Function implements FunctionConstants {
	public boolean eroare = false;
	public String textEroare;
	public String law;

	private Stack<String> output = new Stack<String>();
	private Stack<String> stiva = new Stack<String>();

	public Function(String s) {
		law = s;
		toRPN();
	}

	private void toRPN() {
		int i = 0;
		char c;
		String nr = "";
		int len = law.length();
		String function_name;
		int impins_nr = 0, impins_f = 0;

		while (i < len && !eroare) {
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
					while (stiva.size() > 0 &&
							stiva.peek().isEmpty()
							&& operatori.indexOf(stiva.peek()) != -1
							&& prec[operatori.indexOf(c)] <= prec[operatori
									.indexOf(stiva.peek())])
						output.push(stiva.pop())
						;

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
				while (!stiva.peek().equals("(")
						&& !stiva.peek().equals(""))
					output.push(stiva.pop());
				if (stiva.peek().isEmpty()) {
					textEroare = "Paranteze gresite";
					eroare = true;
				} else {
					stiva.pop();
					if (!stiva.peek().isEmpty()
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
				function_name = ""; // partea de functii
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
				textEroare = "Paranteze gresite";
				eroare = true;
			} else
				output.push(stiva.pop());
		}
		
		if (eroare == true)
			output.clear();

	}

	public double evaluate(double x) {
		Stack<Double> stiva = new Stack<Double>();
		int i = 0;
		double val1 = 0, val2 = 0;
		String el;
		
		while (i < output.size()) {
			el = output.get(i);
			if (!el.equals("") && Character.isDigit(el.charAt(0))) {
				stiva.push(Double.parseDouble(el));
			} else if (operatori.indexOf(el) != -1) {
				val2 = stiva.pop();
				val1 = stiva.pop();
				double res = eval(val1, val2, el);
				stiva.push(res);
			} else if (el.equals("x"))
				stiva.push(x);
			else {
				stiva.push(handleFunctionSymbol(stiva, el));
			}
			i++;
		}

		// stiva.display();
		return stiva.pop();

	}

	private double eval(double val1, double val2, String el) {
		double res = 0;
		switch(el) {
			case "+": res = val1 + val2; break;
			case "-": res = val1 - val2; break;
			case "*": res = val1 * val2; break;
			case "/": res = val1 / val2; break;
			case "^": res = Math.pow(val1, val2); break;
		}
		return res;
	}

	private Double handleFunctionSymbol(Stack<Double> stiva, String el) {
		Double topOfStack = stiva.pop();
		switch (el) {
			case "sin": return Math.sin(topOfStack);
			case "cos": return Math.cos(topOfStack);
			case "tg" :
			case "arccos": return Math.acos(topOfStack); 
			case "arcsin": return Math.asin(topOfStack); 
			case "arctg" : return Math.atan(topOfStack); 
			case "tan":  return Math.tan(topOfStack); 
			case "sqrt": return Math.sqrt(topOfStack); 
			case "ln" :  return Math.log(topOfStack); 
			case "abs"   : return Math.abs(topOfStack); 
			case "floor" : return Math.floor(topOfStack);
			default: return 0.0;
		}
	}

}