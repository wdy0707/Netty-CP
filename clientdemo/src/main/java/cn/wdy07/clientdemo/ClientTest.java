package cn.wdy07.clientdemo;

import cn.wdy07.client.CPClient;
import cn.wdy07.constmodle.NetConfig;

import java.util.Scanner;

/**
 * @author wdy
 * @create 2020-06-21 14:47
 */
public class ClientTest {

    public static void main(String[] args) throws Exception{
        CPClient cpClient = new CPClient(NetConfig.REMOTE_IP, NetConfig.REMOTE_PORT);
        new Thread(cpClient).start();
        //等待连接成功notify用户线程
        while (!cpClient.isConnected()){
            synchronized (cpClient){
                cpClient.wait();
            }
        }
        System.out.println("连接成功---->");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String msg = scanner.next();
            if(msg == null) continue;
            else if("q".equals(msg.toLowerCase())){
                cpClient.close();
                while (cpClient.isConnected()){
                    synchronized (cpClient){
                        cpClient.wait();
                    }
                }
                scanner.close();
                System.exit(0);
            }else{
                cpClient.sengMsg(msg);
            }

        }

    }
}
