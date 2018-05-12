package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.library.GlobalScope;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.TokenStream;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaThread thread = new IthacaThread(IthacaReader.read(new TokenStream("(tail '(1 . 2))")), new GlobalScope());
		System.out.println(thread.run());
	}
}
