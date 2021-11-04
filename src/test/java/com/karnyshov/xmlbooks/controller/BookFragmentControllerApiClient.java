package com.karnyshov.xmlbooks.controller;

import com.karnyshov.xmlbooks.model.BookFragment;
import com.karnyshov.xmlbooks.service.dto.DetailedBookFragmentDto;
import com.karnyshov.xmlbooks.service.pagination.PaginationModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BookFragmentControllerApiClient {
    @GET("/books")
    Call<PaginationModel<BookFragment>> getAll(@Query("page") int page, @Query("pageSize") int pageSize);

    @GET("/books/{id}")
    Call<DetailedBookFragmentDto> get(@Path("id") Long id);
}
