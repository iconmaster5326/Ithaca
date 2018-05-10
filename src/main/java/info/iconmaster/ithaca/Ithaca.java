package info.iconmaster.ithaca;

import java.io.IOException;

import info.iconmaster.ithaca.eval.IthacaThread;
import info.iconmaster.ithaca.eval.Scope;
import info.iconmaster.ithaca.parse.IthacaReader;
import info.iconmaster.ithaca.parse.TokenStream;

public class Ithaca {
	public static void main(String[] args) throws IOException {
		IthacaThread thread = new IthacaThread(IthacaReader.read(new TokenStream("(begin (define x 1) x)")), new Scope());
		System.out.println(thread.run());
	}
}
