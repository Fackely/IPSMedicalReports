package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.ConsultaCostoArticulosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaCostoArticulosDao;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class ConsultaCostoArticulos
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ConsultaCostoArticulosDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public ConsultaCostoArticulos()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * @param tipoBD
	 * @return
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getConsultaCostoArticulosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}

	/**
	 * Método para armar las condiciones de la
	 * consulta para ser mandada al BIRT y poder
	 * ejecutar el reporte 
	 * @param con
	 * @param forma
	 * @return
	 */
	public String consultarCondicionesCostoArticulos(Connection con, ConsultaCostoArticulosForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("codAlmacen", forma.getAlmacen());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("articulo", forma.getCodArticulo());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaCostoArticulosDao().consultarCondicionesCostoArticulos(con, criterios);
	}

	/**
	 * Método encargado de ejecutar la consulta 
	 * de Costo de Artículos para organizar los 
	 * resultados de la misma en un StringBuffer
	 * y mostrarlos en un archivo plano
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarCostoArticulos(Connection con, ConsultaCostoArticulosForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("codAlmacen", forma.getAlmacen());
		criterios.put("clase", forma.getClase());
		criterios.put("grupo", forma.getGrupo());
		criterios.put("subGrupo", forma.getSubGrupo());
		criterios.put("articulo", forma.getCodArticulo());
		//Se valida que tipo de Código se escogio para la búsqueda
		if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
        	criterios.put("tipoCodigo", "1");
		else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
        	criterios.put("tipoCodigo", "2");
        else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
        	criterios.put("tipoCodigo", "3");
        return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaCostoArticulosDao().consultarCostoArticulos(con, criterios);
	}

	/**
	 * Método que organiza los datos de la Consulta
	 * de Costo de Artículos para ser impresos en un
	 * archivo plano con extensión .csv
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaCostoArticulos(HashMap mapa, String nombreReporte, String encabezado, String usuario)
	{
		StringBuffer datos = new StringBuffer();
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
			datos.append(mapa.get("codigoarticulo_"+i)+", "+mapa.get("descripcionarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+mapa.get("nomalmacen_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+""))+"\n");
		
		return datos;
	}
	
}