package com.go2smartphone.paidui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.go2smartphone.paidui.Api.ErrorHandler;
import com.go2smartphone.paidui.Api.PaiduiHttp;
import com.go2smartphone.paidui.R;
import com.go2smartphone.paidui.SanyiMainActivity;
import com.go2smartphone.paidui.model.EventRestMessage;
import com.go2smartphone.paidui.model.FetchTicketResult;
import com.go2smartphone.paidui.model.UpdateStateParam;
import com.go2smartphone.paidui.model.UpdateStateResult;
import com.go2smartphone.paidui.utils.rx.ToastUtils;
import com.sanyipos.sdk.api.SanyiSDK;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.go2smartphone.paidui.model.UpdateStateParam.State;

public class PaiduiListAdapter extends BaseAdapter {
    private List<FetchTicketResult> list = new ArrayList<FetchTicketResult>();
    private Context context;
    public final static int SETONCLICK = 1;
    private SanyiMainActivity activity = null;
    public String TAG = "PaiduiListAdapter";
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch ((Integer) msg.obj) {
                case SETONCLICK:
                    break;
            }
        }
    };

    public PaiduiListAdapter(Context context) {
        this.context = context;
        this.activity = (SanyiMainActivity) context;

    }

    public void setTickets(List<FetchTicketResult> list) {
        this.list = list;
    }

    public void addTicket(FetchTicketResult tick) {
        this.list.add(0, tick);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return null;

    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup group) {
        // TODO Auto-generated method stub+
        LayoutInflater inflater = LayoutInflater.from(context);

        FetchTicketResult result = list.get(list.size() - 1 - position);//从后面拿起
        switch (result.state) {
            case UpdateStateParam.BEGIN:
                view = inflater.inflate(R.layout.listviewitem, null);
                if (position % 2 == 0) {
                    view.setBackgroundColor(Color.parseColor("#ededed"));//item的背景灰白相间
                }
                LinearLayout LinearLayout_beused = (LinearLayout) view.findViewById(R.id.LinearLayout_beused);
                LinearLayout LinearLayout_meal = (LinearLayout) view.findViewById(R.id.LinearLayout_meal);
                final LinearLayout LinearLayout_call = (LinearLayout) view.findViewById(R.id.LinearLayout_call);
                TextView textView_phonenumber = (TextView) view.findViewById(R.id.textView_phonenumber);
                TextView textView_peplecount = (TextView) view.findViewById(R.id.textView_peplecount);
                TextView textView_number = (TextView) view.findViewById(R.id.textView_number);
                textView_peplecount.setText(result.count + "人");
                textView_number.setText(result.queueName + result.tick);
                final TextView textView_time = (TextView) view.findViewById(R.id.textView_time);
                if (result.phone.isEmpty()) {
                    textView_phonenumber.setVisibility(View.GONE);
                } else {
                    textView_phonenumber.setText(result.phone);
                }
                final long currentTime = System.currentTimeMillis();
                final long arrTime = result.createon.getTime();
                textView_time.setText(Math.round((currentTime - arrTime) / (1000 * 60)) + "分钟");

                LinearLayout_call.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                        final Dialog dialog = new Dialog(v.getContext());
//                        dialog.setContentView(R.layout.item_dialog);
//                        dialog.setTitle("提示");
//                        TextView textView_dialog_Prompt = (TextView) dialog.findViewById(R.id.textView_dialog_Prompt);
//                        textView_dialog_Prompt.setText(result.number + " 确认叫号？");
//                        Button button_dialog_cancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);
//                        Button button_dialog_OK = (Button) dialog.findViewById(R.id.button_dialog_OK);
//                        button_dialog_cancel.setOnClickListener(new OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                // TODO Auto-generated method stub
//                                dialog.dismiss();
//                            }
//                        });
//                        button_dialog_OK.setOnClickListener(new OnClickListener() {
//
//                            @Override
//                            public void onClick(View v) {
//                                // TODO Auto-generated method stub
                                activity.addSound(result.number);
//                                dialog.dismiss();
                                EventRestMessage rest = new EventRestMessage();
                                rest.setCurrentCall(result);
                                rest.setmQueueList(SanyiSDK.rest.queues);
                                activity.sendMessage(rest, context);
//                            }
//                        });
//                        dialog.setCanceledOnTouchOutside(true);
//                        dialog.show();
                    }

                });
                LinearLayout_meal.setOnClickListener(v -> {
                    // TODO Auto-generated method stub
                    final Dialog dialog = new Dialog(v.getContext());
                    dialog.setContentView(R.layout.item_dialog);
                    dialog.setTitle("提示");
                    TextView textView_dialog_Prompt = (TextView) dialog.findViewById(R.id.textView_dialog_Prompt);
                    textView_dialog_Prompt.setText(result.number + " 确认就餐？");
                    Button button_dialog_cancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);
                    Button button_dialog_OK = (Button) dialog.findViewById(R.id.button_dialog_OK);
                    button_dialog_cancel.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }
                    });
                    button_dialog_OK.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            updateState(result.detail, State.REPAST);
                            list.remove(list.size() - 1 - position);
                            dialog.dismiss();
                            notifyDataSetChanged();
                            activity.queryTicket();
                            Intent intent=new Intent();//似乎上面两条语句是更新界面，广播也是更新界面，似乎不需要？
                            intent.setAction("noti");
                            context.sendBroadcast(intent);
                        }
                    });
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.show();
                });
                LinearLayout_beused.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final Dialog dialog = new Dialog(v.getContext());
                        dialog.setContentView(R.layout.item_dialog);
                        dialog.setTitle("提示");
                        TextView textView_dialog_Prompt = (TextView) dialog.findViewById(R.id.textView_dialog_Prompt);
                        textView_dialog_Prompt.setText(result.number + " 确认过号？");
                        Button button_dialog_cancel = (Button) dialog.findViewById(R.id.button_dialog_cancel);
                        Button button_dialog_OK = (Button) dialog.findViewById(R.id.button_dialog_OK);
                        button_dialog_cancel.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });
                        button_dialog_OK.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                // TODO Auto-generated method stub
                                updateState(result.detail, State.PASS);
                                list.remove(list.size() - 1 - position);
                                notifyDataSetChanged();
                                dialog.dismiss();
                                Intent intent=new Intent();
                                intent.setAction("noti");
                                context.sendBroadcast(intent);

                            }
                        });
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.show();
                    }
                });

                return view;
            case UpdateStateParam.DELETE:
            case UpdateStateParam.PASS:
            case UpdateStateParam.REPAST:
                view = inflater.inflate(R.layout.historylistitem, null);
                if (position % 2 == 0) {
                    view.setBackgroundColor(Color.parseColor("#ededed"));
                }
                TextView textView_history_past_number = (TextView) view.findViewById(R.id.textView_history_past_number);
                TextView textView_history_past_peoplecount = (TextView) view.findViewById(R.id.textView_history_past_peoplecount);
                TextView textView_history_currentstate = (TextView) view.findViewById(R.id.textView_history_currentstate);
                switch (result.state) {
                    case UpdateStateParam.PASS:
                        textView_history_currentstate.setText("已过号");
                        break;
                    case UpdateStateParam.REPAST:
                        textView_history_currentstate.setText("已就餐");
                        break;

                }
                TextView textView_history_phonenumber = (TextView) view.findViewById(R.id.textView_history_phonenumber);
                ImageView imageView_history_callicon = (ImageView) view.findViewById(R.id.imageView_history_callicon);
                TextView textView_history_arrivetime = (TextView) view.findViewById(R.id.textView_history_arrivetime);
                textView_history_past_number.setText(result.number);
                textView_history_past_peoplecount.setText(result.count + "人");
//        spfs = context.getSharedPreferences("Paidui", Context.MODE_PRIVATE + Context.MODE_APPEND);
//        final SharedPreferences.Editor editor = spfs.edit();

                if (result.phone.isEmpty()) {
                    textView_history_phonenumber.setVisibility(View.GONE);
                    imageView_history_callicon.setVisibility(View.GONE);
                } else {

                    textView_history_phonenumber.setText(String.valueOf(result.phone));
                }
                textView_history_arrivetime.setText(getDate(String.valueOf(result.createon.getTime())));
        }
        return view;
    }

    public String getDate(String unixDate) {
        SimpleDateFormat fm = new SimpleDateFormat("HH:mm");
        return fm.format(new Date(Long.parseLong(unixDate))).toString();
    }

    public void updateState(Long detail, State state) {
        Subscription subscribe = PaiduiHttp.getInstance().getPaiduiService().updateTick(detail, new UpdateStateParam(state))
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(updateStateResult -> updateStateResult)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UpdateStateResult>() {
                    @Override
                    public void onCompleted() {
                        activity.queryTicket();
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(context, "操作失败 " + ErrorHandler.handle(e), ToastUtils.LENGTH_SHORT);
                    }

                    @Override
                    public void onNext(UpdateStateResult updateStateResult) {
                        if (updateStateResult.result) {
                            ToastUtils.show(context, "操作成功", ToastUtils.LENGTH_SHORT);
                        } else {
                            ToastUtils.show(context, "操作失败,请重试", ToastUtils.LENGTH_SHORT);
                        }
                    }

                });
    }
}