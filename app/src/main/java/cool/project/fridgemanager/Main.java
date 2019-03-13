package cool.project.fridgemanager;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cool.project.fridgemanager.create_item.GALLERY_PERMISSIONS_REQUEST;
import static cool.project.fridgemanager.create_item.MAX_DIMENSION;


public class Main extends AppCompatActivity {

    List<Map<String, Object>> mMapList;
    SimpleAdapter mSimpleAdapter;
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<Integer> itemsID = new ArrayList<>();
    private Integer[] itemImage = { R.drawable.img_not_found };
    private String[] itemName = { "高椰菜", "花麗菜", "豬舌", "牛頭皮", "未知食品" };
    private String[] itemTag= { "青菜", "青菜", "肉", "肉", "未知" };
    private String[] itemPutDate = { "11/3/18", "11/1/18", "10/28/18", "10/31/18", "?/?/??" };
    private Integer[] itemRemainedDays = { 3, 5, 20, 12, 0 };
    GridView mGridView;
    SwipeRefreshLayout mSwipeRefreshLayout;

    Bitmap test_bitmap;
    ImageView test_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ItemDB itemDB = new ItemDB(this);

        test_img = findViewById(R.id.test_img);
        testImageViewUpdate(itemDB);

//        itemDB.clear();
        mGridView = findViewById(R.id.item_grid);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh);

        /* method 1 */
//        AdapterView.OnItemClickListener tmp = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // do something
//            }
//        };
//        mGridView.setOnItemClickListener(tmp);

        /* method 2 : anonymous */
//        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                return false;
//            }
//        });

        /* method 3 : lambda */

        mGridView.setOnItemLongClickListener((parent, view, position, id)->{
            System.out.println("View ID: " + id);
            AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
            builder
                    .setMessage(R.string.delete_msg)
                    .setPositiveButton(R.string.ok_ans, (dialog, which) -> {
                        itemDB.delete(itemsID.get((int)id));
                    })
                    .setNegativeButton(R.string.cancel_ans, (dialog, which) -> {});

            builder.create().show();
            return true;
        });

        mGridView.setOnItemClickListener((parent, view, position, id) -> {
            System.out.println("CLICK!");
            Bundle bundle = new Bundle();
            bundle.putInt("ID", (itemsID.get((int)id)));
            //將Bundle物件assign給intent
            Intent intent = new Intent();
            intent.setClass(Main.this, item_detail.class);
            intent.putExtras(bundle);
            startActivity(intent);
        });



        Button clear = findViewById(R.id.clear);
        clear.setOnClickListener(view -> {
            itemDB.clear();
            gridViewUpdate(itemDB);
        });


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            Intent a = new Intent(Main.this, create_item.class);   a.put extra   要放ID進去
            startActivity(new Intent(Main.this, create_item.class));
        });

        gridViewUpdate(itemDB);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            gridViewUpdate(itemDB);
            new Handler().postDelayed(() -> mSwipeRefreshLayout.setRefreshing(false), 1000);
            testImageViewUpdate(itemDB);
        });



    }

    private void testImageViewUpdate(ItemDB itemDB){
        ArrayList<Item> testItems = itemDB.getItems();
        if (testItems != null && testItems.size() > 0) {
            test_bitmap = testItems.get(0).img;
            try {
                System.out.println("%%%%%%%%%%Test%%%%%%%%%% ");
//                Bitmap bitmap = create_item.scaleBitmapDown(
//                        MediaStore.Images.Media.getBitmap(getContentResolver(), test_uri),
//                        MAX_DIMENSION);
                test_img.setImageBitmap(test_bitmap);
                System.out.println("%%%%%%%%%%BitMap%%%%%%%==== " + test_bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            test_img.setImageDrawable(getResources().getDrawable(R.drawable.img_not_found));
        }
    }

    private void gridViewUpdate(ItemDB itemDB){
        mMapList = new ArrayList<>();
        items = itemDB.getItems();
//        for (int i = 0; i < itemName.length; i++) {
//            Map<String, Object> item = new HashMap<>();
//            item.put("img", itemImage[0]);
//            item.put("name", itemName[i]);
//            item.put("tag", itemTag[i]);
//            item.put("put_date", itemPutDate[i]);
//            item.put("remained_days", itemRemainedDays[i].toString());
//            mMapList.add(item);
//        }
        itemsID.clear();
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", items.get(i).name);
            item.put("img", items.get(i).img);
            item.put("tag", items.get(i).tag);
            item.put("put_date", items.get(i).getDateStr("put",'/'));
            item.put("remained_days", items.get(i).getRemainedDays());
           // System.out.println("Item ID: " + items.get(i).id);
            itemsID.add(items.get(i).id);
            mMapList.add(item);
        }

        mSimpleAdapter = new SimpleAdapter(this, mMapList, R.layout.single_grid_item,
                new String[]{"img", "name", "tag", "put_date", "remained_days"},
                new int[]{R.id.grid_item_img, R.id.grid_item_name,
                        R.id.grid_item_tag, R.id.grid_item_put_date, R.id.grid_item_remained_days});
        mGridView.setNumColumns(2);
        mGridView.setAdapter(mSimpleAdapter);
    }

}
