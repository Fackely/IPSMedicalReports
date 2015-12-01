package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.inventarios.MovimientosAlmacenesForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.MovimientosAlmacenesDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jaramillo
 */
public class MovimientosAlmacenes 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private MovimientosAlmacenesDao objetoDao;
	
	/**
	 * Metodo Reset
	 */
	public MovimientosAlmacenes()
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
			objetoDao=myFactory.getMovimientosAlmacenesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap consultarTransacciones(Connection con, String entradaSalida) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenesDao().consultarTransacciones(con, entradaSalida);
	}

	/**
	 * Método que consulta los movimientos de almacenes
	 * y devuelve los datos en un HashMap
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarMovimientos(Connection con, MovimientosAlmacenesForm forma) 
    {
    	HashMap criterios = new HashMap();
    	String codigoTransacciones = this.tiposTransaccionesEscogidas(con, forma);
    	criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
    	criterios.put("almacen", forma.getAlmacen());
    	criterios.put("tiposTransacciones", codigoTransacciones);
    	criterios.put("indicativoES", forma.getIndicativoES());
    	criterios.put("transaccionInicial", forma.getTransaccionInicial());
    	criterios.put("transaccionFinal", forma.getTransaccionFinal());
    	criterios.put("fechaInicial", forma.getFechaInicial());
    	criterios.put("fechaFinal", forma.getFechaFinal());
    	criterios.put("tipoCodigoArticulo", forma.getTipoCodigoArticulo());
    	criterios.put("tipoReporte", forma.getTipoReporte());
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenesDao().consultarMovimientos(con, criterios);
	}

	/**
	 * Método que permite organizar en un string separado por coma
	 * los tipo de transaccion escogidas para realizar el filtrado en 
	 * la consulta
	 * @param con
	 * @param forma
	 * @return
	 */
	public String tiposTransaccionesEscogidas(Connection con, MovimientosAlmacenesForm forma)
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
	 * Método que organiza los datos de la consulta Movimientos Almacenes
	 * para el tipo de reporte Detallado por Tipo de Transaccion para ser
	 * exportados a un archivo plano con extensión .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaDetalladoTipoTransaccion(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
	{
		StringBuffer datos = new StringBuffer();
		int cantPorAlmacen = 0, cantPorTransaccion = 0;
		double totalPorAlmacen = 0, totalPorTransaccion = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");

		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("descptipoconcepto_"+i)+"\n");
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando la cantidad total y el valor total por almacén
				cantPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorAlmacen = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando la cantidad total y el valor total por tipo de transacción
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
					datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			 	}
			}
			//Si el tipo de transacción viene igual se deben de mostrar los otros datos sin mostrar el tipo de transacción
			else if ((mapa.get("tipoconceptoinv_"+i)+"").equals(mapa.get("tipoconceptoinv_"+(i-1))+""))
			{
				//Si el almacén viene igual se colocan los otros datos sin el tipo de transacción y sin el almacén
				if ((mapa.get("codalmacen_"+i)+"").equals(mapa.get("codalmacen_"+(i-1))+""))
				{
					datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
					//Voy sumando la cantidad total y el valor total por almacén
					cantPorAlmacen = cantPorAlmacen + Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
					totalPorAlmacen = totalPorAlmacen + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Voy sumando la cantidad total y el valor total por tipo de transacción
					cantPorTransaccion = cantPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
					totalPorTransaccion = totalPorTransaccion + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Si no existen mas registros se muestran los totales respectivos
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				 	{
						datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
						datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
				 	}
				}
				//Si el almacén viene diferente se muestra el almacén nuevo más los otros datos
				else
				{
					datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
					datos.append(mapa.get("nomalmacen_"+i)+"\n");
					datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
					//Asigno al contador por almacén el nuevo valor
					cantPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
					totalPorAlmacen = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Voy sumando la cantidad total y el valor total por tipo de transacción
					cantPorTransaccion = cantPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
					totalPorTransaccion = totalPorTransaccion + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Si no existen mas registros se muestran los totales respectivos
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				 	{
						datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
						datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
				 	}
				}
			}
			//Si el tipo de transacción viene diferente. Se muestra un nuevo registro con toda la información
			else
			{
				datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
				datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
				//Asigno al contador por almacén el nuevo valor
				cantPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorAlmacen = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Asigno al contador por tipo de transacción el nuevo valor
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				datos.append(mapa.get("descptipoconcepto_"+i)+"\n");
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Subtotal por Almacén, "+cantPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalPorAlmacen)+"\n");
					datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			 	}
			}
		}
		
		return datos;
	}

	/**
	 * Método que organiza los datos de la consulta Movimientos Almacenes
	 * para el tipo de reporte Detallado Proveedor por Tipo de Transaccion para ser
	 * exportados a un archivo plano con extensión .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaDetalladoProveedorTipoTransaccion(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
	{
		StringBuffer datos = new StringBuffer();
		int cantPorTransaccion = 0;
		double totalPorTransaccion = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codalmacen_"+i)+", "+mapa.get("codalmacendestino_"+i)+", "+mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("codigoarticulo_"+i)+" "+mapa.get("proveedor_"+i)+", "+mapa.get("desarticulo_"+i)+" "+mapa.get("razonsocial_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando la cantidad total y el valor total por tipo de transacción
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 		datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
			//Si el tipo de transacción viene igual se deben de mostrar los otros datos sin mostrar el tipo de transacción
			else if ((mapa.get("codigotransaccion_"+i)+"").equals(mapa.get("codigotransaccion_"+(i-1))+""))
			{
				datos.append(mapa.get("codalmacen_"+i)+", "+mapa.get("codalmacendestino_"+i)+", "+mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("codigoarticulo_"+i)+" "+mapa.get("proveedor_"+i)+", "+mapa.get("desarticulo_"+i)+" "+mapa.get("razonsocial_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando la cantidad total y el valor total por tipo de transacción
				cantPorTransaccion = cantPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorTransaccion = totalPorTransaccion + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 		datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
			//Si el tipo de transacción viene diferente. Se muestra un nuevo registro con toda la información
			else
			{
				datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
				//Asigno al contador por tipo de transacción el nuevo valor
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codalmacen_"+i)+", "+mapa.get("codalmacendestino_"+i)+", "+mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("codigoarticulo_"+i)+" "+mapa.get("proveedor_"+i)+", "+mapa.get("desarticulo_"+i)+" "+mapa.get("razonsocial_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
					datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
			
		}	
		
		return datos;
	}

	/**
	 * Método que organiza los datos de la consulta Movimientos Almacenes
	 * para el tipo de reporte Totalizado por Almacén y Tipo de Transacción para ser
	 * exportados a un archivo plano con extensión .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaTotalizadoAlmacenTipoTransaccion(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
	{
		StringBuffer datos = new StringBuffer();
		int cantPorTransaccion = 0;
		double totalPorTransaccion = 0, totalUnitarioPorTransaccion = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("proveedor_"+i)+", "+mapa.get("razonsocial_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando la cantidad total y el valor total por tipo de transacción
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalUnitarioPorTransaccion = Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 		datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalUnitarioPorTransaccion)+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
			//Si el tipo de transacción viene igual se deben de mostrar los otros datos sin mostrar el tipo de transacción
			else if ((mapa.get("codigotransaccion_"+i)+"").equals(mapa.get("codigotransaccion_"+(i-1))+""))
			{
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("proveedor_"+i)+", "+mapa.get("razonsocial_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando la cantidad total y el valor total por tipo de transacción
				cantPorTransaccion = cantPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalUnitarioPorTransaccion = totalUnitarioPorTransaccion + Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+"");
				totalPorTransaccion = totalPorTransaccion + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 		datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalUnitarioPorTransaccion)+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
			//Si el tipo de transacción viene diferente. Se muestra un nuevo registro con toda la información
			else
			{
				datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalUnitarioPorTransaccion)+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
				//Asigno al contador por tipo de transacción el nuevo valor
				cantPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidad_"+i)+"");
				totalUnitarioPorTransaccion = Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+"");
				totalPorTransaccion = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("proveedor_"+i)+", "+mapa.get("razonsocial_"+i)+", "+mapa.get("usuario_"+i)+", "+mapa.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 		datos.append("Total por Tipo de Transacción, "+cantPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalUnitarioPorTransaccion)+", "+UtilidadTexto.formatearExponenciales(totalPorTransaccion)+"\n");
			}
		}
		
		return datos;
	}
	
	/**
	 * Método que organiza los datos de la consulta Movimientos Almacenes
	 * para el tipo de reporte Entradas y Salidas por Almacén y Tipo de Transacción
	 * para ser exportados a un archivo plano con extensión .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaEntradasSalidasTotalizadas(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
	{
		StringBuffer datos = new StringBuffer();
		int cantEntradasPorTransaccion = 0, cantEntradasPorAlmacen = 0, cantEntradasGeneral = 0;
		int cantSalidasPorTransaccion = 0, cantSalidasPorAlmacen = 0, cantSalidasGeneral = 0;
		double totalEntradasPorTransaccion = 0, totalEntradasPorAlmacen = 0, totalEntradasGeneral = 0;
		double totalSalidasPorTransaccion = 0, totalSalidasPorAlmacen = 0, totalSalidasGeneral = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("cantidadentrada_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+""))+", "+mapa.get("cantidadsalida_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+""))+"\n");
				//Voy sumando la cantidad de entradas y salidas y el valor por el rompimiento por Tipo de Transacción
				cantEntradasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Voy sumando la cantidad de entradas y salidas y el valor por el rompimiento por Almacén
				cantEntradasPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasPorAlmacen = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasPorAlmacen = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Voy sumando la cantidad de entradas y salidas y el valor general
				cantEntradasGeneral = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasGeneral = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasGeneral = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasGeneral = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				{
					datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
					datos.append("Subtotal por Almacén, "+cantEntradasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorAlmacen)+", "+cantSalidasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorAlmacen)+"\n");
					datos.append("Total General, "+cantEntradasGeneral+", "+UtilidadTexto.formatearExponenciales(totalEntradasGeneral)+", "+cantSalidasGeneral+", "+UtilidadTexto.formatearExponenciales(totalSalidasGeneral)+"\n");
				}
			}
			//Si el almacén viene igual se colocan los otros datos sin el almacén
			else if ((mapa.get("codalmacen_"+i)+"").equals(mapa.get("codalmacen_"+(i-1))+""))
			{
				//Si el tipo de transacción viene igual se deben de mostrar los otros datos sin mostrar el tipo de transacción y sin mostrar el almacén
				if ((mapa.get("codigotransaccion_"+i)+"").equals(mapa.get("codigotransaccion_"+(i-1))+""))
				{
					datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("cantidadentrada_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+""))+", "+mapa.get("cantidadsalida_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+""))+"\n");
					//Voy sumando la cantidad de entradas y salidas y el valor por el rompimiento por Tipo de Transacción
					cantEntradasPorTransaccion = cantEntradasPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasPorTransaccion = totalEntradasPorTransaccion + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasPorTransaccion = cantSalidasPorTransaccion + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasPorTransaccion = totalSalidasPorTransaccion + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Voy sumando la cantidad de entradas y salidas y el valor por el rompimiento por Almacén
					cantEntradasPorAlmacen = cantEntradasPorAlmacen + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasPorAlmacen = totalEntradasPorAlmacen + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasPorAlmacen = cantSalidasPorAlmacen + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasPorAlmacen = totalSalidasPorAlmacen + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Voy sumando la cantidad de entradas y salidas y el valor general
					cantEntradasGeneral = cantEntradasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasGeneral = totalEntradasGeneral + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasGeneral = cantSalidasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasGeneral = totalSalidasGeneral + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Si no existen mas registros se muestran los totales respectivos
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				 	{
						datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
						datos.append("Subtotal por Almacén, "+cantEntradasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorAlmacen)+", "+cantSalidasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorAlmacen)+"\n");
						datos.append("Total General, "+cantEntradasGeneral+", "+UtilidadTexto.formatearExponenciales(totalEntradasGeneral)+", "+cantSalidasGeneral+", "+UtilidadTexto.formatearExponenciales(totalSalidasGeneral)+"\n");
				 	}
				}
				//Si el tipo de transacción viene diferente se muestra el nuevo registro con el tipo de transacción nuevo
				else
				{
					datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
					datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
					datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("cantidadentrada_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+""))+", "+mapa.get("cantidadsalida_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+""))+"\n");
					//Asigno al contador por tipo de transacción con el nuevo valor
					cantEntradasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Voy sumando la cantidad de entradas y salidas y el valor por el rompimiento por Almacén
					cantEntradasPorAlmacen = cantEntradasPorAlmacen + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasPorAlmacen = totalEntradasPorAlmacen + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasPorAlmacen = cantSalidasPorAlmacen + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasPorAlmacen = totalSalidasPorAlmacen + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Voy sumando la cantidad de entradas y salidas y el valor general
					cantEntradasGeneral = cantEntradasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
					totalEntradasGeneral = totalEntradasGeneral + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
					cantSalidasGeneral = cantSalidasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
					totalSalidasGeneral = totalSalidasGeneral + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
					//Si no existen mas registros se muestran los totales respectivos
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				 	{
						datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
						datos.append("Subtotal por Almacén, "+cantEntradasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorAlmacen)+", "+cantSalidasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorAlmacen)+"\n");
						datos.append("Total General, "+cantEntradasGeneral+", "+UtilidadTexto.formatearExponenciales(totalEntradasGeneral)+", "+cantSalidasGeneral+", "+UtilidadTexto.formatearExponenciales(totalSalidasGeneral)+"\n");
				 	}
				}
			}
			//Si el almacén viene diferente se totaliza el registro anterior y se muestra una nueva línea con los nuevos datos
			else
			{
				datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
				datos.append("Subtotal por Almacén, "+cantEntradasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorAlmacen)+", "+cantSalidasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorAlmacen)+"\n");
				//Asigno al contador por almacén el nuevo valor
				cantEntradasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasPorTransaccion = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasPorTransaccion = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Asigno al contador por tipo de transacción el nuevo valor
				cantEntradasPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasPorAlmacen = Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasPorAlmacen = Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasPorAlmacen = Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Voy sumando el valor general para luego ser impreso después de salir del ciclo del FOR
				cantEntradasGeneral = cantEntradasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadentrada_"+i)+"");
				totalEntradasGeneral = totalEntradasGeneral + Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+"");
				cantSalidasGeneral = cantSalidasGeneral + Utilidades.convertirAEntero(mapa.get("cantidadsalida_"+i)+"");
				totalSalidasGeneral = totalSalidasGeneral + Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+"");
				//Imprimo el nuevo registro
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codigotransaccion_"+i)+" - "+mapa.get("destransaccion_"+i)+"\n");
				datos.append(mapa.get("codigomovimiento_"+i)+", "+mapa.get("fechacierre_"+i)+", "+mapa.get("cantidadentrada_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorentrada_"+i)+""))+", "+mapa.get("cantidadsalida_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorsalida_"+i)+""))+"\n");
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Subtotal por Tipo de Transacción, "+cantEntradasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorTransaccion)+", "+cantSalidasPorTransaccion+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorTransaccion)+"\n");
					datos.append("Subtotal por Almacén, "+cantEntradasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalEntradasPorAlmacen)+", "+cantSalidasPorAlmacen+", "+UtilidadTexto.formatearExponenciales(totalSalidasPorAlmacen)+"\n");
					datos.append("Total General, "+cantEntradasGeneral+", "+UtilidadTexto.formatearExponenciales(totalEntradasGeneral)+", "+cantSalidasGeneral+", "+UtilidadTexto.formatearExponenciales(totalSalidasGeneral)+"\n");
			 	}
			}
		}
		
		return datos;
	}

	/**
	 * Método que organiza los datos de la consulta Movimientos Almacenes
	 * para el tipo de reporte Entradas y Salidas por Articulo
	 * para ser exportados a un archivo plano con extensión .CSV
	 * @param mapa
	 * @param nombreReporte
	 * @param encabezado
	 * @param loginUsuario
	 * @return
	 */
	public StringBuffer cargarMapaEntradasSalidasArticulo(HashMap mapa, String nombreReporte, String encabezado, String loginUsuario)
	{
		StringBuffer datos = new StringBuffer();
		int nuevoSaldo = 0;
		double valorTotal = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FECHA REPORTE: "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"\n");
		datos.append("USUARIO: "+loginUsuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			//Calculamos el campo Nuevo Saldo y el campo Valor Total antes de ser impreso el registro
			nuevoSaldo = Utilidades.convertirAEntero(mapa.get("entradas_"+i)+"") - Utilidades.convertirAEntero(mapa.get("salidas_"+i)+"");
			valorTotal = nuevoSaldo * Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+"");
			
			if(i == 0)
			{
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codaxiomainterfaz_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadarticulo_"+i)+", "+Utilidades.convertirAEntero(mapa.get("entradas_"+i)+"")+", "+Utilidades.convertirAEntero(mapa.get("salidas_"+i)+"")+", "+nuevoSaldo+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(valorTotal)+"\n");
			}
			else if((mapa.get("codalmacen_"+i)+"").equals(mapa.get("codalmacen_"+(i-1))+""))
			{
				datos.append(mapa.get("codaxiomainterfaz_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadarticulo_"+i)+", "+Utilidades.convertirAEntero(mapa.get("entradas_"+i)+"")+", "+Utilidades.convertirAEntero(mapa.get("salidas_"+i)+"")+", "+nuevoSaldo+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(valorTotal)+"\n");
			}
			else
			{
				datos.append(mapa.get("nomalmacen_"+i)+"\n");
				datos.append(mapa.get("codaxiomainterfaz_"+i)+", "+mapa.get("desarticulo_"+i)+", "+mapa.get("unidadarticulo_"+i)+", "+Utilidades.convertirAEntero(mapa.get("entradas_"+i)+"")+", "+Utilidades.convertirAEntero(mapa.get("salidas_"+i)+"")+", "+nuevoSaldo+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorunitario_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(valorTotal)+"\n");
			}
		}
		
		return datos;
	}
	
	/**
	 * Método para insertar el log tipo base de datos
	 * con la información del reporte generado
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void insertarLog(Connection con, MovimientosAlmacenesForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		
		criterios.put("usuario", usuario.getLoginUsuario());
		criterios.put("centroAtencion", Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(forma.getCodigoCentroAtencion())));
    	
		//Se valida el Centro de Costo según si viene vacío o no? 
		if(UtilidadCadena.noEsVacio(forma.getAlmacen()))
    		criterios.put("almacen", Utilidades.obtenerNombreCentroCosto(con, Utilidades.convertirAEntero(forma.getAlmacen()), usuario.getCodigoInstitucionInt()));
    	else
    		criterios.put("almacen", forma.getAlmacen());
    	
    	criterios.put("fechaInicial", forma.getFechaInicial());
    	criterios.put("fechaFinal", forma.getFechaFinal());
    	
    	//Miramos que indicativo se escogio
    	switch(Utilidades.convertirAEntero(forma.getIndicativoES()))
    	{
    		case 1: criterios.put("indicativoEs", "Entrada");
    				break;
    		case 2: criterios.put("indicativoEs", "Salida");
					break;
    		default: criterios.put("indicativoEs", forma.getIndicativoES());
    				 break;
    	}
    	
    	criterios.put("transaccionInicial", forma.getTransaccionInicial());
    	criterios.put("transaccionFinal", forma.getTransaccionFinal());
    	
    	//Validamos que Tipo de Código de Artículo se escogio
    	if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAmbos))
    		criterios.put("tipoCodigoArticulo", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAmbos));
    	else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoAxioma))
			criterios.put("tipoCodigoArticulo", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAxioma));
    	else if(forma.getTipoCodigoArticulo().equals(ConstantesIntegridadDominio.acronimoInterfaz))
			criterios.put("tipoCodigoArticulo", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoInterfaz));
    	
    	//Validamos que Tipo de Reporte se escogio
    	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion))
    		criterios.put("nombreReporte", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoDetalladoTipoTransaccion));
    	else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion))
    		criterios.put("nombreReporte", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoDetalladoProveedorTransaccion));
    	else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion))
    		criterios.put("nombreReporte", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTotalizadoAlmacenTransaccion));
    	else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen))
    		criterios.put("nombreReporte", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEntradasSalidasTotalizadasAlmacen));
    	else if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo))
    		criterios.put("nombreReporte", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEntradasSalidasArticulo));
    	
    	//Validamos que Tipo de Salida se Escogio y si fue Archivo Plano mandamos la ruta del Archivo Plano
    	if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
    	{
    		criterios.put("tipoSalida", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion));
    		criterios.put("archivoPlano", "");
    	}
    	else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
    	{
    		criterios.put("tipoSalida", ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo));
    		criterios.put("archivoPlano", forma.getPathArchivoTxt());
    	}
    	
    	DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenesDao().insertarLog(con, criterios);
	}

	/**
	 * 
	 * @param codigoCentroAtencion
	 * @param almacen
	 * @param indicativoES
	 * @param codigoTransacciones
	 * @param transaccionInicial
	 * @param transaccionFinal
	 * @param tipoReporte
	 * @param codigoAImprimir
	 * @return
	 */
	public String obtenerSqlReporte(String centroAtencion,
			String almacen, String indicativoES, String codigoTransacciones,
			String transaccionInicial, String transaccionFinal,
			String tipoReporte, String codigoAImprimir) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientosAlmacenesDao().obtenerSqlReporte(centroAtencion, almacen, indicativoES, codigoTransacciones, transaccionInicial, transaccionFinal, tipoReporte, codigoAImprimir);
	}

}