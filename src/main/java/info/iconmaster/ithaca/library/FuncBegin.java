package info.iconmaster.ithaca.library;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaFunc;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.util.ListUtils;

public class FuncBegin extends IthacaFunc {
	@Override
	public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		return args.get(args.size()-1);
	}
}
