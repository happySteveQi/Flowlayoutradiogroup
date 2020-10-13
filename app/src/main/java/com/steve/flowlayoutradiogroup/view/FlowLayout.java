package com.steve.flowlayoutradiogroup.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

public class FlowLayout extends ViewGroup {

    private int horizontalSpacing = 15;//水平间距
    private int verticalSpacing = 15;//行与行之间的垂直间距

    //用来存放所有的Line对象
    private ArrayList<Line> lineList = new ArrayList<Line>();
    public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FlowLayout(Context context) {
        super(context);
        init();
    }
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    private void setViewState(View child) {
        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                setViewState(view.getChildAt(i));
            }
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        setViewState(child);
        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FlowLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new FlowLayout.LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof FlowLayout.LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return FlowLayout.class.getName();
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     * <p/>
     * <p>See
     * {@link  LinearLayout Attributes}
     * for a list of all child view attributes that this class supports.</p>
     */
    public static class LayoutParams extends LinearLayout.LayoutParams {
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h) {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a          the styled attributes set
         * @param widthAttr  the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *  @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        void onCheckedChanged(FlowLayout group, int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            setListener(child);

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            removeListener(child);

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    /**
     * 设置监听
     *
     * @param child
     */
    private void setListener(View child) {
        if (child instanceof RadioButton) {
            int id = child.getId();
            // generates an id if it's missing
            if (id == View.NO_ID) {
                id = child.hashCode();
                child.setId(id);
            }
            ((RadioButton) child).setOnCheckedChangeListener(
                    mChildOnCheckedChangeListener);
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                setListener(view.getChildAt(i));
            }
        }
    }


    /**
     * 移除监听
     *
     * @param child
     */
    private void removeListener(View child) {
        if (child instanceof RadioButton) {
            ((RadioButton) child).setOnCheckedChangeListener(null);
        } else if (child instanceof ViewGroup) {
            ViewGroup view = (ViewGroup) child;
            for (int i = 0; i < view.getChildCount(); i++) {
                removeListener(view.getChildAt(i));
            }
        }
    }
    /**
     * 设置水平间距
     * @param horizontalSpacing
     */
    public void setHorizontalSpacing(int horizontalSpacing){
        this.horizontalSpacing = horizontalSpacing;
    }

    /**
     * 设置垂直间距
     * @param verticalSpacing
     */
    public void setVerticalSpacing(int verticalSpacing){
        this.verticalSpacing = verticalSpacing;
    }

    /**
     * 分行：遍历所有的子View，判断哪几个子View在同一行(排座位表)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        lineList.clear();

        //1.获取FlowLayout的宽度
        int width = MeasureSpec.getSize(widthMeasureSpec);
        //2.获取用于实际比较的宽度，就是除去2边的padding的宽度
        int noPaddingWidth = width-getPaddingLeft()-getPaddingRight();

        //3.遍历所有的子View，拿子View的宽和noPaddingWidth进行比较
        Line line = new Line();//准备Line对象
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.measure(0, 0);//保证能够获取到宽高

            //4.如果当前line中木有子View，则不用比较直接放入line中，因为要保证每行至少有一个子View;
            if(line.getViewList().size()==0){
                line.addLineView(childView);//直接存入
            }else if(line.getLineWidth()+horizontalSpacing+childView.getMeasuredWidth()>noPaddingWidth){
                //5.如果当前line的宽+水平间距+子View的宽大于noPaddingWidth,则child需要换行
                //需要先存放好之前的line对象，否则会造成丢失
                lineList.add(line);

                line = new Line();//创建新的Line，
                line.addLineView(childView);//将当前child放入新的行中
            }else {
                //6.说明当前child应该放入当前Line中
                line.addLineView(childView);
            }

            //7.如果当前child是最后的子View，那么需要保存最后的line对象
            if(i==(getChildCount()-1)){
                lineList.add(line);//保存最后的Line
            }
        }

        //for循环结束了，lineList存放了所有的Line，而每个Line又记录了自己行所有的VIew;
        //计算FLowLayout需要的高度
        int height = getPaddingTop()+getPaddingTop();//先计算上下的padding值
        for (int i = 0; i < lineList.size(); i++) {
            height += lineList.get(i).getLineHeight();//再加上所有行的高度
        }
        height += (lineList.size()-1)*verticalSpacing;//最后加上所有的行间距

        //设置当前控件的宽高，或者向父VIew申请宽高
        setMeasuredDimension(width, height);
    }
    /**
     * 去摆放所有的子View，让每个人真正的坐到自己的位置上
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        for (int i = 0; i < lineList.size(); i++) {
            Line line = lineList.get(i);//获取Line对象

            //从第二行开始，每行的top总是比上一行的top多一个行高和垂直间距
            if(i>0){
                paddingTop += verticalSpacing+lineList.get(i-1).getLineHeight();
            }

            ArrayList<View> viewList = line.getViewList();//获取line的view的集合

            //1.获取每行的留白的宽度
            int remainSpacing = getLineRemainSpacing(line);
            //2.计算每个view平均得到的值
            float perSpacing = remainSpacing/viewList.size();

            for (int j = 0; j < viewList.size(); j++) {
                View childView = viewList.get(j);
                //3.将得到的perSpacing增加到view的宽度上面
                int widthSpec = MeasureSpec.makeMeasureSpec((int) (childView.getMeasuredWidth()+perSpacing),MeasureSpec.EXACTLY);
                childView.measure(widthSpec,0);

                if(j==0){
                    //如果是每行的第一行，name直接靠左边摆放
                    childView.layout(paddingLeft,paddingTop,paddingLeft+childView.getMeasuredWidth(),
                            paddingTop+childView.getMeasuredHeight());
                }else {
                    //如果不是第一个，需要参考前一个view的right
                    View preView = viewList.get(j-1);
                    //当前view的left是前一个view的right+水平间距
                    int left = preView.getRight()+horizontalSpacing;
                    childView.layout(left, preView.getTop(),left+childView.getMeasuredWidth(),preView.getBottom());
                }
            }
        }
    }
    /**
     * 获取指定line的留白
     * @param line
     * @return
     */
    private int getLineRemainSpacing(Line line){
        return getMeasuredWidth()-getPaddingLeft()-getPaddingRight()-line.getLineWidth();
    }

    /**
     * 封装每行的数据，包括所有的子View，行的宽高
     * @author Administrator
     *
     */
    class Line{
        private ArrayList<View> viewList;//用来存放当前行所有的子View
        private int width;//表示所有子View的宽+水平间距
        private int height;//行的高度

        public Line(){
            viewList = new ArrayList<View>();
        }

        /**
         * 记录子VIew
         * @param child
         */
        public void addLineView(View child){
            if(!viewList.contains(child)){
                viewList.add(child);

                //1.更新Line的width
                if(viewList.size()==1){
                    //说明添加的是第一个子View，那么line的宽就是子view的宽度
                    width = child.getMeasuredWidth();
                }else {
                    //如果添加的不是第一个子View，那么应该加等于水平间距和子VIew的宽度
                    width += child.getMeasuredWidth()+horizontalSpacing;
                }
                //2.更新line的height
                height = Math.max(height,child.getMeasuredHeight());
            }
        }

        /**
         * 获取当前行的宽度
         * @return
         */
        public int getLineWidth(){
            return width;
        }
        /**
         * 获取当前行的高度
         * @return
         */
        public int getLineHeight(){
            return height;
        }
        /**
         * 获取当前行的所有的子View
         * @return
         */
        public ArrayList<View> getViewList(){
            return viewList;
        }
    }

}