package info.iconmaster.ithaca.eval;

import info.iconmaster.ithaca.object.IthacaObject;

public interface IthacaRunnable {
	public abstract void call(IthacaThread thread, IthacaObject argList);
}
