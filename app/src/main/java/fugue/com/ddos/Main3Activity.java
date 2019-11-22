package fugue.com.ddos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.TrafficStats;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    private Button fireButton;
    private ServiceDenier serviceDenier = new ServiceDenier();
    private String selectedTarget = "NONE";
    private TextView tex;
    protected PowerManager.WakeLock mWakeLock;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        fireButton = (Button) findViewById(R.id.fireButton);
        tex = (TextView)findViewById(R.id.textView10);
        tex.setText(Stats.getSelectedtarget() + "    " + Stats.getPort());
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DDOS");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        serviceDenier.stop();
    }

    @Override
    public void onBackPressed() {
        if (ServiceDenier.firing) {
            serviceDenier.stop();
            fireButton.setText("FIRE");
        } else {
            super.onBackPressed();
        }
    }

    private void vibrate(int i) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }

    public void fire(View v) {
        this.mWakeLock.acquire(10*60*1000L /*10 minutes*/);
        vibrate(50);
        if (Stats.getSelectedtarget().equals("NONE")) {
            Toast.makeText(this, "No target selected!", Toast.LENGTH_SHORT).show();
        } else {
            getTotalRxBytes();
            final Button b = (Button) v;
            if (!ServiceDenier.firing) {
                int timeout = Stats.getTimeoutET();
                int port = Stats.getPort();
                String message = Stats.getMessage();
                final int method =Stats.getMethod();
                int threads = Stats.getThreads();
                String target = Stats.getSelectedtarget();

                serviceDenier.DDOS(target, port, method, threads, timeout, message, Stats.getPause());
                b.setText("STOP");

                Runnable r = new Runnable() {

                    public void run() {
                        final TextView tvHits = (TextView) findViewById(R.id.statusTV);
                        final TextView tvTime = (TextView) findViewById(R.id.statusTVtime);
                        final TextView tvPacketsPerSec = (TextView) findViewById(R.id.statusTVpackets);
                        while (ServiceDenier.firing) {
                            switch (method) {
                                case 0:
                                    Main3Activity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0 + "s");
                                            tvHits.setText("TCP Hits: " + TCPSocketThread.count);
                                            tvPacketsPerSec.setText("Packets/sec: " + Math.round(TCPSocketThread.count / ((System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0)));

                                        }
                                    });
                                    break;
                                case 1:
                                    Main3Activity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - UDPSocketThread.startTime) / 1000.0 + "s");
                                            tvHits.setText("UDP Hits: " + UDPSocketThread.count);
                                            tvPacketsPerSec.setText("Packets/sec: " + Math.round(UDPSocketThread.count / ((System.currentTimeMillis() - UDPSocketThread.startTime) / 1000.0)));

                                        }
                                    });
                                    break;
                                case 2:
                                    Main3Activity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - HTTPSocketThread.startTime) / 1000.0 + "s");
                                            tvHits.setText("HTTP Hits: " + HTTPSocketThread.count);
                                            tvPacketsPerSec.setText("Packets/sec: " + Math.round(HTTPSocketThread.count / ((System.currentTimeMillis() - HTTPSocketThread.startTime) / 1000.0)));

                                        }
                                    });
                                    break;
                            }

                            if (ServiceDenier.error) {
                                serviceDenier.stop();
                                tvTime.setText("Elapsed Time: " + (System.currentTimeMillis() - TCPSocketThread.startTime) / 1000.0 + "s");
                                tvHits.setText("Exception Occurred");
                                tvPacketsPerSec.setText("Error - Check log for details");
                                b.setText("FIRE");
                            }

                            try {
                                Thread.sleep(50);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }                        }
                        Main3Activity.this.mWakeLock.release();
                    }
                };
                new Thread(r).start();
            } else {
                serviceDenier.stop();
                b.setText("FIRE");
            }
        }
    }


@SuppressLint("DefaultLocale")
public String getLinkRate(){
    TrafficStats ts = new TrafficStats();
    WifiManager wm = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    assert wm != null;
    WifiInfo wi = wm.getConnectionInfo();
    return String.format("%d Mbps", wi.getLinkSpeed());
}
    private void getTotalRxBytes() {

        Thread t = new Thread() {

            @Override
            public void run() {

                while (ServiceDenier.firing) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                final TextView tvHits2 = (TextView) findViewById(R.id.statusTVpackets2);
                long BeforeTime = System.currentTimeMillis();
                long TotalTxBeforeTest = TrafficStats.getTotalTxBytes();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                long TotalTxAfterTest = TrafficStats.getTotalTxBytes();
                long AfterTime = System.currentTimeMillis();
                double TimeDifference = AfterTime - BeforeTime;
                double txDiff = TotalTxAfterTest - TotalTxBeforeTest;
                double txBPS = (txDiff / (TimeDifference/1000));
                final String upspeed = String.valueOf((int)txBPS/1000) + "Kb/s.";

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       tvHits2.setText(upspeed);
                    }
                });
            }}
        };

        t.start();
    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        EditText timeoutET = (EditText) findViewById(R.id.timeoutET);
        EditText portET = (EditText) findViewById(R.id.portET);
        EditText messageET = (EditText) findViewById(R.id.messageET);
        switch (arg2) {
            case 0:
                timeoutET.setEnabled(true);
                portET.setEnabled(true);
                messageET.setEnabled(true);
                break;
            case 1:
                timeoutET.setEnabled(false);
                portET.setEnabled(true);
                messageET.setEnabled(true);
                break;
            case 2:
                timeoutET.setEnabled(true);
                portET.setText("80");
                portET.setEnabled(false);
                messageET.setEnabled(false);
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
