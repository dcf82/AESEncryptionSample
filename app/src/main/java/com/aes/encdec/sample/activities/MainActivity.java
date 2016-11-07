package com.aes.encdec.sample.activities;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aes.encdec.sample.encryption.Encryptor;
import com.firebase.cloud.messaging.R;

public class MainActivity extends AppCompatActivity {
    private boolean mCalculating;

    private TextView mTextEncrypted;
    private TextView mTextDecrypted;
    private EditText mTextToCalculate;

    private static final String SALT = "BC8C70FCC4304F1C";
    private static final String KEY = "1E3C8C43E86A8B1AE99F86E5ADCDFE4D";
    private static final String IV = "50724D969BAC067A596C1D1E6A47F0AC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mTextEncrypted = (TextView) findViewById(R.id.textEncrypted);
        mTextDecrypted = (TextView) findViewById(R.id.textDecrypted);
        mTextToCalculate = (EditText) findViewById(R.id.textToCalculate);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calculate:

                if (mCalculating) return;
                mCalculating = true;

                String text = mTextToCalculate.getText().toString().toString();
                if (text.length() == 0) return;

                new EncryptorDecryptor().execute(text);
                break;
        }
    }

    private class EncryptorDecryptor extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String[] in) {
            String result[] = new String[2];

            // Encryption
            byte[] out = Encryptor.encrypt(in[0].getBytes(), KEY.getBytes(), IV.getBytes());
            result[0] = out != null ? new String(out) : "";

            // Decryption
            out = Encryptor.decrypt(out, KEY.getBytes(), IV.getBytes());
            result[1] = out != null ? new String(out) : "";

            return result;
        }

        protected void onPostExecute(String[] result) {
            mTextEncrypted.setText(result[0]);
            mTextDecrypted.setText(result[1]);
            mCalculating = false;
        }
    }
}
