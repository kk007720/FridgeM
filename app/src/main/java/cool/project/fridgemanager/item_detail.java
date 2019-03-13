package cool.project.fridgemanager;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class item_detail extends create_item {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail);
        ItemDB itemDB = new ItemDB(this);

        Bundle bundle_item_detail = getIntent().getExtras();
        int item_id = bundle_item_detail.getInt("ID");
        Item item = itemDB.getItemById(item_id);

        itemName = findViewById(R.id.item_name);
        itemTag = findViewById(R.id.item_tag);

        itemName.setText(item.name);
        itemTag.setText(item.tag);

        mImageDetails = findViewById(R.id.image_details);

        AlertDialog.Builder builder = new AlertDialog.Builder(item_detail.this);
        builder
                .setMessage(R.string.dialog_select_prompt)
                .setPositiveButton(R.string.dialog_select_gallery, (dialog, which) -> startGalleryChooser())
                .setNegativeButton(R.string.dialog_select_camera, (dialog, which) -> startCamera());
        mMainImage = findViewById(R.id.imageView);
        mMainImage.setOnClickListener(v -> {
            builder.create().show();
        });
        mBitmap = item.img;
        mMainImage.setImageBitmap(item.img);
//        System.out.println("*** [ITEM_DETAIL] ITEM_IMG: " + item.img + "***");

        putDateText = findViewById(R.id.put_date);
        putDateText.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            putDate = c.getTime();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(item_detail.this, (view2,year,month,day)->{
                String format = setDateFormat(year,month,day);
                putDateText.setText(format);
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                putDate = c.getTime();
            }, mYear,mMonth, mDay).show();
        });
        putDateText.setText(item.getDateStr("put", '/'));

        dueDateText = findViewById(R.id.due_date);
        dueDateText.setOnClickListener(view -> {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(item_detail.this, (view2,year,month,day)->{
                String format = setDateFormat(year,month,day);
                dueDateText.setText(format);
                c.set(Calendar.YEAR, year);
                c.set(Calendar.MONTH, month);
                c.set(Calendar.DAY_OF_MONTH, day);
                dueDate = c.getTime();
            }, mYear,mMonth, mDay).show();
        });
        dueDateText.setText(item.getDateStr("due",'/'));

        okayBtn = findViewById(R.id.item_page_okay);
        okayBtn.setOnClickListener(view -> {
            // TODO: save data
            String name = itemName.getText().toString();
            String tag = itemTag.getText().toString();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 99, outputStream);
            mBytes = outputStream.toByteArray();
            itemDB.update(item_id, name, mBytes, tag, putDate, dueDate); // TODO: catch img from db
            finish();
        });

        cancelBtn = findViewById(R.id.item_page_cancel);
        cancelBtn.setOnClickListener(view -> {
            finish();
        });
    }


}
