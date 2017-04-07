package com.ider.launchermovie;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ider.launchermovie.util.FileAdapter;
import com.ider.launchermovie.util.FileControl;

import java.io.File;
import java.util.ArrayList;

import static android.R.attr.path;

public class FileActivity extends ListActivity {
    private static final String ROOT_PATH = "/";
    //存储文件名称
    private ArrayList<String> names = null;
    //存储文件路径
    private ArrayList<String> paths = null;
    private FileControl fileControl=new FileControl();
    private View view;
    private ListView listView;
    private LinearLayout main;
    private EditText editText;
    private TextView titlePath;
    private String nowPath = ROOT_PATH;;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);

        titlePath = (TextView)findViewById(R.id.title_path);
        if (ContextCompat.checkSelfPermission(FileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
        //显示文件列表
        showMainDir();
        }
//        Log.i("mainactivity", Build.VERSION.SDK_INT+"");

    }
    private void showMainDir(){
        nowPath = ROOT_PATH;
        titlePath.setText(nowPath);
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(ROOT_PATH);
        File[] files = file.listFiles();
        for (File f : files) {
            if (!f.getPath().equals("/storage/emulated")&&!f.getPath().equals("/storage/self")) {
                names.add(f.getName());
                paths.add(f.getPath());
            }
        }
        this.setListAdapter(new FileAdapter(this, names, paths));
    }
    private void showFileDir(String path){
        titlePath.setText(path);
        names = new ArrayList<String>();
        paths = new ArrayList<String>();
        File file = new File(path);
        File[] files = file.listFiles();
        //如果当前目录不是根目录
        if (!ROOT_PATH.equals(path)) {
//                names.add("@1");
//                paths.add(ROOT_PATH);
//                names.add("@2");
//                paths.add(file.getParent());
        }
        //添加文件
        for (File f : files) {
            if (f.isDirectory()) {
                names.add(f.getName());
                paths.add(f.getPath());
            }else if (FileControl.isAdd(f)){
                names.add(f.getName());
                paths.add(f.getPath());
            }
        }
        if (files != null) {
            nowPath = path;
        }
        this.setListAdapter(new FileAdapter(this, names, paths));

    }
    @Override
    public void onBackPressed(){
        if (!nowPath.equals(ROOT_PATH)){
            File file = new File(nowPath);
            if (file.getParent().equals(ROOT_PATH)){
                showMainDir();
            }else {
                showFileDir(file.getParent());
            }
        }else {
            finish();
        }

    }
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        if (ContextCompat.checkSelfPermission(FileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(FileActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }else {
            // 文件存在并可读
            openFile(position);
        }
        super.onListItemClick(l, v, position, id);
    }
    private void openFile(int position){
        String path = paths.get(position);
        File file = new File(path);
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {

                //显示子目录及文件
                showFileDir(path);

            } else {
                //处理文件
                openFile(file);
            }
        }
        //没有权限
        else {
            Resources res = getResources();
            new AlertDialog.Builder(this).setTitle("Message")
                    .setMessage(res.getString(R.string.no_permission))
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch (requestCode){
            case 1:
                if (grantResults.length>0&& grantResults[0] ==PackageManager.PERMISSION_GRANTED){
                    showMainDir();
                }else {
                    Toast.makeText(this,"You denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
    //对文件进行增删改
    private void fileHandle(final File file){
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 打开文件
                if (which == 0){
                    openFile(file);
                }
                //修改文件名
                else if(which == 1){
                    LayoutInflater factory = LayoutInflater.from(FileActivity.this);
                    view = factory.inflate(R.layout.rename_dialog, null);
                    editText = (EditText)view.findViewById(R.id.editText);
                    editText.setText(file.getName());
                    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            String modifyName = editText.getText().toString();
                            final String fpath = file.getParentFile().getPath();
                            final File newFile = new File(fpath + "/" + modifyName);
                            if (newFile.exists()){
                                //排除没有修改情况
                                if (!modifyName.equals(file.getName())){
                                    new AlertDialog.Builder(FileActivity.this)
                                            .setTitle("注意!")
                                            .setMessage("文件名已存在，是否覆盖？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    if (file.renameTo(newFile)){
                                                        showFileDir(fpath);
                                                        displayToast("重命名成功！");
                                                    }
                                                    else{
                                                        displayToast("重命名失败！");
                                                    }
                                                }
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .show();
                                }
                            }
                            else{
                                if (file.renameTo(newFile)){
                                    showFileDir(fpath);
                                    displayToast("重命名成功！");
                                }
                                else{
                                    displayToast("重命名失败！");
                                }
                            }
                        }
                    };
                    AlertDialog renameDialog = new AlertDialog.Builder(FileActivity.this).create();
                    renameDialog.setView(view);
                    renameDialog.setButton("确定", listener2);
                    renameDialog.setButton2("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    });
                    renameDialog.show();
                }
                //删除文件
                else{
                    new AlertDialog.Builder(FileActivity.this)
                            .setTitle("注意!")
                            .setMessage("确定要删除此文件吗？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(file.delete()){
                                        //更新文件列表
                                        showFileDir(file.getParent());
                                        displayToast("删除成功！");
                                    }
                                    else{
                                        displayToast("删除失败！");
                                    }
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                }
            }
        };
        //选择文件时，弹出增删该操作选项对话框
        String[] menu = {"打开文件","重命名","删除文件"};
        new AlertDialog.Builder(FileActivity.this)
                .setTitle("请选择要进行的操作!")
                .setItems(menu, listener)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
    //打开文件
    private void openFile(File file){
        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        String type = FileControl.getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);

    }
    //获取文件mimetype
    private String getMIMEType(File file){
        String type = "";
        String name = file.getName();
        //文件扩展名
        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")){
            type = "audio";
        }
        else if(end.equals("mp4") || end.equals("3gp")) {
            type = "video";
        }
        else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("bmp") || end.equals("gif")){
            type = "image";
        }
        else {
            //如果无法直接打开，跳出列表由用户选择
            type = "*";
        }
        type += "/*";
        return type;
    }
    private void displayToast(String message){
        Toast.makeText(FileActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
