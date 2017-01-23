package io.whataa.fragmentapp.menudemo;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.whataa.fragmentapp.R;

import io.whataa.fragmentapp.common.BaseActivity;

/**
 * Created by Administrator on 2017/1/22.
 */

public class MenuActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        setSupportActionBar((Toolbar) findViewById(R.id.my_toolbar));

        /**
         * 1. "Up Button" 表示可以回到所指定的Activity，取代默认的回到栈中上一个页面的逻辑，这个Activity称作parent；<br/>
         * 2. parent需要在Manifest中为当前Activity通过android:parentActivityName指定；<br/>
         * 3. 原理：其实就是Activity的“出栈”，通过该按钮可以直接destroy掉栈中parent与当前Activity之间的元素，并显示parent；<br/>
         * 4. 若指定的parent并不存在于Activity栈中或者存在但已经destroy了，则按钮的事件和普通的finish无异；<br/>
         * 5. 若并没有指定parent，那么按钮除了照例回调onOptionsItemSelected外，没有任何事件响应；<br/>
         * 6. 默认launchMode下，parent若还“alive”，会先被destroy再create（注意：与状态保存恢复机制的重建不同）；<br/>
         * 7. 若不需要parent先被destroy，可以将launchMode指定为singleTop；<br/>
         */
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button button1 = (Button) findViewById(R.id.button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.act_menudemo_popup);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(MenuActivity.this, "click:" + item, Toast.LENGTH_SHORT).show();
                if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
                    getSupportFragmentManager().getFragments().get(0).setHasOptionsMenu(true);
                    return true;
                }
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.menu_act_container, new MenuFragment())
                        .addToBackStack(null)
                        .commit();
                return true;
            }
        });
        popupMenu.show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * 1. 整个初始化流程为onCreateOptionsMenu -> onPrepareOptionsMenu；<br/>
     * 2. 当初始化完毕，每次点击菜单按钮，只会触发回调onPrepareOptionsMenu；<br/>
     * 3. invalidateOptionsMenu()可触发重新初始化流程；<br/>
     * 4. 当Activity包含Fragment时，若Fragment调用了setHasOptionsMenu(boolean)：<br/>
     *      4.1. 则会触发invalidateOptionsMenu()；<br/>
     *      4.2. 若传入的参数为true，则随后的流程在回调时同时还会回调Fragment对应的方法；<br/>
     * @param menu
     * @return  返回false不会进行到下一步的onPrepareOptionsMenu流程
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(getTAG(), "onCreateOptionsMenu: " + menu);
        getMenuInflater().inflate(R.menu.act_menudemo_option1, menu);
        return true;
    }

    /**
     *
     * @param menu
     * @return  返回false菜单按钮不会出现
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.d(getTAG(), "onPrepareOptionsMenu: " + menu);
        return true;
    }

    /**
     * 估计该方法已被废弃，所以bug也不管了，未找到任何方法触发该方法的回调。
     * @param menu
     */
    @Override
    public void onOptionsMenuClosed(Menu menu) {
        Log.d(getTAG(), "onOptionsMenuClosed: " + menu);
    }

    /**
     * 所有的actions事件不管系统是否处理，最后都会来到该方法。
     * 如果 Activity 包括片段，
     * 则系统将依次为 Activity 和每个片段（按照每个片段的添加顺序）调用 onOptionsItemSelected()，
     * 直到有一个返回结果为 true 或所有片段均调用完毕为止。
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 调用super是一个良好的习惯，更何况有一些action的动画(app:actionViewClass)必须通过super以让系统执行。
        boolean backValue = super.onOptionsItemSelected(item);
        // "Up Button" 按钮的事件除了响应系统默认动作外，还会回调到该方法，但是不建议处理。
        switch (item.getItemId()) {
            case R.id.action_one:
                break;
            case R.id.action_two:
                break;
            case R.id.action_three:
                break;
            case R.id.action_four:
                break;
            default:
                return backValue;
        }
        Toast.makeText(MenuActivity.this, "option click:" + item, Toast.LENGTH_SHORT).show();
        return false;
    }
}
