package com.example.portadroid;

import java.io.IOException;
import java.util.UUID;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
//import android.widget.TextView;
import android.widget.Toast;

public class ControlPortail extends Activity {
	
    ImageButton imgOuverture, imgFermeture;
    //TextView myLabel;
    Button  discnt, abt;
    String address = null;
   
  
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent newint = getIntent();
		
		//Reception de l'adresse du périphérique Bluetooth
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); 
		setContentView(R.layout.activity_control_portail);
		
		//appel des widgets
		imgOuverture = (ImageButton)findViewById(R.id.ouverture);
		imgFermeture = (ImageButton)findViewById(R.id.fermeture);
        discnt = (Button)findViewById(R.id.btnDeonnect);
        abt = (Button)findViewById(R.id.btnApropos);
        //myLabel = (TextView)findViewById(R.id.labelText);
        
        new ConnectBT().execute(); //Appel de classe ConnectBT pour la connexion
        
        imgOuverture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                OuverturePortail();   //methode pour l'ouverture du portail
            }
        });

        imgOuverture.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                FermeturePortail();   //méthode pour la fermeture du portail
            }
        });

        discnt.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Disconnect(); //fermeture de la connection
            }
        });
	}
	
	private void Disconnect(){
        if (btSocket!=null){ 
            try
            {
                btSocket.close(); //Fermeture de la connexion
            }
            catch (IOException e)
            {
            	showMessage("Erreur lors de la deconnexion");
            }
        }
        finish(); //retour au premier layout
    }
	
    private void OuverturePortail(){
        if (btSocket!=null){
            try {
                btSocket.getOutputStream().write("0".toString().getBytes());
               // myLabel.setText("Portail en ouverture");
            }
            catch (IOException e) {
            	showMessage("Erreur lors de l'ouverture du portail");
            }
        }
    }
    
    private void FermeturePortail(){
        if (btSocket!=null){
            try{
                btSocket.getOutputStream().write("1".toString().getBytes());
               // myLabel.setText("Portail en fermeture");
            }
            catch (IOException e){
            	showMessage("Erreur lors de la fermeture du portail");
            }
        }
    }
    
    // Methode d'appel au Toast
    private void showMessage(String theMsg){
        Toast.makeText(getApplicationContext(),theMsg,Toast.LENGTH_LONG).show();
    }
    
    public  void about(View v){
        if(v.getId() == R.id.btnApropos){
            Intent i = new Intent(this, AproposActivity.class);
            startActivity(i);
        }
    }

	public boolean onCreateOptionsMenu(Menu menu){
		getMenuInflater().inflate(R.menu.control_portail, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

   
	private class ConnectBT extends AsyncTask<Void, Void, Void>{
        private boolean ConnectSuccess = true;
        protected void onPreExecute(){	
        	  //Affiche d'un progress dialog
            progress = ProgressDialog.show(ControlPortail.this, "Connexion...", "Veuillez attendre!!!");
        }
        
        //La connexion se fait en background lors de l'affiche du progress dialogue
        protected Void doInBackground(Void... devices) {
            try{
                if (btSocket == null || !isBtConnected){
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();
                }
            }
            catch (IOException e){
                ConnectSuccess = false;
            }
            return null;
        }

        protected void onPostExecute(Void result){
            super.onPostExecute(result);

            if (!ConnectSuccess){
            	showMessage("La connection a échoué. Y'as-t-il une périphérique Bluetooth ? Veuillez réessayer.");
                finish();
            }
            else {
            	showMessage("Connecté.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
