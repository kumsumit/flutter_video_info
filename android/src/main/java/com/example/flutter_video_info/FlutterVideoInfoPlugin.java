package com.example.flutter_video_info;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.io.File;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterVideoInfoPlugin
 */
public class FlutterVideoInfoPlugin implements FlutterPlugin, MethodCallHandler {
    private Context context;
    private MethodChannel channel;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(),
                "flutter_video_info");
        context = flutterPluginBinding.getApplicationContext();
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getVidInfo")) {
            String path = call.argument("path");
            result.success(getVidInfo(path));
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (channel != null) {
            channel.setMethodCallHandler(null);
            channel = null;
        }
        context = null;
    }

    String getVidInfo(String path) {
        if (path == null) {
            path = "";
        }
        File file = new File(path);
        boolean isFileExists=file.exists();
        String title,author,album,artist,genre,dateString,mimeType,location,frameRateStr,widthStr,heightStr,durationStr,orientation,bitrateStr,hasAudioStr,frameCountStr;
        double filesize;
        if(isFileExists){
            MediaMetadataRetriever mediaRetriever = new MediaMetadataRetriever();
            try {
                mediaRetriever.setDataSource(context, Uri.fromFile(file));

                title = getData(MediaMetadataRetriever.METADATA_KEY_TITLE, mediaRetriever);
                author = getData(MediaMetadataRetriever.METADATA_KEY_AUTHOR, mediaRetriever);
                album = getData(MediaMetadataRetriever.METADATA_KEY_ALBUM, mediaRetriever);
                artist = getData(MediaMetadataRetriever.METADATA_KEY_ARTIST, mediaRetriever);
                genre = getData(MediaMetadataRetriever.METADATA_KEY_GENRE, mediaRetriever);
                dateString = getData(MediaMetadataRetriever.METADATA_KEY_DATE, mediaRetriever);
                dateString = formatDate(dateString);
                mimeType = getData(MediaMetadataRetriever.METADATA_KEY_MIMETYPE, mediaRetriever);
                location = getData(MediaMetadataRetriever.METADATA_KEY_LOCATION, mediaRetriever);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    frameRateStr = getData(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE, mediaRetriever);
                }else{
                    frameRateStr="";
                }
                durationStr = getData(MediaMetadataRetriever.METADATA_KEY_DURATION, mediaRetriever);
                widthStr = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH, mediaRetriever);
                heightStr = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT, mediaRetriever);
                filesize = file.length();
                orientation = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION, mediaRetriever);
                bitrateStr = getData(MediaMetadataRetriever.METADATA_KEY_BITRATE, mediaRetriever);
                hasAudioStr = getData(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO, mediaRetriever);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    frameCountStr = getData(MediaMetadataRetriever.METADATA_KEY_VIDEO_FRAME_COUNT, mediaRetriever);
                } else {
                    frameCountStr = "";
                }
            } catch (Exception e) {
                e.printStackTrace();
                title="";
                author="";
                album="";
                artist="";
                genre="";
                dateString="";
                mimeType="";
                location="";
                frameRateStr="";
                widthStr="";
                heightStr="";
                durationStr="";
                orientation="";
                bitrateStr="";
                hasAudioStr="";
                frameCountStr="";
                filesize = file.length();
            } finally {
                try {
                     mediaRetriever.release();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
        }else{
            title="";
            author="";
            album="";
            artist="";
            genre="";
            dateString="";
            mimeType="";
            location="";
            frameRateStr="";
            widthStr="";
            heightStr="";
            durationStr="";
            orientation="";
            bitrateStr="";
            hasAudioStr="";
            frameCountStr="";
            filesize=0;
        }

        JSONObject json = new JSONObject();
        try {
            json.put("path", path);
            json.put("title", title);
            json.put("mimeType", mimeType);
            json.put("author", author);
            json.put("album", album);
            json.put("artist", artist);
            json.put("genre", genre);
            json.put("date", dateString);
            json.put("width", widthStr);
            json.put("height", heightStr);
            json.put("location", location);
            json.put("frameRate", frameRateStr);
            json.put("duration", durationStr);
            json.put("fileSize", filesize);
            json.put("orientation", orientation);
            json.put("bitrate", bitrateStr);
            json.put("hasAudio", "yes".equalsIgnoreCase(hasAudioStr));
            json.put("frameCount", frameCountStr);
            json.put("isFileExist",isFileExists);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json.toString();
    }

    String getData(int key, MediaMetadataRetriever mediaRetriever) {
        try {
            String value = mediaRetriever.extractMetadata(key);
            return value == null ? "" : value;
        } catch (Exception e) {
            return "";
        }
    }

    String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "";
        }
        try {
            SimpleDateFormat readFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.getDefault());
            Date date = readFormat.parse(dateString);
            SimpleDateFormat outFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return date == null ? dateString : outFormat.format(date);
        } catch (Exception e) {
            return dateString;
        }
    }

}
