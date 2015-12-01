package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaMovimientosConsignacionesDao;

public class ConsultaMovimientosConsignaciones 
{

	
	/**
	 * 
	 */
	private ConsultaMovimientosConsignacionesDao objetoDao;
	
	
	/**
	 * 
	 */
	public ConsultaMovimientosConsignaciones()
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getConsultaMovimientosConsignacionesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}

	
	/**
	 * 
	 * @param con
	 * @param centroAtencion
	 * @param almacen
	 * @param proveedor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param tipoCodigo
	 * @return
	 */
	public HashMap consultarMovimientosConsignaciones(Connection con, String centroAtencion, String almacen, String proveedor, String fechaInicial, String fechaFinal, String tipoCodigo) 
	{
		return objetoDao.consultarMovimientosConsignaciones(con, centroAtencion, almacen, proveedor, fechaInicial, fechaFinal, tipoCodigo);
	}
	
	
	/**
	 * 
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public static StringBuffer movimientosConsignaciones(HashMap mapa, String nombreReporte, String fechaReporte, String encabezado, String usuario, String centroAtencion)
	{
		StringBuffer datos = new StringBuffer();
		double valorTotalProveedor = 0;
		double cantidad = 0;
		double total= 0;
		double canTotalProveedor = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("CENTRO ATENCION: "+centroAtencion+"\n");
		datos.append("PERIODO: "+fechaReporte+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				
				cantidad = Utilidades.convertirADouble(mapa.get("cantidadentrada_"+i)+"")-Utilidades.convertirADouble(mapa.get("cantidadsalida_"+i)+"");
				total = cantidad * Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"");
				
				datos.append(mapa.get("nitproveedor_"+i)+", "+mapa.get("descproveedor_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("descarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+cantidad+", "+Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"")+", "+UtilidadTexto.formatearValores(total)+"\n");
				//Voy sumando el Subtotal por Convenio
				canTotalProveedor = cantidad;
				//Voy sumando el Subtotal por Via de Ingreso
				valorTotalProveedor = total;
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Cantidad Movimientos Proveedor: "+canTotalProveedor+"\n");
					datos.append("Total Proveedor: "+UtilidadTexto.formatearExponenciales(valorTotalProveedor)+"\n");
				}
			}
			//Si el nombre de la via de ingreso vienen iguales se deben de mostrar los otros datos sin la via de ingreso
			else if((mapa.get("nitproveedor_"+i)+"").equals(mapa.get("nitproveedor_"+(i-1))+""))
			{
				
				cantidad = Utilidades.convertirADouble(mapa.get("cantidadentrada_"+i)+"")-Utilidades.convertirADouble(mapa.get("cantidadsalida_"+i)+"");
				total = cantidad * Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"");
				
				datos.append(mapa.get("nitproveedor_"+i)+", "+mapa.get("descproveedor_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("descarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+cantidad+", "+Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"")+", "+UtilidadTexto.formatearValores(total)+"\n");
				//Voy sumando el Subtotal por Convenio
				canTotalProveedor = canTotalProveedor + cantidad;
				//Voy sumando el Subtotal por Via de Ingreso
				valorTotalProveedor = valorTotalProveedor + total;
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Cantidad Movimientos Proveedor: "+canTotalProveedor+"\n");
					datos.append("Total Proveedor: "+UtilidadTexto.formatearExponenciales(valorTotalProveedor)+"\n");
				}
					
			}
			//Si el nombre de la via de ingreso es diferente se debe incluir los datos a partir de la via de ingreso incluyendo el respectivo tipo paciente
			else
			{
				
				cantidad = Utilidades.convertirADouble(mapa.get("cantidadentrada_"+i)+"")-Utilidades.convertirADouble(mapa.get("cantidadsalida_"+i)+"");
				total = cantidad * Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"");
				
				
				datos.append("Cantidad Movimientos Proveedor: "+canTotalProveedor+"\n");
				datos.append("Total Proveedor: "+UtilidadTexto.formatearExponenciales(valorTotalProveedor)+"\n");
				
				datos.append(mapa.get("nitproveedor_"+i)+", "+mapa.get("descproveedor_"+i)+", "+mapa.get("codigoarticulo_"+i)+", "+mapa.get("descarticulo_"+i)+", "+mapa.get("unidadmedida_"+i)+", "+cantidad+", "+Utilidades.convertirADouble(mapa.get("costopromedio_"+i)+"")+", "+UtilidadTexto.formatearValores(total)+"\n");
				//Voy sumando el Subtotal por Convenio
				canTotalProveedor = cantidad;
				//Voy sumando el Subtotal por Via de Ingreso
				valorTotalProveedor = total;
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Cantidad Movimientos Proveedor: "+canTotalProveedor+"\n");
					datos.append("Total Proveedor: "+UtilidadTexto.formatearValores(valorTotalProveedor)+"\n");
			 	}
			}
		}
		//Imprimimos el ultimo campo que seria el Total General
		//datos.append("Total Ingresos: "+canTotalIngreso+"\n");
		//datos.append("Valor Total: "+UtilidadTexto.formatearExponenciales(totalIngreso)+"\n");
		return datos;
	}


	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param almacen
	 * @param proveedor
	 * @param centroAtencion 
	 * @return
	 */
	public String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String almacen, String proveedor, String centroAtencion) 
	{
		return objetoDao.cambiarConsulta(con, fechaInicial, fechaFinal, almacen, proveedor, centroAtencion);
	}
	
	
	
}
