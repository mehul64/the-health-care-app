package com.thc.app.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thc.app.BaseActivity;
import com.thc.app.BaseApplication;
import com.thc.app.R;
import com.thc.app.models.Food;
import com.thc.app.utils.DataPreference;
import com.thc.app.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.gujun.android.taggroup.TagGroup;

public class SelectFoodDialogActivity extends BaseActivity {

    long calarieValue = 0;
    @Bind(R.id.foodEditText)
    EditText foodEditText;
    @Bind(R.id.calarieIncr)
    Button calarieIncr;
    @Bind(R.id.caloriesTextView)
    TextView caloriesTextView;
    @Bind(R.id.calarieDecr)
    Button calarieDecr;
    @Bind(R.id.tag_group)
    TagGroup tagGroup;

    ArrayList<String> stringArrayList = new ArrayList<>();
    HashMap<String, Integer> integerHashMap = new HashMap<>();
    private String detectMeal;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_food);
        ButterKnife.bind(this);
        setTitle("Enter Food");
        detectMeal = getIntent().getStringExtra("meal");

        if (detectMeal == null) {
            Utils.showThisMsg(activity, "Error:", "Something looks wrong please try again later.", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            }, null);
        } else {

            caloriesTextView.setText(String.valueOf(calarieValue));

            tagGroup.setTags(getFoodList());

            tagGroup.setOnTagClickListener(new TagGroup.OnTagClickListener() {
                @Override
                public void onTagClick(String tag) {
                    String food = foodEditText.getText().toString();
                    food += " +" + tag;
                    foodEditText.setText(food);
                    if (integerHashMap.get(tag) != null) {
                        calarieValue += integerHashMap.get(tag);
                    }
                    caloriesTextView.setText(String.valueOf(calarieValue));
                }
            });

            findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Dialog d=new Dialog(SelectFoodDialogActivity.this);
                    d.setContentView(R.layout.add_food_layout);
                    d.findViewById(R.id.add_food_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(((EditText)d.findViewById(R.id.add_food_name)).getText().toString().isEmpty() ||
                                    ((EditText)d.findViewById(R.id.add_food_calorie)).getText().toString().isEmpty())
                            {
                                Toast.makeText(SelectFoodDialogActivity.this,"Fields Empty",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Food food=new Food(((EditText)d.findViewById(R.id.add_food_name)).getText().toString(),
                                        Integer.parseInt(((EditText)d.findViewById(R.id.add_food_calorie)).getText().toString()));
                                food.save(SelectFoodDialogActivity.this);

                                tagGroup.setTags(getFoodList());
                                d.dismiss();
                            }
                        }
                    });
                    d.show();
                }
            });

        }
    }

    private ArrayList<String> getFoodList() {
        stringArrayList = new ArrayList<>();
        integerHashMap = new HashMap<>();

        integerHashMap.put("Bread", 96);
        stringArrayList.add("Bread");

        integerHashMap.put("Chapatis", 250);
        stringArrayList.add("Chapatis");

        integerHashMap.put("Cornflakes", 130);
        stringArrayList.add("Cornflakes");

        integerHashMap.put("Pasta", 330);
        stringArrayList.add("Pasta");

        integerHashMap.put("Potatoes", 210);
        stringArrayList.add("Potatoes");

        integerHashMap.put("Rice", 420);
        stringArrayList.add("Rice");

        integerHashMap.put("Beef", 300);
        stringArrayList.add("Beef");

        integerHashMap.put("Chicken", 220);
        stringArrayList.add("Chicken");

        integerHashMap.put("Ham", 6);
        stringArrayList.add("Ham");

        integerHashMap.put("Lamb", 300);
        stringArrayList.add("Lamb");

        integerHashMap.put("Prawns", 180);
        stringArrayList.add("Prawns");

        integerHashMap.put("Pork", 320);
        stringArrayList.add("Pork");

        integerHashMap.put("Apple", 44);
        stringArrayList.add("Apple");

        integerHashMap.put("Banana", 107);
        stringArrayList.add("Banana");

        integerHashMap.put("Cabbage", 15);
        stringArrayList.add("Cabbage");

        integerHashMap.put("Carrot", 16);
        stringArrayList.add("Carrot");

        integerHashMap.put("Mushrooms", 100);
        stringArrayList.add("Mushrooms");

        integerHashMap.put("Pineapple", 40);
        stringArrayList.add("Pineapple");

        integerHashMap.put("Sweetcorn", 95);
        stringArrayList.add("Sweetcorn");

        integerHashMap.put("Tomato", 30);
        stringArrayList.add("Tomato");

        integerHashMap.put("Cheese", 110);
        stringArrayList.add("Cheese");

        integerHashMap.put("Custard", 210);
        stringArrayList.add("Custard");

        integerHashMap.put("Ice cream", 200);
        stringArrayList.add("Ice cream");

        integerHashMap.put("Milk", 175);
        stringArrayList.add("Milk");

        integerHashMap.put("Butter", 112);
        stringArrayList.add("Butter");

        integerHashMap.put("Jam", 38);
        stringArrayList.add("Jam");

        integerHashMap.put("Popcorn", 150);
        stringArrayList.add("Popcorn");

        integerHashMap.put("Kiwi", 34);
        stringArrayList.add("Kiwi");

        integerHashMap.put("Satsumas", 35);
        stringArrayList.add("Satsumas");

        integerHashMap.put("Pizza", 400);
        stringArrayList.add("Pizza");

        integerHashMap.put("Chinese Fried Rice ", 450);
        stringArrayList.add("Chinese Fried Rice ");

        integerHashMap.put("Potato fried", 200);
        stringArrayList.add("Potato fried");

        integerHashMap.put("Indian sweets", 150);
        stringArrayList.add("Indian sweets");

        integerHashMap.put("Samosa", 100);
        stringArrayList.add("Samosa");

        integerHashMap.put("Indian sweets", 150);
        stringArrayList.add("Indian sweets");

        integerHashMap.put("Beer", 200);
        stringArrayList.add("Beer");

        try
        {
            SQLiteDatabase db=openOrCreateDatabase("THC",MODE_PRIVATE,null);
            Cursor cursor=db.rawQuery("select * from food;",null);
            if(cursor.moveToFirst())
            {
                do {
                    integerHashMap.put(cursor.getString(0),cursor.getInt(1));
                    stringArrayList.add(cursor.getString(0));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch (Exception e)
        {

        }
        return stringArrayList;
    }


    @OnClick({R.id.calarieIncr, R.id.calarieDecr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calarieIncr:
                calarieValue = calarieValue + 10;
                break;
            case R.id.calarieDecr:
                calarieValue = calarieValue - 10;
                break;
        }
        if (calarieValue < 0) {
            calarieValue = 0;
        }
        caloriesTextView.setText(String.valueOf(calarieValue));
    }

    @OnClick(R.id.submitButton)
    public void onSubmitButtonClick() {
        DataPreference dataPreference = new DataPreference(activity);
        if (foodEditText.getText().toString().trim().length() == 0) {
            Utils.showToast(activity, "Please enter food");
        } else {
            switch (detectMeal) {
                case "BREAKFAST":
                    dataPreference.setBreakFast(foodEditText.getText().toString().trim());
                    dataPreference.setBreakFastCarbs(calarieValue);
                    break;
                case "LUNCH":
                    dataPreference.setLunch(foodEditText.getText().toString().trim());
                    dataPreference.setLunchCarbs(calarieValue);
                    break;
                case "DINNER":
                    dataPreference.setDinner(foodEditText.getText().toString().trim());
                    dataPreference.setDinnerCarbs(calarieValue);
                    break;
            }
            BaseApplication.sendDataToServer(this, dataPreference);
            finish();
        }
    }
}
