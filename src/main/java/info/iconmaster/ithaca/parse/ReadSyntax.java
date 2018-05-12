package info.iconmaster.ithaca.parse;

import info.iconmaster.ithaca.object.IthacaObject;

public abstract class ReadSyntax {
	public String identifier;
	
	public ReadSyntax(String identifier) {
		super();
		this.identifier = identifier;
	}
	
	public abstract IthacaObject read(IthacaReader ir);
}
