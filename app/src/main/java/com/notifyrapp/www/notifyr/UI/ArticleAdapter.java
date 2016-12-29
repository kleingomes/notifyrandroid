package com.notifyrapp.www.notifyr.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.notifyrapp.www.notifyr.Business.CallbackInterface;
import com.notifyrapp.www.notifyr.Business.DownloadImageTask;
import com.notifyrapp.www.notifyr.Business.ImageCacheManager;
import com.notifyrapp.www.notifyr.Model.Article;
import com.notifyrapp.www.notifyr.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by K on 12/27/2016.
 */

public class ArticleAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<Article> mDataSource;


    public ArticleAdapter(Context context, List<Article> articles) {
        mContext = context;
        mDataSource = articles;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
        //return mDataSource.get(position).getId();
    }

    //4
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {

        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_article_image, parent, false);

        // Get Title element
        TextView titleTextView = (TextView) rowView.findViewById(R.id.txtImageArticleTitle);

        // Get Subtitle element
        TextView subtitleTextView = (TextView) rowView.findViewById(R.id.txtImageArticleSubTitle);

        // Get image
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgArticle);

        // Get Article
        final Article article = (Article) getItem(position);

        // Get Progress Bar
        final ProgressBar mProgressBar =  (ProgressBar) rowView.findViewById(R.id.pbHeaderProgress);

        // Load the Elements with data
        titleTextView.setText(article.getTitle());
        subtitleTextView.setText(article.getSource() + " - " + article.getTimeAgo());

        // Load the image element ( TODO: Image loads everytime articles are seen....need to cache this locally somehow??? )
        String imageUrl = article.getIurl();

        // Check if the image is in cache
        Bitmap image = ImageCacheManager.getImage(String.valueOf(article.getId()), mContext);
        if(image != null)
        {
            imageView.setImageBitmap(image);
        }
        else
        {
            mProgressBar.setVisibility(View.VISIBLE);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                new DownloadImageTask(imageView,mProgressBar, new CallbackInterface() {
                    @Override
                    public void onCompleted(Object data) {
                        if(data != null) {
                            Bitmap articleImage = (Bitmap) data;
                            ImageCacheManager.saveImage(String.valueOf(article.getId()), articleImage,mContext);
                        }
                        mProgressBar.setVisibility(View.GONE);
                    }
                }).execute(imageUrl);
            }
        }

        return rowView;
    }

}
