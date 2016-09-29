package com.attmm.admin.projectt;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.attmm.admin.projectt.adapter.HistoryAdapter;
import com.attmm.admin.projectt.view.History;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

/**
 * Created by imo on 2016/8/13.
 */
public class HistoryActivity extends AppCompatActivity implements Constant {

    int[] color ={Color.argb(0,0x3b,0xb6,0xd2)
            ,Color.argb(0,0xeb,0x13,0x46)
            ,Color.argb(0,0xfa,0xbf,0x0c)
            ,Color.argb(0,0x67,0x3A,0xB7)};
    Firebase ref;
    SharedPreferences setting,settler;
    RecyclerView mHistoryContainer;
    HistoryAdapter mHistoryHolderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Firebase.setAndroidContext(this);
        setting = getSharedPreferences(LOGIN_SHAREDPREFERENCE,0);
        settler = getSharedPreferences("set", 0);
        ref = new Firebase("https://mis-atm.firebaseio.com/history");
        mHistoryContainer = (RecyclerView) findViewById(R.id.history_container);
        mHistoryHolderAdapter = new HistoryAdapter(getColor());
        mHistoryContainer.setAdapter(mHistoryHolderAdapter);
        mHistoryContainer.setLayoutManager(new LinearLayoutManager(this));
        mHistoryContainer.addItemDecoration(new DividerItemDecoration(this, R.drawable.history_divider));

        getData();

    }

    private void getData(){
        ref.child(setting.getString(LOGIN_ID,""))
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot d :dataSnapshot.getChildren()){
                    final History h =d.getValue(History.class);
                    mHistoryHolderAdapter.addHistory(h);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private int getColor(){
        return color[settler.getInt("color",1)];
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

        private Drawable mDivider;

        /**
         * Default divider will be used
         */
        public DividerItemDecoration(Context context) {
            final TypedArray styledAttributes = context.obtainStyledAttributes(ATTRS);
            mDivider = styledAttributes.getDrawable(0);
            styledAttributes.recycle();
        }

        /**
         * Custom divider will be used
         */
        public DividerItemDecoration(Context context, int resId) {
            mDivider = ContextCompat.getDrawable(context, resId);
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);

                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

                int top = child.getBottom() + params.bottomMargin;
                int bottom = top + mDivider.getIntrinsicHeight();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
