/*
 * Copyright (C) 2019 Aayat Mimiko
 */

package com.example.xyzreader.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.example.xyzreader.models.ArticleData;
import com.example.xyzreader.utils.network_utils.ApiConstants;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArticleDataParser {
    //check for int/double errors
    private static String checkAndConvertNumber(Object number) {
        return String.valueOf(number);
    }

    public static List<ArticleData> parseData(String returnedJSONString, Context context) {

        //skips code below if returnedJSONString null or empty
        if (returnedJSONString == null || returnedJSONString.equals("")){
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        ArrayList results = gson.fromJson(returnedJSONString, ArrayList.class);

        //ListAdapter data will be stored as ArrayList<ArticleData>
        List<ArticleData> articleDataList = new ArrayList<>();

        //iterate through each article in results
        for (int index = 0; index < results.size() ; index++) {
            Map currentObject = (Map) results.get(index);//get current article

            //create new ArticleData from currentObject
            ArticleData articleData =
                    new ArticleData(
                            //get by keys
                            (String) (currentObject.get(ApiConstants.ID_KEY)),
                            (""),
                            (String) (currentObject.get(ApiConstants.TITLE_KEY)),
                            (String) (currentObject.get(ApiConstants.AUTHOR_KEY)),
                            (String) (currentObject.get(ApiConstants.BODY_KEY)),
                            (String) (currentObject.get(ApiConstants.THUMBNAIL_NAME_KEY)),
                            (String) (currentObject.get(ApiConstants.PHOTO_PATH_KEY)),
                            String.valueOf((Double) (currentObject.get(ApiConstants.ASPECT_RATIO_KEY))),
                            (String) (currentObject.get(ApiConstants.PUBLISHED_DATE_KEY))

                    );

            articleDataList.add(articleData);

        }

        return articleDataList;
    }
    
}
