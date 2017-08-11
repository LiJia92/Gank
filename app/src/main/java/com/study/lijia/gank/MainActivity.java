package com.study.lijia.gank;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.study.lijia.gank.Utils.ImageUtils;
import com.study.lijia.gank.presenter.IGankPresenter;
import com.study.lijia.gank.presenter.impl.GankPresenter;
import com.study.lijia.gank.view.IGankView;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IGankView {

    @BindView(R.id.content_text_view)
    TextView mContent;

    @BindView(R.id.show_image)
    ImageView mImageView;

    private IGankPresenter mGankPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mGankPresenter = new GankPresenter(this);
        mGankPresenter.getDaily("2017", "08", "03");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDaily(String daily) {
        mContent.setText(daily);
        hello("John, Ben, Jack");
        showBitmap();
    }

    public static void hello(String... names) {
//        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Hi");
//                subscriber.onNext("Aloha");
//                subscriber.onCompleted();
//            }
//        });

//        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                subscriber.onNext("Hello");
//                subscriber.onNext("Hi");
//                subscriber.onNext("Aloha");
//                subscriber.onCompleted();
//            }
//        });
        Observable<String> observable = Observable.just("Hello", "Hi", "Aloha");
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("TAG", "===" + s + "===");
            }
        };
        Subscriber<String> subscriber1 = new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("TAG", "+++" + s + "+++");
            }
        };
        observable.subscribe(subscriber);
        observable.subscribe(subscriber1);
        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.d("TAG", s);
            }
        };
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {

            }
        };
        Action0 onCompletedAction = new Action0() {
            @Override
            public void call() {
                Log.d("TAG", "completed");
            }
        };

        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);


        subscriber.unsubscribe();
        observable.subscribe(subscriber1);
    }

    private void showBitmap() {
        final int drawableRes = R.mipmap.ic_launcher_round;
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Drawable drawable = getResources().getDrawable(drawableRes);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .map(new Func1<Drawable, Drawable>() {
                    @Override
                    public Drawable call(Drawable drawable) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        Bitmap blurBitmap = ImageUtils.fastBlur(MainActivity.this, bitmap, 20);
                        return new BitmapDrawable(blurBitmap);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onNext(Drawable drawable) {
                        mImageView.setImageDrawable(drawable);
                    }

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
