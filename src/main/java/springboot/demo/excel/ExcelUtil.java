package springboot.demo.excel;

import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @className: ExcelUtil
 * @author: Lying
 * @description: TODO
 * @date: 2023/4/6 上午10:35
 */
@SuppressWarnings("unused")
public class ExcelUtil {
    private final static String EXCEL_L2003 = "xls";
    private final static String EXCEL_L2007 = "xlsx";

    public static Boolean checkExcelExtension(File excel) {
        String fileName = excel.getName();

        if (StringUtils.isEmpty(fileName)) {
            return false;
        }

        int index = fileName.indexOf(".");

        if (index == -1) {
            return false;
        }

        String extension = fileName.substring(index + 1);

        return Arrays.asList(EXCEL_L2003, EXCEL_L2007).contains(extension);
    }
}
