package com.example.restaurantapp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.zero.clientsocketmanager.ClientSocketManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity 
{
	EditText ip_box;
	Button login_button;
	TextView text;
	Handler handler;
	final int port = 12121;
	ClientSocketManager client_socket_manager;
	Socket socket;
	String ip;
	
	void init()
	{
		ip_box = (EditText)findViewById(R.id.ipBoxId);
		login_button = (Button)findViewById(R.id.loginBtnId);
		text = (TextView)findViewById(R.id.textId);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();
		
		handler = new Handler()
		{
			public void handleMessage(Message msg)
			{
				String words = (String)msg.obj;
				if( words.equals("ok") == true )
				{
					Toast.makeText(MainActivity.this, "来自服务器的问候：登陆成功！！欢迎使用~",Toast.LENGTH_LONG).show();
					finish();
				}
				else if(words.equals("did not login") )
				{
					Toast.makeText(MainActivity.this,"ip错了或者是主机没有开", Toast.LENGTH_SHORT).show();
				}
				else
				{
					text.setText((String)msg.obj);
				}
			}
		};
		
		class GetThread implements Runnable
		{
			public void getMsg()
			{
				try 
				{
					Scanner in = new Scanner(socket.getInputStream());
					String gotmsg = in.nextLine();
					Message msg = new Message();
					msg.obj = gotmsg;
					MainActivity.this.handler.sendMessage(msg);
					
				} catch (UnknownHostException e) 
				{
					Message msg = new Message();
					msg.obj = "net error";
					MainActivity.this.handler.sendMessage(msg);
				} catch (IOException e) 
				{
					Message msg = new Message();
					msg.obj = "net error";
					MainActivity.this.handler.sendMessage(msg);
				}
				
			}
			
			@Override
			public void run() 
			{
				try 
				{
					socket = new Socket();
					socket.connect(new InetSocketAddress(ip, port) , 5000);
				} catch (UnknownHostException e) 
				{
					Message msg = new Message();
					msg.obj = "did not login";
					MainActivity.this.handler.sendMessage(msg);
				} catch (IOException e) {
					Message msg = new Message();
					msg.obj = "did not login";
					MainActivity.this.handler.sendMessage(msg);
				}
				
				if( socket != null)
					getMsg();
				else
				{
					Message msg = new Message();
					msg.obj = "did not login";
					MainActivity.this.handler.sendMessage(msg);
				}
			}
		}
		
		login_button.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				ip = ip_box.getText().toString();
				client_socket_manager = new ClientSocketManager(ip, port, handler);
				boolean logged = client_socket_manager.login();
				if(!logged)
					return;
				client_socket_manager.openGetThread();
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
