package com.example.mylibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mylibrary.Model.Book;

public class AddNewBookDialog extends DialogFragment {
    private static final String TAG = "AddNewBookDialog";
    private EditText editName,editAuthor,editLanguage,editPages,editDescription,editImageURL;
    private Button btnAdd,btnCancel;

    interface AddNewBook{
        void onAddingNewBookResult(Book book);
    }

    private AddNewBook addNewBook;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add_new_book,null);
        initialize(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Add New Book")
                .setView(view);

        btnAdd.setOnClickListener(v -> {

            boolean isEmpty = false;

            if(editName.getText().toString().trim().equals("")){
                editName.setError("All fields are required");
                isEmpty = true;
            }
            if(editAuthor.getText().toString().trim().equals("")){
                editAuthor.setError("All fields are required");
                isEmpty = true;
            }
            if(editDescription.getText().toString().trim().equals("")){
                editDescription.setError("All fields are required");
                isEmpty = true;
            }
            if(editLanguage.getText().toString().trim().equals("")){
                editLanguage.setError("All fields are required");
                isEmpty = true;
            }
            if(editPages.getText().toString().trim().equals("")){
                editPages.setError("All fields are required");
                isEmpty = true;
            }
            if(editImageURL.getText().toString().trim().equals("")){
                editImageURL.setError("All fields are required");
                isEmpty = true;
            }

            if(!isEmpty){
                Book book = new Book();
                book.setName(editName.getText().toString());
                book.setAuthor(editAuthor.getText().toString());
                book.setLanguage(editLanguage.getText().toString());
                book.setDescription(editDescription.getText().toString());
                book.setPages(Integer.parseInt(editPages.getText().toString()));
                book.setImageURL(editImageURL.getText().toString());
                book.setStatus("default");
                book.setFavourite(false);

                try {
                    addNewBook = (AddNewBook) getActivity();
                    addNewBook.onAddingNewBookResult(book);
                    dismiss();
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());

        return builder.create();
    }

    private void initialize(View view) {

        editName = view.findViewById(R.id.editName);
        editAuthor = view.findViewById(R.id.editAuthor);
        editLanguage = view.findViewById(R.id.editLanguage);
        editPages = view.findViewById(R.id.editPages);
        editDescription = view.findViewById(R.id.editDescription);
        editImageURL = view.findViewById(R.id.editImageURL);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);
    }
}
