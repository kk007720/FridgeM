package cool.project.fridgemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FridgeData.db"; // 資料庫檔案名稱
    private static final int DB_VERSION = 3;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sql = "CREATE TABLE IF NOT EXISTS Item "
                + "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(60), img BLOB,"
                + "tag VARCHAR(60), put_date DATETIME, due_date DATETIME,"
                + " timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP )";
        db.execSQL(sql);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Item");
        onCreate(db);
    }
}
