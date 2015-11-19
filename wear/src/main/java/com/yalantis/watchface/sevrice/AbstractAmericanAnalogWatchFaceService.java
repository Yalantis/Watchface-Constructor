package com.yalantis.watchface.sevrice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.view.SurfaceHolder;

import com.yalantis.watchface.Constants;
import com.yalantis.watchface.events.WatchfaceUpdatedEvent;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

public abstract class AbstractAmericanAnalogWatchFaceService extends CanvasWatchFaceService {

    protected abstract Context getContext();

    protected abstract Map<String, Bitmap> getBitmaps();

    private final static float ORIGINAL_SIZE = 320;

    @Override
    public CanvasWatchFaceService.Engine onCreateEngine() {
        return new Engine();
    }

    /**
     * Manages the US flag watch face behaviour.
     */
    protected class Engine extends CanvasWatchFaceService.Engine {

        private final int MSG_UPDATE_TIME = 0;
        private final long INTERACTIVE_UPDATE_RATE_MS = TimeUnit.SECONDS.toMillis(1);

        private final int MINUTES_TO_DEGREES = 360 / 60;
        private final int HOURS_TO_DEGREES = 360 / 12;
        private float k = 1;

        private int secondOffset;
        private int hoursOffset;
        private int minuteOffset;

        private Bitmap wfBitmap;
        private Bitmap secBitmap;
        private Bitmap secScaledBitmap;
        private Bitmap minBitmap;
        private Bitmap minScaledBitmap;
        private Bitmap hrBitmap;
        private Bitmap hrScaledBitmap;
        private Bitmap wfAmbientBitmap;
        private Bitmap hrAmbientBitmap;
        private Bitmap hrAmbientScaledBitmap;
        private Bitmap minAmbientBitmap;
        private Bitmap minAmbientScaledBitmap;

        private boolean registerReceiverFlag;

        private float centerX;
        private float centerY;

        private Calendar calendar;
        private Map<String, Bitmap> bundle;
        /**
         * Handled to update time each second in interactive mode.
         */
        private final Handler updateTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                super.handleMessage(message);
                switch (message.what) {
                    case MSG_UPDATE_TIME:
                        invalidate();
                        if (shouldTimerBeRunning()) {
                            long timeMs = System.currentTimeMillis();
                            long delayMs = INTERACTIVE_UPDATE_RATE_MS
                                    - (timeMs % INTERACTIVE_UPDATE_RATE_MS);
                            updateTimeHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, delayMs);
                        }
                        break;
                }
            }
        };


        /**
         * Called when time zone changes in app runtime.
         */
        final BroadcastReceiver timeZoneReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                calendar.setTimeZone(TimeZone.getDefault());
                invalidate();
            }
        };


        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);
            EventBus.getDefault().register(this);
            bundle = getBitmaps();
            initBitmaps();

            calendar = Calendar.getInstance();

            setWatchFaceStyle(new WatchFaceStyle.Builder(AbstractAmericanAnalogWatchFaceService.this)
                    .setCardPeekMode(WatchFaceStyle.PEEK_MODE_SHORT)
                    .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                    .setShowSystemUiTime(false)
                    .build());
        }

        private void initBitmaps() {
            wfBitmap = bundle.get(Constants.BG_BUNDLE_FLAG);
            minBitmap = bundle.get(Constants.MIN_TICK_BUNDLE_FLAG);
            hrBitmap = bundle.get(Constants.HR_TICK_BUNDLE_FLAG);
            secBitmap = bundle.get(Constants.SEC_TICK_BUNDLE_FLAG);

            //Init bitmaps for ambient mode.
            wfAmbientBitmap = bundle.get(Constants.BG_AMBIENT_BUNDLE_FLAG);
            minAmbientBitmap = bundle.get(Constants.MIN_TICK_AMBIENT_BUNDLE_FLAG);
            hrAmbientBitmap = bundle.get(Constants.HR_TICK_AMBIENT_BUNDLE_FLAG);
        }


        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);
        }


        /**
         * Updates screen each minute.
         */
        @Override
        public void onTimeTick() {
            super.onTimeTick();
            invalidate();
        }


        public void onEvent(WatchfaceUpdatedEvent event) {
            if (event.getResourceBitmap() != null) {
                bundle.put(event.getResourceKey(), event.getResourceBitmap());
                initBitmaps();
            } else {
                initOffset(event.getResourceKey(), event.getOffset());
            }
            invalidate();
        }

        private void initOffset(String key, int offset) {
            if (key.contains("seconds")) {
                secondOffset = offset;
            } else if (key.contains("hours")) {
                hoursOffset = offset;
            } else {
                minuteOffset = offset;
            }
        }


        /**
         * Device goes into ambient or interactive mode.
         *
         * @param inAmbientMode true if device is in ambient mode after mode change.
         */
        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);
            invalidate();
            updateTimer();
        }

        /**
         * Called within invalidate() to update (redraw) screen.
         *
         * @param canvas Main Canvas instance where the watch face in drawn.
         * @param bounds Physical bounds of the device.
         */
        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            int width = bounds.width();
            int height = bounds.height();

            calendar.setTimeInMillis(System.currentTimeMillis());
            //Scale bitmaps to expected sizes.
            wfBitmap = Bitmap.createScaledBitmap(wfBitmap, width, height, true);
            wfAmbientBitmap = Bitmap.createScaledBitmap(wfAmbientBitmap, width, height, true);
            if (height < ORIGINAL_SIZE) {
                k = (float) height / ORIGINAL_SIZE;
                secScaledBitmap = scaleBitmap(secBitmap);
                minScaledBitmap = scaleBitmap(minBitmap);
                hrScaledBitmap = scaleBitmap(hrBitmap);
                hrAmbientScaledBitmap = scaleBitmap(hrAmbientBitmap);
                minAmbientScaledBitmap = scaleBitmap(minAmbientBitmap);
            } else {
                secScaledBitmap = secBitmap;
                minScaledBitmap = minBitmap;
                hrScaledBitmap = hrBitmap;
                hrAmbientScaledBitmap = hrAmbientBitmap;
                minAmbientScaledBitmap = minAmbientBitmap;
            }
            centerX = width / 2f;
            centerY = height / 2f;
            //Compute time units to display.
            float seconds = calendar.get(Calendar.SECOND) + calendar.get(Calendar.MILLISECOND) / 1000f;
            float minutes = calendar.get(Calendar.MINUTE) + seconds / 60f;
            float hours = calendar.get(Calendar.HOUR) + minutes / 60f;
            //Calculate ticks rotation angle;
            float minutesRotation = minutes * MINUTES_TO_DEGREES + 360f;
            float hoursRotation = hours * HOURS_TO_DEGREES + 360f;
            //Is device in interactive mode?
            boolean isInteractive = !isInAmbientMode();
            Paint mFilterPaint = new Paint();
            mFilterPaint.setFilterBitmap(true);

            if (isInteractive) {
                //In interactive mode
                canvas.drawColor(Color.BLACK);
                canvas.drawBitmap(wfBitmap, 0, 0, mFilterPaint);
                drawBaseTicks(canvas, minutesRotation, hoursRotation, seconds);
            } else {
                //In ambient mode
                canvas.drawBitmap(wfAmbientBitmap, 0, 0, mFilterPaint);
                drawAmbientTicks(canvas, minutesRotation, hoursRotation);
            }
        }

        /**
         * Scales tick bitmap for given device.
         *
         * @param originalBitmap Original Bitmap instance to be source of scaling.
         * @return Scaled for given device Bitmap.
         */
        private Bitmap scaleBitmap(Bitmap originalBitmap) {
            return Bitmap.createScaledBitmap(originalBitmap, (int) (originalBitmap.getWidth() * k), (int) (originalBitmap.getHeight() * k), true);
        }

        /**
         * Called when device does into or out of interactive mode.
         *
         * @param visible true if the device is in interactive mode after mode change.
         */
        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                registerReceiver();
                calendar.setTimeZone(TimeZone.getDefault());
            } else {
                unregisterReceiver();
            }
            updateTimer();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            EventBus.getDefault().unregister(this);
        }

        /**
         * Updates watch if it's necessary more often then once a minute.
         */


        private void updateTimer() {
            updateTimeHandler.removeMessages(MSG_UPDATE_TIME);
            if (shouldTimerBeRunning()) {
                updateTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME);
            }
        }


        /**
         * @return true if device is in interactive mode.
         */
        private boolean shouldTimerBeRunning() {
            return isVisible() && !isInAmbientMode();
        }


        /**
         * Register time zone change BroadcastReceiver instance.
         */
        private void registerReceiver() {
            if (registerReceiverFlag) {
                return;
            }
            registerReceiverFlag = true;
            IntentFilter intentFilter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
            AbstractAmericanAnalogWatchFaceService.this.registerReceiver(timeZoneReceiver, intentFilter);
        }

        /**
         * Unregister time zone change BroadcastReceiver instance.
         */
        private void unregisterReceiver() {
            if (!registerReceiverFlag) {
                return;
            }
            registerReceiverFlag = false;
            AbstractAmericanAnalogWatchFaceService.this.unregisterReceiver(timeZoneReceiver);
        }

        /**
         * Draws watch ticks in interactive mode.
         *
         * @param canvas          Canvas instance to draw ticks on.
         * @param minutesRotation Angle of minute tick rotation.
         * @param hoursRotation   Angle of hour tick rotation.
         * @param seconds         Number of past seconds in current minute.
         */
        private void drawBaseTicks(Canvas canvas, float minutesRotation, float hoursRotation, float seconds) {
            Paint mFilterPaint = new Paint();
            mFilterPaint.setFilterBitmap(true);
            mFilterPaint.setAntiAlias(true);

            //Draw main ticks.
            canvas.save();
            canvas.rotate(minutesRotation, centerX, centerY);
            canvas.drawBitmap(minScaledBitmap, centerX - minScaledBitmap.getWidth() / 2f,
                    centerY - minScaledBitmap.getHeight() + minuteOffset * k, mFilterPaint);
            canvas.restore();


            canvas.save();
            canvas.rotate(hoursRotation, centerX, centerY);
            canvas.drawBitmap(hrScaledBitmap, centerX - hrScaledBitmap.getWidth() / 2f,
                    centerY - hrScaledBitmap.getHeight() + hoursOffset * k, mFilterPaint);

            canvas.restore();

            //Show second tick in interactive mode only.
            float secondsRotation = seconds * MINUTES_TO_DEGREES + 360f;
            canvas.save();
            canvas.rotate(secondsRotation, centerX, centerY);
            canvas.drawBitmap(secScaledBitmap, (centerX - secScaledBitmap.getWidth() / 2f),
                    (centerY - secScaledBitmap.getHeight()) + secondOffset * k, mFilterPaint);

            canvas.restore();
        }

        /**
         * Draws ticks in ambient mode.
         *
         * @param canvas          Canvas instance on draw ticks on.
         * @param minutesRotation Angle of minute tick rotation.
         * @param hoursRotation   Angle of hour tick rotation.
         */
        private void drawAmbientTicks(Canvas canvas, float minutesRotation, float hoursRotation) {
            Paint mFilterPaint = new Paint();
            mFilterPaint.setFilterBitmap(true);
            mFilterPaint.setAntiAlias(false);

            //Draw main ticks.
            canvas.save();
            canvas.rotate(minutesRotation, centerX, centerY);
            canvas.drawBitmap(minAmbientScaledBitmap, centerX - minAmbientScaledBitmap.getWidth() / 2f,
                    centerY - minAmbientScaledBitmap.getHeight() + 10 * k, mFilterPaint);
            canvas.restore();


            canvas.save();
            canvas.rotate(hoursRotation, centerX, centerY);
            canvas.drawBitmap(hrAmbientScaledBitmap, centerX - hrAmbientScaledBitmap.getWidth() / 2f,
                    centerY - hrAmbientScaledBitmap.getHeight() + 10 * k, mFilterPaint);

            canvas.restore();

        }
    }
}
