package com.fuint.excel.export.service;

import com.fuint.excel.export.dto.ExcelExportDto;

/**
 * 导出Excel文件业务接口
 *
 * Created by FSQ
 * Contact wx fsq_better
 * Site https://www.fuint.cn
 */
public interface ExportService {

    /**
     * 导出Excel文件
     *
     * @param data
     */
    void exportExcel(ExcelExportDto data);

    /**
     * 直接导出本地文件
     *
     * @param data srcPath 文件相对路径
     *             srcTemplateFileName 文件名称
     *             out 输出流
     */
    void exportLocalFile(ExcelExportDto data) throws Exception;
}
