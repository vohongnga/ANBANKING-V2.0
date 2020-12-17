package banking;
import banking.balance;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Locale;

class threadServer extends Thread{
	Socket socket = null;
	public threadServer(Socket socket)
	{
		this.socket = socket;
	}
	public void run()
	{
		try 
		{
			DataInputStream din = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());	
			Locale localeVN = new Locale("vi", "VN");
		    NumberFormat currencyVN = NumberFormat.getCurrencyInstance(localeVN);
			while(true)
			{
				String sql;
				try {
					String menu = din.readUTF();
					System.out.println(menu);
					sql newsql = new sql();
					switch(menu) {
					case "add":				
						int n = din.readInt();
						String str1 = currencyVN.format(n);
						System.out.println(n);
						int result = newsql.add(n);
								if(result>0)
								{
									dos.writeUTF("Nạp tiền thành công "+ str1);
									System.out.println("SUCCESS");
								}
								else
								{
									dos.writeUTF("Có lỗi xảy ra");
									System.out.println("NO SUCCESS");
								}
							break;
						
					case "trans":
						int n1 = din.readInt();
						String str2 = currencyVN.format(n1);
						System.out.println(n1);
						int resultTrans = newsql.trans(n1);
								if(resultTrans>0)
								{
									dos.writeUTF("Chuyển tiền thành công "+str2);
									System.out.println("SUCCESS");
								}
								else
								{
									dos.writeUTF("Có lỗi xảy ra");
									System.out.println("NO SUCCESS");
								}
						break;
						
					case "sub":
						int n11 = din.readInt();
						String str3 = currencyVN.format(n11);
						System.out.println(n11);
						int resultSub = newsql.sub(n11);
								if(resultSub>0)
								{
									dos.writeUTF("Rút tiền thành công "+str3);
									System.out.println("SUCCESS");
								}
								else
								{
									dos.writeUTF("Có lỗi xảy ra");
									System.out.println("NO SUCCESS");
								}
						break;
					case "check":
						ResultSet rs = newsql.check();
						while(rs.next()) {
							int balance = rs.getInt("tk");
							System.out.println(balance);
							dos.writeInt(balance);
							rs.close();
						}
						break;
						
					case "exit":
						break;
					}
				}
				catch(SQLException ex) {
					System.out.println("SQLException: " + ex.getMessage());
				    System.out.println("SQLState: " + ex.getSQLState());
				    System.out.println("VendorError: " + ex.getErrorCode());
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
		
}

class sql{
	public Statement connect() throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		String url="jdbc:mysql://127.0.0.1:3306/account";
		Connection conn = DriverManager.getConnection(url,"root","");
		Statement stmt = conn.createStatement();
		return stmt;
	}
	public synchronized int add(int n) throws ClassNotFoundException, SQLException {
		Statement stmt = connect();
		try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	String sql="SELECT tk FROM balance";
	ResultSet rs = stmt.executeQuery(sql);				
	while(rs.next()) {
		int balance = rs.getInt("tk");								
		System.out.println(balance);
		int tk = balance+n;
		System.out.println(tk);
		String name = "Nga";
		sql="UPDATE balance SET tk="+tk+" WHERE id=102170104";
		int result = stmt.executeUpdate(sql);
		return result;
	}
	return 0;
	}
	
	public synchronized int sub(int n) throws ClassNotFoundException, SQLException {
		Statement stmt = connect();
		try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	String sql="SELECT tk FROM balance";
	ResultSet rs = stmt.executeQuery(sql);				
	while(rs.next()) {
		int balance = rs.getInt("tk");								
		System.out.println(balance);
		int tk = balance-n;
		System.out.println(tk);
		String name = "Nga";
		sql="UPDATE balance SET tk="+tk+" WHERE id=102170104";
		int result = stmt.executeUpdate(sql);
		return result;
	}
	return 0;	
	}
	
	public synchronized int trans(int n) throws ClassNotFoundException, SQLException {
		Statement stmt = connect();
		try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	String sql="SELECT tk FROM balance";
	ResultSet rs = stmt.executeQuery(sql);				
	while(rs.next()) {
		int balance = rs.getInt("tk");								
		System.out.println(balance);
		int tk = balance-n;
		System.out.println(tk);
		String name = "Nga";
		sql="UPDATE balance SET tk="+tk+" WHERE id=102170104";
		int result = stmt.executeUpdate(sql);
		return result;
	}
	return 0;	
	}
	
	public synchronized ResultSet check() throws ClassNotFoundException, SQLException {
		try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		Statement stmt = connect();
		String sql="SELECT tk FROM balance";
		ResultSet rs = stmt.executeQuery(sql);
		return rs;
	}
}
public class server {
	public static void main(String args[]) throws IOException
	{
		ServerSocket server = new ServerSocket(9001);
		while(true)
		{
			Socket sk = server.accept();
			threadServer th = new threadServer(sk);
			th.start();
			
		}
			
	}
}
