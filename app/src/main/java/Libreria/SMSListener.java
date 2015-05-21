package Libreria;


import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

/**
 * Created by Vero on 06/05/2015.
 */
public class SMSListener extends Activity {
    String SORT_ORDER = "date DESC";
    String[] column = { "_id", "address", "body"  };
    String sms;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uriSMSURI = Uri.parse("content://sms/inbox");
        Cursor cur = getContentResolver().query(uriSMSURI, column, null, null,SORT_ORDER);
        sms = "";
        while (cur.moveToNext()) {
            sms += "From :" + cur.getString(1) + " : " + cur.getString(2)+"\n";
        }

    }

}
