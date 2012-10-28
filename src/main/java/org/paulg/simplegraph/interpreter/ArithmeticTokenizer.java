package org.paulg.simplegraph.interpreter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Splits an arithmetic expression into tokens. 
 */
public class ArithmeticTokenizer {

	private final Scanner scanner;

	public ArithmeticTokenizer(String string) {
		scanner = new Scanner(string);
		scanner.useDelimiter("");
	}

	public List<String> allTokens() {
		String token;
		List<String> tokens = new ArrayList<String>();
		while ((token = next()) != null) {
			tokens.add(token);
		}
		return tokens;
	}
	
	public String next() {
		String token = scanner.findInLine("(\\d)+|[\\w]+|([+*-/()]{1})");
		skipWhitespace();
		return token;
	}

	private void skipWhitespace() {
		if (scanner.hasNext("\\s"))
			scanner.next("(\\s)*");
	}

}
