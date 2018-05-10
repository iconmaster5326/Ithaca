package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.library.FuncCons;
import info.iconmaster.ithaca.object.IthacaString;
import info.iconmaster.ithaca.util.ListUtils;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaThread thread = new IthacaThread();
		thread.eval(ListUtils.wrapList(new FuncCons(), new IthacaString("Hello"), new IthacaString("World")));
		System.out.println(thread.run());
	}
}
