package ua.itstep.android11.mapsdownload;

import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/**
 * Created by Maksim Baydala on 26/01/18.
 */

public class DownloadRequestsSubscriber {

    private Subscription downloadRequestsSubscription;
    private FlowableEmitter downloadsFlowablEmitter;
    private ItemDownloadCallback itemDownloadCallback;


    public DownloadRequestsSubscriber(ItemDownloadCallback itemDownloadCallback) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" DownloadRequestsSubscriber" );
        this.itemDownloadCallback = itemDownloadCallback;

        FlowableOnSubscribe flowableOnSubscribe = new FlowableOnSubscribe() {
            @Override
            public void subscribe(FlowableEmitter e) throws Exception {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" DownloadRequestsSubscriber subscribe" );
                downloadsFlowablEmitter = e;
            }
        };

        final Flowable flowable = Flowable.create(flowableOnSubscribe, BackpressureStrategy.BUFFER);
        final Subscriber subscriber = getSubscriber();
        flowable.subscribeWith(subscriber);
         //returns Subscription  that you can then call dispose on
    }

    public void requestFile(int number) {
        downloadRequestsSubscription.request(number);
    }

    public void emitNextItem(RegionModel downloadableItem) {
        Log.d(Prefs.TAG, getClass().getSimpleName() +" emitNextItem" );
        if (!downloadsFlowablEmitter.isCancelled()) {
            downloadsFlowablEmitter.onNext(downloadableItem);

        }
    }

    private Subscriber getSubscriber() {
        return new Subscriber() {
            @Override
            public void onSubscribe(Subscription s) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Subscriber onSubscribe" );
                downloadRequestsSubscription = s;
                downloadRequestsSubscription.request(Prefs.MAX_COUNT_OF_SIMULTANEOUS_DOWNLOADS);
            }

            @Override
            public void onNext(Object o) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Subscriber onNext" );
                if (!(o instanceof RegionModel)) {
                    return;
                }
                itemDownloadCallback.onDownloadStarted((RegionModel)o);
            }

            @Override
            public void onError(Throwable t) {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Subscriber onError" );
            }

            @Override
            public void onComplete() {
                Log.d(Prefs.TAG, getClass().getSimpleName() +" Subscriber onComplete" );
                itemDownloadCallback.onDownloadCompleted();
            }
        };
    }

    public void performCleanUp() {

        if (downloadRequestsSubscription != null) {
            downloadRequestsSubscription.cancel();
            Log.d(Prefs.TAG, getClass().getSimpleName() +" performCleanUp" );
        }
    }


}
