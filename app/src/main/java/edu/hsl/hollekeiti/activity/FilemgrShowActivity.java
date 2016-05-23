package edu.hsl.hollekeiti.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.hsl.hollekeiti.R;
import edu.hsl.hollekeiti.adapter.FileAdapter;
import edu.hsl.hollekeiti.base.MyActivity;
import edu.hsl.hollekeiti.bean.FileInfo;
import edu.hsl.hollekeiti.phone.db.FileManager;
import edu.hsl.hollekeiti.phone.util.CommonUtil;
import edu.hsl.hollekeiti.phone.util.FileTypeUtil;

public class FilemgrShowActivity extends MyActivity {
    private int            id;
    private TextView       tv_size;
    private TextView       tv_number;
    private Button         btn_delall;
    private ListView       listview;
    private FileAdapter    adapter;
    private List<FileInfo> fileInfos;
    private long   fileSize   = 0;
    private long   fileNumber = 0;
    private String title      = "全部文件";
    private CheckBox cb_delall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filemgr_show);
        id = getIntent().getIntExtra("id", R.id.file_classlist_all);
        initMainData(id);
        initMainUI();
        initMainUIData();
    }

    private void initMainUIData() {
        tv_size.setText(CommonUtil.getFileSize(fileSize));
        tv_number.setText(fileNumber + "个");
        adapter = new FileAdapter(getApplicationContext());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(getItemListener());
        adapter.addDataToAdapter(fileInfos);
        adapter.notifyDataSetChanged();
    }

    @NonNull
    private AdapterView.OnItemClickListener getItemListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileInfo info   = adapter.getItem(position);
                File     file   = info.getFile();
                String   type   = FileTypeUtil.getMIMEType(file);
                Intent   intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), type);
                startActivity(intent);
            }
        };
    }

    private void initMainUI() {
        initActionBar(title, R.mipmap.btn_homeasup_default, -1, getListener());
        tv_size = (TextView) findViewById(R.id.tv_file_all_size);
        tv_number = (TextView) findViewById(R.id.tv_file_number);
        listview = (ListView) findViewById(R.id.lv_file_view);
        btn_delall = (Button) findViewById(R.id.btn_delall);
        cb_delall = (CheckBox) findViewById(R.id.cb_delall);
        cb_delall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                List<FileInfo> infos = adapter.getDataList();
                for (FileInfo info : infos) {
                    info.setSelect(isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        });
        btn_delall.setOnClickListener(getListener());
    }

    private void initMainData(int id) {
        fileInfos = new ArrayList<>();
        switch (id) {
            case R.id.file_classlist_all:
                fileSize = FileManager.getFileManager().getAnyFileSize();
                fileInfos = FileManager.getFileManager().getAnyFileList();
                title = getResources().getString(R.string.filetype_all);
                break;
            case R.id.file_classlist_apk:
                fileSize = FileManager.getFileManager().getApkFileSize();
                fileInfos = FileManager.getFileManager().getApkFileList();
                title = getResources().getString(R.string.filetype_apk);
                break;
            case R.id.file_classlist_audio:
                fileSize = FileManager.getFileManager().getAudioFileSize();
                fileInfos = FileManager.getFileManager().getAudioFileList();
                title = getResources().getString(R.string.filetype_audio);
                break;
            case R.id.file_classlist_zip:
                fileSize = FileManager.getFileManager().getZipFileSize();
                fileInfos = FileManager.getFileManager().getZipFileList();
                title = getResources().getString(R.string.filetype_zip);
                break;
            case R.id.file_classlist_image:
                fileSize = FileManager.getFileManager().getImageFileSize();
                fileInfos = FileManager.getFileManager().getImageFileList();
                title = getResources().getString(R.string.filetype_image);
                break;
            case R.id.file_classlist_txt:
                fileSize = FileManager.getFileManager().getTxtFileSize();
                fileInfos = FileManager.getFileManager().getTxtFileList();
                title = getResources().getString(R.string.filetype_txt);
                break;
            case R.id.file_classlist_video:
                fileSize = FileManager.getFileManager().getVideoFileSize();
                fileInfos = FileManager.getFileManager().getVideoFileList();
                title = getResources().getString(R.string.filetype_video);
                break;
        }
        fileNumber = fileInfos.size();
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
                    case R.id.btn_delall:
                        delFile();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void delFile() {
        List<FileInfo> dellist = new ArrayList<>();
        for (FileInfo info : fileInfos) {
            if (info.isSelect()) {
                dellist.add(info);
            }
        }
        for (FileInfo info : dellist) {
            File file = info.getFile();
            long size = file.length();
            if (file.delete()) {
                adapter.getDataList().remove(info);
                FileManager.getFileManager().getAnyFileList().remove(info);
                FileManager.getFileManager().setAnyFileSize
                        (FileManager.getFileManager().getAnyFileSize() - size);
                switch (id) {
                    case R.id.file_classlist_all:
                        FileManager.getFileManager().getAnyFileList().remove(info);
                        FileManager.getFileManager().setAnyFileSize
                                (FileManager.getFileManager().getAnyFileSize() - size);
                        break;
                    case R.id.file_classlist_apk:
                        FileManager.getFileManager().getApkFileList().remove(info);
                        FileManager.getFileManager().setApkFileSize
                                (FileManager.getFileManager().getApkFileSize() - size);
                        break;
                    case R.id.file_classlist_audio:
                        FileManager.getFileManager().getAudioFileList().remove(info);
                        FileManager.getFileManager().setAudioFileSize
                                (FileManager.getFileManager().getAudioFileSize() - size);
                        break;
                    case R.id.file_classlist_zip:
                        FileManager.getFileManager().getZipFileList().remove(info);
                        FileManager.getFileManager().setZipFileSize
                                (FileManager.getFileManager().getZipFileSize() - size);
                        break;
                    case R.id.file_classlist_image:
                        FileManager.getFileManager().getImageFileList().remove(info);
                        FileManager.getFileManager().setImageFileSize
                                (FileManager.getFileManager().getImageFileSize() - size);
                        break;
                    case R.id.file_classlist_txt:
                        FileManager.getFileManager().getTxtFileList().remove(info);
                        FileManager.getFileManager().setTxtFileSize
                                (FileManager.getFileManager().getTxtFileSize() - size);
                        break;
                    case R.id.file_classlist_video:
                        FileManager.getFileManager().getVideoFileList().remove(info);
                        FileManager.getFileManager().setVideoFileSize
                                (FileManager.getFileManager().getVideoFileSize() - size);
                        break;
                }
            }
        }
        adapter.notifyDataSetChanged();
        fileNumber = adapter.getDataList().size();
        tv_number.setText(fileNumber + "个");
        System.gc();
        Thread.yield();//放弃线程执行权
    }
}
