package com.lqr.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.lqr.wechat.R;
import com.lqr.wechat.model.Contact;
import com.lqr.wechat.model.UploadGoodsBean;
import com.lqr.wechat.utils.Config;
import com.lqr.wechat.utils.DbTOPxUtil;
import com.lqr.wechat.view.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.PhotoSelectorActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-05-06.
 */

public class AddCaseActivity extends BaseActivity {
    public static final int REQ_CHANGE_ADD_CASE = 201;

    private LayoutInflater inflater;
    private ImageView add_IB;
    private MyGridView my_imgs_history;
    private MyGridView my_imgs_assay;
    private MyGridView my_imgs_image;
    private MyGridView my_imgs_medication;
    private int screen_widthOffset;
    private ArrayList<UploadGoodsBean> img_uri4history = new ArrayList<UploadGoodsBean>();
    private ArrayList<UploadGoodsBean> img_uri4assay = new ArrayList<UploadGoodsBean>();
    private ArrayList<UploadGoodsBean> img_uri4image = new ArrayList<UploadGoodsBean>();
    private ArrayList<UploadGoodsBean> img_uri4medication = new ArrayList<UploadGoodsBean>();

    private List<PhotoModel> single_photos = new ArrayList<PhotoModel>();
    GridImgHistoryAdapter gridImgsAdapter4history;
    GridImgAssayAdapter gridImgsAdapter4assay;
    GridImgImageAdapter gridImgsAdapter4image;
    GridImgMedicationAdapter gridImgsAdapter4medication;

    private int Channel = 0;
    private Contact mContact;
    @Override
    public void init() {
        mContact = (Contact) getIntent().getSerializableExtra("contact");
        if (mContact == null) {
            interrupt();
            return;
        }
        img_uri4history = new ArrayList<UploadGoodsBean>();
        img_uri4assay = new ArrayList<UploadGoodsBean>();
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_add_case);
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true).cacheOnDisc(true).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext()).defaultDisplayImageOptions(
                defaultOptions).build();
        ImageLoader.getInstance().init(config);
        Config.ScreenMap = Config.getScreenSize(this, this);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screen_widthOffset = (display.getWidth() - (3* DbTOPxUtil.dip2px(this, 2)))/4;
        inflater = LayoutInflater.from(this);

        my_imgs_history = (MyGridView) findViewById(R.id.my_goods_history);
        gridImgsAdapter4history = new GridImgHistoryAdapter();
        my_imgs_history.setAdapter(gridImgsAdapter4history);
        img_uri4history.add(null);
        gridImgsAdapter4history.notifyDataSetChanged();


        my_imgs_assay = (MyGridView) findViewById(R.id.my_goods_assay);
        gridImgsAdapter4assay = new GridImgAssayAdapter();
        my_imgs_assay.setAdapter(gridImgsAdapter4assay);
        img_uri4assay.add(null);
        gridImgsAdapter4assay.notifyDataSetChanged();

        my_imgs_image = (MyGridView) findViewById(R.id.my_goods_image);
        gridImgsAdapter4image = new GridImgImageAdapter();
        my_imgs_image.setAdapter(gridImgsAdapter4image);
        img_uri4image.add(null);
        gridImgsAdapter4image.notifyDataSetChanged();

        my_imgs_medication = (MyGridView) findViewById(R.id.my_goods_medication);
        gridImgsAdapter4medication = new GridImgMedicationAdapter();
        my_imgs_medication.setAdapter(gridImgsAdapter4medication);
        img_uri4medication.add(null);
        gridImgsAdapter4medication.notifyDataSetChanged();
    }


    class GridImgHistoryAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri4history.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.activity_addstory_img_item, null);
            add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            ImageView delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (img_uri4history.get(position) == null) {
                delete_IV.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, add_IB);
                add_IB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(AddCaseActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 9 - (img_uri4history.size() - 1));
                        intent.putExtra("channel",1);
                        startActivityForResult(intent, 0);
                    }
                });

            } else {
                ImageLoader.getInstance().displayImage("file://" + img_uri4history.get(position).getUrl(), add_IB);
                delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri4history.remove(position).getUrl();
                        for (int i = 0; i < img_uri4history.size(); i++) {
                            if (img_uri4history.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri4history.add(null);
                        }
//						FileUtils.DeleteFolder(img_url);
                        gridImgsAdapter4history.notifyDataSetChanged();
                    }
                });

                add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putString("save","save");
                        CommonUtils.launchActivity(AddCaseActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }
    class GridImgAssayAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri4assay.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.activity_addstory_img_item, null);
            add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            ImageView delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (img_uri4assay.get(position) == null) {
                delete_IV.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, add_IB);
                add_IB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(AddCaseActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 9 - (img_uri4assay.size() - 1));
                        intent.putExtra("channel",2);
                        startActivityForResult(intent, 0);
                    }
                });

            } else {
                ImageLoader.getInstance().displayImage("file://" + img_uri4assay.get(position).getUrl(), add_IB);
                delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri4assay.remove(position).getUrl();
                        for (int i = 0; i < img_uri4assay.size(); i++) {
                            if (img_uri4assay.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri4assay.add(null);
                        }
//						FileUtils.DeleteFolder(img_url);
                        gridImgsAdapter4assay.notifyDataSetChanged();
                    }
                });

                add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putString("save","save");
                        CommonUtils.launchActivity(AddCaseActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }

        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }
    class GridImgImageAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri4image.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.activity_addstory_img_item, null);
            add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            ImageView delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (img_uri4image.get(position) == null) {
                delete_IV.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, add_IB);
                add_IB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(AddCaseActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 9 - (img_uri4image.size() - 1));
                        intent.putExtra("channel",3);
                        startActivityForResult(intent, 0);
                    }
                });

            } else {
                ImageLoader.getInstance().displayImage("file://" + img_uri4image.get(position).getUrl(), add_IB);
                delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri4image.remove(position).getUrl();
                        for (int i = 0; i < img_uri4image.size(); i++) {
                            if (img_uri4image.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri4image.add(null);
                        }
//						FileUtils.DeleteFolder(img_url);
                        gridImgsAdapter4image.notifyDataSetChanged();
                    }
                });

                add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putString("save","save");
                        CommonUtils.launchActivity(AddCaseActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }
    class GridImgMedicationAdapter extends BaseAdapter implements ListAdapter {
        @Override
        public int getCount() {
            return img_uri4medication.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.activity_addstory_img_item, null);
            add_IB = (ImageView) convertView.findViewById(R.id.add_IB);
            ImageView delete_IV = (ImageView) convertView.findViewById(R.id.delete_IV);
            AbsListView.LayoutParams param = new AbsListView.LayoutParams(screen_widthOffset, screen_widthOffset);
            convertView.setLayoutParams(param);
            if (img_uri4medication.get(position) == null) {
                delete_IV.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage("drawable://" + R.drawable.iv_add_the_pic, add_IB);
                add_IB.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        Intent intent = new Intent(AddCaseActivity.this, PhotoSelectorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        intent.putExtra("limit", 9 - (img_uri4medication.size() - 1));
                        intent.putExtra("channel",4);
                        startActivityForResult(intent, 0);
                    }
                });

            } else {
                ImageLoader.getInstance().displayImage("file://" + img_uri4medication.get(position).getUrl(), add_IB);
                delete_IV.setOnClickListener(new View.OnClickListener() {
                    private boolean is_addNull;
                    @Override
                    public void onClick(View arg0) {
                        is_addNull = true;
                        String img_url = img_uri4medication.remove(position).getUrl();
                        for (int i = 0; i < img_uri4medication.size(); i++) {
                            if (img_uri4medication.get(i) == null) {
                                is_addNull = false;
                                continue;
                            }
                        }
                        if (is_addNull) {
                            img_uri4medication.add(null);
                        }
//						FileUtils.DeleteFolder(img_url);
                        gridImgsAdapter4medication.notifyDataSetChanged();
                    }
                });

                add_IB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photos",(Serializable)single_photos);
                        bundle.putInt("position", position);
                        bundle.putString("save","save");
                        CommonUtils.launchActivity(AddCaseActivity.this, PhotoPreviewActivity.class, bundle);
                    }
                });
            }
            return convertView;
        }
        class ViewHolder {
            ImageView add_IB;
            ImageView delete_IV;
        }
    }
    private ArrayList<UploadGoodsBean> getImgUrl(int channel){
        if(channel == 1){
            return img_uri4history;
        }else if(channel == 2){
            return img_uri4assay;
        }else if(channel == 3){
            return img_uri4image;
        }else{
            return img_uri4medication;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data != null) {
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");
                    //int local_Channel = (int) data.getExtras().getSerializable("channel");
                    int local_Channel = data.getIntExtra("channel", 0);
                    if (getImgUrl(local_Channel).size() > 0) {

                        getImgUrl(local_Channel).remove(getImgUrl(local_Channel).size() - 1);
                    }

                    for (int i = 0; i < paths.size(); i++) {
                        getImgUrl(local_Channel).add(new UploadGoodsBean(paths.get(i), false));
                        //上传参数
                    }
                    for (int i = 0; i < paths.size(); i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(paths.get(i));
                        photoModel.setChecked(true);
                        single_photos.add(photoModel);
                    }
                    if (getImgUrl(local_Channel).size() < 9) {
                        getImgUrl(local_Channel).add(null);
                    }
                    if(local_Channel == 1){
                        gridImgsAdapter4history.notifyDataSetChanged();
                    }else if(local_Channel == 2){
                        gridImgsAdapter4assay.notifyDataSetChanged();
                    }else if(local_Channel == 3){
                        gridImgsAdapter4image.notifyDataSetChanged();
                    }else{
                        gridImgsAdapter4medication.notifyDataSetChanged();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
