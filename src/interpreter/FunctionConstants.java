package interpreter;
/*
 * @author Paul Grigoras
 */

public interface FunctionConstants {
	final int[] left_asoc={1,1,1,1,0};
	final int[] right_asoc={1,1,1,1,0};
	final int prec[]={1,1,10,10,100};
	final String operatori="+-*/^";
	final String functions="sin,sqrt,cos,arcsin,arctg,arccos,tg,tan,ln,floor,abs";
}
