package dev.duras.translator;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.TranslateOptions;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import java.util.Vector;

public class GeoTiffTranslator {

    static {
        gdal.AllRegister();
    }

    private static Dataset process(Dataset geoTiffDataSet, String outputFilePath) {
        Driver driver = gdal.GetDriverByName("GTiff");
        if (driver == null) {
            System.out.println("GTiff驱动未找到");
            return null;
        }

        Vector<String> options = new Vector<>();
        options.add("-of"); // output format 输出格式
        options.add("COG"); // Cloud Optimized GeoTIFF

        options.add("-co"); // creation options 创建选项
        options.add("COMPRESS=LZW");    // LZW压缩方法 基于表查询算法删除重复数据从而实现减小文件大小的无损压缩算法 .eg: 89.8M -> 7.66M

        TranslateOptions translateOptions = new TranslateOptions(options);
        Dataset cogDataSet = gdal.Translate(outputFilePath, geoTiffDataSet, translateOptions);
        geoTiffDataSet.delete();
        return cogDataSet;
    }

    /**
     * 转换GeoTiff文件为COG文件
     *
     * @param inputFilePath  输入GeoTiff文件路径
     * @param outputFilePath 输出COG文件路径
     * @return 转换是否成功
     */
    public boolean transformGeoTiff2CloudOptimizedGeoTiff(String inputFilePath, String outputFilePath) {
        Dataset geoTiffDataSet = gdal.Open(inputFilePath, gdalconstConstants.GA_ReadOnly);
        if (geoTiffDataSet == null) {
            System.out.println("无法打开输入文件: " + inputFilePath);
            return false;
        }

        Dataset cogDataSet = process(geoTiffDataSet, outputFilePath);

        geoTiffDataSet.delete();
        if (cogDataSet != null) {
            cogDataSet.delete();
            return true;
        }
        return false;
    }

    /**
     * 转换GeoTiff数据集为COG数据集
     *
     * @param geoTiffDataSet GeoTiff数据集
     * @param outputFilePath 输出COG文件路径
     * @return COG数据集
     */
    public Dataset transformGeoTiff2CloudOptimizedGeoTiff(Dataset geoTiffDataSet, String outputFilePath) {
        return process(geoTiffDataSet, outputFilePath);
    }
}
