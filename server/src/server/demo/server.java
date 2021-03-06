package server.demo;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;

import org.json.*;
import java.util.*;

public class server extends Thread {
    public static void main(String[] args) {
        Thread t = new server();
        t.run();

    }

    void writeXMLFile(String data, String fileName) throws DocumentException, IOException {

        Document dom = DocumentHelper.parseText(data);
//
        OutputFormat format = new OutputFormat();
        format.setIndentSize(4);  // 行缩进
        format.setNewlines(true); // 一个结点为一行
        format.setTrimText(true); // 去重空格
        format.setPadText(true);
        format.setNewLineAfterDeclaration(false); // 放置xml文件中第二行为空白行

        Writer out = new PrintWriter(fileName, "utf-8");
        XMLWriter xwriter = new XMLWriter(out, format);

        xwriter.write(dom);

        out.close();
        xwriter.close();
    }

    /**
     * 往json文件中写入数据
     * @param jsonPath json文件路径
     * @param inMap Map类型数据
     * @param flag 写入状态，true表示在文件中追加数据，false表示覆盖文件数据
     * @return 写入文件状态  成功或失败
     */
    public String writeJson(String jsonPath, String jsondata, boolean flag) {
        // Map数据转化为Json，再转换为String
//        String data = new JSONObject(inMap).toString();
        File jsonFile = new File(jsonPath);
        try {
            // 文件不存在就创建文件
            if (!jsonFile.exists()) {
                jsonFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(jsonFile.getAbsoluteFile(), flag);
            BufferedWriter bw = new BufferedWriter(fileWriter);
            bw.write(jsondata);
            bw.close();
            return "success";
        } catch (IOException e) {
            return "error";
        }
    }


    public void run() {
        int serverPort = 7899;
        ServerSocket listenSocket;
        Socket clientSocket;
        try {
//				******************接收读取客户端传来的内容******************************************
            listenSocket = new ServerSocket(serverPort);
            clientSocket = listenSocket.accept();

            DataInputStream input = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());


            String data = null;
            while (!clientSocket.isClosed()) {

                System.out.println("准备接受");
                data = input.readUTF();
                System.out.println(data);
                JSONObject xmlJSONObj = XML.toJSONObject(data);
                //设置缩进
                String jsonPrettyPrintString = xmlJSONObj.toString(4);
                //输出格式化后的json
                System.out.println(jsonPrettyPrintString);
//                writeXMLFile(data, "D:serverP.xml");
                writeJson("D:serverP.txt",jsonPrettyPrintString,false);

                System.out.println("准备写入流");
                output.writeUTF(data);
                output.flush();
                System.out.println("写入流OK");

            }

//             回包


//            clientOut.write("abcdefg".getBytes("UTF-8"));
//            clientOut.close();

//					******************向客户端写xml文件******************************************
//            DataOutputStream out=new DataOutputStream(clientSocket.getOutputStream());
//            // 定义工厂 API，使应用程序能够从 XML 文档获取生成 DOM 对象树的解析器
//            DocumentBuilderFactory factory = DocumentBuilderFactory
//                    .newInstance();
//            // 定义 API， 使其从 XML 文档获取 DOM 文档实例。使用此类，应用程序员可以从 XML 获取一个 Document
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            // Document 接口表示整个 HTML 或 XML 文档。从概念上讲，它是文档树的根，并提供对文档数据的基本访问
//            Document document = builder.newDocument();
//            //组织生产xml文件内容
//            Element root = document.createElement("persons");
//            document.appendChild(root);
//            Element person = document.createElement("person");
//            Element name = document.createElement("name");
//            name.appendChild(document.createTextNode("java小强"));
//            person.appendChild(name);
//            Element sex = document.createElement("sex");
//            sex.appendChild(document.createTextNode("man"));
//            person.appendChild(sex);
//            Element age = document.createElement("age");
//            age.appendChild(document.createTextNode("99"));
//            person.appendChild(age);
//            root.appendChild(person);
//
//            TransformerFactory tf = TransformerFactory.newInstance();
//            // 此抽象类的实例能够将源树转换为结果树
//            Transformer transformer;
//            transformer = tf.newTransformer();
//
//            DOMSource source = new DOMSource(document);
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            transformer.transform(source, new StreamResult(bos));
//            String xmlStr = bos.toString();
//            out.writeUTF(xmlStr);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}