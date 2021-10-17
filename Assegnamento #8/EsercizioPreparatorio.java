import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.net.*;
/*Scrivere un programma JAVA che implementi un server che apre una listening socket
su una porta e resta in attesa di richieste di connessione.
• quando arriva una richiesta di connessione, il server accetta la connessione,
trasferisce al client un messaggio ("HelloClient") e poi chiude la connessione.
• usare canali non bloccanti e il selettore (e i buffer di tipo ByteBuffer).
• lato client è possibile utilizzare telnet
Opzione più semplice
• come primo esercizio potete sviluppare un programma in cui dopo il controllo
key.isAcceptable il server scrive subito sulla socketChannel restituita dall'operazione
di accept e chiude la connessione
Opzione più completa (ma un po' più complicata - vedi esempio IntGenServer
sulle slide)
• dopo il controllo key.isAcceptable la socketChannel restituita dall'operazione di
accept viene registrata sul selettore (con interesse all'operazione di WRITE) e il
messaggio viene inviato quando il canale è pronto per la scrittura (key.isWritable è
true)
*/
public class EsercizioPreparatorio 
{
     final static int DEFAULT_PORT = 9999;
     static ByteBuffer bb = ByteBuffer.allocateDirect(12);
     public static void main(String[] args) throws IOException{
        int port = DEFAULT_PORT;
        if (args.length > 0)
             port = Integer.parseInt(args[0]);
         System.out.println("Server starting ... listening on port " + port);
         InetAddress ip = InetAddress.getByName("localhost"); 
         InetSocketAddress isa = new InetSocketAddress(ip, port); 
         ServerSocketChannel ssc = ServerSocketChannel.open();
         ServerSocket ss = ssc.socket();
         ss.bind(isa);
         ssc.configureBlocking(false);
         Selector s = Selector.open();
         ssc.register(s, SelectionKey.OP_ACCEPT);
         while (true){ 
        	 int n= s.select();
             if (n==0) continue;
             Iterator <SelectionKey> it = s.selectedKeys().iterator();
             while (it.hasNext()){
                  SelectionKey key = (SelectionKey) it.next();
                  if (key.isAcceptable())
                  {
                   SocketChannel sc;
                   ssc = (ServerSocketChannel) key.channel();
                   sc=ssc.accept();                  
                   if (sc == null)
                   continue;
                    System.out.println("Receiving connection");
                    bb.clear();
                    bb.put("HelloClient\n".getBytes());
                    bb.flip();
                    System.out.println("Writing message to client");
                    while (bb.hasRemaining())
                      sc.write(bb);
                      sc.close();
                     }
                   it.remove();
               }
         }
     }
}