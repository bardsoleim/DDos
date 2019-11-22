package fugue.com.ddos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
public class Main2Activity extends AppCompatActivity {
    private Button next2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        next2 = (Button) findViewById(R.id.buttonNext2);
        next2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText timeoutET = (EditText) findViewById(R.id.timeoutET);
                EditText portET = (EditText) findViewById(R.id.portET);
                EditText messageET = (EditText) findViewById(R.id.messageET);
                Spinner methodS = (Spinner) findViewById(R.id.methodList);
                EditText threadsET = (EditText) findViewById(R.id.threadsET);
                ProgressBar bar = (ProgressBar) findViewById(R.id.speedTrackbar);
                Stats.setTimeoutET(Integer.parseInt(timeoutET.getText().toString()));
                Stats.setPort(Integer.parseInt(portET.getText().toString()));
                Stats.setMessage(messageET.getText().toString());
                Stats.setMethod(methodS.getSelectedItemPosition());
                Stats.setThreads(Integer.parseInt(threadsET.getText().toString()));
                Stats.setPause(bar.getProgress());

                Intent myIntent = new Intent(Main2Activity.this, Main3Activity.class);

                Main2Activity.this.startActivity(myIntent);

            }
        });





    }
}