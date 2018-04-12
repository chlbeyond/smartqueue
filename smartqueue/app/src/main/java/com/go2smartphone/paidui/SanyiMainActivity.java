package com.go2smartphone.paidui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.hardware.usb.UsbManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.go2smartphone.paidui.Api.ErrorHandler;
import com.go2smartphone.paidui.Api.PaiduiHttp;
import com.go2smartphone.paidui.fragment.EmptyTableListAdapter;
import com.go2smartphone.paidui.fragment.FragmentHistory;
import com.go2smartphone.paidui.fragment.FragmentTable;
import com.go2smartphone.paidui.fragment.PaiduiListAdapter;
import com.go2smartphone.paidui.model.EventRestMessage;
import com.go2smartphone.paidui.model.FetchTicketParam;
import com.go2smartphone.paidui.model.FetchTicketResult;
import com.go2smartphone.paidui.model.Restaurant;
import com.go2smartphone.paidui.model.ShopTable;
import com.go2smartphone.paidui.model.Ticket;
import com.go2smartphone.paidui.model.UpdateStateParam;
import com.go2smartphone.paidui.utils.rx.DeviceUtils;
import com.go2smartphone.pritln.SanyiUSBDriver;
import com.go2smartphone.pritln.UsbPrinter;
import com.go2smartphone.sdk.checkServices.DownloadApkDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.annotations.SerializedName;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;
import com.sanyipos.sdk.api.SanyiSDK;
import com.sanyipos.sdk.api.inters.IPollingListener;
import com.sanyipos.sdk.model.OperationData;
import com.sanyipos.sdk.model.SeatEntity;
import com.sanyipos.sdk.model.rest.KdsOrders;
import com.sanyipos.sdk.model.rest.Rest;
import com.sanyipos.sdk.utils.JsonUtil;
import com.smartpos.login.LoginActivity;

import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class SanyiMainActivity extends FragmentActivity implements MyReceiver.MyMessage {
    private static final String TAG = "SanyiMainActivity";
    private TextView textView_edit_peoplecount;
    private LinearLayout LinearLayout_editphonenumber;
    private LinearLayout LinearLayout_editmealpeoplecount;
    private LinearLayout LinearLayout_editquerynumber;
    private int edit = 0;//标记区别 就餐人数0、手机号码1、查询号码2
    private String editString = "";
    private TextView textView_edit_phonenumber;
    private TextView textView_main_fetchnumber;
    private TextView textView_main_query_number;
    private TextView textView_main_logout;
    private AdvancedPagerSlidingTabStrip linearLayoutCookingMethod;
    private List<Fragment> mFragments;
    private TextView textViewFreeTable;
    private FragmentManager mFragmentManager;
    private static BlockingQueue<String> broadcastTexts = new LinkedBlockingQueue<String>();
    private Timer timer;
    private Ticket ticket;
    private FragmentHistory historyFragment;
    private MyReceiver myReceiver;
    private PagerAdapter myPagerAdapter;

    private ViewPager mViewPager;
    Thread playSouncTherad = new Thread(new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    String text = broadcastTexts.take();
                    for (int i = 0; i < 2; i++) {
                        switch (text.substring(0, 1).hashCode()) {
                            case 65:
                                createMediaPlayForSound(R.raw.a).start();
                                break;
                            case 66:
                                createMediaPlayForSound(R.raw.b).start();
                                break;
                            case 67:
                                createMediaPlayForSound(R.raw.c).start();
                                break;
                            case 68:
                                createMediaPlayForSound(R.raw.d).start();
                                break;
                            case 69:
                                createMediaPlayForSound(R.raw.e).start();
                                break;
                            case 70:
                                createMediaPlayForSound(R.raw.f).start();
                                break;
                            case 71:
                                createMediaPlayForSound(R.raw.g).start();
                                break;
                            case 72:
                                createMediaPlayForSound(R.raw.h).start();
                                break;
                            case 73:
                                createMediaPlayForSound(R.raw.i).start();
                                break;
                            default:
                                break;
                        }
                        Thread.sleep(300);
                        for (char b : text.substring(1, text.length()).toCharArray()) {
                            switch (b) {
                                case 48:
                                    createMediaPlayForSound(R.raw.n0).start();
                                    break;
                                case 49:
                                    createMediaPlayForSound(R.raw.n1).start();
                                    break;
                                case 50:
                                    createMediaPlayForSound(R.raw.n2).start();
                                    break;
                                case 51:
                                    createMediaPlayForSound(R.raw.n3).start();
                                    break;
                                case 52:
                                    createMediaPlayForSound(R.raw.n4).start();
                                    break;
                                case 53:
                                    createMediaPlayForSound(R.raw.n5).start();
                                    break;
                                case 54:
                                    createMediaPlayForSound(R.raw.n6).start();
                                    break;
                                case 55:
                                    createMediaPlayForSound(R.raw.n7).start();
                                    break;
                                case 56:
                                    createMediaPlayForSound(R.raw.n8).start();
                                    break;
                                case 57:
                                    createMediaPlayForSound(R.raw.n9).start();
                                    break;
                            }
                            Thread.sleep(300);
                        }
                        createMediaPlayForSound(R.raw.end).start();
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    });
    private Dialog emptydialog;
    private GridView emptyGridView;
    private EmptyTableListAdapter mEmptyAdapter;
    private Context mContext;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (mViewPager.getCurrentItem() < mFragments.size() - 1) {
                ((FragmentTable) mFragments.get(mViewPager.getCurrentItem())).refresh();
            } else {
                ((FragmentHistory) mFragments.get(mViewPager.getCurrentItem())).refresh();
            }
            return false;
        }
    });
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        //注册广播,就餐或者过号以后,刷新过号历史
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("noti");
        registerReceiver(myReceiver, intentFilter);
        myReceiver.setMsg(this);

        playSouncTherad.start();
        textView_main_fetchnumber = (TextView) findViewById(R.id.textView_main_fetchnumber);
        textView_edit_peoplecount = (TextView) findViewById(R.id.textView_edit_peoplecount);
        textView_edit_phonenumber = (TextView) findViewById(R.id.textView_edit_phonenumber);
        textView_main_query_number = (TextView) findViewById(R.id.textView_main_query_number);
        textView_main_logout = (TextView) findViewById(R.id.tv_main_logout);
        textViewFreeTable = (TextView) findViewById(R.id.textView_main_free_number);
        TextView textView_main_title = (TextView) findViewById(R.id.textView_main_title);
        if (SanyiSDK.rest.operationData.shop.name != null)
            textView_main_title.setText(SanyiSDK.rest.operationData.shop.name);
        else
            textView_main_title.setText("获取数据失败,请重新登录");
        textView_main_title.setOnClickListener(v -> {
                    EventRestMessage rest = new EventRestMessage();
                    rest.setShop(SanyiSDK.rest.operationData.shop.name);
                    rest.setmQueueList(SanyiSDK.rest.queues);
                    SanyiMainActivity.sendMessage(rest, this);
                }
        );
        LinearLayout_editphonenumber = (LinearLayout) findViewById(R.id.LinearLayout_edit_phonenumber);
        LinearLayout_editmealpeoplecount = (LinearLayout) findViewById(R.id.LinearLayout_editmealpeoplecount);
        LinearLayout_editquerynumber = (LinearLayout) findViewById(R.id.LinearLayout_editquerynumber);

        initTab();
//        initPrint();
        initPollListener();
        queryTicket();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private void initPollListener() {
        //轮询后台,得知餐厅数据更新后,刷新界面
        SanyiSDK.getSDK().installPollingListener(getClass().getName(), new IPollingListener() {
            @Override
            public void OperationDataUpdate() {
                Log.e("````", "OperationDataUpdate()");
            }

            @Override
            public void RestaurantDataUpdate() {
                Subscription subscription = PaiduiHttp.getInstance().getPaiduiService().downLoadRest()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io())
                        .map(rest -> rest)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<Rest>() {
                            @Override
                            public void onCompleted() {

                                Log.d(TAG, "onCompleted");
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(mContext, "餐厅数据更新失败" + ErrorHandler.handle(e), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(Rest r) {
                                SanyiSDK.rest = r;
                                Toast.makeText(mContext, "餐厅数据已更新", Toast.LENGTH_SHORT).show();
                                new AlertDialog.Builder(SanyiMainActivity.this)
                                        .setTitle("更新")
                                        .setMessage("餐厅数据已更新,请点击确定重新登录")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Restaurant.STAFF_ID = 0;
                                                Intent loginintent = new Intent(SanyiMainActivity.this, LoginActivity.class);
                                                loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(loginintent);
                                                SanyiMainActivity.this.finish();
                                            }
                                        }).create().show();
//                                initTab();
//                                queryTicket();
                            }
                        });
            }

            @Override
            public void RestaurantDataUpdateComplete() {
                Log.e("````", "RestaurantDataUpdateComplete()");
            }

            @Override
            public void SoldOutUpdate(OperationData.Notification notification) {
                Log.e("````", "SoldOutUpdate()");
            }

            @Override
            public void ClearSoldOutUpdate(OperationData.Notification notification) {
                Log.e("````", "ClearSoldOutUpdate()");
            }

            @Override
            public void ForceUnlockTable(OperationData.Notification notification) {
                Log.e("````", "ForceUnlockTable()");
            }

            @Override
            public void VersionUpdate(OperationData.Notification notification) {
                Log.e("````", "VersionUpdate()");
                // 不退出直接下载
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 发现新版本
//                        Map<String, String> map = new HashMap<String, String>();
//                        map.put("name", " 食通宝");
//                        map.put("version", "1");
//                        map.put("currentVersionName", DeviceUtils.getVersionName(mContext));
//                        map.put("currentVersionCode", DeviceUtils.getVersionCode(mContext) + "");
//                        map.put("url", notification.extraInfo);
//                        new DownloadApkDialog(mContext, map, Integer.toString(SanyiSDK.registerData.getDeviceId()), new DownloadApkDialog.IDownloadListener() {
//
//                            @Override
//                            public void onFail() {
//
//                            }
//
//                            @Override
//                            public void onCompleted(String apkPath) {
//                                installAPK(apkPath);
//                            }
//
//                            @Override
//                            public void onCancel() {
//
//                            }
//                        }).show();
//                    }
//                });
                //退出到登录页面进行下载安装
                new AlertDialog.Builder(SanyiMainActivity.this)
                        .setTitle("更新")
                        .setMessage("检测到有新版本,请点击确定开始下载")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Restaurant.STAFF_ID = 0;
                                Intent loginintent = new Intent(SanyiMainActivity.this, LoginActivity.class);
                                loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(loginintent);
                                SanyiMainActivity.this.finish();
                            }
                        }).create().show();

            }

            @Override
            public void ShopClosed(OperationData.Notification notification) {
                Log.e("````", "ShopClosed()");
            }

            @Override
            public void ShopOpen(OperationData.Notification notification) {
                Log.e("````", "ShopOpen()");
            }

            @Override
            public void ShopMaintain(OperationData.Notification notification) {
                Log.e("````", "ShopMaintain()");
            }

            @Override
            public void PasswordChanged(OperationData.Notification notification) {
                Log.e("````", "PasswordChanged()");
            }

            @Override
            public void WeChatPaySuccessed(OperationData.Notification notification) {
                Log.e("````", "WeChatPaySuccessed()");
            }

            @Override
            public void UpdateSoldoutDish() {
                Log.e("````", "UpdateSoldoutDish()");
            }

            @Override
            public void net_normal() {
                Log.e("````", "net_normal()");
            }

            @Override
            public void QueueDataUpdate() {
                Log.e("````", "QueueDataUpdate()");
                queryTicket();
            }

            @Override
            public void kdsOrderUpdate(KdsOrders kdsOrders) {

            }


            @Override
            public void onFail(String s) {
                Log.e("````", "onFail()");
            }
        });
    }

    public void queryTicket() {//网络请求所有队伍数据Ticket
        Subscription subscribe = PaiduiHttp.getInstance().getPaiduiService().queryTick()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Ticket, Ticket>() {
                    @Override
                    public Ticket call(Ticket ticket) {
                        return ticket;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Ticket>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        ToastUtils.show(SanyiMainActivity.this, "取号失败" + ErrorHandler.handle(e), Toast.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(Ticket ticket) {
//                        ToastUtils.show(SanyiMainActivity.this, "取号成功", ToastUtils.LENGTH_SHORT);
                        parseTicket(ticket);
                    }
                });
    }

    public void parseTicket(Ticket ticket) {
        this.ticket = ticket;//将队伍数据保存在全局，查询时需要
        List<FetchTicketResult> passLists = new ArrayList<>();//历史队伍数组
        for (int i = 0; i < SanyiSDK.rest.queues.size(); i++) { //SanyiSDK.rest.queues.size()这个的值不包含历史队伍
            List<FetchTicketResult> mList = new ArrayList<>();
            for (FetchTicketResult tick : ticket.details) {
                if (tick.queue == SanyiSDK.rest.queues.get(i).id) {//筛选出一个队伍的数据
                    if (tick.state == UpdateStateParam.BEGIN) {
                        mList.add(tick);
                    } else {
                        passLists.add(tick);  //历史队伍的值是由其他队伍数据生成的
                    }
                }
            }
            linearLayoutCookingMethod.showDot(i, Integer.toString(mList.size()));//设置并显示队伍小红点显示的数据
//            ((FragmentTable) mFragments.get(i)).init();
            ((FragmentTable) mFragments.get(i)).setTickets(mList);//更新队伍数据
        }
        ((FragmentHistory) mFragments.get(mFragments.size() - 1)).setTickets(passLists);//更新历史队伍数据

        if (mViewPager.getCurrentItem() < mFragments.size() - 1) {//更新界面
            ((FragmentTable) mFragments.get(mViewPager.getCurrentItem())).refresh();
        } else {
            ((FragmentHistory) mFragments.get(mViewPager.getCurrentItem())).refresh();
        }
        int emptyTable = 0;
        for (SeatEntity seat : SanyiSDK.rest.operationData.shopTables) {//空台数
            if (seat.state == 1) {
                emptyTable++;
            }
        }
        textViewFreeTable.setText(Integer.toString(emptyTable));
    }

    //如果不输入数值就查询，那么查到的就是没有输入电话的
    public List<FetchTicketResult> findTick(String find) {//查询号码
        List<FetchTicketResult> mList = new ArrayList<>();
        for (FetchTicketResult tick : ticket.details) {
            if (String.valueOf(tick.tick).equals(find) || tick.phone.equals(find)) {//此时tick.phone与find可能都是空的，所以也为true
                mList.add(tick);
            }
        }
        return mList;
    }


    public void addSound(String t) {//叫号
        try {
            broadcastTexts.put(t);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void initTab() {
        // TODO Auto-generated method stub6

        linearLayoutCookingMethod = (AdvancedPagerSlidingTabStrip) findViewById(R.id.linearLayoutCookingMethod);
        mViewPager = (ViewPager) findViewById(R.id.viewPager_main_activity);
        if (mFragments != null)
            mFragments.clear();
        else
            mFragments = new ArrayList<>();
        for (int i = 0; i < SanyiSDK.rest.queues.size(); i++) {
            FragmentTable fragmentTable = new FragmentTable();
            fragmentTable.setQueueId(i);
            mFragments.add(fragmentTable);
        }
        historyFragment = new FragmentHistory();
        mFragments.add(historyFragment);
        myPagerAdapter = new PagerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(myPagerAdapter);
        mViewPager.setOffscreenPageLimit(SanyiSDK.rest.queues.size()); //SanyiSDK.rest.queues.size()这个的值不包含历史队伍
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //每次滑动viewpager都要请求一次数据
            @Override
            public void onPageSelected(int position) {
                queryTicket();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        linearLayoutCookingMethod.setViewPager(mViewPager);
        linearLayoutCookingMethod.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position < mFragments.size() - 1) {
//                    ((FragmentTable) mFragments.get(position)).refresh();
//                } else {
//                    ((FragmentHistory) mFragments.get(position)).refresh();
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void getMsg(String msg) {//广播后回调
        if (historyFragment != null)
            historyFragment.refresh();
    }


    public class PagerAdapter extends FragmentPagerAdapter implements AdvancedPagerSlidingTabStrip.TipsProvider {

        private List<Fragment> mFragmentList;

        public PagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mFragmentList = list;
        }

        @Override
        public Fragment getItem(int position) {
            if (mFragmentList != null) {
                return mFragmentList.get(position);
            }
            return null;
        }

        @Override
        public int getCount() {
            if (mFragmentList != null) {
                return mFragmentList.size();
            }
            return 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < SanyiSDK.rest.queues.size()) {
                return SanyiSDK.rest.queues.get(position).minSize + "-" + SanyiSDK.rest.queues.get(position).maxSize + "位";
            } else {
                return "历史";
            }
        }

        //该方法用于设置小圆点的相对位置
        @Override
        public int[] getTipsRule(int position) {
            return new int[0];
        }

        //该方法用于设置小圆点在 tab 中的间距大小
        @Override
        public Margins getTipsMargins(int position) {
            return new Margins(10, 15, 15, 15);
        }

        //该方法用于设置小圆点的背景，默认为红色圆角图
        @Override
        public Drawable getTipsDrawable(int position) {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SanyiMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.go2smartphone.paidui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "SanyiMain Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.go2smartphone.paidui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    public static class ShopTables {

        @SerializedName("shop_table")
        public List<ShopTable> shopTable;
    }

    @SuppressWarnings("unchecked")
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.LinearLayout_freeTable:
                if (emptydialog == null) {
                    emptydialog = new Dialog(view.getContext());
                    emptydialog.setContentView(R.layout.popupwindow);
                    emptydialog.setTitle("空台信息");
                    emptyGridView = (GridView) emptydialog.findViewById(R.id.gridView_empty_table);
                    mEmptyAdapter = new EmptyTableListAdapter(SanyiSDK.rest.operationData.shopTables, this);
                    emptyGridView.setAdapter(mEmptyAdapter);
                } else {
                    mEmptyAdapter.setSeatEntitys(SanyiSDK.rest.operationData.shopTables);
                }
                emptydialog.show();
                // mPopupWindow = new PopupWindow(popupWindow, 1500, 500, true);
                // mPopupWindow.setTouchable(true);
                // mPopupWindow.setOutsideTouchable(true);
                // ColorDrawable dw = new ColorDrawable(0000000000);
                // // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
                // mPopupWindow.setBackgroundDrawable(dw);
                // // mPopupWindow.setFocusable(false);
                // // mPopupWindow.showAsDropDown(view, 20, 0);
                // mPopupWindow.showAtLocation(view, Gravity.CENTER, 50, 100);
                // mPopupWindow.setTouchInterceptor(new OnTouchListener() {
                //
                // @Override
                // public boolean onTouch(View arg0, MotionEvent arg1) {
                // // TODO Auto-generated method stub
                // if (arg1.getAction() == MotionEvent.ACTION_OUTSIDE) {
                // mPopupWindow.dismiss();
                // return true;
                // }
                // return false;
                // }
                // });
                break;
            case R.id.s_button1:
                appendNumber("1");
                break;
            case R.id.s_button2:
                appendNumber("2");
                break;
            case R.id.s_button3:
                appendNumber("3");
                break;
            case R.id.s_button4:
                appendNumber("4");
                break;
            case R.id.s_button5:
                appendNumber("5");
                break;
            case R.id.s_button6:
                appendNumber("6");
                break;
            case R.id.s_button7:
                appendNumber("7");
                break;
            case R.id.s_button8:
                appendNumber("8");
                break;
            case R.id.s_button9:
                appendNumber("9");
                break;
            case R.id.s_button0:
                appendNumber("0");
                break;
            case R.id.button_delete:
                editString = "";
                setText();
                break;
            case R.id.button_back:
                if (!editString.isEmpty()) {
                    editString = editString.substring(0, editString.length() - 1);
                    setText();
                }
                break;
            case R.id.LinearLayout_editmealpeoplecount:
                LinearLayout_editmealpeoplecount.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                LinearLayout_editphonenumber.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                LinearLayout_editquerynumber.setBackground(getResources().getDrawable(R.drawable.text_editnumber_selector_search));
                edit = 0;
                editString = textView_edit_peoplecount.getText().toString();
                textView_main_fetchnumber.setText("取  号");
                textView_main_query_number.setText("");
                break;
            case R.id.LinearLayout_edit_phonenumber:
                LinearLayout_editmealpeoplecount.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                LinearLayout_editphonenumber.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                LinearLayout_editquerynumber.setBackground(getResources().getDrawable(R.drawable.text_editnumber_selector_search));
                edit = 1;
                editString = textView_edit_phonenumber.getText().toString();
                textView_main_fetchnumber.setText("取  号");
                textView_main_query_number.setText("");
                break;
            case R.id.LinearLayout_editquerynumber:
                LinearLayout_editmealpeoplecount.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                LinearLayout_editphonenumber.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                LinearLayout_editquerynumber.setBackground(getResources().getDrawable(R.drawable.text_editnumber_noselector_search));
                edit = 2;
                textView_main_fetchnumber.setText("查  询");
                textView_edit_peoplecount.setText("");
                textView_edit_phonenumber.setText("");
                textView_main_query_number.setText("");
                editString = textView_main_query_number.getText().toString();
                break;
            case R.id.textView_main_fetchnumber:
                if (edit != 2) {//取号
                    if (!textView_edit_peoplecount.getText().toString().isEmpty()) {
                        int count = Integer.valueOf(textView_edit_peoplecount.getText().toString());
                        FetchTicketParam param = new FetchTicketParam();
                        param.setCount(count);
                        param.setPhone(textView_edit_phonenumber.getText().toString());
                        PaiduiHttp.getInstance().getPaiduiService().fetchTick(param)
                                .subscribeOn(Schedulers.io())
                                .observeOn(Schedulers.io())
                                .map(fetchTicketResult -> {
                                    return fetchTicketResult;
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<FetchTicketResult>() {
                                    @Override
                                    public void onCompleted() {

                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        Toast.makeText(SanyiMainActivity.this, "取号失败" + ErrorHandler.handle(e), Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onNext(FetchTicketResult fetchTicketResult) {
                                        switchFragment(fetchTicketResult);
                                        queryTicket();
                                        Toast.makeText(SanyiMainActivity.this, "取号成功", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }

                    textView_edit_peoplecount.setText("");
                    textView_edit_phonenumber.setText("");
                    editString = "";
                    edit = 0;
                    LinearLayout_editmealpeoplecount.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_noselector));
                    LinearLayout_editphonenumber.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_editnumber_selector));
                    textView_main_fetchnumber.setEnabled(false);
                } else {//查询号码
                    List<FetchTicketResult> ticketResults = findTick(textView_main_query_number.getText().toString());
                    if (ticketResults.size() == 0) {
                        Toast.makeText(this, "找不到该号码", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Dialog dialog = new Dialog(this);
                    View searchView = LayoutInflater.from(this).inflate(R.layout.printdialog, null);
                    ListView listView = (ListView) searchView.findViewById(R.id.listView_content_dialog);
                    PaiduiListAdapter adapter = new PaiduiListAdapter(this);
                    adapter.setTickets(ticketResults);
                    listView.setAdapter(adapter);
                    dialog.setContentView(searchView);
                    dialog.show();
                }
                break;
            case R.id.LinearLayout_setting:

                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_main_logout:
                //注销
                Restaurant.STAFF_ID = 0;
                Intent loginintent = new Intent(SanyiMainActivity.this, LoginActivity.class);
                loginintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(loginintent);
                SanyiMainActivity.this.finish();
                break;
            default:
                break;
        }
    }

    public void switchFragment(FetchTicketResult ticketResult) {
        for (int i = 0; i < SanyiSDK.rest.queues.size(); i++) {
            if (SanyiSDK.rest.queues.get(i).id == ticketResult.queue) {
                mViewPager.setCurrentItem(i);
                ((FragmentTable) mFragments.get(i)).addTicket(ticketResult);
                ((FragmentTable) mFragments.get(i)).refresh();

            }
        }

    }

    public void appendNumber(String inNumb) {
        if (edit == 0) {//0 表示输入就餐人数
            if (editString.length() < 2) { //就餐人数不能超过两位数
                editString = editString + inNumb;
                setText();
            }
        } else {//输入手机号或查询号
            editString = editString + inNumb;
            setText();
        }

    }

    public static void sendMessage(Object o, Context context) {//不清楚这个方法做什么
        new Thread(() -> {
            DatagramSocket clientSocket;
            try {
                clientSocket = new DatagramSocket();
                clientSocket.setBroadcast(true);
                String data = JsonUtil.toJson(o);
                try {
                    DatagramPacket packet = new DatagramPacket(data.getBytes(), data.length(), Restaurant.getBroadcastAddress(context),
                            2562);
                    Log.d(TAG, "sendMessage: " + packet.getLength());
                    clientSocket.send(packet);
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                }

                clientSocket.close();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        }).start();
    }

    public void setText() {
        if (edit == 0) {//就餐人数
            if (editString.length() < 3) {
                textView_edit_peoplecount.setText(editString);

                if (editString.length() != 0
                        && Integer.valueOf(editString) != 0
                        && Integer.valueOf(editString) <= SanyiSDK.rest.queues.get(SanyiSDK.rest.queues.size() - 1).maxSize) {
                    textView_main_fetchnumber.setEnabled(true);//输入的就餐人数符合时才能取号
                    if (isPhoneNumber(textView_edit_phonenumber.getText().toString()) && !textView_edit_peoplecount.getText().toString().isEmpty()) {
                        textView_main_fetchnumber.setEnabled(true);
                    }
                } else
                    textView_main_fetchnumber.setEnabled(false);
            }
        } else if (edit == 1) {
            textView_edit_phonenumber.setText(editString);
            if (isPhoneNumber(textView_edit_phonenumber.getText().toString()) && !textView_edit_peoplecount.getText().toString().isEmpty()) {
                textView_main_fetchnumber.setEnabled(true);
            } else {
                textView_main_fetchnumber.setEnabled(false);
            }
        } else if (edit == 2) {
            textView_main_query_number.setText(editString);
            if (!textView_main_query_number.getText().toString().isEmpty()) {
                textView_main_fetchnumber.setEnabled(true);
            }
        }
    }

    public static boolean isPhoneNumber(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }


    public void initPrint() {
        UsbManager usbManager = (UsbManager) this.getSystemService(Context.USB_SERVICE);
        Restaurant.usbDriver = new SanyiUSBDriver(usbManager, this, PendingIntent.getBroadcast(this, 0, new Intent(Restaurant.ACTION_USB_PERMISSION),
                0));
        final List<UsbPrinter> allPrinters = Restaurant.usbDriver.getAllPrinters();
        if (allPrinters.size() > 0) {
            List<String> printStringList = new ArrayList<String>();
            for (final UsbPrinter printer : allPrinters) {
                printStringList.add(printer.getDescription());
            }
            final Dialog printDialog = new Dialog(this);
            printDialog.setTitle("请选择打印机");
            printDialog.setContentView(R.layout.printdialog);
            ListView listView_content_dialog = (ListView) printDialog.findViewById(R.id.listView_content_dialog);
            listView_content_dialog.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, printStringList));
            listView_content_dialog.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    Restaurant.usbDriver.setUsingPrinter(allPrinters.get(position));
                    printDialog.dismiss();
                }

            });
            printDialog.show();
        } else {
            Toast.makeText(this, "找不到打印机", Toast.LENGTH_SHORT).show();
        }
    }


    public MediaPlayer createMediaPlayForSound(int resourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(SanyiMainActivity.this, resourceId);
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        return mediaPlayer;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

//        queryTicket();
//        timer = new Timer();
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                handler.sendEmptyMessage(1);
//            }
//        }, 0, 2000);
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
//        timer.cancel();
    }

    public void installAPK(String apkpath) {
        File apkfile = new File(apkpath);
        if (!apkfile.exists()) {
            return;
        }
        // 通过Intent安装APK文件
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}