package com.aimerneige.nosese;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;


public class MainActivity extends AppCompatActivity {

    public static final int ALBUM_APPLE_RESULT_CODE = 1;
    public static final int ALBUM_OTHERS_RESULT_CODE = 2;

    private ImageView appleImageView;
    private ImageView othersImageView;

    private static int appleWidth;
    private static int appleHeight;
    private static int othersWidth;
    private static int othersHeight;

    private static boolean appleImported = false;
    private static boolean othersImported = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        MaterialToolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        Button importAppleBtn = findViewById(R.id.import_apple_btn);
        importAppleBtn.setOnClickListener(v -> onImportAppleBtnClicked());

        Button importOthersBth = findViewById(R.id.import_others_btn);
        importOthersBth.setOnClickListener(v -> onImportOthersBtnClicked());

        Button generateBtn = findViewById(R.id.generate_btn);
        generateBtn.setOnClickListener(v -> onGenerateBtnClicked());

        appleImageView = findViewById(R.id.apple_image);

        othersImageView = findViewById(R.id.other_image);
    }

    private void onImportAppleBtnClicked() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_APPLE_RESULT_CODE);
    }

    private void onImportOthersBtnClicked() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, ALBUM_OTHERS_RESULT_CODE);
    }

    private void onGenerateBtnClicked() {
        if (!appleImported) {
            Toast.makeText(getApplicationContext(), "请先导入苹果设备图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!othersImported) {
            Toast.makeText(getApplicationContext(), "请先导入其他设备图片！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (appleWidth != othersWidth || appleHeight != othersHeight) {
            Toast.makeText(getApplicationContext(), "图片尺寸不统一！", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), "todo", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        handleImageOnKitKat(data, requestCode);
    }

    private void handleImageOnKitKat(Intent data, int requestCode) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是 document 类型的 Uri，则通过 document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                // 解析出数字格式的 id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content: //downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 content 类型的 Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是 file 类型的 Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        switch (requestCode) {
            case ALBUM_APPLE_RESULT_CODE: {
                appleImageView.setImageBitmap(bitmap);
                appleHeight = bitmap.getHeight();
                appleWidth = bitmap.getWidth();
                appleImported = true;
                break;
            }
            case ALBUM_OTHERS_RESULT_CODE: {
                othersImageView.setImageBitmap(bitmap);
                othersHeight = bitmap.getHeight();
                othersWidth = bitmap.getWidth();
                othersImported = true;
                break;
            }
            default: {
                break;
            }
        }
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_settings: {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.menu_item_about: {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
