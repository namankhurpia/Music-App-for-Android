package com.namankhurpia.cred_challenge.Services;

import com.namankhurpia.cred_challenge.models.Schema;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MusicDataService {

    @GET("studio")
    Call<List<Schema>> GetAllMusic();
}
