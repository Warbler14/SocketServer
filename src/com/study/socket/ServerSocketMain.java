package com.study.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerSocketMain {
	
	public static void main(String[] args) {
	
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(7000);

			System.out.println("Start socket server");
			while(true) {
				Socket socket = serverSocket.accept();
				System.out.println("socket accepted : " + socket.getPort());
				
				new Thread() {
					
					DataInputStream dis;
					DataOutputStream dos;
					
					@Override
					public void run() {
						try {
							dis = new DataInputStream( socket.getInputStream() );
							dos = new DataOutputStream( socket.getOutputStream() );
							
							System.out.println("start sync");
							while(true) {
								try {

									byte[] receivedMsgByte = new byte[dis.readInt()];
									dis.readFully(receivedMsgByte);
									String receivedMsg = new String(receivedMsgByte);
									System.out.println( "From Client : " + receivedMsg );

									String sendMsg = "server send data";
									byte[] sendMsgByte = sendMsg.getBytes();
									dos.writeInt(sendMsgByte.length);
									dos.write(sendMsgByte);
									dos.flush();

								} catch (Exception e) {
									e.printStackTrace();
									break;
								}
							}
							
							
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								dis.close();
								dos.close();
								socket.close();
								
							} catch (Exception e2) {
								// TODO: handle exception
							}
						}
						System.out.println("end sync");
						
					}
				}.start();
				
				
			}//end while
			
			
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if( serverSocket != null ) {
				try {
					serverSocket.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
			}
		}
		
	}
}
