package kidslauncher.alex.com.kidslauncher.utils;

import android.os.CountDownTimer;

public class LockTimer extends CountDownTimer{

    private TimerCallbacks mTimerCallbacks;

    public LockTimer(long millisInFuture, long countDownInterval, TimerCallbacks timerCallbacks) {
        super(millisInFuture, countDownInterval);
        this.mTimerCallbacks = timerCallbacks;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        mTimerCallbacks.onTimerTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        mTimerCallbacks.onTimerFinish();
    }

    public interface TimerCallbacks {
        void onTimerTick(long millisUntilFinished);

        void onTimerFinish();
    }

}
