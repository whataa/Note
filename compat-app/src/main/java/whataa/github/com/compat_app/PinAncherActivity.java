package whataa.github.com.compat_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Summer on 2016/10/8.
 */

public class PinAncherActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_ancher);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new MyAdapter());
    }

    static class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pin_ancher, null);
                holder = new Holder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }
            holder.tvAncher.setText("posiotion:"+position);
            return convertView;
        }

        class Holder {
            TextView tvAncher;
            Holder(View v) {
                tvAncher = (TextView) v.findViewById(R.id.item_pin_ancher_ancher);
            }
        }
    }
}
