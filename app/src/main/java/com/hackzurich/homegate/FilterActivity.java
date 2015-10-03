package com.hackzurich.homegate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class FilterActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {

    private EditText mPriceInput;
    private EditText mPriceInputMax;
    private EditText mRoomInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Homegate rent apartment");

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBarMin);
        seekBar.setOnSeekBarChangeListener(this);

        SeekBar seekBarMax = (SeekBar) findViewById(R.id.seekBarMax);
        seekBarMax.setOnSeekBarChangeListener(this);
        SeekBar seekBarRoom = (SeekBar) findViewById(R.id.seekBarRoom);
        seekBarRoom.setOnSeekBarChangeListener(this);

        mPriceInput = (EditText) findViewById(R.id.priceLabel);
        mPriceInputMax = (EditText) findViewById(R.id.priceLabelMax);
        mRoomInput = (EditText) findViewById(R.id.roomLabel);

        Button submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_PRICE_MIN, mPriceInput.getText().toString());
                intent.putExtra(MainActivity.EXTRA_PRICE_MAX, mPriceInputMax.getText().toString());
                intent.putExtra(MainActivity.EXTRA_ROOM_MIN, mRoomInput.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        EditText priceInput;

        if (seekBar.getId() == R.id.seekBarRoom) {
            float v = progress * 0.5F;
            mRoomInput.setText(String.valueOf(v));
            return;
        }

        if (seekBar.getId() == R.id.seekBarMin) {
            priceInput = mPriceInput;
        } else {
            priceInput = mPriceInputMax;
        }
        priceInput.setText("" + progress * progress * progress);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
