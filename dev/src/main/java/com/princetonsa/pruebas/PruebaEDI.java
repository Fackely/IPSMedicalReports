/**
 * 
 */
package com.princetonsa.pruebas;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import util.xml.UtilidadAutorizacionesEDI;

/**
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class PruebaEDI {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		generarInformeUrgencias();
		generarInformePresuntaInconsistencia();
		generarSolicitudAutorizacionServicios();
	}

	

	private static void generarInformeUrgencias() 
	{
		com.princetonsa.autorizaciones.informeUrgencias.EstructuraInformeUrgencias informe=new com.princetonsa.autorizaciones.informeUrgencias.EstructuraInformeUrgencias();
		com.princetonsa.autorizaciones.informeUrgencias.General general=new com.princetonsa.autorizaciones.informeUrgencias.General();
		XMLGregorianCalendar fecha;
		try 
		{
			fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			fecha.setDay(25);
			fecha.setMonth(8);
			fecha.setYear(2009);
			informe.setCoberturaSalud("VALOR DE LA COBERTURA");
			informe.setFechaIngreso(fecha);
			general.setNumero(1);
			general.setFecha(fecha);
			informe.setGeneral(general);
			UtilidadAutorizacionesEDI.generarArchivoEDIInformeUrgencias("/home/axioma/pruebaEDIInformeUrgencias.xml", informe);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}

	private static void generarInformePresuntaInconsistencia() 
	{
		com.princetonsa.autorizaciones.informePresuntaInconsistencia.EstructuraInformeUrgencias informe=new com.princetonsa.autorizaciones.informePresuntaInconsistencia.EstructuraInformeUrgencias();
		com.princetonsa.autorizaciones.informePresuntaInconsistencia.General general=new com.princetonsa.autorizaciones.informePresuntaInconsistencia.General();
		XMLGregorianCalendar fecha;
		try 
		{
			fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			fecha.setDay(25);
			fecha.setMonth(8);
			fecha.setYear(2009);
			informe.setCoberturaSalud("VALOR DE LA COBERTURA");
			general.setNumero(1);
			general.setFecha(fecha);
			informe.setGeneral(general);
			UtilidadAutorizacionesEDI.generarArchivoEDIInformePresuntaInconsistencia("/home/axioma/pruebaEDIInformePresuntaInconsistencia.xml", informe);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	

	private static void generarSolicitudAutorizacionServicios() 
	{
		com.princetonsa.autorizaciones.solicitudAutorizacionServicios.EstructuraSolicitudAutorizacion informe=new com.princetonsa.autorizaciones.solicitudAutorizacionServicios.EstructuraSolicitudAutorizacion();
		com.princetonsa.autorizaciones.solicitudAutorizacionServicios.General general=new com.princetonsa.autorizaciones.solicitudAutorizacionServicios.General();
		XMLGregorianCalendar fecha;
		try 
		{
			fecha = DatatypeFactory.newInstance().newXMLGregorianCalendar();
			fecha.setDay(25);
			fecha.setMonth(8);
			fecha.setYear(2009);
			informe.setCoberturaSalud("VALOR DE LA COBERTURA");
			general.setFecha(fecha);
			informe.setGeneral(general);
			UtilidadAutorizacionesEDI.generarArchivoEDISolicitudAutorizacionServicios("/home/axioma/pruebaEDISolicitudAutorizacionServicios.xml", informe);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
