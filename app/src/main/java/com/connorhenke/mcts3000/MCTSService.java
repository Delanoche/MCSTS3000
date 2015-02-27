package com.connorhenke.mcts3000;

import retrofit.RestAdapter;

public class MCTSService {

    public MCTSService() {
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://")
                .build();

    }
}
