package org.noamichael.java.interceptor;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Michael
 */
public class Registrar {

    private static final List<FutureProxy> FUTURE_PROXIES = new ArrayList();
    private static final Registrar CURRENT_INSTANCE = new Registrar();

    public static Registrar getCurrentInstance() {
        return CURRENT_INSTANCE;
    }

    private Registrar() {
        if (FUTURE_PROXIES.isEmpty()) {
            readProxies();
        }
    }

    private void readProxies() throws RuntimeException {
        try {
            
            File xml = new File(ClassLoader.getSystemResource("META-INF/proxies.xml").toURI());
            if (!xml.canRead()) {
                System.out.println("No proxies.xml file detected under META-INF");
            } else {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xml);
                doc.normalize();
                doc.getDocumentElement().normalize();
                
                NodeList proxies = doc.getDocumentElement().getChildNodes();
                String currentName = "";
                String currentClass = "";
                
                for (int i = 0; i < proxies.getLength(); i++) {
                    NodeList insidesProxies = proxies.item(i).getChildNodes();
                    for (int j = 0; j < insidesProxies.getLength(); j++) {
                        Node node = insidesProxies.item(j);
                        if (node.getNodeType() == Node.ELEMENT_NODE) {
                            if (node.getNodeName().equals("name")) {
                                currentName = node.getTextContent();
                            }
                            if (node.getNodeName().equals("class")) {
                                currentClass = node.getTextContent();
                            }
                        }
                        if (currentClass.length() > 0 && currentName.length() > 0) {
                            FUTURE_PROXIES.add(new FutureProxy(currentName, currentClass));
                            currentClass = "";
                            currentName = "";
                            System.out.println(FUTURE_PROXIES);
                        }
                    }
                }
            }
        } catch (URISyntaxException | ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public class FutureProxy {

        private String name;
        private String clazz;

        public FutureProxy() {
        }

        public FutureProxy(String name, String clazz) {
            this.name = name;
            this.clazz = clazz;
        }

        /**
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the clazz
         */
        public String getClazz() {
            return clazz;
        }

        /**
         * @param clazz the clazz to set
         */
        public void setClazz(String clazz) {
            this.clazz = clazz;
        }

        @Override
        public String toString() {
            return "FutureProxy{" + "name=" + name + ", clazz=" + clazz + '}';
        }

    }

}
