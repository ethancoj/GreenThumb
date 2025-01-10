package com.plants;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.ArrayList;
import java.util.List;

public class GuideFragment extends Fragment implements View.OnClickListener {

    RecyclerView recyclerView;
    List<Article> articleList = new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1, btn2, btn3, btn4;
    private NewsApiClient newsApiClient;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.news_recycler_view);
        progressIndicator = view.findViewById(R.id.progress_bar);
        btn1 = view.findViewById(R.id.btn_1);
        btn2 = view.findViewById(R.id.btn_2);
        btn3 = view.findViewById(R.id.btn_3);
        btn4 = view.findViewById(R.id.btn_4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        newsApiClient = new NewsApiClient("ebb1998f09924ce585edcfb87e2bb2f7");

        setRecyclerView();
        getNews();
    }

    void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean show) {
        if (show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    private void getNews() {
        changeInProgress(true);
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("nature AND sustainability")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                changeInProgress(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsAPI", throwable.getMessage());
                        changeInProgress(false);
                    }
                }
        );
    }

    private void getProjects() {
        changeInProgress(true);
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("diy AND crafts OR sustainable living")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                changeInProgress(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsAPI", throwable.getMessage());
                        changeInProgress(false);
                    }
                }
        );
    }

    private void getEvents() {
        changeInProgress(true);
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("environmental events OR climate action")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                changeInProgress(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                            });
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.e("NewsAPI", throwable.getMessage());
                        changeInProgress(false);
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_1) {
            getNews();
        } else if (v.getId() == R.id.btn_3) {
            getProjects();
        } else if (v.getId() == R.id.btn_4) {
            getEvents();
        }
    }
}