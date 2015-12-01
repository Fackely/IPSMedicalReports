/**
 * 
 */
package util.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.axioma.util.xml.EscrituraXmlException;
import org.axioma.util.xml.UtilidadXMLJAXB;



/**
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class UtilidadAutorizacionesEDI 
{
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(UtilidadAutorizacionesEDI.class);
	
	/**
	 * 
	 * @return
	 */
	public static boolean generarArchivoEDIInformeUrgencias(String nombreRutaArchivo,com.princetonsa.autorizaciones.informeUrgencias.EstructuraInformeUrgencias informe)
	{
		String paquete="com.princetonsa.autorizaciones.informeUrgencias";
		com.princetonsa.autorizaciones.informeUrgencias.ObjectFactory obj=new com.princetonsa.autorizaciones.informeUrgencias.ObjectFactory();
		try 
		{
			String resultado=UtilidadXMLJAXB.marshallerXml(paquete, obj.createInformeUrgencias(informe));
			FileWriter wri=new FileWriter(nombreRutaArchivo);
			wri.write(resultado);
			wri.close();
			return true;
		} 
		catch (Exception e) 
		{
			logger.error("ERROR GENERANDO ARCHIVO EDI INFORMEURGENCIAS",e);
		}
		return false;
	}


	/**
	 * 
	 * @return
	 */
	public static boolean generarArchivoEDIInformePresuntaInconsistencia(String nombreRutaArchivo,com.princetonsa.autorizaciones.informePresuntaInconsistencia.EstructuraInformeUrgencias informe)
	{
		String paquete="com.princetonsa.autorizaciones.informePresuntaInconsistencia";
		com.princetonsa.autorizaciones.informePresuntaInconsistencia.ObjectFactory obj=new com.princetonsa.autorizaciones.informePresuntaInconsistencia.ObjectFactory();
		try 
		{
			String resultado=UtilidadXMLJAXB.marshallerXml(paquete, obj.createInformePresuntaInconsistencia(informe));
			FileWriter wri=new FileWriter(nombreRutaArchivo);
			wri.write(resultado);
			wri.close();
			return true;
		} 
		catch (Exception e) 
		{
			logger.error("ERROR GENERANDO ARCHIVO EDI InformePresuntaInconsistencia",e);
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean generarArchivoEDISolicitudAutorizacionServicios(String nombreRutaArchivo,com.princetonsa.autorizaciones.solicitudAutorizacionServicios.EstructuraSolicitudAutorizacion informe)
	{
		String paquete="com.princetonsa.autorizaciones.solicitudAutorizacionServicios";
		com.princetonsa.autorizaciones.solicitudAutorizacionServicios.ObjectFactory obj=new com.princetonsa.autorizaciones.solicitudAutorizacionServicios.ObjectFactory();
		try 
		{
			String resultado=UtilidadXMLJAXB.marshallerXml(paquete, obj.createSolicitudAutorizacionServicios(informe));
			FileWriter wri=new FileWriter(nombreRutaArchivo);
			wri.write(resultado);
			wri.close();
			return true;
		} 
		catch (Exception e) 
		{
			logger.error("ERROR GENERANDO ARCHIVO EDI InformePresuntaInconsistencia",e);
		}
		return false;
	}

}
