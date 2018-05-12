package info.iconmaster.ithaca;

import java.io.File;
import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaRuntime;
import info.iconmaster.ithaca.library.GlobalScope;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaRuntime runtime = new IthacaRuntime(new GlobalScope());
		runtime.spawn(new File("test.ithaca"));
		runtime.run();
	}
}
