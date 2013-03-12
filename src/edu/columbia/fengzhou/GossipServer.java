/*
 * The GossipServer class would wait for client request and once there's a request it will create a
 * GossipServerThread to process that request. It supports multiple client requests by creating a 
 * new thread for each client request.
 * 
 */



package edu.columbia.fengzhou;


import java.io.*; 
import java.util.*; 
import java.net.*;

public class GossipServer{ 
	//Specify Port number
	final int RECEIVE_PORT=6666; 
	
	//Constructor of the server
	public GossipServer() { 
		ServerSocket rServer=null; 
		Socket request=null; 
		Thread receiveThread=null; 
		
		try{ 
			rServer=new ServerSocket(RECEIVE_PORT); 
			System.out.println("Welcome to the Gossip Server!"); 
			System.out.println(new Date()); 
			System.out.println("The port of server: "+ RECEIVE_PORT); 
			
			while(true){ 
				request=rServer.accept();
				System.out.println();
				System.out.println("Requset from client connected on port: "+request.getPort());
				receiveThread=new GossipServerThread(request); 
				receiveThread.start(); 

			} 
		}catch(IOException e){ 
			System.out.println(e.getMessage());} 
	} 
	
	public static void main(String args[]){ 
		new GossipServer(); 
	} 
} 
