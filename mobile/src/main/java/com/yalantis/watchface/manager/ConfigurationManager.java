package com.yalantis.watchface.manager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yalantis.watchface.Constants;
import com.yalantis.watchface.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author andrewkhristyan on 10/21/15.
 */
public class ConfigurationManager {

    private static final String DEFAULT_JSON_CONFIGURATION = "{seconds_offset:0,minutes_offset:0,hours_offset:0}";
    public static final String BACKGROUND = "bg";
    public static final String BG_AMBIENT = "bg_ambient";
    public static final String TICK = "tick";
    public static final String MINUTE = "minute";
    public static final String HOURS = "hours";
    public static final String HOURS_AMBIENT = "hrs_ambient";
    public static final String MIN_AMBIENT = "min_ambient";

    private Map<String, Bitmap> configMap = new HashMap<>();
    private JSONObject mJsonObjectConfig;
    private File mJsonFile;

    public void init(Context context) {
        Resources resources = context.getResources();
        fillMap(resources);
        mJsonObjectConfig = getJsonString();
    }

    @Nullable
    private JSONObject getJsonString() {
        StringBuilder builder = new StringBuilder();
        JSONObject jsonObject = null;
        try {
            File sdcard = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/configuration/");
            if (!sdcard.exists()) {
                sdcard.mkdirs();
            }
            mJsonFile = new File(sdcard, "configuration.json");
            if (!mJsonFile.exists()) {
                FileWriter writer = new FileWriter(mJsonFile);
                writer.append(DEFAULT_JSON_CONFIGURATION);
                writer.flush();
                writer.close();
                jsonObject = new JSONObject(DEFAULT_JSON_CONFIGURATION);
            } else {
                BufferedReader bufferedReader =
                        new BufferedReader(new FileReader(mJsonFile));
                String str;
                while ((str = bufferedReader.readLine()) != null) {
                    builder.append(str);
                }
                jsonObject = new JSONObject(builder.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void updateConfigurationParameter(String key, int data) {
        if (mJsonObjectConfig != null) {
            try {
                mJsonObjectConfig.put(key, data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void fillMap(Resources resources) {
        configMap.put(BACKGROUND, BitmapFactory.decodeResource(resources, R.drawable.bg));
        configMap.put(BG_AMBIENT, BitmapFactory.decodeResource(resources, R.drawable.bg_ambient));
        configMap.put(TICK, BitmapFactory.decodeResource(resources, R.drawable.tick));
        configMap.put(MINUTE, BitmapFactory.decodeResource(resources, R.drawable.min));
        configMap.put(HOURS, BitmapFactory.decodeResource(resources, R.drawable.hrs));
        configMap.put(HOURS_AMBIENT, BitmapFactory.decodeResource(resources, R.drawable.hrs_ambient));
        configMap.put(MIN_AMBIENT, BitmapFactory.decodeResource(resources, R.drawable.min_ambient));
    }

    public void updateField(int name, Bitmap bitmap) {
        String key = Constants.resourceKeyMap.get(name);
        configMap.put(key, bitmap);
    }

    public void resetWatchFace(Resources resources) {
        fillMap(resources);
    }

    public void saveConfiguration() {
        for (Map.Entry<String, Bitmap> entry : configMap.entrySet()) {
            File pictureFile = getOutputMediaFile(entry.getKey());
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                entry.getValue().compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
                updateConfigurationFile();
            } catch (FileNotFoundException e) {
                Log.d("Configuration", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("Configuration", "Error accessing file: " + e.getMessage());
            }
        }
    }

    private void updateConfigurationFile() throws IOException {
        FileWriter writer = new FileWriter(mJsonFile);
        writer.append(mJsonObjectConfig.toString());
        writer.flush();
        writer.close();
    }

    public int getConfigItem(String key) {
        int configItem = 0;
        try {
            configItem = mJsonObjectConfig.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return configItem;
    }


    private File getOutputMediaFile(String fileName) {
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/drawable-nodpi/");
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + fileName + ".png");
        return mediaFile;
    }
}
