package com.ricamgar.challenge.data.api;


import com.ricamgar.challenge.domain.model.Estimate;
import com.ricamgar.challenge.domain.model.EstimateRequest;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Single;

public interface CabifyApi {

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 6o_FrppOEQ5RrCkBOEKaBM-puJleMKrRn5nW_cy7H9Y"
    })
    @POST("/api/v2/estimate")
    Single<List<Estimate>> getEstimates(@Body EstimateRequest estimateRequest);
}
