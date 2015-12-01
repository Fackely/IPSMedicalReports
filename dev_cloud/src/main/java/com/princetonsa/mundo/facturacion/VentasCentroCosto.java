package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.VentasCentroCostoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.VentasCentroCostoDao;

public class VentasCentroCosto 
{

	
	/**
	 * 
	 */
	private VentasCentroCostoDao objetoDao;
	
	
	/**
	 * 
	 */
	public VentasCentroCosto()
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
			objetoDao=myFactory.getVentasCentroCostoDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}

	
	/**
	 * 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroCosto
	 * @return
	 */
	public String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String centroCosto) 
	{
		return objetoDao.cambiarConsulta(con, fechaInicial, fechaFinal, centroCosto);
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarVentasCentroCosto(Connection con, VentasCentroCostoForm forma) 
	{
		HashMap vo= new HashMap();
		vo.put("fechainicial", forma.getFechaInicial());
		vo.put("fechafinal", forma.getFechaFinal());
		vo.put("centroatencion", forma.getCentroAtencion());
		vo.put("centrocosto", forma.getCentroCosto());
		return objetoDao.consultarVentasCentroCosto(con, vo);
	}
	
	
	/**
	 * 
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param usuario
	 * @return
	 */
	public static StringBuffer ventasCentroCosto(HashMap mapa, String nombreReporte, String fechaReporte, String encabezado, String usuario, String centroAtencion)
	{
		StringBuffer datos = new StringBuffer();
		double valorTotalCentroCosto = 0, valorDescuentosCentroCosto = 0, ventasTotalesCentroCosto = 0;
		int canSubTotalIngresoConvenio = 0, canSubTotalIngresoVia = 0, canTotalIngreso = 0;
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
				datos.append(mapa.get("centrocostosolicita_"+i)+" - "+mapa.get("nombreccsolicita_"+i)+"\n");
				datos.append(mapa.get("nombreccejecuta_"+i)+", "+mapa.get("cuenta_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valortotal_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("descuentocomercial_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("ventastotal_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				valorTotalCentroCosto = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando el Subtotal por Via de Ingreso
				valorDescuentosCentroCosto = Utilidades.convertirADouble(mapa.get("descuentocomercial_"+i)+"");
				
				ventasTotalesCentroCosto = Utilidades.convertirADouble(mapa.get("ventastotal_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorTotalCentroCosto)+"\n");
					datos.append("Descuento Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorDescuentosCentroCosto)+"\n");
					datos.append("Ventas Totales por Centro de Costo: "+UtilidadTexto.formatearValores(ventasTotalesCentroCosto)+"\n");
			 	}
			}
			//Si el nombre de la via de ingreso vienen iguales se deben de mostrar los otros datos sin la via de ingreso
			else if((mapa.get("centrocostosolicita_"+i)+"").equals(mapa.get("centrocostosolicita_"+(i-1))+""))
			{
				datos.append(mapa.get("nombreccejecuta_"+i)+", "+mapa.get("cuenta_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valortotal_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("descuentocomercial_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("ventastotal_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				valorTotalCentroCosto = valorTotalCentroCosto + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando el Subtotal por Via de Ingreso
				valorDescuentosCentroCosto = valorDescuentosCentroCosto + Utilidades.convertirADouble(mapa.get("descuentocomercial_"+i)+"");
				
				ventasTotalesCentroCosto = ventasTotalesCentroCosto + Utilidades.convertirADouble(mapa.get("ventastotal_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorTotalCentroCosto)+"\n");
					datos.append("Descuento Total por Centro de Costo : "+UtilidadTexto.formatearValores(valorDescuentosCentroCosto)+"\n");
					datos.append("Ventas Totales por Centro de Costo: "+UtilidadTexto.formatearValores(ventasTotalesCentroCosto)+"\n");
			 	}
					
			}
			//Si el nombre de la via de ingreso es diferente se debe incluir los datos a partir de la via de ingreso incluyendo el respectivo tipo paciente
			else
			{
				datos.append("Valor Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorTotalCentroCosto)+"\n");
				datos.append("Descuento Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorDescuentosCentroCosto)+"\n");
				datos.append("Ventas Totales por Centro de Costo: "+UtilidadTexto.formatearValores(ventasTotalesCentroCosto)+"\n");
				datos.append(mapa.get("centrocostosolicita_"+i)+" - "+mapa.get("nombreccsolicita_"+i)+"\n");
				datos.append(mapa.get("nombreccejecuta_"+i)+", "+mapa.get("cuenta_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valortotal_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("descuentocomercial_"+i)+"")+", "+UtilidadTexto.formatearValores(mapa.get("ventastotal_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				valorTotalCentroCosto = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando el Subtotal por Via de Ingreso
				valorDescuentosCentroCosto = Utilidades.convertirADouble(mapa.get("descuentocomercial_"+i)+"");
				
				ventasTotalesCentroCosto = Utilidades.convertirADouble(mapa.get("ventastotal_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorTotalCentroCosto)+"\n");
					datos.append("Descuento Total por Centro de Costo: "+UtilidadTexto.formatearValores(valorDescuentosCentroCosto)+"\n");
					datos.append("Ventas Totales por Centro de Costo: "+UtilidadTexto.formatearValores(ventasTotalesCentroCosto)+"\n");
			 	}
			}
		}
		//Imprimimos el ultimo campo que seria el Total General
		//datos.append("Total Ingresos: "+canTotalIngreso+"\n");
		//datos.append("Valor Total: "+UtilidadTexto.formatearExponenciales(totalIngreso)+"\n");
		return datos;
	}
	
	
	
}
