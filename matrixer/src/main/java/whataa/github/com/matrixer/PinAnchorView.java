package whataa.github.com.matrixer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * Created by Summer on 2016/10/8.
 */

public class PinAnchorView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = PinAnchorView.class.getSimpleName();

    public PinAnchorView(Context context) {
        this(context, null);
    }

    public PinAnchorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PinAnchorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.d(TAG, scrollState+" ");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        Log.d(TAG, firstVisibleItem+" "+visibleItemCount+" "+totalItemCount);
    }
}
