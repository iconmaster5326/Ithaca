package info.iconmaster.ithaca.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.iconmaster.ithaca.util.ListUtils;

/**
 * Represents a parsed arglist, for use in IthacaUserFunc and IthacaUserMacro.
 * @author iconmaster
 *
 */
public class ParameterList {
	public List<IthacaSymbol> params = new ArrayList<>();
	public IthacaSymbol rest;
	
	public ParameterList(List<IthacaSymbol> params, IthacaSymbol rest) {
		this.params.addAll(params);
		this.rest = rest;
	}
	
	public ParameterList(IthacaObject paramList) {
		List<IthacaObject> args = ListUtils.unwrapDottedList(paramList);
		IthacaObject tail = args.remove(args.size()-1);
		for (IthacaObject arg : args) {
			if (arg instanceof IthacaSymbol) {
				params.add((IthacaSymbol) arg);
			} else {
				throw new IllegalArgumentException("Illegal item in param-list: "+arg);
			}
		}
		if (tail != IthacaNull.NULL) {
			if (tail instanceof IthacaSymbol) {
				rest = (IthacaSymbol) tail;
			} else {
				throw new IllegalArgumentException("Illegal item in param-list: "+tail);
			}
		}
	}
	
	public Map<IthacaSymbol,IthacaObject> parseArgList(IthacaObject argList) {
		Map<IthacaSymbol,IthacaObject> result = new HashMap<>();
		List<IthacaObject> args = ListUtils.unwrapDottedList(argList);
		
		int nargs = args.size()-1;
		for (IthacaSymbol param : params) {
			if (args.size() == 1) {
				throw new IllegalArgumentException("Expected "+params.size()+" args, got "+nargs);
			}
			IthacaObject arg = args.remove(0);
			result.put(param, arg);
		}
		
		if (rest != null) {
			result.put(rest, ListUtils.wrapDottedList(args));
		} else if (args.size() != 1) {
			throw new IllegalArgumentException("Expected "+params.size()+" args, got "+nargs);
		} else if (args.get(0) != IthacaNull.NULL) {
			throw new IllegalArgumentException("Given unexpected dotted list as arg-list: "+argList);
		}
		
		return result;
	}
}
