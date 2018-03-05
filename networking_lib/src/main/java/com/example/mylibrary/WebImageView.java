package com.example.mylibrary;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
//import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

public class WebImageView extends AppCompatImageView {
    protected Drawable mPlaceholder, mImage;

    private static final long TRANISTION_MILLISECONDS = 150;


    public WebImageView(Context context, AttributeSet set) {
        super(context,set);

        initImageTransitionAnimations();
    }
    public WebImageView(Context context) {
        super(context);

        initImageTransitionAnimations();
    }


    Animation animation1;
    Animation animation2;

    private void initImageTransitionAnimations() {
        animation1 = new AlphaAnimation(1.0f, 0.0f);
        animation1.setDuration(TRANISTION_MILLISECONDS);

        animation2 = new AlphaAnimation(0.0f, 1.0f);
        animation2.setDuration(TRANISTION_MILLISECONDS);
        animation2.setStartOffset(TRANISTION_MILLISECONDS);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setImageDrawable(mImage);
                startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void doTransitionAnimations(){
        startAnimation(animation1);
    }

    public void setImageUrl(String url) {
        DownloadTask task = new DownloadTask();
        task.execute(url);
    }

    private class DownloadTask extends AsyncTask<String, Void, Bitmap> {
        private static final String TAG = "DownloadTask";
        private static final int DEFAULTBUFFERSIZE = 50;
        private static final int NODATA = -1;

        @Override
        protected Bitmap doInBackground(String... params) {

            // site we want to connect to
            String url = params[0];

            // note streams are left willy-nilly here because it declutters the
            // example
            try {
                URL url1 = new URL(url);

                // this does no network IO
                HttpURLConnection connection = (HttpURLConnection) url1.openConnection();

                // can further configure connection before getting data
                // cannot do this after connected
                // connection.setRequestMethod("GET");
                // connection.setReadTimeout(timeoutMillis);
                // connection.setConnectTimeout(timeoutMillis);

                // this opens a connection, then sends GET & headers
                connection.connect();

                // lets see what we got make sure its one of
                // the 200 codes (there can be 100 of them
                // http_status / 100 != 2 does integer div any 200 code will = 2
                int statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    Log.e(TAG, "Error-connection.getResponseCode returned "
                            + Integer.toString(statusCode));
                    return null;
                }

                // get our streams, a more concise implementation is
                // BufferedInputStream bis = new
                // BufferedInputStream(connection.getInputStream());
                InputStream is = connection.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                // the following buffer will grow as needed
                ByteArrayOutputStream baf = new ByteArrayOutputStream(DEFAULTBUFFERSIZE);
                //ByteArrayBuffer baf = new ByteArrayBuffer(DEFAULTBUFFERSIZE);
                int current = 0;

                // wrap in finally so that stream bis is sure to close
                try {
                    while ((current = bis.read()) != NODATA) {
                        baf.write((byte) current);
                    }

                    // convert to a bitmap
                    byte[] imageData = baf.toByteArray();
                    return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                } finally {
                    // close resource no matter what exception occurs
                    bis.close();
                }
            } catch (Exception exc) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            mImage = new BitmapDrawable(result);

            if (mImage != null) {
                WebImageView.this.setPlaceholderImage(mImage);

//                doTransitionAnimations();
            }
        }



        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onCancelled()
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    };

    public void setPlaceholderImage(Drawable drawable){

    }

}
