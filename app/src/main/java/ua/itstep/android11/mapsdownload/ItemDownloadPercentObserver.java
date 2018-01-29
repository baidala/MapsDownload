package ua.itstep.android11.mapsdownload;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class ItemDownloadPercentObserver {

    private ObservableEmitter percentageObservableEmitter;
    private Disposable downloadPercentDisposable;
    private final ItemPercentCallback callback;

    public ItemDownloadPercentObserver(ItemPercentCallback callback) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" ItemDownloadPercentObserver" );

        this.callback=callback;
        ObservableOnSubscribe observableOnSubscribe = new ObservableOnSubscribe() {
            @Override
            public void subscribe(ObservableEmitter e) throws Exception {
                percentageObservableEmitter = e;
            }
        };

        final Observable observable = Observable.create(observableOnSubscribe);

        final Observer subscriber = getObserver();
        observable.subscribeWith(subscriber);
    }

    public ObservableEmitter getPercentageObservableEmitter() {
        return percentageObservableEmitter;
    }

    private Observer getObserver() {
        return new Observer() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Observer onSubscribe" );
                downloadPercentDisposable = d;
            }

            @Override
            public void onNext(Object value) {
                if (!(value instanceof RegionModel)) {
                    return;
                }
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Observer onNext" );
                callback.updateDownloadableItem((RegionModel) value);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Observer onError" );
                if (downloadPercentDisposable != null) {
                    downloadPercentDisposable.dispose();
                }
            }

            @Override
            public void onComplete() {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Observer onComplete" );
                if (downloadPercentDisposable != null) {
                    downloadPercentDisposable.dispose();
                }
            }
        };
    }

    public void performCleanUp() {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" Observer performCleanUp" );
        if (downloadPercentDisposable != null) {
            downloadPercentDisposable.dispose();
        }
    }

}
