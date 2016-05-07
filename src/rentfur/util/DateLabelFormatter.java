/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rentfur.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JFormattedTextField.AbstractFormatter;

/**
 *
 * @author FDuarte
 */
public class DateLabelFormatter extends AbstractFormatter {
    //private String datePattern = "yyyy-MM-dd";
    private final String datePattern;
    //private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
    private final SimpleDateFormat dateFormatter;

    public DateLabelFormatter(String datePattern) {
        this.datePattern = datePattern;
        this.dateFormatter = new SimpleDateFormat(this.datePattern);
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        return dateFormatter.parseObject(text);
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }

        return "";
    }
    
}
