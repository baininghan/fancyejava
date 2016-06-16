package com.fancye.schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 通过schema校验XML文件
 * 
 * @author Fancye
 *
 */
public class XmlSchemaValidationHelper {

	/**
	 * @param args
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void main(String[] args) {

	}

	/**
	 * sample example.
	 * @return
	 */
	public boolean validarXML() {
		boolean correcto = false;
		// http://www.w3.org/2001/XMLSchema
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			Schema schema = schemaFactory.newSchema(new File(""));
			Validator validator = schema.newValidator();

			validator.validate(new StreamSource(""));

			correcto = true;
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return correcto;
	}

	public void validateAgainstSchema(File xmlFile, File xsdFile) {
		try {
			// parse an XML document into a DOM tree
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder parser = builderFactory.newDocumentBuilder();
			Document document = parser.parse(xmlFile);

			// create a SchemaFactory capable of understanding WXS schemas
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance
			Schema schema = factory.newSchema(new StreamSource(xsdFile));

			// create a Validator instance, which can be used to validate an
			// instance document
			Validator validator = schema.newValidator();

			// validate the DOM tree
			validator.validate(new DOMSource(document));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void validateAgainstSchema(String xml, File xsdFile) {
		try {
			// parse an XML document into a DOM tree 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder parser = builderFactory.newDocumentBuilder();
			Document document = parser.parse(xml);

			// create a SchemaFactory capable of understanding WXS schemas 
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance 
			Source schemaFile = new StreamSource(xsdFile);
			Schema schema = factory.newSchema(schemaFile);

			// create a Validator instance, which can be used to validate an 
			// instance document 
			Validator validator = schema.newValidator();

			// validate the DOM tree 
			validator.validate(new DOMSource(document));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void validateAgainstSchema(String xml, InputStream xsdFile) {
		try {
			// parse an XML document into a DOM tree 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder parser = builderFactory.newDocumentBuilder();
			Document document = parser.parse(new InputSource(new StringReader(xml)));

			// create a SchemaFactory capable of understanding WXS schemas 
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance 
			Source schemaFile = new StreamSource(xsdFile);
			Schema schema = factory.newSchema(schemaFile);

			// create a Validator instance, which can be used to validate an 
			// instance document 
			Validator validator = schema.newValidator();

			// validate the DOM tree 
			validator.validate(new DOMSource(document));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void validateAgainstSchema(File xmlFile, InputStream xsdFile) {
		try {
			// parse an XML document into a DOM tree 
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory
					.newInstance();
			builderFactory.setNamespaceAware(true);
			DocumentBuilder parser = builderFactory.newDocumentBuilder();
			// parser.isNamespaceAware(); 
			Document document = parser.parse(xmlFile);

			// create a SchemaFactory capable of understanding WXS schemas 
			SchemaFactory factory = SchemaFactory
					.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

			// load a WXS schema, represented by a Schema instance 
			Source schemaFile = new StreamSource(xsdFile);
			Schema schema = factory.newSchema(schemaFile);

			// create a Validator instance, which can be used to validate an 
			// instance document
			Validator validator = schema.newValidator();

			// validate the DOM tree
			validator.validate(new DOMSource(document));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
