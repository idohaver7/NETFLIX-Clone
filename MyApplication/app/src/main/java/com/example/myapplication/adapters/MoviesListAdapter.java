//package com.example.myapplication.adapters;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.myapplication.MovieInfoActivity;
//import com.example.myapplication.R;
//import com.example.myapplication.entities.Movie;
//
//import java.util.List;
//
//public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {
//    class MovieViewHolder extends RecyclerView.ViewHolder {
//        private final ImageView ivPic;
//
//        private MovieViewHolder(View itemView, Context context) {
//            super(itemView);
//        ivPic=itemView.findViewById(R.id.movie_image);
//        ivPic.setOnClickListener(v -> {
//            Intent intent = new Intent(context, MovieInfoActivity.class);
//            intent.putExtra("id", movies.get(getAdapterPosition()).getId());
//            context.startActivity(intent);
//            });
//        }
//        private final LayoutInflater mInflater;
//        private List<Movie> movies;
//        private Context context;
//
//        //Constructor for the MoviesListAdapter.
//        public MoviesListAdapter(Context context) {
//            mInflater = LayoutInflater.from(context);
//            this.context = context;
//        }
//
//        @Override
//        public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = mInflater.inflate(R.layout.movie_layout, parent, false);
//            return new MovieViewHolder(itemView, itemView.getContext());
//        }
//
//        //Called by RecyclerView to display the data at the specified position.
//        @Override
//        public void onBindViewHolder(MovieViewHolder holder, int position) {
//            if (movies != null && position < movies.size()) {
//                final Movie current = movies.get(position);
//                if (current != null) {
//                    // Set the thumbnail image if available
//                    if (current.getThumbpath() != null) {
//                        Bitmap savedThumbnail = BitmapFactory.decodeFile(current.getThumbpath());
//                        if (savedThumbnail != null) {
//                            holder.ivPic.setImageBitmap(savedThumbnail);
//                        } else {
//                            holder.ivPic.setImageResource(R.drawable.mypic); // Set a default image in case of failure
//                        }
//                    } else {
//                        holder.ivPic.setImageResource(current.getPic());
//                    }
//
//                }
//            }
//
//
//        }
//
//
//        // Sets the list of videos to be displayed in the adapter.
//        public void setVideos(List<Video> s) {
//            videos = s;
//            notifyDataSetChanged();
//        }
//
//        //Returns the total number of items in the data set held by the adapter.
//        @Override
//        public int getItemCount() {
//            if (videos != null)
//                return videos.size();
//            else return 0;
//        }
//
//        //Gets the list of videos in the adapter.
//        public List<Video> getVideos() {
//            return videos;
//        }
//
//        //Finds the ID of a video in the list by its position.
//        public int getById(int id) {
//            for (Video v : videos) {
//                if (v.getId() == id) {
//                    return v.getId();
//                }
//            }
//            return -1;
//        }
//
//    }
//    }
//
