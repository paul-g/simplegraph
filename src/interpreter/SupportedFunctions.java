package interpreter;

public enum SupportedFunctions {
	
	sin, sqrt, cos, arcsin, arctg, arccos, tg, tan, ln, floor, abs;
	
	public static double evaluate(String el, double topOfStack) {
		switch (SupportedFunctions.valueOf(el)) {
		case sin:
			return Math.sin(topOfStack);
		case cos:
			return Math.cos(topOfStack);
		case arccos:
			return Math.acos(topOfStack);
		case arcsin:
			return Math.asin(topOfStack);
		case arctg:
			return Math.atan(topOfStack);
		case tg:
		case tan:
			return Math.tan(topOfStack);
		case sqrt:
			return Math.sqrt(topOfStack);
		case ln:
			return Math.log(topOfStack);
		case abs:
			return Math.abs(topOfStack);
		case floor:
			return Math.floor(topOfStack);
		default:
			return 0.0;
		}
	}
}
