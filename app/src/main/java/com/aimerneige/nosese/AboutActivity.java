package com.aimerneige.nosese;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RelativeLayout;

public class AboutActivity extends AppCompatActivity {

    private static final String referUrl = "https://github.com/DavidBuchanan314/ambiguous-png-packer";
    private static final String openSourceUrl = "https://github.com/aimerneige/Nosese";
    private static final String githubUrl = "https://github.com/aimerneige";
    private static final String blogUrl = "https://aimerneige.com/zh";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

        RelativeLayout aboutApp = findViewById(R.id.about_about_app);
        aboutApp.setOnClickListener(v -> onAboutAppClicked());

        RelativeLayout openSource = findViewById(R.id.about_open_source);
        openSource.setOnClickListener(v -> onOpenSourceClicked());

        RelativeLayout github = findViewById(R.id.about_github);
        github.setOnClickListener(v -> onGitHubClicked());

        RelativeLayout blog = findViewById(R.id.about_blog);
        blog.setOnClickListener(v -> onBlogClicked());
    }


    private void onAboutAppClicked() {
        new AlertDialog.Builder(this)
            .setTitle("关于本软件")
            .setMessage("本软件完全开源，相关合成算法均来源于：" + referUrl + "，点击确认可跳转到合成算法项目开源地址。")
            .setCancelable(true)
            .setPositiveButton("确认", (dialogInterface, i) -> startUrlActivity(referUrl))
            .setNegativeButton("取消", null)
            .show();
    }

    private void onOpenSourceClicked() {
        startUrlActivity(openSourceUrl);
    }

    private void onGitHubClicked() {
        startUrlActivity(githubUrl);
    }

    private void onBlogClicked() {
        startUrlActivity(blogUrl);
    }

    private void startUrlActivity(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
