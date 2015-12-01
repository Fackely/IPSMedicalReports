package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.TotalFacturadoConvenioContratoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.TotalFacturadoConvenioContratoDao;

public class TotalFacturadoConvenioContrato 
{

	
	/**
	 * 
	 */
	private TotalFacturadoConvenioContratoDao objetoDao;
	
	
	/**
	 * 
	 */
	public TotalFacturadoConvenioContrato()
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
			objetoDao=myFactory.getTotalFacturadoConvenioContratoDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
	}
	
	
	/**
	 * 
	 * @param con
	 * @param excluirFacturas
	 * @param convenio
	 * @param contrato
	 * @return
	 */
	public String cambiarConsulta(Connection con, String excluirFacturas, String convenio, String contrato, String periodo) 
	{
		return objetoDao.cambiarConsulta(con, excluirFacturas, convenio, contrato, periodo);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarTotalFacturado(Connection con, TotalFacturadoConvenioContratoForm forma, String mes) 
	{
		HashMap vo = new HashMap();
		String periodo="";
		if(forma.getMes().equals(""))
			periodo=forma.getAnio()+"-"+mes;
		else
			periodo=forma.getAnio()+"-"+forma.getMes();
		vo.put("periodo", periodo);
		vo.put("excluirfacturas", forma.getExcluirFacturas());
		vo.put("convenio", forma.getConvenio());
		vo.put("contrato", forma.getContrato());
		return objetoDao.consultarTotalFacturado(con, vo);
	}

	
	/**
	 * 
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param usuario
	 * @param convenio
	 * @return
	 */
	public static StringBuffer totalFacturadoConvenioContrato(HashMap mapa, String nombreReporte, String fechaReporte, String encabezado, String usuario)
	{
		StringBuffer datos = new StringBuffer();
		double subTotalConvenio = 0, totalFinalConvenio = 0, totalFinalPaciente = 0, totalFinal = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("PERIODO: "+fechaReporte+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				
				datos.append(mapa.get("nombreconvenio_"+i)+", "+mapa.get("numerocontrato_"+i)+" Vig. "+mapa.get("fechainicial_"+i)+"-"+mapa.get("fechafinal_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"")) +", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy sumando el Subtotal por Convenio
				totalFinalConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalFinalPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalFinal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				
			}
			else
			{
				datos.append(mapa.get("nombreconvenio_"+i)+", "+mapa.get("numerocontrato_"+i)+" Vig. "+mapa.get("fechainicial_"+i)+"-"+mapa.get("fechafinal_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"")) +"\n");
				
				totalFinalConvenio = totalFinalConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalFinalPaciente = totalFinalPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalFinal = totalFinal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				
			}
		}
		//Imprimimos el ultimo campo que seria el Total General
		datos.append("VALOR TOTAL CONVENIO: "+UtilidadTexto.formatearExponenciales(totalFinalConvenio)+"\n");
		datos.append("VALOR TOTAL PACIENTE: "+UtilidadTexto.formatearExponenciales(totalFinalPaciente) +"\n");
		datos.append("VALOR TOTAL: "+UtilidadTexto.formatearExponenciales(totalFinal)+"\n");
		return datos;
	}
	
	
}
