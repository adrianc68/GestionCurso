package util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateFormatter {

    public static Date getDateTimeByString(String date) {
        Date dateSQL = null;
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            dateSQL = new Date( simpleDateFormat.parse(date).getTime() );
        } catch (ParseException e) {
            Logger.getLogger( DateFormatter.class.getName() ).log(Level.WARNING, null, e);
        }
        return dateSQL;
    }

}
