package com.example.kitchenwhiz.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kitchenwhiz.R;

import java.io.File;
import java.io.FileOutputStream;

public class Add_food extends AppCompatActivity {
    ImageView image;
    EditText txtname, txtdes, txtser, txttime,txtins;
    Button btnadd;
    TextView btnaddin;
    LinearLayout ingredientContainer;
    ActivityResultLauncher<Intent> pickImage;
    File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_food);
            mapping();
            addRow();
            addRow();
            btnaddin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addRow();
                }
            });

            pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imageUri = result.getData().getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                        image.setImageBitmap(bitmap);
                        imageFile = saveBitmapToFile(bitmap);
                    } catch (Exception ex) {
                        Toast.makeText(this, "Error loading image: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            image.setOnClickListener(v -> {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImage.launch(intent);
            });

            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void mapping() {
        image = findViewById(R.id.image);
        txtname = findViewById(R.id.txtnamefood);
        txtdes = findViewById(R.id.txtdes);
        txtins = findViewById(R.id.txtins);
        txtser = findViewById(R.id.txtser);
        txttime = findViewById(R.id.txttime);
        btnaddin = findViewById(R.id.addingre);
        btnadd = findViewById(R.id.btnaddfood);
        ingredientContainer = findViewById(R.id.ingredient_container);
    }

    private void addRow() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View ingredientRow = inflater.inflate(R.layout.ingredient_rows, ingredientContainer, false);
        ingredientContainer.addView(ingredientRow);
    }

    private  File saveBitmapToFile(Bitmap bitmap) {
        File fileDir = getFilesDir();
        File imageFile = new File(fileDir, "dish.jpeg");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return imageFile;
        } catch (Exception e) {
            Log.d("IMAGE_ADD_FOOD", e.getMessage());
            Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }


}