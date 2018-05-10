package info.iconmaster.ithaca.util;

import java.util.ArrayList;
import java.util.List;

import info.iconmaster.ithaca.object.IthacaNull;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;

public class ListUtils {
	private ListUtils() {}
	
	public static boolean isList(IthacaObject o) {
		return o instanceof IthacaNull || o instanceof IthacaPair;
	}
	
	public static List<IthacaObject> unwrapProperList(IthacaObject o) {
		if (!isList(o)) {
			throw new IllegalArgumentException("Expected proper list, got "+o);
		}
		
		IthacaObject lst = o;
		List<IthacaObject> result = new ArrayList<>();
		while (!(lst instanceof IthacaNull)) {
			if (!(lst instanceof IthacaPair)) {
				throw new IllegalArgumentException("Expected proper list, got "+o);
			}
			
			IthacaPair pair = (IthacaPair) lst;
			result.add(pair.head);
			lst = pair.tail;
		}
		
		return result;
	}
	
	public static IthacaObject wrapList(IthacaObject... os) {
		IthacaObject result = IthacaNull.NULL;
		for (int i = os.length-1; i >= 0; i--) {
			IthacaObject o = os[i];
			result = new IthacaPair(o, result);
		}
		return result;
	}
	
	public static IthacaObject wrapList(List<IthacaObject> os) {
		IthacaObject result = IthacaNull.NULL;
		for (int i = os.size()-1; i >= 0; i--) {
			IthacaObject o = os.get(i);
			result = new IthacaPair(o, result);
		}
		return result;
	}
}
