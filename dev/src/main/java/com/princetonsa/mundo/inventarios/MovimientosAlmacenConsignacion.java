package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.action.inventarios.MovimientosAlmacenConsignacionAction;
import com.princetonsa.actionform.inventarios.MovimientosAlmacenConsignacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MovimientosAlmacenConsignacionDao;
import com.princetonsa.mundo.InstitucionBasica;

/**
 * Anexo 684
 * Creado el 9 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class MovimientosAlmacenConsignacion 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(MovimientosAlmacenConsignacionAction.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private MovimientosAlmacenConsignacionDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public MovimientosAlmacenConsignacion()
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
			objetoDao=myFactory.getMovimientosAlmacenConsignacionDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * Método consultar transacciones
	 * @param con
	 * @return
	 */
	public static HashMap consultarTransacciones(Connection con, int codInstitucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenConsignacionDao().consultarTransacciones(con, codInstitucion);
	}
	
	/**
	 * Método consultar transacciones
	 * @param con
	 * @return
	 */
	public static HashMap consultarProveedores(Connection con, String proveedor) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenConsignacionDao().consultarProveedores(con, proveedor);
	}

	/**
	 * Método que consulta los movimientos de almacenes
	 * y devuelve los datos en un HashMap
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarMovimientos(Connection con, MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, HashMap mapa) 
    {
		logger.info("===> Entré a consultarMovimientosAlmacenConsignacion");
    	HashMap criterios = new HashMap();
    	String codigoTransacciones = this.tiposTransaccionesEscogidas(con, forma);
    	criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
    	criterios.put("almacen", forma.getAlmacen());
    	criterios.put("nit", forma.getNit());
    	criterios.put("proveedor", forma.getProveedor());
    	criterios.put("fechaInicial", forma.getFechaInicial());
    	criterios.put("fechaFinal", forma.getFechaFinal());
    	criterios.put("tiposTransacciones", codigoTransacciones);
    	criterios.put("numIngreso", forma.getNumIngreso());
    	criterios.put("tipoCodigoArticulo", forma.getTipoCodigoArticulo());
    	criterios.put("tipoReporte", forma.getTipoReporte());
    	
    	logger.info("===> Aquí voy a imprimir el mapa...");
    	Utilidades.imprimirMapa(criterios);
 	
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenConsignacionDao().consultarMovimientos(con, forma, mundo, criterios);
	}

	/**
	 * Método que permite organizar en un string separado por coma
	 * los tipo de transaccion escogidas para realizar el filtrado en 
	 * la consulta
	 * @param con
	 * @param forma
	 * @return codigoTransacciones
	 */
	public String tiposTransaccionesEscogidas(Connection con, MovimientosAlmacenConsignacionForm forma)
	{
		String codigoTransacciones = "";
		//Recorremos la cadena de string para visualizar y separar que tipo de solicitudes se tomaron para hacer el filtrado
		int numReg = forma.getTipoTransaccionSeleccionado().length;
		if (numReg == 1)
		{
			if (!(forma.getTipoTransaccionSeleccionado()[0]).equals(""))
				codigoTransacciones = "'"+forma.getTipoTransaccionSeleccionado()[0]+"'";
		}
		else
		{
			if (numReg > 1)
			{
				for (int i=0; i<numReg; i++)
				{
					if (i == 0)
						codigoTransacciones = "'"+forma.getTipoTransaccionSeleccionado()[i]+"'";
					else
						codigoTransacciones += ",'"+forma.getTipoTransaccionSeleccionado()[i]+"'";
				}
			}
		}
		return codigoTransacciones;
	}
	
	/**
	 * Método que organiza los datos de la consulta Movimiento Almacen Consignacion
	 * para el tipo reporte: Detallado Almacén por Proveedor
	 * Destino: archivo plano .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return datos
	 */
	public StringBuffer cargarMapaDetalladoAlmacenXProveedor(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario, InstitucionBasica institucion)
	{
		StringBuffer datos = new StringBuffer();
		double totalCant = 0, totalValor = 0;
		System.gc();
		//Razon social institucion
		datos.append(institucion.getRazonSocial()+"\n");
		//Nit
		datos.append(institucion.getNit()+"\n");
		//Direccion
		datos.append(institucion.getDireccion()+"\n");
		//Telefono
		datos.append(institucion.getTelefono()+"\n");
		//Titulo del reporte
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		logger.info("===> Aquí vamos a imprimir el mapa");
		Utilidades.imprimirMapa(mapa);
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(
					mapa.get("nit_"+i)+","+
					mapa.get("nit_"+i)+","+
					//mapa.get("proveedor_"+i)+","+
					mapa.get("almacenpropiedad_"+i)+","+
					mapa.get("almacenconsignacion_"+i)+","+
					mapa.get("nrotransaccion_"+i)+","+
					mapa.get("codart_"+i)+","+
					mapa.get("articulo_"+i)+","+
					mapa.get("umed_"+i)+","+
					mapa.get("cant_"+i)+","+
					mapa.get("valunid_"+i)+","+
					mapa.get("valtotal_"+i)+"\n"
					);
			totalCant += Utilidades.convertirAEntero(mapa.get("cant_"+i)+"");
			totalValor += Utilidades.convertirAEntero(mapa.get("valtotal_"+i)+"");
		}
		/*
		 * El proveedor aún no se logra obtener en la vista, se espera modificación para poder mostrarlo
		 * por eso se repite el nit
		 */
		datos.append("Total Proveedor, " +totalCant+","+totalValor+"\n");
		return datos;
	}
	
	/**
	 * Método que organiza los datos de la consulta Movimiento Almacen Consignacion
	 * para el tipo reporte: Detallado Paciente por Proveedor
	 * Destino: archivo plano .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return datos
	 */
	public StringBuffer cargarMapaDetalladoPacienteXProveedor(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario, InstitucionBasica institucion)
	{
		StringBuffer datos = new StringBuffer();
		double totalCant = 0, totalValor = 0;
		System.gc();
		//Razon social institucion
		datos.append(institucion.getRazonSocial()+"\n");
		//Nit
		datos.append(institucion.getNit()+"\n");
		//Direccion
		datos.append(institucion.getDireccion()+"\n");
		//Telefono
		datos.append(institucion.getTelefono()+"\n");
		//Titulo del reporte
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		logger.info("===> Aquí vamos a imprimir el mapa");
		Utilidades.imprimirMapa(mapa);
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			datos.append(
					mapa.get("nroingreso_"+i)+","+
					mapa.get("paciente_"+i)+","+
					mapa.get("almacen_"+i)+","+
					mapa.get("nrotransaccion_"+i)+","+
					mapa.get("nit_"+i)+","+
					//mapa.get("proveedor_"+i)+","+
					mapa.get("nit_"+i)+","+
					mapa.get("codart_"+i)+","+
					mapa.get("articulo_"+i)+","+
					mapa.get("umed_"+i)+","+
					mapa.get("cant_"+i)+","+
					mapa.get("valunid_"+i)+","+
					mapa.get("valtotal_"+i)+"\n"
					
					);
			totalCant += Utilidades.convertirAEntero(mapa.get("cant_"+i)+"");
			totalValor += Utilidades.convertirAEntero(mapa.get("valtotal_"+i)+"");
		}
		/*
		 * El proveedor aún no se logra obtener en la vista, se espera modificación para poder mostrarlo
		 * Por eso se repite el nit
		 */
		datos.append("Total Proveedor, " +totalCant+","+totalValor+"\n");
		return datos;
	}
}