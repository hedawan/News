package com.example.hdw.news.activity.view.builder;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.hdw.news.R;
import com.example.hdw.news.data.save.SettingData;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * Created by HDW on 2018/1/6.
 */

public class SettingViewBuilder extends ViewBuilder {
    private static final String TAG = "SettingViewBuilder";
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout mReadMode;
    private SwitchButton mNewsUpdate;
    private RelativeLayout mNewsUpdateTime;
    private RelativeLayout mNewsUrl;

    public SettingViewBuilder(Context context) {
        super(context, R.layout.setting_view);
    }

    public SettingViewBuilder(Context context, int resource) {
        super(context, resource);
    }

    @Override
    public void buildToolbar() {
        if (mToolbar == null) {
            mToolbar = findViewById(R.id.toolbar);
            ((AppCompatActivity) getContext()).setSupportActionBar(mToolbar);
        }
        mToolbar.setTitle(R.string.setting);
    }

    @Override
    public void buildDrawerLayout() {
        if (mDrawerLayout == null) {
            mDrawerLayout = ((AppCompatActivity) getContext()).findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle((AppCompatActivity) getContext(), mDrawerLayout, mToolbar, R.string.open, R.string.close);
            drawerToggle.syncState();
            mDrawerLayout.addDrawerListener(drawerToggle);
        }
    }

    @Override
    public void buildAdapter() {
        final SettingData settingData = SettingData.getInstance();
        final TextView textView = findViewById(R.id.news_update_time_text);
        final int onColor = getContext().getResources().getColor(R.color.black);
        final int offColor = getContext().getResources().getColor(R.color.offColor);
        if (mReadMode == null) {
            mReadMode = findViewById(R.id.news_read_mode);
            mReadMode.setOnClickListener(new View.OnClickListener() {
                private int mSingleChoiceItem;

                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.news_read_mode)
                            .setSingleChoiceItems(R.array.news_open_mode, settingData.getReadNewsMode(), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSingleChoiceItem = which;
                                }
                            })
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (mSingleChoiceItem) {
                                        case SettingData.WEB_VIEW:
                                            settingData.setReadNewsMode(SettingData.WEB_VIEW);
                                            break;
                                        case SettingData.SYSTEM_BROWSER:
                                            settingData.setReadNewsMode(SettingData.SYSTEM_BROWSER);
                                            break;
                                    }
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
            });
        }
        if (mNewsUpdateTime == null) {
            mNewsUpdateTime = findViewById(R.id.news_update_time);
            mNewsUpdateTime.setOnClickListener(new View.OnClickListener() {
                private int mSingleChoiceItem;

                @Override
                public void onClick(View v) {
                    if (settingData.isNewsUpdate()) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle(R.string.news_update_time)
                                .setSingleChoiceItems(R.array.select_news_update_time, timeParse((int) settingData.getNewsUpdateTime()), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mSingleChoiceItem = which;
                                    }
                                })
                                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (mSingleChoiceItem) {
                                            case 0:
                                                settingData.setNewsUpdateTime(900000);
                                                break;
                                            case 1:
                                                settingData.setNewsUpdateTime(1800000);
                                                break;
                                            case 2:
                                                settingData.setNewsUpdateTime(3600000);
                                                break;
                                            case 3:
                                                settingData.setNewsUpdateTime(10000);
                                                break;
                                        }
                                    }
                                })
                                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        builder.show();
                    }
                }

                public int timeParse(int time) {
                    int mode = 0;
                    switch (time) {
                        case 900000:
                            mode = 0;
                            break;
                        case 1800000:
                            mode = 1;
                            break;
                        case 3600000:
                            mode = 2;
                            break;
                        case 10000:
                            mode = 3;
                            break;
                    }
                    return mode;
                }
            });
        }
        if (mNewsUpdate == null) {
            mNewsUpdate = findViewById(R.id.news_update);
            if (settingData.isNewsUpdate()) {
                mNewsUpdate.setChecked(SettingData.OPEN);
                textView.setTextColor(onColor);
            } else {
                mNewsUpdate.setChecked(SettingData.CLOSE);
                textView.setTextColor(offColor);
            }
            Log.d(TAG, "buildAdapter: is checked=" + mNewsUpdate.isChecked());
            mNewsUpdate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(TAG, "onCheckedChanged: check=" + isChecked);
                    if (isChecked) {
                        settingData.setNewsUpdate(SettingData.OPEN);
                        textView.setTextColor(onColor);
                    } else {
                        settingData.setNewsUpdate(SettingData.CLOSE);
                        textView.setTextColor(offColor);
                    }
                }
            });
        }

        if (mNewsUrl == null) {
            mNewsUrl = findViewById(R.id.news_url);
            mNewsUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    final EditText editText = new EditText(getContext());
                    editText.setText(settingData.getNewsUrl());
                    builder.setTitle(R.string.news_url)
                            .setView(editText)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    settingData.setNewsUrl(editText.getText().toString());
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
            });
        }
    }
}
