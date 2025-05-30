package com.example.kitchenwhiz.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import com.bumptech.glide.Glide;
import com.example.kitchenwhiz.Model.Logoutrequest;
import com.example.kitchenwhiz.Model.RandomFactResponse;
import com.example.kitchenwhiz.Model.RecipeModel;
import com.example.kitchenwhiz.Model.User;
import com.example.kitchenwhiz.R;
import com.example.kitchenwhiz.Service.RecipeApiService;
import com.example.kitchenwhiz.Service.RetrofitClient;
import com.example.kitchenwhiz.Util.JWT;
import com.google.android.material.imageview.ShapeableImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity {
TextView txtusername, txtwhattoeat, txthome_food, txtdes;
EditText tbSearch;
ShapeableImageView avatar, image_food, popup_avatar;
View viewRandomfood, viewCreatefood;
ImageView favoriteIcon, recentlyIcon, suggestedIcon;
ImageButton image_search;
SharedPreferences shared;
GifImageView fact_food;
ActivityResultLauncher<Intent> pickImage;
File imageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        mapping();

        Intent intent = getIntent();
        User user = (User) getIntent().getSerializableExtra("user");
        txtusername.setText(user.getUsername());
        if (!user.getAvatar_url().isEmpty() && user.getAvatar_url() != null) {
            Glide.with(this)
                    .load(user.getAvatar_url())
                    .error(R.drawable.error)
                    .placeholder(R.drawable.loading_icon)
                    .into(avatar);
        }
        getTime(txtwhattoeat);
        RandomFood(user);

        tbSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String query = tbSearch.getText().toString().trim();
                    if (query.isEmpty()) {
                        Toast.makeText(Home.this, "Vui lòng nhập nội dung tìm kiếm", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    Intent intent = new Intent(Home.this, List_food.class);
                    intent.putExtra("search_query", query);
                    intent.putExtra("user", user);
                    intent.putExtra("list", "search");
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = tbSearch.getText().toString().trim();
                if (query.isEmpty()) {
                    Toast.makeText(Home.this, "Vui lòng nhập nội dung tìm kiếm", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("search_query", query);
                intent.putExtra("user", user);
                intent.putExtra("list", "search");
                startActivity(intent);
            }
        });
        
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = tbSearch.getText().toString().trim();
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("search_query", query);
                intent.putExtra("user", user);
                intent.putExtra("list", "search");
                startActivity(intent);
            }
        });

        viewCreatefood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Add_food.class);
                intent.putExtra("user", user);
                startActivity(intent);
            }
        });

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "favorite");
                startActivity(intent);

            }
        });

        recentlyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "viewed");
                startActivity(intent);
            }
        });

        suggestedIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, List_food.class);
                intent.putExtra("user", user);
                intent.putExtra("list", "top");
                startActivity(intent);
            }
        });

        fact_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fact_food.post(() -> {
                    int[] location = new int[2];
                    fact_food.getLocationOnScreen(location);
                    int x = location[0] - fact_food.getWidth() - 730;
                    int y = location[1];

                    showPopupRandomFact(x, y, user);
                });
            }
        });

        pickImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri imageUri = result.getData().getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                    popup_avatar.setImageBitmap(bitmap);
                    imageFile = saveBitmapToFile(bitmap);
                } catch (Exception ex) {
                    Toast.makeText(this, "Error loading image: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopuUserInfo(user);
            }
        });

    }

    private void mapping(){
        txtusername = findViewById(R.id.txtname);
        tbSearch = findViewById(R.id.home_search);
        avatar = findViewById(R.id.avatar);
        image_food = findViewById(R.id.home_img_food);
        viewRandomfood = findViewById(R.id.rcmrandomfood);
        viewCreatefood = findViewById(R.id.createdish);
        favoriteIcon = findViewById(R.id.btnfavorite);
        recentlyIcon = findViewById(R.id.btncooked);
        suggestedIcon = findViewById(R.id.btnsuggested);
        txtwhattoeat = findViewById(R.id.txtwhattoeat);
        txthome_food = findViewById(R.id.txthome_food);
        txtdes = findViewById(R.id.txtdescrip_food);
        fact_food = findViewById(R.id.random_fact);
        image_search = findViewById(R.id.image_search);
    }

    private void getTime(TextView txtwhattoeat) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int totalMinutes = hour * 60 + minute;

        if (inRange(totalMinutes, 4*60, 9*60+59)) {
            txtwhattoeat.setText("Kitchenwhiz lo bữa sáng!");
        }
        else if (inRange(totalMinutes, 10*60, 15*60+59)) {
            txtwhattoeat.setText("Trưa ăn gì? Kitchenhiz lo");
        }
        else if (inRange(totalMinutes, 16*60, 19*60 + 59)){
            txtwhattoeat.setText("Kitchenwhiz – Tối ngon!");
        }
        else {
            txtwhattoeat.setText("Đói khuya? Có Kitchenwhiz");
        }
    }

    private static boolean inRange(int value, int start, int end) {
        return value >= start && value <= end;
    }

    private void RandomFood(User user) {
        RetrofitClient.getRecipeApiService(this, user).randomRecipe().enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                try {

                    if (response.isSuccessful() && response.body() != null) {
                        RecipeModel recipeModel = response.body();
                        Glide.with(Home.this)
                                .load(recipeModel.getImage())
                                .placeholder(R.drawable.loading_icon)
                                .error(R.drawable.error)
                                .into(image_food);
                        txthome_food.setText(recipeModel.getTitle());
                        txtdes.setText(recipeModel.getSummary());
                        String foodid = recipeModel.getId();
                        viewRandomfood.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Home.this, Food_information.class);
                                intent.putExtra("Foodid", foodid);
                                intent.putExtra("user", user);
                                startActivity(intent);
                            }
                        });
                    }
                    else {
                        image_food.setImageResource(R.drawable.error);
                        txthome_food.setText("Hết ý tưởng món rồi!");
                        txtdes.setText("");
                }} catch (Exception e) {
                    Log.d("API_RANDOM", e.getMessage());
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toast.makeText(Home.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPopupRandomFact(int x, int y, User user) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup, null);

        PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.NO_GRAVITY, x, y);


        TextView popupText = popupView.findViewById(R.id.popup_text);

        getRandomFact(popupText, user);
    }

    private void showPopuUserInfo(User user) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.logout, null);

        int width = (int)(Resources.getSystem().getDisplayMetrics().widthPixels * 0.8);
        PopupWindow popupWindow = new PopupWindow(popupView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.alpha = 0.3f;
        getWindow().setAttributes(params);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setFocusable(true);

        popupWindow.showAtLocation(getWindow().getDecorView().getRootView(), Gravity.CENTER, 0, 0);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        popup_avatar = popupView.findViewById(R.id.popup_ava);
        Glide.with(this)
                .load(user.getAvatar_url())
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.error)
                .into(popup_avatar);
        TextView txt_username_popup = popupView.findViewById(R.id.popup_username);
        Button changeava = popupView.findViewById(R.id.button_changepass);
        Button logout = popupView.findViewById(R.id.button_logout);

        txt_username_popup.setText(user.getUsername());
        Logoutrequest logoutrequest = new Logoutrequest(user.getRefreshToken());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Home.this);
                dialog.setTitle("Đăng xuất");
                dialog.setMessage("Bạn có muốn đăng xuất?");
                dialog.setIcon(R.drawable.logo);
                dialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Logout(logoutrequest, user);
                    }
                });

                dialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                dialog.show();

            }
        });

        popup_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickImage.launch(intent);
            }
        });

        changeava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFile == null) {
                    Toast.makeText(Home.this, "Vui lòng chọn ảnh đại diện trước", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(Home.this, "Đang cập nhật avatar...", Toast.LENGTH_SHORT).show();
                setAvatar(imageFile, user, popup_avatar);
            }
        });

        popupWindow.setOnDismissListener(() -> {
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        });


    }

    private  File saveBitmapToFile(Bitmap bitmap) {
        File fileDir = getFilesDir();
        File imageFile = new File(fileDir, "avatar.jpeg");
        try {
            FileOutputStream fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            return imageFile;
        } catch (Exception e) {
            Log.d("IMAGE_AVATAR", e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
    private void Logout(Logoutrequest logoutrequest, User user) {
        RetrofitClient.getUserApiService(this, user).logout(logoutrequest).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    JWT jwt = new JWT(Home.this);
                    jwt.clearToken();
                    Toast.makeText(Home.this, "Đăng xuất thành công!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Home.this, Login.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(Home.this, "Lỗi", Toast.LENGTH_SHORT).show();
                    Log.d("LOGOUT", response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Home.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getRandomFact(TextView txt, User user) {
        RetrofitClient.getFactApiService(this, user).getRandomFact().enqueue(new Callback<RandomFactResponse>() {
            @Override
            public void onResponse(Call<RandomFactResponse> call, Response<RandomFactResponse> response) {
                if (response.isSuccessful()) {
                    RandomFactResponse fact = response.body();
                    txt.setText(fact.getQuote());
                }
                else {
                    txt.setText("Hôm nay không có fact nào rùi");
                }

            }

            @Override
            public void onFailure(Call<RandomFactResponse> call, Throwable t) {
                Toast.makeText(Home.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAvatar(File imageFile, User user, ShapeableImageView popup_avatar) {
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

        RetrofitClient.getUserApiService(this, user).updateAvater(imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String body = response.body().string();
                        JSONObject json = new JSONObject(body);
                        String url = json.optString("avatar_url");
                        Toast.makeText(Home.this, "Cập nhập avatar thành công", Toast.LENGTH_SHORT).show();
                        Glide.with(Home.this)
                                .load(url)
                                .placeholder(R.drawable.loading_icon)
                                .error(R.drawable.error)
                                .into(avatar);
                        Glide.with(Home.this)
                                .load(url)
                                .placeholder(R.drawable.loading_icon)
                                .error(R.drawable.error)
                                .into(popup_avatar);
                    } catch (Exception e) {
                        Log.d("RESPONSE", e.getMessage());
                    }
                }
                else {
                    Log.d("API_AVA", response.message());
                    Toast.makeText(Home.this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Home.this, "Không thể kết nối Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}