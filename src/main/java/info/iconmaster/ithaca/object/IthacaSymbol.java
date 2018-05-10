package info.iconmaster.ithaca.object;

import java.util.HashMap;
import java.util.Map;

public class IthacaSymbol extends IthacaObject {
	public static Map<String, IthacaSymbol> internedSymbols = new HashMap<>();
	public String value;

	private IthacaSymbol(String value) {
		super();
		this.value = value;
	}
	
	public static IthacaSymbol intern(String value) {
		if (internedSymbols.containsKey(value)) {
			return internedSymbols.get(value);
		} else {
			IthacaSymbol sym = new IthacaSymbol(value);
			internedSymbols.put(value, sym);
			return sym;
		}
	}
	
	public static IthacaSymbol unintern(String value) {
		return new IthacaSymbol(value);
	}
	
	@Override
	public String toString() {
		return value;
	}
}
