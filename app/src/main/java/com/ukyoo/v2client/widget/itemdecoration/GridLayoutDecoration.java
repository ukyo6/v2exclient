package com.ukyoo.v2client.widget.itemdecoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by hewei26 on 2017/6/6.
 */

public class GridLayoutDecoration extends RecyclerView.ItemDecoration {

    private int mspaceHorizontal;
    private int mspaceVertival;
    private int mGridSize;

    private boolean mNeedLeftSpacing = false;

    public GridLayoutDecoration(int spaceHorizontal, int spaceVertical, int gridSize) {
        mspaceHorizontal = spaceHorizontal;
        mspaceVertival = spaceVertical;
        mGridSize = gridSize;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int frameWidth = (int) ((parent.getWidth() - (float) mspaceHorizontal * (mGridSize - 1)) / mGridSize); //每个item的宽度
        int padding = parent.getWidth() / mGridSize - frameWidth;


        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
        if (itemPosition < mGridSize) { //第一行没有top,下面的都有
            outRect.top = 0;
        } else {
            outRect.top = mspaceVertival;
        }

        if (itemPosition % mGridSize == 0) {  //第一列
            outRect.left = 0;
            outRect.right = padding;
            mNeedLeftSpacing = true;
        } else if ((itemPosition + 1) % mGridSize == 0) {   //最后一列
            mNeedLeftSpacing = false;
            outRect.right = 0;
            outRect.left = padding;
        } else if (mNeedLeftSpacing) { //第二列
            mNeedLeftSpacing = false;
            outRect.left = mspaceHorizontal - padding;
            if ((itemPosition + 2) % mGridSize == 0) {
                outRect.right = mspaceHorizontal - padding;
            } else {
                outRect.right = mspaceHorizontal / 2;
            }
        } else if ((itemPosition + 2) % mGridSize == 0) {
            mNeedLeftSpacing = false;
            outRect.left = mspaceHorizontal / 2;
            outRect.right = mspaceHorizontal - padding;
        } else {
            mNeedLeftSpacing = false;
            outRect.left = mspaceHorizontal / 2;
            outRect.right = mspaceHorizontal / 2;
        }
        outRect.bottom = 0;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
}
