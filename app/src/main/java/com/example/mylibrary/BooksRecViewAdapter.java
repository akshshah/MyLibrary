package com.example.mylibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mylibrary.Model.Book;

import java.util.ArrayList;

public class BooksRecViewAdapter extends RecyclerView.Adapter<BooksRecViewAdapter.ViewHolder> {
    private static final String TAG = "BooksRecViewAdapter";

    public interface DeleteBook{
        void onDeletingResult (int bookId);
    }

    public interface ChangeStatus{
        void onChangeStatusResult (Book book, String status);
    }

    private DeleteBook deleteBook;
    private ChangeStatus changeStatus;

    private Context context;
    private String activity;
    private ArrayList<Book> books = new ArrayList<>();

    public BooksRecViewAdapter(Context context, String activity) {
        this.context = context;
        this.activity = activity;
    }

    public BooksRecViewAdapter() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookName.setText(books.get(position).getName());
        holder.parent.setOnClickListener(v -> {
            Intent intent = new Intent(context,BookActivity.class);
            intent.putExtra("bookId",books.get(position).getId());
            context.startActivity(intent);
        });

        if(activity.equals("AllBooksActivity")){
            holder.parent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Deleting a book")
                        .setMessage("Are you sure you want to delete " + books.get(position).getName() + " ?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                deleteBook = (DeleteBook) context;
                                deleteBook.onDeletingResult(books.get(position).getId());
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }).setNegativeButton("No", (dialog, which) -> {

                        });

                builder.create().show();
                return true;
            });
        }
        else if(activity.equals("CurrentlyReadingActivity")){
            holder.parent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Remove from current readings")
                        .setMessage("Do you want to remove " + books.get(position).getName() + " from current readings?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                changeStatus = (ChangeStatus) context;
                                changeStatus.onChangeStatusResult(books.get(position),"default");
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }).setNegativeButton("No", (dialog, which) -> {

                        });
                builder.create().show();
                return true;
            });
        }
        else if(activity.equals("WantToActivity")){
            holder.parent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Remove from want to read")
                        .setMessage("Do you want to remove " + books.get(position).getName() + " from want to read list?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                changeStatus = (ChangeStatus) context;
                                changeStatus.onChangeStatusResult(books.get(position),"default");
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }).setNegativeButton("No", (dialog, which) -> {

                        });
                builder.create().show();
                return true;
            });
        }
        else if(activity.equals("AlreadyReadActivity")){
            holder.parent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Remove from already read")
                        .setMessage("Do you want to remove " + books.get(position).getName() + " from already read list?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                changeStatus = (ChangeStatus) context;
                                changeStatus.onChangeStatusResult(books.get(position),"default");
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }).setNegativeButton("No", (dialog, which) -> {

                        });
                builder.create().show();
                return true;
            });
        }
        else if(activity.equals("FavouritesActivity")){
            holder.parent.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Remove from favourite books")
                        .setMessage("Do you want to remove " + books.get(position).getName() + " from favourites list?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            try {
                                changeStatus = (ChangeStatus) context;
                                changeStatus.onChangeStatusResult(books.get(position),"false");
                            }catch (ClassCastException e){
                                e.printStackTrace();
                            }
                        }).setNegativeButton("No", (dialog, which) -> {

                        });
                builder.create().show();
                return true;
            });

        }


        Glide.with(context)
                .asBitmap()
                .load(books.get(position).getImageURL())
                .into(holder.bookImage);

    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void clearAdapter(){
        this.books.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private CardView parent;
        private TextView bookName;
        private ImageView bookImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            bookName = itemView.findViewById(R.id.bookName);
            bookImage = itemView.findViewById(R.id.bookImage);
        }
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
        notifyDataSetChanged();
    }
}
