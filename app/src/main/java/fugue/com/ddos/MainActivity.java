package fugue.com.ddos;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;

import android.os.PowerManager;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.net.InetAddress;
public class MainActivity extends AppCompatActivity  {

    private Button lockURL, lockIP, fireButton, buttonnext;
    private TextView urlTV, selectedTargetTV;

    private String selectedTarget = "NONE";
    protected PowerManager.WakeLock mWakeLock;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lockURL = (Button) findViewById(R.id.lockOnURLButton);
        lockIP = (Button) findViewById(R.id.lockOnIpButton);
        fireButton = (Button) findViewById(R.id.fireButton);
        urlTV = (TextView) findViewById(R.id.urlTV);
        selectedTargetTV = (TextView) findViewById(R.id.selectedTargetTV);
        buttonnext = (Button) findViewById(R.id.buttonNext);
        lockURL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Lock on to URL
                vibrate(20);
                lockOn(urlTV.getText().toString());
            }
        });
        buttonnext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!selectedTarget.equals("NONE")) {
                    Intent myIntent = new Intent(MainActivity.this, Main2Activity.class);

                    MainActivity.this.startActivity(myIntent);
                }
                else{
                    Toast.makeText(MainActivity.this,
                            "Lock onto a target first!", Toast.LENGTH_SHORT).show();

                }
            }
        });
        lockIP.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vibrate(20);
                // Lock on to IP
                EditText ip1 = (EditText) findViewById(R.id.ip1);
                EditText ip2 = (EditText) findViewById(R.id.ip2);
                EditText ip3 = (EditText) findViewById(R.id.ip3);
                EditText ip4 = (EditText) findViewById(R.id.ip4);
                String ip = ip1.getText().toString() + "." + ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString();
                lockOn(ip);
            }
        });
        addListeners();
        final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "DDOS");


        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (prefs.getBoolean("warning2", true)) {
            new AlertDialog.Builder(this).setTitle("Terms of Use").setMessage("DDOS is a tool for network stress testing. The developer assumes no responsibilities for unintended use of this tool. DOS Responsibly!").setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putBoolean("warning2", false).commit();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putBoolean("warning2", true).commit();
                }
            }).show();
        }
    }


    private void vibrate(int i) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(50);
    }



    public void lockOn(final String address) {
        Runnable r = new Runnable() {
            public void run() {
                try {
                    String domain = address.replace("http://", "").replace("www.", "").replace(" ", "").replace("https://", "");
                    if (domain.length() > 0) {
                        selectedTarget = InetAddress.getByName(domain).getHostAddress();
                        Stats.setSelectedtarget(selectedTarget);
                        Runnable r = new Runnable() {

                            public void run() {
                                selectedTargetTV.setText(selectedTarget);
                                selectedTargetTV.setTextColor(Color.parseColor("#00ff00"));
                            }

                        };
                        MainActivity.this.runOnUiThread(r);
                        Log.d("Lock On", selectedTarget);
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(r).start();
    }

    public void addListeners() {
        final EditText ip1 = (EditText) findViewById(R.id.ip1);
        final EditText ip2 = (EditText) findViewById(R.id.ip2);
        final EditText ip3 = (EditText) findViewById(R.id.ip3);
        final EditText ip4 = (EditText) findViewById(R.id.ip4);

        ip1.addTextChangedListener((new TextWatcher() {
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    ip2.requestFocus();
                    int i = Integer.parseInt(ip1.getText().toString());
                    if (i > 255) {
                        ip1.setText("255");
                    }
                }
            }

            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    ip1.setText(s.toString().substring(0, s.length() - 1));
                    int i = Integer.parseInt(ip1.getText().toString());
                    if (i > 255) {
                        ip1.setText("255");
                    }
                }
            }
        }));
        ip2.addTextChangedListener((new TextWatcher() {
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    ip3.requestFocus();
                    int i = Integer.parseInt(ip2.getText().toString());
                    if (i > 255) {
                        ip2.setText("255");
                    }
                }
            }

            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    ip2.setText(s.toString().substring(0, s.length() - 1));
                    int i = Integer.parseInt(ip2.getText().toString());
                    if (i > 255) {
                        ip2.setText("255");
                    }
                }
            }
        }));
        ip3.addTextChangedListener((new TextWatcher() {
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    ip4.requestFocus();
                    int i = Integer.parseInt(ip3.getText().toString());
                    if (i > 255) {
                        ip3.setText("255");
                    }
                }
            }

            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    ip3.setText(s.toString().substring(0, s.length() - 1));
                    int i = Integer.parseInt(ip3.getText().toString());
                    if (i > 255) {
                        ip3.setText("255");
                    }
                }
            }
        }));
        ip4.addTextChangedListener((new TextWatcher() {
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            public void afterTextChanged(Editable s) {
                if (s.length() > 3) {
                    ip4.setText(s.toString().substring(0, s.length() - 1));
                    int i = Integer.parseInt(ip4.getText().toString());
                    if (i > 255) {
                        ip4.setText("255");
                    }
                }
            }
        }));
        ip2.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (ip2.getText().toString().length() < 1) {
                        ip1.requestFocus();
                    }
                }
                return false;
            }
        });

        ip3.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (ip3.getText().toString().length() < 1) {
                        ip2.requestFocus();
                    }
                }
                return false;
            }
        });

        ip4.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    if (ip4.getText().toString().length() < 1) {
                        ip3.requestFocus();
                    }
                }
                return false;
            }
        });
    }}

