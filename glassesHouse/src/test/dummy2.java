package test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class dummy2 {

	public static void main(String[] args) throws IOException {
	
		String path = "data\\order.dat";
		String path2 = "data\\orderTimeStamp.dat";
		
		File file = new File(path);
		File file2 = new File(path2);
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(file2));
		
		String line = null;
		int orderCode = 1;
		while((line = reader.readLine())!= null){

			String[] temp = line.split(",");
			int num = Integer.parseInt(temp[0]);
			int year, month, date, hour, minute;
			if(OrderStatus.DEPOSIT_STAY.toString().equals(temp[3])) {
				year = 2021;
				month = (int)(Math.random()*1)+9;
				date = (int)(Math.random()*28) +1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				Calendar now = Calendar.getInstance();
				String str = orderCode+","+ordertime+",null,null,null,null\r\n";
				writer.write(str);
				
			}else if(OrderStatus.DELIVERY.toString().equals(temp[3])) {
				
				year = 2021;
				month = (int)(Math.random()*2)+9;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				String str = orderCode+","+ordertime+",null,null,null,null\r\n";
				writer.write(str);
				
			}else if(OrderStatus.BEING_DELIVERY.toString().equals(temp[3])) {
			
				year = 2021;
				month = (int)(Math.random()*2)+9;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime =  CTOS(da);
				
				String str = orderCode+","+ordertime+",null,null,null,null\r\n";
				writer.write(str);
				
				
			}else if(OrderStatus.DELIVERY_COMPLITE.toString().equals(temp[3])) {
				
				year = 2021;
				month = (int)(Math.random()*10)+1;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				da.add(Calendar.DATE, 1);
				System.out.printf("%tF %tT\n", da, da);
				String endtime = CTOS(da);
				String str = orderCode+","+ordertime+","+endtime+",null,null,null\r\n";
				writer.write(str);
				
			}else if(OrderStatus.PURCHASE_CONFIRMED.toString().equals(temp[3])) {
				year = 2021;
				month = (int)(Math.random()*10)+1;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				da.add(Calendar.DATE, 1);
				String endtime = CTOS(da);
				da.add(Calendar.DATE, 7);
				String checktime = CTOS(da);
				String str = orderCode+","+ordertime+","+endtime+","+checktime+",null,null\r\n";
				writer.write(str);
				
			}else if(OrderStatus.REQUEST_RETURN.toString().equals(temp[3])
					||OrderStatus.REQUEST_EXCHANGE.toString().equals(temp[3])) {
				year = 2021;
				month = (int)(Math.random()*10)+1;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				da.add(Calendar.DATE, 1);
				String endtime = CTOS(da);
				da.add(Calendar.DATE, (int)(Math.random()*3)+1);
				String requestreturn = CTOS(da);
				String str = orderCode+","+ordertime+","+endtime+",null,"+requestreturn+",null\r\n";
				writer.write(str);
				
			}else if(OrderStatus.RETURN_DONE.toString().equals(temp[3])
					||OrderStatus.EXCHANGE_DONE.toString().equals(temp[3])) {
				year = 2021;
				month = (int)(Math.random()*10)+1;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				da.add(Calendar.DATE, 1);
				String endtime = CTOS(da);
				da.add(Calendar.DATE, (int)(Math.random()*3)+1);
				String requestreturn = CTOS(da);
				da.add(Calendar.DATE, 2);
				String donereturn = CTOS(da);
				String str = orderCode+","+ordertime+","+endtime+",null,"+requestreturn+","+donereturn+"\r\n";
				writer.write(str);
				
			}else if(OrderStatus.CANCEL_ORDER.toString().equals(temp[3])) {
				year = 2021;
				month = (int)(Math.random()*10)+1;
				date = (int)(Math.random()*28)+1;
				hour = (int)(Math.random()*23);
				minute = (int)(Math.random()*59);
				Calendar da = Calendar.getInstance();
				da.set(year, month, date, hour, minute);
				String ordertime = CTOS(da);
				da.add(Calendar.DATE, (int)(Math.random()*3)+1);
				String cancel = CTOS(da);
				String str = orderCode+","+ordertime+",null,null,"+cancel+","+cancel+"\r\n";
				writer.write(str);
			}
			
			orderCode++;
	}
		reader.close();
		writer.close();
	}
	
	public static void CTOT(Calendar da) {
		System.out.printf("%tF %tT\n", da, da);
	}
	
	public static String CTOS(Calendar da) {
		
		String time = da.get(Calendar.YEAR)+"-"
				+da.get(Calendar.MONTH)+"-"
				+da.get(Calendar.DATE)+"-"
				+da.get(Calendar.HOUR_OF_DAY)+"-"
				+da.get(Calendar.MINUTE);
		System.out.println(time);
		
		return time;
		
	}
}

enum OrderStatus{
	
	DEPOSIT_STAY, 
	DELIVERY, 
	BEING_DELIVERY,
	DELIVERY_COMPLITE,
	PURCHASE_CONFIRMED,
	REQUEST_RETURN,
	RETURN_DONE,
	REQUEST_EXCHANGE,
	EXCHANGE_DONE,
	CANCEL_ORDER;
	
}