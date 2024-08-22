package dev.duras.translator;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class GeoTiffTranslatorTest {

    @Test
    public void transformGeoTiff2CloudOptimizedGeoTiffTest() {
        String inputGeoTiff = "src/test/resources/f41078e1.tif";
        String outputGeoTiff = "src/test/resources/f41078e1_cog.tif";
        GeoTiffTranslator geoTiffTranslator = new GeoTiffTranslator();
        boolean b = geoTiffTranslator.transformGeoTiff2CloudOptimizedGeoTiff(inputGeoTiff, outputGeoTiff);
        assertTrue(b);
    }

    @Test
    public void transformGeoTiff2CloudOptimizedGeoTiffTest1() {
        String inputFilePath = "src/test/resources/f41078e1.tif";
        String outputGeoTiff = "src/test/resources/f41078e1_cog.tif";

        gdal.AllRegister();

        Dataset geoTiffDataSet = gdal.Open(inputFilePath, gdalconstConstants.GA_ReadOnly);
        if (geoTiffDataSet == null) {
            System.out.println("无法打开输入文件: " + inputFilePath);
        }

        GeoTiffTranslator geoTiffTranslator = new GeoTiffTranslator();
        Dataset dataset = geoTiffTranslator.transformGeoTiff2CloudOptimizedGeoTiff(geoTiffDataSet, outputGeoTiff);
        assertNotNull(dataset);
        dataset.delete();

        try {
            Path path = Paths.get(outputGeoTiff);
            boolean result = Files.deleteIfExists(path);
            System.out.println("删除生成的COG文件:" + result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
