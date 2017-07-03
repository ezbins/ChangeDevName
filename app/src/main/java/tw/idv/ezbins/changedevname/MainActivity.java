package tw.idv.ezbins.changedevname;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainActivity extends Activity {
    private Button chg_btn;
    private EditText input_value;
    WifiP2pManager manager;
    WifiP2pManager.Channel channel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        chg_btn = (Button) findViewById(R.id.btn);
        input_value = (EditText) findViewById(R.id.devName);
        Press_Me();
    }

    public void Press_Me() {
        chg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                    channel = manager.initialize(getApplicationContext(), getMainLooper(), new WifiP2pManager.ChannelListener() {
                        @Override
                        public void onChannelDisconnected() {
                            manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
                        }
                    });
                    Class[] paramTypes = new Class[3];
                    paramTypes[0] = WifiP2pManager.Channel.class;
                    paramTypes[1] = String.class;
                    paramTypes[2] = WifiP2pManager.ActionListener.class;
                    Method setDeviceName = manager.getClass().getMethod(
                            "setDeviceName", paramTypes);
                    setDeviceName.setAccessible(true);

                    Object arglist[] = new Object[3];
                    arglist[0] = channel;
                    arglist[1] = input_value.getText().toString().trim();
                    arglist[2] = new WifiP2pManager.ActionListener() {

                        @Override
                        public void onSuccess() {
                            Toast.makeText(getApplicationContext(),"Success Change!",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(int reason) {
                            Toast.makeText(getApplicationContext(),"Change Failed!",Toast.LENGTH_LONG).show();
                        }
                    };
                    setDeviceName.invoke(manager, arglist);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
