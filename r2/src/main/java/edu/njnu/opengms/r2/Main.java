package edu.njnu.opengms.r2;

import com.mxgraph.canvas.mxICanvas2D;
import com.mxgraph.reader.mxSaxOutputHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.StringReader;

/**
 * @ClassName Main
 * @Description ass we see
 * @Author sun_liber
 * @Date 2020/5/14
 * @Version 1.0.0
 */
public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {


    }

    protected static void parseXmlSax(String xml, mxICanvas2D canvas)
            throws ParserConfigurationException, IOException, SAXException {
        // Creates SAX handler for drawing to graphics handle
        mxSaxOutputHandler handler = new mxSaxOutputHandler(canvas);

        // Creates SAX parser for handler
        XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
                .getXMLReader();
        reader.setContentHandler(handler);

        // Renders XML data into image
        reader.parse(new InputSource(new StringReader(xml)));
    }
}
