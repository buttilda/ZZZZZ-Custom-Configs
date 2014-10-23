package ganymedes01.zzzzzcustomconfigs.xml;

public class XMLProperty {

	final String value;
	final String name;

	public XMLProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String toString() {
		return name + "=" + "\"" + value + "\"";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof XMLProperty))
			return false;
		XMLProperty property = (XMLProperty) obj;
		return name.equals(property.name) && value.equals(property.value);
	}
}