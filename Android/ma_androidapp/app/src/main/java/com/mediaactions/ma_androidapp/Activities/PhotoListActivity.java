package com.mediaactions.ma_androidapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.mediaactions.ma_androidapp.R;
import com.mediaactions.ma_androidapp.RESTClasses.Image;
import com.mediaactions.ma_androidapp.RESTClasses.ImgList;
import com.mediaactions.ma_androidapp.RESTClasses.ParamRest;
import com.mediaactions.ma_androidapp.RESTClasses.RestGallery;
import com.mediaactions.ma_androidapp.RESTClasses.User_;
import com.mediaactions.ma_androidapp.Utils.ImageDownloader;
import com.mediaactions.ma_androidapp.Utils.ImageListAdapter;
import com.mediaactions.ma_androidapp.Utils.ImgDl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

public class PhotoListActivity extends AppCompatActivity {

    private User_               _user;
    private ArrayList<ImgDl>    imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_list);

        _user = (User_)getIntent().getSerializableExtra("user");
        imgList = new ArrayList<>();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity httpEntity = new HttpEntity(httpHeaders);
        ParamRest paramRest = new ParamRest(
                Globals.galleryApiURL,
                httpEntity,
                HttpMethod.GET,
                String.class
        );

        new RestGallery(this).execute(paramRest);
    }

    public void updateDisplay(ImgList restImages) {

        List<Image> listImg = restImages.getImages();

        for (Image img : listImg) {
            ImgDl tmp = new ImgDl(Globals.siteURL + img.getPath());
            new ImageDownloader(this).execute(tmp);
        }
    }

    public void launchUpload(View view) {
        Intent intent = new Intent(this, UploadActivity.class);
        intent.putExtra("user", _user);
        startActivity(intent);
    }

    public void setElement(ImgDl imgDl) {

        if (imgDl != null)
            imgList.add(imgDl);

        ImageListAdapter imageListAdapter = new ImageListAdapter(this, imgList);
        ListView listView = findViewById(R.id.imgView);
        listView.setAdapter(imageListAdapter);
    }
}