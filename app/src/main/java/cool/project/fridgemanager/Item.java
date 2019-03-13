package cool.project.fridgemanager;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Item {

    /* Field */
    Context context = null;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int id;
    String name = "";
    Bitmap img = null;
    String tag = "";
    Date putdate;
    Date duedate;

    /* Method */

    public Item(Context c) {
        context = c;
    }

    public void setId(String stringid){
        id = Integer.parseInt(stringid);
    }

    public void setValues(String n, Bitmap i, String t, Date pd, Date dd){
        name = n;
        img = i;
        tag = t;
        putdate = pd;
        duedate = dd;
    }

    public void setValues(String n, byte[] imgByte, String t, String pd, String dd){
        name = n;

        // TODO: update here after
        if(imgByte != null){
            System.out.println("***IMAGE DECODED***");
            img = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
        }
        tag = t;
        try {
            if(pd == null)
                putdate = null;
            else
                putdate = format.parse(pd);
            if(dd == null)
                duedate = null;
            else
                duedate = format.parse(dd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public String getDateStr(String which, char divider) {
        Calendar cal = Calendar.getInstance();
        if(which == "put"){
            if(putdate == null)
                return "unknown";
            cal.setTime(putdate);

        }else if(which == "due"){
            if(duedate == null)
                return "unknown";
            cal.setTime(duedate);
        }else{
            return "unknown";
        }
        String ret = Integer.toString(cal.get(Calendar.YEAR)) + divider
                + Integer.toString(cal.get(Calendar.MONTH)+1) + divider
                + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        return ret;
    }

    public String getRemainedDays(){
        if(duedate == null)
            return "? days";
        Date now = new Date(System.currentTimeMillis());
        int days = (int) getDateDiff(now, duedate, TimeUnit.DAYS);

        String tmp = "";

        switch (days){
            case 1:
                return "1 day";
            case 0:
                return "today";
            case -1:
                return "-1 day";
            default:
                return days + " days";
        }
    }

    private long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }

    public final Uri getUriToDrawable(@NonNull Context context, @AnyRes int drawableId) {
        Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.getResources().getResourcePackageName(drawableId)
                + '/' + context.getResources().getResourceTypeName(drawableId)
                + '/' + context.getResources().getResourceEntryName(drawableId) );
        return imageUri;
    }
}
