package info.iconmaster.ithaca.eval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaSymbol;
import info.iconmaster.ithaca.parse.ReadSyntax;

public class Scope {
	public List<Scope> parents = new ArrayList<>();
	public Map<IthacaSymbol, IthacaObject> bindings = new HashMap<>();
	public Map<String, ReadSyntax> readSyntax = new HashMap<>();
	
	public Scope() {}
	public Scope(Scope... parents) {
		this.parents.addAll(Arrays.asList(parents));
	}
	
	// bindings
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
	
	public void defineBindings(Map<IthacaSymbol, IthacaObject> binds) {
		bindings.putAll(binds);
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
	
	// read syntax
	public boolean hasReadSyntax(String s) {
		return getReadSyntax(s) != null;
	}
	
	public ReadSyntax getReadSyntax(String s) {
		if (readSyntax.containsKey(s)) {
			return readSyntax.get(s);
		} else {
			for (Scope scope : parents) {
				ReadSyntax result = scope.getReadSyntax(s);
				if (result != null) return result;
			}
			
			return null;
		}
	}
	
	public void defineReadSyntax(ReadSyntax rs) {
		readSyntax.put(rs.identifier, rs);
	}
	
	public void defineReadSyntax(Collection<ReadSyntax> rss) {
		for (ReadSyntax rs : rss) {
			defineReadSyntax(rs);
		}
	}
}
