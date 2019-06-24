package com.example.portadroid;

import java.util.ArrayList;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeviceList extends Activity {
    Button btnPaired;
    ListView devicelist;
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";

	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		
        btnPaired = (Button)findViewById(R.id.btnPaired);
        devicelist = (ListView)findViewById(R.id.listView);
        myBluetooth = BluetoothAdapter.getDefaultAdapter();  
        
        if(myBluetooth == null){
            msg("L'appareil se supporte pas le Bluetooth.");
            finish(); //fin apk
        }
        
        else if(!myBluetooth.isEnabled()){
                //demande à l'utilisateur d'activer le Bluetooth
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon,1);
        }
        
        btnPaired.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                pairedDevicesList();
            }
        });
	}

		private void pairedDevicesList(){
            pairedDevices = myBluetooth.getBondedDevices();
            ArrayList<String> list = new ArrayList<String>();
            //ArrayList list = new ArrayList();

            if (pairedDevices.size()>0){
                for(BluetoothDevice bt : pairedDevices){	
                	// Recupération du nom et de l'adresse du support
                    list.add(bt.getName() + "\n" + bt.getAddress());
                }
            }
            else{
            
                //msg("Aucun périphérique trouvé.");
                Toast.makeText(getApplicationContext(), "Aucun périphérique trouvé.", Toast.LENGTH_LONG).show();
            }

            //final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
            final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
            //ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            devicelist.setAdapter(adapter);
            devicelist.setOnItemClickListener(myListClickListener);
        }

        private void msg(String s){
        	Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
		}

		private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener(){
            public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3){
                // Recupération de l'adresse MAC, les 17 derniers caractères dans le View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                // Un intent pour la prochaine activité
                Intent i = new Intent(DeviceList.this, ControlPortail.class);

                //change d'activité
                i.putExtra(EXTRA_ADDRESS, address); //ceci sera reçu à l'activité ControlPortail            
                startActivity(i);
            }
        };
        
        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.device_list, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item){
            int id = item.getItemId();
            if (id == R.id.action_settings) {
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
	}

