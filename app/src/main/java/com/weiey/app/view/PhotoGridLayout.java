package com.weiey.app.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.weiey.app.R;
import com.weiey.app.utils.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class PhotoGridLayout extends RecyclerView {
    private PhotoAdapter mPhotoAdapter;
    private ItemActonListener itemActonListener;
    private GridLayoutManager mGridLayoutManager;

    /**
     * 是否开启加号
     */
    private boolean mPlusEnable;
    private boolean mSortable;
    private int mDeleteDrawableResId;
    private boolean mDeleteDrawableOverlapQuarter;
    private int mPhotoTopRightMargin;
    private int mMaxItemCount;
    private int mItemSpanCount;
    private int mPlusDrawableResId;
    private int mItemCornerRadius;
    private int mItemWhiteSpacing;
    private int mOtherWhiteSpacing;
    private int mPlaceholderDrawableResId;
    private boolean mEditable;

    private int mItemWidth;

    public PhotoGridLayout(Context context) {
        this(context, null);
    }

    public PhotoGridLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PhotoGridLayout(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initDefaultAttrs();
        initCustomAttrs(context, attrs);
        afterInitDefaultAndCustomAttrs();
    }

    private void initDefaultAttrs() {
        mPlusEnable = true;
        mSortable = true;
        mEditable = true;
        mDeleteDrawableResId = R.drawable.pgl_ic_delete_image;
        mDeleteDrawableOverlapQuarter = false;
        mMaxItemCount = 9;
        mItemSpanCount = 3;
        mItemWidth = 0;
        mItemCornerRadius = 0;
        mPlusDrawableResId = R.drawable.pgl_ic_add_image_item_bg;
        mItemWhiteSpacing = dp2px(4);
        mPlaceholderDrawableResId = R.drawable.pgl_ic_place_holder_light;
        mOtherWhiteSpacing = dp2px(100);
    }

    public int dp2px(float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getContext().getResources().getDisplayMetrics());
    }

    private void initCustomAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoGridLayout);
        final int N = typedArray.getIndexCount();
        for (int i = 0; i < N; i++) {
            initCustomAttr(typedArray.getIndex(i), typedArray);
        }
        typedArray.recycle();
    }

    private void initCustomAttr(int attr, TypedArray typedArray) {
        if (attr == R.styleable.PhotoGridLayout_pgl_plusEnable) {
            mPlusEnable = typedArray.getBoolean(attr, mPlusEnable);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_deleteDrawable) {
            mDeleteDrawableResId = typedArray.getResourceId(attr, mDeleteDrawableResId);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_deleteDrawableOverlapQuarter) {
            mDeleteDrawableOverlapQuarter = typedArray.getBoolean(attr, mDeleteDrawableOverlapQuarter);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_maxItemCount) {
            mMaxItemCount = typedArray.getInteger(attr, mMaxItemCount);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_itemSpanCount) {
            mItemSpanCount = typedArray.getInteger(attr, mItemSpanCount);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_plusDrawable) {
            mPlusDrawableResId = typedArray.getResourceId(attr, mPlusDrawableResId);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_itemCornerRadius) {
            mItemCornerRadius = typedArray.getDimensionPixelSize(attr, 0);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_itemWhiteSpacing) {
            mItemWhiteSpacing = typedArray.getDimensionPixelSize(attr, mItemWhiteSpacing);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_otherWhiteSpacing) {
            mOtherWhiteSpacing = typedArray.getDimensionPixelOffset(attr, mOtherWhiteSpacing);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_placeholderDrawable) {
            mPlaceholderDrawableResId = typedArray.getResourceId(attr, mPlaceholderDrawableResId);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_editable) {
            mEditable = typedArray.getBoolean(attr, mEditable);
        } else if (attr == R.styleable.PhotoGridLayout_pgl_itemWidth) {
            mItemWidth = typedArray.getDimensionPixelSize(attr, mItemWidth);
        }
    }

    private void afterInitDefaultAndCustomAttrs() {
        if (mItemWidth == 0) {
            mItemWidth = (getScreenWidth() - mOtherWhiteSpacing) / mItemSpanCount;
        } else {
            mItemWidth += mItemWhiteSpacing;
        }

        setOverScrollMode(OVER_SCROLL_NEVER);


        mGridLayoutManager = new GridLayoutManager(getContext(), mItemSpanCount);
        setLayoutManager(mGridLayoutManager);
        addItemDecoration(new GridDivider(mItemWhiteSpacing / 2));


        mPhotoAdapter = new PhotoAdapter(getContext());

        setAdapter(mPhotoAdapter);
        mPhotoAdapter.setOnClickItemViewListener(new onClickItemViewListener() {
            @Override
            public void onClick(View v, int position) {
                if (v.getId() == R.id.iv_item_photo) {
                    if (mPhotoAdapter.isPlusItem(position)) {
                        if (itemActonListener != null) {
                            itemActonListener.onClickAddPhotoItem(PhotoGridLayout.this, v, position, getData());
                        }
                    } else {
                        if (itemActonListener != null && ViewCompat.getScaleX(v) <= 1.0f) {
                            itemActonListener.onClickPhotoItem(PhotoGridLayout.this, v, position, mPhotoAdapter.getItem(position), getData());
                        }
                    }
                } else if (v.getId() == R.id.iv_item_photo_flag) {
                    if (itemActonListener != null) {
                        String model = mPhotoAdapter.getItem(position);
                        itemActonListener.onClickDeletePhotoItem(PhotoGridLayout.this, v, position, model, getData());
                    }
                }
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int spanCount = mItemSpanCount;
        int itemCount = mPhotoAdapter.getItemCount();
        if (itemCount > 0 && itemCount < mItemSpanCount) {
            spanCount = itemCount;
        }
        mGridLayoutManager.setSpanCount(spanCount);

        int expectWidth = mItemWidth * spanCount;
        int expectHeight = 0;
        if (itemCount > 0) {
            int rowCount = (itemCount - 1) / spanCount + 1;
            expectHeight = mItemWidth * rowCount;
        }

        int width = resolveSize(expectWidth, widthMeasureSpec);
        int height = resolveSize(expectHeight, heightMeasureSpec);
        width = Math.min(width, expectWidth);
        height = Math.min(height, expectHeight);

        setMeasuredDimension(width, height);
    }

    /**
     * 获取图片路径数据集合
     *
     * @return
     */
    public ArrayList<String> getData() {
        return (ArrayList<String>) mPhotoAdapter.getData();
    }

    /**
     * 计算图片右上角 margin
     */
    private void calculatePhotoTopRightMargin() {
        if (mDeleteDrawableOverlapQuarter) {
            int deleteDrawableWidth = BitmapFactory.decodeResource(getResources(), mDeleteDrawableResId).getWidth();
            int deleteDrawablePadding = getResources().getDimensionPixelOffset(R.dimen.pgl_size_delete_padding);
            mPhotoTopRightMargin = deleteDrawablePadding + deleteDrawableWidth / 2;
        } else {
            mPhotoTopRightMargin = 0;
        }
    }

    public void setItemActonListener(ItemActonListener l) {
        this.itemActonListener = l;
    }

    /**
     * 设置是否可编辑，默认值为 true
     *
     * @param editable
     */
    public void setEditable(boolean editable) {
        mEditable = editable;
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 获取是否可编辑
     *
     * @return
     */
    public boolean isEditable() {
        return mEditable;
    }

    /**
     * 设置删除按钮图片资源id，默认值为
     *
     * @param deleteDrawableResId
     */
    public void setDeleteDrawableResId(@DrawableRes int deleteDrawableResId) {
        mDeleteDrawableResId = deleteDrawableResId;
        calculatePhotoTopRightMargin();
    }

    /**
     * 设置可选择图片的总张数,默认值为 9
     *
     * @param maxItemCount
     */
    public void setMaxItemCount(int maxItemCount) {
        mMaxItemCount = maxItemCount;
    }

    /**
     * 获取选择的图片的最大张数
     *
     * @return
     */
    public int getMaxItemCount() {
        return mMaxItemCount;
    }

    /**
     * 获取图片总数
     *
     * @return
     */
    public int getItemCount() {
        return mPhotoAdapter.getData().size();
    }

    /**
     * 设置列数,默认值为 3
     *
     * @param itemSpanCount
     */
    public void setItemSpanCount(int itemSpanCount) {
        mItemSpanCount = itemSpanCount;
        mGridLayoutManager.setSpanCount(mItemSpanCount);
    }

    /**
     * 设置添加按钮图片，默认值为 R.mipmap.bga_pp_ic_plus
     *
     * @param plusDrawableResId
     */
    public void setPlusDrawableResId(@DrawableRes int plusDrawableResId) {
        mPlusDrawableResId = plusDrawableResId;
    }

    /**
     * 设置 Item 条目圆角尺寸，默认值为 0dp
     *
     * @param itemCornerRadius
     */
    public void setItemCornerRadius(int itemCornerRadius) {
        mItemCornerRadius = itemCornerRadius;
    }

    /**
     * 设置图片路径数据集合
     *
     * @param photos
     */
    public void setData(ArrayList<String> photos) {
        mPhotoAdapter.setData(photos);
    }

    /**
     * 在集合尾部添加更多数据集合
     *
     * @param photos
     */
    public void addMoreData(ArrayList<String> photos) {
        if (photos != null) {
            mPhotoAdapter.getData().addAll(photos);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 删除指定索引位置的图片
     *
     * @param position
     */
    public void removeItem(int position) {
        mPhotoAdapter.removeItem(position);
    }

    public class PhotoAdapter extends Adapter<PhotoAdapter.VH> {
        private Context context;
        private int mImageSize;
        private onClickItemViewListener onClickListener;

        //② 创建ViewHolder
        class VH extends ViewHolder {
            public CircleImageView iv_item_photo;
            public ImageView iv_item_photo_flag;

            public VH(View v) {
                super(v);
                iv_item_photo = (CircleImageView) v.findViewById(R.id.iv_item_photo);
                iv_item_photo_flag = (ImageView) v.findViewById(R.id.iv_item_photo_flag);
            }
        }

        public void setOnClickItemViewListener(onClickItemViewListener onClickFlagListener) {
            this.onClickListener = onClickFlagListener;
        }

        public PhotoAdapter(Context context) {
            this.context = context;
            mImageSize = getScreenWidth() / (mItemSpanCount > 3 ? 8 : 6);
        }

        private List<String> mDatas = new ArrayList<>();

        public void setDatas(List<String> mDatas) {
            this.mDatas = mDatas;
            notifyDataSetChanged();
        }


        public List<String> getData() {
            return mDatas;
        }

        /**
         * 清空数据列表
         */
        public void clear() {
            mDatas.clear();
            notifyDataSetChanged();
        }

        /**
         * 删除指定索引数据条目
         *
         * @param position
         */
        public void removeItem(int position) {
            mDatas.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getItemCount());
        }


        public void setData(List<String> list) {
            if (list != null && !list.isEmpty()) {
                mDatas = list;
            } else {
                mDatas.clear();
            }
        }

        //③ 在Adapter中实现3个方法
        @Override
        public void onBindViewHolder(VH holder, final int position) {
            holder.iv_item_photo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //item 点击事件
                }
            });
            MarginLayoutParams params = (MarginLayoutParams) holder.iv_item_photo.getLayoutParams();
            params.setMargins(0, mPhotoTopRightMargin, mPhotoTopRightMargin, 0);

            if (mItemCornerRadius > 0) {
                holder.iv_item_photo.setCornerRadius(mItemCornerRadius);
            }

            if (isPlusItem(position)) {
                holder.iv_item_photo_flag.setVisibility(View.GONE);
                holder.iv_item_photo.setImageResource(mPlusDrawableResId);

            } else {
                if (mEditable) {
                    holder.iv_item_photo_flag.setVisibility(View.VISIBLE);
                    holder.iv_item_photo_flag.setImageResource(mDeleteDrawableResId);
                } else {
                    holder.iv_item_photo_flag.setVisibility(View.GONE);
                }
                ImageLoader.getInstance().displayImage(context, getItem(position), holder.iv_item_photo);
            }
            holder.iv_item_photo_flag.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(v, position);
                    }
                }
            });
            holder.iv_item_photo.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onClickListener != null) {
                        onClickListener.onClick(v, position);
                    }
                }
            });
        }

        public String getItem(int position) {
            if (isPlusItem(position)) {
                return null;
            }
            return mDatas.get(position);
        }

        public boolean isPlusItem(int position) {
            return mEditable && mPlusEnable && mDatas.size() < mMaxItemCount && position == getItemCount() - 1;
        }

        @Override
        public int getItemCount() {
            if (mEditable && mPlusEnable && mDatas.size() < mMaxItemCount) {
                return mDatas.size() + 1;
            }

            return mDatas.size();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_grid_layout_item, parent, false);
            return new VH(v);
        }
    }

    class GridDivider extends ItemDecoration {
        private int mSpace;

        private GridDivider(int space) {
            mSpace = space;
        }


        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.top = mSpace;
            outRect.bottom = mSpace;
        }
    }

    public interface onClickItemViewListener {
        void onClick(View view, int position);
    }

    public interface ItemActonListener {
        void onClickAddPhotoItem(PhotoGridLayout photoGridLayout, View view, int position, ArrayList<String> models);

        void onClickDeletePhotoItem(PhotoGridLayout photoGridLayout, View view, int position, String model, ArrayList<String> models);

        void onClickPhotoItem(PhotoGridLayout photoGridLayout, View view, int position, String model, ArrayList<String> models);
    }
}
