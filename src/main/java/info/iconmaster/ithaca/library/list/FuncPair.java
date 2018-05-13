package info.iconmaster.ithaca.library.list;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaFunc;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaPair;
import info.iconmaster.ithaca.util.ListUtils;

public class FuncPair extends IthacaFunc {
	@Override
	public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		return new IthacaPair(args.get(0), args.get(1));
	}
}
