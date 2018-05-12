package info.iconmaster.ithaca.library;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaFunc;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;
import info.iconmaster.ithaca.util.ListUtils;

public class FuncHead extends IthacaFunc {
	@Override
	public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		
		if (!(args.get(0) instanceof IthacaPair)) {
			throw new IllegalArgumentException("Expected list, got: "+args.get(0));
		}
		IthacaPair pair = (IthacaPair) args.get(0);
		
		return pair.head;
	}
}
