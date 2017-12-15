package org.geowe.datastore.layer;

public enum DataFormat {

	// @formatter:off
	GEOJSON("GEOJSON"),
	GEOJSON_CSS("GEOJSON_CSS"),
	WKT("WKT"),
	KML("KML"),
	GML("GML"),
	TOPO_JSON("TOPO_JSON"),
	CUSTOM_TEXT("CUSTOM_TEXT");
	// @formatter:on
	private String name;

	DataFormat(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
	
}
