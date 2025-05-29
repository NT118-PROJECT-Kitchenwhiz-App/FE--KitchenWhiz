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

import com.example.kitchenwhiz.Model.Ingredients;
import com.example.kitchenwhiz.Model.RecipeInfo;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RecipeApiService;
import com.example.kitchenwhiz.Service.RetrofitClient;
import com.example.kitchenwhiz.Service.UserApiService;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Add_food extends AppCompatActivity {
    ImageView image;
    EditText txtname, txtdes, txtser, txttime,txtins;
    Button btnadd;
    TextView btnaddin;
    LinearLayout ingredientContainer;
    List<Ingredients> ingredients = new ArrayList<>();
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
        Intent intent = getIntent();
        User user = (User) getIntent().getSerializableExtra("user");
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
                    Intent inte = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    pickImage.launch(inte);
            });

            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = txtname.getText().toString().trim();
                    String des = txtdes.getText().toString().trim();
                    String ins = txtins.getText().toString().trim();
                    String servings_str = txtser.getText().toString().trim();
                    String time = txttime.getText().toString().trim();
                    addIngredient();
                    if (name.isEmpty() || des.isEmpty() || ins.isEmpty() || servings_str.isEmpty() || time.isEmpty()) {
                        Toast.makeText(Add_food.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        int readyInMinutes = Integer.parseInt(time);
                        int servings = Integer.parseInt(servings_str);
                        RecipeInfo info = new RecipeInfo(name, servings, readyInMinutes, des, ins, ingredients);
                        Gson gson = new Gson();
                        String recipeInfoJson = gson.toJson(info);

                        if (recipeInfoJson == null || recipeInfoJson.isEmpty()) {
                            Toast.makeText(Add_food.this, "Lỗi JSON", Toast.LENGTH_SHORT).show();
                            Log.e("ADD_FOOD", "Json is null or empty");
                            return;
                        }

                        AddRecipe(user, imageFile);

                        btnadd.setEnabled(true);

                    }
                    catch (Exception ex) {
                        Log.d("ADD_FOOD", ex.getMessage());
                    }
                }
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
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void addIngredient() {
        ingredients.clear();
        int count = 0;
        for (int i = 0; i < ingredientContainer.getChildCount(); i++){
            View row = ingredientContainer.getChildAt(i);
            EditText txtnameingre = row.findViewById(R.id.ingredient_name);
            EditText txtamount = row.findViewById(R.id.ingredient_amount);
            EditText txtunit = row.findViewById(R.id.ingredient_unit);

            String name_ingredients = txtnameingre.getText().toString().trim();
            String amount_ingredients = txtamount.getText().toString().trim();
            String unit_ingredients = txtunit.getText().toString().trim();
            if (count < 1) {
                if (name_ingredients.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập ít nhất một nguyên liệu", Toast.LENGTH_SHORT).show();
                }
            }
            if (!name_ingredients.isEmpty()) {
                if ((!amount_ingredients.isEmpty() && unit_ingredients.isEmpty()) || (!unit_ingredients.isEmpty() && amount_ingredients.isEmpty())) {
                    Toast.makeText(this, "Số lượng phải đi kèm đơn vị", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    if (amount_ingredients.isEmpty()) {
                        ingredients.add(new Ingredients(name_ingredients, 0, null));
                    }
                    else {
                        double double_amount = Double.parseDouble(amount_ingredients);
                        ingredients.add(new Ingredients(name_ingredients, double_amount, unit_ingredients));
                    }

                } catch (Exception e) {
                    Toast.makeText(this, "Số lượng phải là số", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        }
    }

    private void AddRecipe(User user, File imageFile) {
        btnadd.setEnabled(false);
        if (imageFile == null) {
            Toast.makeText(this, "Vui lòng chọn ảnh", Toast.LENGTH_SHORT).show();
            return;
        }
        String mimeType = URLConnection.guessContentTypeFromName(imageFile.getName());
        if (mimeType == null) {
            mimeType = "image/jpeg";
        }
        RequestBody imageRequestBody = RequestBody.create(MediaType.parse(mimeType), imageFile);

        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), imageRequestBody);
        RecipeApiService apiService = RetrofitClient.getRecipeApiService(this, user);
        Call<ResponseBody> call = apiService.addRecipe(imagePart);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Add_food.this, "Thêm công thức thành công", Toast.LENGTH_SHORT).show();
                    resetForm();
                } else {
                    if (response.errorBody() != null) {
                        try {
                            Toast.makeText(Add_food.this, "Món ăn này đã tồn tại", Toast.LENGTH_SHORT).show();
                            Log.d("API_ERROR", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Add_food.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetForm(){
        txtname.setText("");
        txttime.setText("");
        txtser.setText("");
        txtins.setText("");
        txtdes.setText("");
        ingredients.clear();
        ingredientContainer.removeAllViews();
        addRow();
        addRow();
        image.setImageResource(R.drawable.uploadphotos);
        imageFile = null;
    }

}