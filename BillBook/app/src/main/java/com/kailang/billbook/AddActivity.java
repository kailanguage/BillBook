package com.kailang.billbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddActivity extends AppCompatActivity {
    private static final int OUT = 1;
    private static final int IN = 2;
    private Button btnAdd;
    private RadioGroup radioGroup;
    private EditText etCount, etDescribe;
    private RadioButton radioIn, radioOut;
    private TextView tvInfo;
    private int countType = OUT;
    private LiveData<List<Count>> counts;
    private List<Count> allCount;
    private CountViewModel countViewModel;
    private int countID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bill_add);

        countViewModel = ViewModelProviders.of(AddActivity.this).get(CountViewModel.class);

        etCount = findViewById(R.id.etCount);
        etDescribe = findViewById(R.id.etDescribe);
        radioGroup = findViewById(R.id.radioGroup);
        tvInfo = findViewById(R.id.tvInfo);
        btnAdd = findViewById(R.id.btnAdd);
        radioIn = findViewById(R.id.radioIn);
        radioOut = findViewById(R.id.radioOut);


        Intent intent = getIntent();
        if (intent != null) {
            countID = intent.getIntExtra("count", Integer.MAX_VALUE);
            if (countID != Integer.MAX_VALUE) {
                Count countToEdit = countViewModel.getCountById(countID);
                etCount.setText(countToEdit.getMoney() + "");
                etDescribe.setText(countToEdit.getDescribe());
                countType = countToEdit.getType();
                if (countType == IN) {
                    radioIn.toggle();
                } else if (countType == OUT) {
                    radioOut.toggle();
                }
            }
        }



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioOut:
                        countType = OUT;
                        break;
                    case R.id.radioIn:
                        countType = IN;
                        break;
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etCount.getText().toString().isEmpty()&&!etDescribe.getText().toString().isEmpty()) {
                    Count count = new Count();
                    long time = System.currentTimeMillis();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String str = format.format(new Date(time));
                    count.setDate(str);
                    count.setMoney(Double.parseDouble(etCount.getText().toString().trim()));
                    count.setDescribe(etDescribe.getText().toString().trim());
                    count.setType(countType);
                    if (countID == Integer.MAX_VALUE)
                        countViewModel.insertCount(count);
                    else {
                        count.setId(countID);
                        countViewModel.updateCount(count);
                        Toast.makeText(AddActivity.this, getResources().getString(R.string.insert_success), Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(AddActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(AddActivity.this,getResources().getString(R.string.warming),Toast.LENGTH_SHORT).show();
                }
            }
        });

        List<Count> listIN=countViewModel.getCountByType(IN);
        List<Count> listOUT=countViewModel.getCountByType(OUT);
        double allIN=0,allOUT=0;
        for(Count count:listIN){
            allIN+=count.getMoney();
        }
        for (Count count:listOUT){
            allOUT+=count.getMoney();
        }
        Log.e(this.getLocalClassName(),allIN+" "+allOUT+"");
        if(allIN!=0||allOUT!=0){
            tvInfo.setText(getResources().getString(R.string.total_in)+":"+allIN+" "+getResources().getString(R.string.total_out)+":"+allOUT);
        }
    }
}
