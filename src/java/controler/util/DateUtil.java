/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controler.util;

/**
 *
 * @author moulaYounes
 */
public class DateUtil {

    public static java.sql.Date getSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public static int compare(java.util.Date date1, java.util.Date date2) {
        if (date1.getYear() > date2.getYear()) {
            return 1;
        } else if (date2.getYear() > date1.getYear()) {
            return -1;
        } else {
            if (date1.getMonth() > date2.getMonth()) {
                return 1;
            } else if (date2.getMonth() > date1.getMonth()) {
                return -1;
            } else {
                if (date1.getDay() > date2.getDay()) {
                    return 1;
                } else if (date2.getDay() > date1.getDay()) {
                    return -1;
                }else{
                    return 0;
                }
            }
        }
    }
}
