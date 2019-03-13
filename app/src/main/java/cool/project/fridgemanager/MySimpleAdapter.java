package cool.project.fridgemanager;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySimpleAdapter extends SimpleAdapter {
    private Context mContext;
    public LayoutInflater inflater = null;
    private List<? extends Map<String, ?>> mData;

    @Override
    public Object getItem(int position){
        if(mData != null)
            return mData.get(position).get("uri");
        return null;
    }

    public MySimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to){
        super(context, data, resource, from, to);
        mContext = context;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.single_grid_item, parent, false);
        }
        ImageView imageView = convertView.findViewById(R.id.grid_item_img);
        try{
            //imageView.setImageURI(Uri.parse((String) getItem(position)));
        } catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
