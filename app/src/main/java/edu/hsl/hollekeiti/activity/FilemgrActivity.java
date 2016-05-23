package edu.hsl.hollekeiti.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.phone.db.FileManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;
import edu.hsl.hollekeiti.phone.util.FileTypeUtil;

public class FilemgrActivity extends MyActivity {
    private Thread      thread;
    private FileManager manager;
    private TextView    tv_size;
    private View        view_all;
    private View        view_txt;
    private View        view_video;
    private View        view_audio;
    private View        view_image;
    private View        view_zip;
    private View        view_apk;
    private TextView    tv_size_all;
    private TextView    tv_size_txt;
    private TextView    tv_size_video;
    private TextView    tv_size_audio;
    private TextView    tv_size_image;
    private TextView    tv_size_zip;
    private TextView    tv_size_apk;
    private ProgressBar pb_loading_all;
    private ProgressBar pb_loading_txt;
    private ProgressBar pb_loading_video;
    private ProgressBar pb_loading_audio;
    private ProgressBar pb_loading_image;
    private ProgressBar pb_loading_zip;
    private ProgressBar pb_loading_apk;
    private ImageView   iv_righticon_all;
    private ImageView   iv_righticon_txt;
    private ImageView   iv_righticon_video;
    private ImageView   iv_righticon_audio;
    private ImageView   iv_righticon_image;
    private ImageView   iv_righticon_zip;
    private ImageView   iv_righticon_apk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemgr);
        initActionBar(getResources().getString(R.string.filemgr),
                R.mipmap.btn_homeasup_default, -1, getListener());
        initMainUI();
        asynLoadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.setStopSearch(true);
        thread.interrupt();
        thread = null;
    }

    @Override
    protected void myHandleMessage(Message m) {
        if (m.what == 1) {
            tv_size.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            tv_size_all.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            String typeName = m.obj.toString();
            if (typeName.equals(FileTypeUtil.TYPE_APK)) {
                tv_size_apk.setText(CommonUtil.getFileSize(manager.getApkFileSize()));
            } else if (typeName.equals(FileTypeUtil.TYPE_ZIP)) {
                tv_size_zip.setText(CommonUtil.getFileSize(manager.getZipFileSize()));
            } else if (typeName.equals(FileTypeUtil.TYPE_IMAGE)) {
                tv_size_image.setText(CommonUtil.getFileSize(manager.getImageFileSize()));
            } else if (typeName.equals(FileTypeUtil.TYPE_VIDEO)) {
                tv_size_video.setText(CommonUtil.getFileSize(manager.getVideoFileSize()));
            } else if (typeName.equals(FileTypeUtil.TYPE_TXT)) {
                tv_size_txt.setText(CommonUtil.getFileSize(manager.getTxtFileSize()));
            } else if (typeName.equals(FileTypeUtil.TYPE_AUDIO)) {
                tv_size_audio.setText(CommonUtil.getFileSize(manager.getAudioFileSize()));
            }
        }
        if (m.what == 2) {
            tv_size.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            tv_size_all.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            tv_size_audio.setText(CommonUtil.getFileSize(manager.getAudioFileSize()));
            tv_size_txt.setText(CommonUtil.getFileSize(manager.getTxtFileSize()));
            tv_size_video.setText(CommonUtil.getFileSize(manager.getVideoFileSize()));
            tv_size_image.setText(CommonUtil.getFileSize(manager.getImageFileSize()));
            tv_size_zip.setText(CommonUtil.getFileSize(manager.getZipFileSize()));
            tv_size_apk.setText(CommonUtil.getFileSize(manager.getApkFileSize()));
            pb_loading_all.setVisibility(View.GONE);
            pb_loading_audio.setVisibility(View.GONE);
            pb_loading_apk.setVisibility(View.GONE);
            pb_loading_video.setVisibility(View.GONE);
            pb_loading_image.setVisibility(View.GONE);
            pb_loading_zip.setVisibility(View.GONE);
            pb_loading_txt.setVisibility(View.GONE);
            iv_righticon_all.setVisibility(View.VISIBLE);
            iv_righticon_audio.setVisibility(View.VISIBLE);
            iv_righticon_apk.setVisibility(View.VISIBLE);
            iv_righticon_video.setVisibility(View.VISIBLE);
            iv_righticon_image.setVisibility(View.VISIBLE);
            iv_righticon_zip.setVisibility(View.VISIBLE);
            iv_righticon_txt.setVisibility(View.VISIBLE);
            view_all.setOnClickListener(getListener());
            view_audio.setOnClickListener(getListener());
            view_apk.setOnClickListener(getListener());
            view_image.setOnClickListener(getListener());
            view_zip.setOnClickListener(getListener());
            view_txt.setOnClickListener(getListener());
            view_video.setOnClickListener(getListener());
        }
    }

    private void asynLoadData() {
        manager = FileManager.getFileManager();
        manager.setListener(getSearchFileListener());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                manager.searchSDCardFile();
            }
        });
        thread.start();
    }

    @NonNull
    private FileManager.SearchFileListener getSearchFileListener() {
        return new FileManager.SearchFileListener() {
            @Override
            public void searching(String typename) {
                Message message = handler.obtainMessage();
                message.what = 1;
                message.obj = typename;
                handler.sendMessage(message);
            }

            @Override
            public void end(boolean isExceptionEnd) {
                handler.sendEmptyMessage(2);
            }
        };
    }

    private void initMainUI() {
        tv_size = (TextView) findViewById(R.id.tv_file_any_size);
        view_all = findViewById(R.id.file_classlist_all);
        view_txt = findViewById(R.id.file_classlist_txt);
        view_video = findViewById(R.id.file_classlist_video);
        view_audio = findViewById(R.id.file_classlist_audio);
        view_image = findViewById(R.id.file_classlist_image);
        view_zip = findViewById(R.id.file_classlist_zip);
        view_apk = findViewById(R.id.file_classlist_apk);
        tv_size_all = (TextView) findViewById(R.id.file_all_size);
        tv_size_txt = (TextView) findViewById(R.id.file_txt_size);
        tv_size_video = (TextView) findViewById(R.id.file_video_size);
        tv_size_audio = (TextView) findViewById(R.id.file_audio_size);
        tv_size_image = (TextView) findViewById(R.id.file_image_size);
        tv_size_zip = (TextView) findViewById(R.id.file_zip_size);
        tv_size_apk = (TextView) findViewById(R.id.file_apk_size);
        pb_loading_all = (ProgressBar) findViewById(R.id.file_all_loading);
        pb_loading_txt = (ProgressBar) findViewById(R.id.file_txt_loading);
        pb_loading_video = (ProgressBar) findViewById(R.id.file_video_loading);
        pb_loading_audio = (ProgressBar) findViewById(R.id.file_audio_loading);
        pb_loading_image = (ProgressBar) findViewById(R.id.file_image_loading);
        pb_loading_zip = (ProgressBar) findViewById(R.id.file_zip_loading);
        pb_loading_apk = (ProgressBar) findViewById(R.id.file_apk_loading);
        iv_righticon_all = (ImageView) findViewById(R.id.file_all_righticon);
        iv_righticon_txt = (ImageView) findViewById(R.id.file_txt_righticon);
        iv_righticon_video = (ImageView) findViewById(R.id.file_video_righticon);
        iv_righticon_audio = (ImageView) findViewById(R.id.file_audio_righticon);
        iv_righticon_image = (ImageView) findViewById(R.id.file_image_righticon);
        iv_righticon_zip = (ImageView) findViewById(R.id.file_zip_righticon);
        iv_righticon_apk = (ImageView) findViewById(R.id.file_apk_righticon);
    }

    @NonNull
    private View.OnClickListener getListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.image_left:
                        finish();
                        break;
                    case R.id.file_classlist_all:
                    case R.id.file_classlist_txt:
                    case R.id.file_classlist_audio:
                    case R.id.file_classlist_apk:
                    case R.id.file_classlist_zip:
                    case R.id.file_classlist_video:
                    case R.id.file_classlist_image:
//                        Bundle b = new Bundle();
//                        b.putInt("id", v.getId());
//                        startActivity(FilemgrShowActivity.class, b, 0);
                        Intent i = new Intent(getApplicationContext(), FilemgrShowActivity.class);
                        i.putExtra("id", v.getId());
                        startActivityForResult(i, 1);//用于需要返回值的跳转
                        break;
                    default:
                        break;
                }

            }
        };
    }

    /**
     * 传回返回值必须重写onActivityResult方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            tv_size.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            tv_size_all.setText(CommonUtil.getFileSize(manager.getAnyFileSize()));
            tv_size_audio.setText(CommonUtil.getFileSize(manager.getAudioFileSize()));
            tv_size_txt.setText(CommonUtil.getFileSize(manager.getTxtFileSize()));
            tv_size_video.setText(CommonUtil.getFileSize(manager.getVideoFileSize()));
            tv_size_image.setText(CommonUtil.getFileSize(manager.getImageFileSize()));
            tv_size_zip.setText(CommonUtil.getFileSize(manager.getZipFileSize()));
            tv_size_apk.setText(CommonUtil.getFileSize(manager.getApkFileSize()));
        }
    }
}
