//package com.example.signin;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.AsyncTask;
//
//import com.ctaiot.ctprinter.ctpl.CTPL;
//import com.ctaiot.ctprinter.ctpl.param.PaperType;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//
//class ImageAsyncTask extends AsyncTask<String, Void, Bitmap>
//{
//    private Bitmap bitmap;
//
//
//    @Override
//    protected Bitmap doInBackground(String... params)
//    {
//        InputStream is = null;
//        try
//        {
//            URL url = new URL(params[0]);
//            URLConnection openConnection = url.openConnection();
//            is = openConnection.getInputStream();
//            bitmap = BitmapFactory.decodeStream(is);
//            if (bitmap != null)
//            {
//                hadImage.put(params[0], bitmap);
//            }
//        }
//        catch (MalformedURLException e)
//        {
//
//            e.printStackTrace();
//        }
//        catch (IOException e)
//        {
//
//            e.printStackTrace();
//        }
//        finally
//        {
//            if (is != null)
//            {
//                try
//                {
//                    is.close();
//                }
//                catch (IOException e)
//                {
//
//                    e.printStackTrace();
//                }
//            }
//        }
//        return bitmap;
//    }
//
//    @Override
//    protected void onPostExecute(Bitmap b)
//    {
//        // 下载完显示
//        CTPL.getInstance().setPaperType(PaperType.Label).setPrintSpeed(1)
//                .setSize(
//                        data.cardW.toDouble().toInt(),
//                        data.cardH.toDouble().toInt()
//                ) //设置纸张尺寸,单位:毫米
//                .drawBitmap(
//                        Rect(
//                                0,
//                                0,
//                                data.cardW.toDouble().toInt() * 8 + 50,
//                                data.cardH.toDouble().toInt() * 8 + 30
//                        ), b, true, null
//                ) //绘制图像, 单位:像素
//                .print(1)
//                .execute(); //执行打印
//
//        super.onPostExecute(result);
//    }
//}
//
