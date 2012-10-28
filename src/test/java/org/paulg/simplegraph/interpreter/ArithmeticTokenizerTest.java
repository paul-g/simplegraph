package org.paulg.simplegraph.interpreter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArithmeticTokenizerTest {

	@Test
	public void testMatchSimpleExpression() {
		ArithmeticTokenizer t = new ArithmeticTokenizer(
				"2*x+ 100/5 - (22 * 1 + 5)");
		List<String> tokens = t.allTokens();
		List<String> expected = toList("2 * x + 100 / 5 - ( 22 * 1 + 5 )");
		assertEquals(expected, tokens);
		assertNull(t.next());
	}

	@Test
	public void testMatchFunctions() {
		ArithmeticTokenizer t = new ArithmeticTokenizer("cos(x) + sin(25)");
		List<String> tokens = t.allTokens();
		List<String> expected = toList("cos ( x ) + sin ( 25 )");
		assertEquals(expected, tokens);
		assertNull(t.next());
	}

	private List<String> toList(String exp) {
		return Arrays.asList(exp.split(" "));
	}

}
