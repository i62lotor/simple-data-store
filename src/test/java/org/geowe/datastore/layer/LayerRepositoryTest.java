package org.geowe.datastore.layer;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LayerRepositoryTest {

	private static final String GEOJSON = "{\"type\":\"FeatureCollection\",\"features\":[{\"type\":\"Feature\",\"properties\":{\"T187_ID\":\"1257762\",\"T187_NOMBRE\":\"Vuelo LIDAR Villoslada de Cameros Bloque 001\",\"T187_CAPA_SIMPLE\":\"T187\",\"T187_CAPA_SIMPLE_DENO\":\"LIDAR\",\"T187_187_PROYECTO\":\"Vuelo LIDAR Villoslada de Cameros\",\"T187_187_PROMOTOR\":\"Gobierno de La Rioja\",\"T187_187_FECHA\":\"20091119000000\",\"T187_187_BLOQUE\":\"1\",\"T187_187_DENSIDAD\":\"1.5\",\"T187_187_OBSERVACIONES\":\"Fichero LAS no clasificado. Altura Elipsoidal. Sistema Geogr�fico de Referencia: UTM ETRS889 Huso 30N (EPSG:25830)\",\"T187_000_URL\":\"ftp://ftp.larioja.org/sig/tmt/t187/laz/laz_20091119_bloque_0001_epsg25830.laz\",\"DOCUMENTOS\":\"<a target='_blank' href='http://ias1.larioja.org/iderioja/archivosTMT?codPadre=1257762|T187'>Documentos</a>\"},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-2.733785,42.13680500000001],[-2.721685,42.13677599999999],[-2.721645,42.14578300000001],[-2.7337469999999997,42.145810999999995],[-2.733785,42.13680500000001]]]}}]}";
	private static final String LAYER_NAME = "layer-name";
	private static final String DEFAULT_PROJECTION = "WGS84";
	
	@Autowired
	private LayerRepository repository;
	
	@Before
	public void setUp() {
		repository.deleteAll();
	}
	
	@Test
	public void Given_LayerInfo_When_Save_Then_LayerStoreInDB(){
		Layer layer = repository.save(createLayer());
		System.out.println("Layer: "+ layer);
		Assert.assertTrue("Layer not saved", layer.getName().equals(LAYER_NAME));
	}
	
	private Layer createLayer(){
		Layer layer= new Layer(LAYER_NAME, DataFormat.GEOJSON ,DEFAULT_PROJECTION);
		layer.setLastUpdate(LocalDateTime.now());
		layer.setData(GEOJSON);
		layer.setOpenData(true);
		return layer;
	}
	
}
