package info.iconmaster.ithaca.library;

import java.util.List;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.object.IthacaFunc;
import info.iconmaster.ithaca.object.IthacaObject;
import info.iconmaster.ithaca.object.IthacaVoid;
import info.iconmaster.ithaca.util.ListUtils;

public class FuncPrint extends IthacaFunc {
	@Override
	public IthacaObject callFunc(IthacaThread thread, IthacaObject argList) {
		List<IthacaObject> args = ListUtils.unwrapProperList(argList);
		System.out.println(args.get(0));
		return IthacaVoid.VOID;
	}
}
