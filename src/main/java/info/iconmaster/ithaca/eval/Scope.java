package info.iconmaster.ithaca.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;

public class Scope {
	public List<Scope> parents = new ArrayList<>();
	public Map<IthacaSymbol, IthacaObject> bindings = new HashMap<>();
	
	public Scope() {}
	public Scope(Scope... parents) {
		this.parents.addAll(Arrays.asList(parents));
	}
	
	public boolean isBound(IthacaSymbol symbol) {
		return getBinding(symbol) != null;
	}
	
	public IthacaObject getBinding(IthacaSymbol symbol) {
		if (bindings.containsKey(symbol)) {
			return bindings.get(symbol);
		} else {
			for (Scope scope : parents) {
				IthacaObject result = scope.getBinding(symbol);
				if (result != null) return result;
			}
			
			return null;
		}
	}
	
	public void defineBinding(IthacaSymbol symbol, IthacaObject value) {
		bindings.put(symbol, value);
	}
	
	private boolean setBindingImpl(IthacaSymbol symbol, IthacaObject value) {
		if (bindings.containsKey(symbol)) {
			bindings.put(symbol, value);
			return true;
		} else {
			for (Scope scope : parents) {
				if (scope.setBindingImpl(symbol, value)) return true;
			}
			
			return false;
		}
	}
	
	public void setBinding(IthacaSymbol symbol, IthacaObject value) {
		if (!setBindingImpl(symbol, value)) {
			throw new IllegalArgumentException("attempt to set a nonexistent binding: "+symbol);
		}
	}
}
