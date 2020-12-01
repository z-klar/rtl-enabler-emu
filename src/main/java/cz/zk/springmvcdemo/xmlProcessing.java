package cz.zk.springmvcdemo;


import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class xmlProcessing {


    /**********************************************************************************
     *
     * @param inputs
     * @param outputs
     * @return
     **********************************************************************************/
    public static String getCurrentState(int [] inputs, int [] outputs) {
        String sout = "";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            // suppress the "STANDALONE" attribute in the XML header. If omitted,
            // header looks like:
            // <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            doc.setXmlStandalone(true); //before creating the DOMSource
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);
            // inputs
            for(int i=0; i<2; i++) {
                Element el = doc.createElement("din");
                addAttr("id", String.format("%d", i+1) , el, doc);
                addAttr("name", "" , el, doc);
                addAttr("sts", "0" , el, doc);
                addAttr("val", String.format("%d", inputs[i]) , el, doc);
                addAttr("pic", "0" , el, doc);
                addAttr("cmo", "0" , el, doc);
                addAttr("cnt", "0" , el, doc);
                rootElement.appendChild(el);
            }
            // outputs
            for(int i=0; i<16; i++) {
                Element el = doc.createElement("dout");
                addAttr("id", String.format("%d", i+1) , el, doc);
                addAttr("name", "" , el, doc);
                addAttr("sts", "0" , el, doc);
                addAttr("val", String.format("%d", outputs[i]) , el, doc);
                addAttr("pic", "0" , el, doc);
                addAttr("mde", "0" , el, doc);
                addAttr("pars", "" , el, doc);
                rootElement.appendChild(el);
            }

            //---------------------  store to string  ------------------------
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            sout = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            return(sout);
        }
        catch(Exception ex) {
            sout = LogException(ex);
            return(sout);
        }
    }

    /**--------------------------------------------------------------------------
     *
     * @param key
     * @param val
     * @param el
     ---------------------------------------------------------------------------*/
    private static void addAttr(String key, String val, Element el, Document doc) {
        Attr attr;

        attr = doc.createAttribute(key);
        attr.setValue(val);
        el.setAttributeNode(attr);

    }

    /**=========================================================================
     *
     * @param ex
     * @return
     ==========================================================================*/
    private static String LogException(Exception ex) {
        String spom = "ERR: \r\n";

        spom +="Exception: " + ex.getMessage();
        StackTraceElement [] trace;
        int i;
        spom += ex.getClass().toString();
        spom += "Stack Trace:";
        trace = ex.getStackTrace();
        i = 0;
        for (StackTraceElement se: trace) {
            if(se.getClassName().startsWith("cz"))
                spom += String.format("Class:%s Method: %s Line: %d",
                        se.getClassName(), se.getMethodName(), se.getLineNumber());
            i++;
            if(i>400)  break;
        }
        return(spom);
    }
    /**
     *
     * @param output
     * @return
     */
    public static String getCurrentOutputState(int output) {
        String sout = "";

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            // root elements
            Document doc = docBuilder.newDocument();
            // suppress the "STANDALONE" attribute in the XML header. If omitted,
            // header looks like:
            // <?xml version="1.0" encoding="UTF-8" standalone="no"?>
            doc.setXmlStandalone(true); //before creating the DOMSource
            Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);

            Element el = doc.createElement("result");
            addAttr("val", String.format("%d", output), el, doc);
            addAttr("status", String.format("%d", 1), el, doc);
            rootElement.appendChild(el);

            //---------------------  store to string  ------------------------
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(doc), new StreamResult(writer));
            sout = writer.getBuffer().toString(); //.replaceAll("\n|\r", "");
            return(sout);
        }
        catch(Exception ex) {
            sout = LogException(ex);
            return(sout);
        }
    }


}
