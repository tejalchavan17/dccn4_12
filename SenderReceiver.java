import java.net.*;
import java.io.*;
import java.util.*;
 
public class Sender
{
    int n,i,sum=0;
    int data[]=new int[50];
    Scanner sc=new Scanner(System.in);
    private Socket          socket   = null;
    private ServerSocket    server   = null;
    private DataInputStream in       =  null;
    private DataOutputStream out     = null;
    public Sender(int port)
    {
        try
        {
            server = new ServerSocket(port);
	    System.out.println("--------------Sender--------------");
 
            System.out.println("Waiting...");
 
            socket = server.accept();
            System.out.println("Connection accepted");
 
            in = new DataInputStream(socket.getInputStream());

	    out    = new DataOutputStream(socket.getOutputStream());
 
            while (true)
            {
                try
                {

		    System.out.println("Enter No. of Data to send:");
		    n=sc.nextInt();
		    System.out.println("Enter Data:");
		    for(i=0;i<n;i++)
			data[i]=sc.nextInt();
		    for(i=0;i<n;i++)
			sum=sum+data[i];
		    sum=sum*(-1);
		    data[i]=sum;
		    n++;
		    System.out.println("Checksum: "+sum);
		    out.writeInt(n);
		    for(i=0;i<n;i++)
			out.writeInt(data[i]);
                    if (in.readUTF().equals("No Error")) 
                    {   
			System.out.println("Acknowledge from receiver:");
                	System.out.println("Message received Successfully without error!");
                	break;
            	    }
                    else
                    {
                        System.out.println("Message received with error!");
                        break;
                    }

                }
                catch(IOException i)
                {
                    System.out.println(i);
                }
            }
            socket.close();
            in.close();
	    out.close();
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Sender sender = new Sender(5000);
    }
}


import java.net.*;
import java.io.*;
import java.util.*;
 
public class Receiver
{

    int n,i,sum=0,checksum;
    int data[]=new int[50];
    Scanner sc=new Scanner(System.in);
    private Socket socket=null;
    private DataInputStream in=null;
    private DataOutputStream out=null;
 
    public Receiver(String address, int port)
    {
        try
        {
	    System.out.println("--------------Receiver--------------");
	    socket = new Socket(address, port);
            System.out.println("Connected");

	    in = new DataInputStream(socket.getInputStream());		
 
            out    = new DataOutputStream(socket.getOutputStream());
 
            while (true)
            {
            	try
            	{
			n=in.readInt();
			System.out.println("Data received with Checksum:");
			for(i=0;i<n;i++)
			{
				data[i]=in.readInt();
				System.out.println(data[i]);
			}
			for(i=0;i<(n-1);i++)
			{
				sum=sum+data[i];
			}
			checksum=sum+data[n-1];
			System.out.println("sum of data received:"+sum);
			System.out.println("Checksum: "+checksum);
			if(checksum==0)
			{
				System.out.println("No Error");
				out.writeUTF("No Error");
				break;
			}
			else
			{
				System.out.println("Error");
				out.writeUTF("Error");
				break;
			}
            	}
            	catch(IOException i)
            	{
                	System.out.println(i);
            	}
       	    }
 
            in.close();
            out.close();
            socket.close();
	}
        catch(IOException i)
        {
            System.out.println(i);
        }
    }
 
    public static void main(String args[])
    {
        Receiver receiver = new Receiver("127.0.0.1", 5000);
    }
}

/*Output:
tejal@ubuntu:~/Desktop$ javac Sender.java
tejal@ubuntu:~/Desktop$ java Sender
--------------Sender--------------
Waiting...
Connection accepted
Enter No. of Data to send:
5
Enter Data:
9
6
8
3
5
Checksum: -31
Acknowledge from receiver:
Message received Successfully without error!
tejal@ubuntu:~/Desktop$ 

tejal@ubuntu:~/Desktop$ javac Receiver.java
tejal@ubuntu:~/Desktop$ java Receiver
--------------Receiver--------------
Connected
Data received with Checksum:
9
6
8
3
5
-31
sum of data received:31
Checksum: 0
No Error
tejal@ubuntu:~/Desktop$ 
*/
