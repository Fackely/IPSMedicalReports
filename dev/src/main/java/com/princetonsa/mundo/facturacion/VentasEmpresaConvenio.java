package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.VentasEmpresaConvenioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.VentasEmpresaConvenioDao;
import com.princetonsa.util.birt.reports.DesignEngineApi;

public class VentasEmpresaConvenio 
{

	
	/**
	 * 
	 */
	private VentasEmpresaConvenioDao objetoDao;
	
	
	/**
	 * 
	 */
	public VentasEmpresaConvenio()
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
			objetoDao=myFactory.getVentasEmpresaConvenioDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}

	
	/**
	 * 
	 * @param con
	 * @param comp
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param empresa
	 * @param convenio
	 * @return
	 */
	public String cambiarConsulta(Connection con, DesignEngineApi comp, String fechaInicial, String fechaFinal, String empresa, String convenio) 
	{
		return objetoDao.cambiarConsulta(con, comp, fechaInicial, fechaFinal, empresa, convenio);
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarVentasCentroCosto(Connection con, VentasEmpresaConvenioForm forma) 
	{
		HashMap vo = new HashMap();
		vo.put("fechainicial", forma.getFechaInicial());
		vo.put("fechafinal", forma.getFechaFinal());
		vo.put("centroatencion", forma.getCentroAtencion());
		vo.put("empresa", forma.getEmpresa());
		vo.put("convenio", forma.getConvenio());
		
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
	public static StringBuffer ventasEmpresaConvenio(HashMap mapa, String nombreReporte, String fechaReporte, String encabezado, String usuario, String centroAtencion)
	{
		StringBuffer datos = new StringBuffer();
		double subTotalConvenio = 0, totalFinal = 0;
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
				datos.append(mapa.get("nit_"+i)+" - "+mapa.get("nombreempresa_"+i)+"\n");
				datos.append(mapa.get("convenio_"+i)+", "+mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valorsuma_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				subTotalConvenio = Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				totalFinal = Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Convenio: "+UtilidadTexto.formatearValores(subTotalConvenio)+"\n");
				}
			}
			//Si el nombre de la via de ingreso vienen iguales se deben de mostrar los otros datos sin la via de ingreso
			else if((mapa.get("nit_"+i)+"").equals(mapa.get("nit_"+(i-1))+""))
			{
				datos.append(mapa.get("convenio_"+i)+", "+mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valorsuma_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				subTotalConvenio = subTotalConvenio + Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				totalFinal = totalFinal + Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Convenio: "+UtilidadTexto.formatearValores(subTotalConvenio)+"\n");
				}
					
			}
			//Si el nombre de la via de ingreso es diferente se debe incluir los datos a partir de la via de ingreso incluyendo el respectivo tipo paciente
			else
			{
				datos.append("Valor Total por Convenio: "+UtilidadTexto.formatearValores(subTotalConvenio)+"\n");
				datos.append(mapa.get("nit_"+i)+" - "+mapa.get("nombreempresa_"+i)+"\n");
				datos.append(mapa.get("convenio_"+i)+", "+mapa.get("nombreconvenio_"+i)+", "+UtilidadTexto.formatearValores(mapa.get("valorsuma_"+i)+"")+"\n");
				//Voy sumando el Subtotal por Convenio
				subTotalConvenio = Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				totalFinal = totalFinal + Utilidades.convertirADouble(mapa.get("valorsuma_"+i)+"");
				
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Valor Total por Convenio: "+UtilidadTexto.formatearValores(subTotalConvenio)+"\n");
				}
			}
		}
		//Imprimimos el ultimo campo que seria el Total General
		datos.append("TOTALES FINALES: "+UtilidadTexto.formatearValores(totalFinal)+"\n");
		return datos;
	}
	
	
	
}
