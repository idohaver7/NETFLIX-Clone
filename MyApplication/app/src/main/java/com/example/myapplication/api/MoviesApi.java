package com.example.myapplication.api;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.example.myapplication.entities.Movie;
import com.example.myapplication.utils.FileUtils;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MoviesApi {
    private final WebServiceApi webServiceApi;
    private final Retrofit retrofit;
    private Context context;

    public MoviesApi(Context context) {
        this.context = context;
        this.retrofit = RetrofitClient.getInstance();
        this.webServiceApi = retrofit.create(WebServiceApi.class);
    }

    // Alternate constructor if needed.
    public MoviesApi(Retrofit retrofit) {
        this.retrofit = retrofit;
        this.webServiceApi = retrofit.create(WebServiceApi.class);
    }

    public void getMovies(String token, MovieCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.getMoviesByCategory(authToken).enqueue(new Callback<Map<String, List<Movie>>>() {
            @Override
            public void onResponse(Call<Map<String, List<Movie>>> call, Response<Map<String, List<Movie>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Movies fetched: " + response.body().size() + " categories");
                    callback.onSuccess(response.body());
                } else {
                    Log.e("API_ERROR", "Failed to fetch movies. Response Code: " + response.code());
                    callback.onError("Failed to fetch movies.");
                }
            }
            @Override
            public void onFailure(Call<Map<String, List<Movie>>> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    /**
     * Create a movie using a JSON payload.
     */
    public void createMovieMultipart(String token,
                                     String title,
                                     String category,
                                     String description,
                                     Uri videoUri,
                                     Uri imageUri,
                                     MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        RequestBody titleBody = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody categoryBody = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody descriptionBody = RequestBody.create(MediaType.parse("text/plain"), description);

        // Prepare video part.
        MultipartBody.Part videoPart = null;
        if (videoUri != null) {
            File videoFile = FileUtils.createTempFileFromUri(context, videoUri, ".mp4");
            if (videoFile != null) {
                Log.d("MOVIES_API", "Video temp file: " + videoFile.getAbsolutePath());
                RequestBody videoReq = RequestBody.create(MediaType.parse("video/mp4"), videoFile);
                videoPart = MultipartBody.Part.createFormData("video", videoFile.getName(), videoReq);
            } else {
                Log.e("MOVIES_API", "Failed to create temp file for video");
            }
        }

        // Prepare image part.
        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            File imageFile = FileUtils.createTempFileFromUri(context, imageUri, ".jpg");
            if (imageFile != null) {
                Log.d("MOVIES_API", "Image temp file: " + imageFile.getAbsolutePath());
                RequestBody imageReq = RequestBody.create(MediaType.parse("image/*"), imageFile);
                imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageReq);
            } else {
                Log.e("MOVIES_API", "Failed to create temp file for image");
            }
        }

        // Enqueue the multipart request.
        webServiceApi.createMovie(authToken, titleBody, categoryBody, descriptionBody, videoPart, imagePart)
                .enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.d("API_RESPONSE", "Movie created successfully.");
                            callback.onSuccess();
                        } else {
                            Log.e("API_ERROR", "Failed to create movie. Response Code: " + response.code());
                            callback.onError("Failed to create movie.");
                        }
                    }

                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.e("API_ERROR", "Network error: " + t.getMessage());
                        callback.onError("Network error: " + t.getMessage());
                    }
                });
    }

    /**
     * Update a movie using a JSON payload.
     *
     * @param token    The authentication token.
     * @param movieId  The ID of the movie to update.
     * @param movie    A JsonObject containing the updated fields.
     * @param callback The callback for success or error.
     */
    public void updateMovie(String token, String movieId, JsonObject movie, MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.updateMovie(authToken, movieId, movie).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie updated successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to update movie. Response Code: " + response.code());
                    callback.onError("Failed to update movie.");
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public void deleteMovie(String token, String movieId, MovieActionCallback callback) {
        String authToken = "Bearer " + token;
        webServiceApi.DeleteMovie(authToken, movieId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie deleted successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to delete movie. Response Code: " + response.code());
                    callback.onError("Failed to delete movie.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    public void getRecommendedMovies(String token,String movieId, MovieListCallback callback) {
        webServiceApi.getRecommendsMovies(movieId,"Bearer " + token).enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>>  response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_RESPONSE", "Recommended Movies fetched: " + response.body().size() );

                    // Send the entire movies structure
                    callback.onSuccess(response.body());
                } else {
                    Log.e("API_ERROR", "Failed to fetch movies. Response Code: " + response.code());
                    callback.onError("Failed to fetch movies.");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>>  call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
    public void addMovieToWatchList(String token,String movieId,MovieActionCallback callback){
        webServiceApi.addToWatchedBy(movieId,"Bearer " + token).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("API_RESPONSE", "Movie added to watchedBy successfully.");
                    callback.onSuccess();
                } else {
                    Log.e("API_ERROR", "Failed to add movie to watched by. Response Code: " + response.code());
                    callback.onError("Failed to add movie to watched by.");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("API_ERROR", "Network error: " + t.getMessage());
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }



    public interface MovieCallback {
        void onSuccess(Map<String, List<Movie>> moviesByCategory);
        void onError(String error);
    }

    public interface MovieActionCallback {
        void onSuccess();
        void onError(String error);
    }
    public interface MovieListCallback {
        void onSuccess(List<Movie> recommendedMovie);
        void onError(String error);
    }
}
