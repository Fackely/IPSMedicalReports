package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.PacientesPorFacturarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.PacientesPorFacturarDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class PacientesPorFacturar
{

	/**
     * Constructor de la Clase
     */
    public PacientesPorFacturar()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto,para trabajar con la fuente de datos
	 */
	private static PacientesPorFacturarDao aplicacionDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g.,Oracle,PostgreSQL,etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao == null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getPacientesPorFacturarDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * Metodo que llena el HashMap con los datos de
	 * consumos por facturar de pacientes
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap consultarConsumosPorFacturar(Connection con, PacientesPorFacturarForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("viaIngreso", forma.getViaIngreso());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		criterios.put("tipoConvenio", forma.getTipoConvenioSeleccionado());
		criterios.put("convenio", forma.getConvenioSeleccionado());
		criterios.put("tipoEgreso", forma.getTipoEgreso());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPacientesPorFacturarDao().consultarConsumosPorFacturar(con, criterios);
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano 
	 * de consumos por facturar de pacientes
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @param usuario
	 * @return
	 */
	public static StringBuffer consumosPorFacturar(HashMap<String, Object> mapa, String nombreReporte, String fechaReporte, String encabezado, String usuario)
	{
		StringBuffer datos = new StringBuffer();
		double subTotalIngresoConvenio = 0, subTotalIngresoVia = 0, totalIngreso = 0;
		int canSubTotalIngresoConvenio = 0, canSubTotalIngresoVia = 0, canTotalIngreso = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("PERIODO: "+fechaReporte+"\n");
		datos.append("USUARIO: "+usuario+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("nomviaingreso_"+i)+" - "+mapa.get("tipopaciente_"+i)+"\n");
				datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("convenio_"+i)+"\n");
				datos.append(mapa.get("ingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("fechaegreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valor_"+i)+""))+", "+mapa.get("usuario_"+i)+"\n");
				//Voy sumando el Subtotal por Convenio
				subTotalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Voy sumando el Subtotal por Via de Ingreso
				subTotalIngresoVia = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Voy sumando el Total
				totalIngreso = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Contador facturas
				canSubTotalIngresoConvenio++;
				canSubTotalIngresoVia++;
				canTotalIngreso++;
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
					datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
					datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
					datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
			 	}
			}
			//Si el nombre de la via de ingreso vienen iguales se deben de mostrar los otros datos sin la via de ingreso
			else if((mapa.get("nomviaingreso_"+i)+"").equals(mapa.get("nomviaingreso_"+(i-1))+""))
			{
				//Se valida el tipo paciente. Si viene igual se muestran los otros datos sin el tipo
				if ((mapa.get("tipopaciente_"+i)+"").equals(mapa.get("tipopaciente_"+(i-1))+""))
				{
					//Si el nombre del convenio vienen iguales se deben de mostrar los otros datos sin la via de ingreso y sin el convenio
					if ((mapa.get("convenio_"+i)+"").equals(mapa.get("convenio_"+(i-1))+""))
					{
						datos.append(mapa.get("ingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("fechaegreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valor_"+i)+""))+", "+mapa.get("usuario_"+i)+"\n");
						//Voy sumando el Subtotal por Convenio
						subTotalIngresoConvenio = subTotalIngresoConvenio + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Voy sumando el Subtotal por Via de Ingreso
						subTotalIngresoVia = subTotalIngresoVia + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Voy sumando el Total
						totalIngreso = totalIngreso + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Contador facturas
						canSubTotalIngresoConvenio++;
						canSubTotalIngresoVia++;
						canTotalIngreso++;					
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
					 	{
							datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
							datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
							datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
							datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
					 	}
					}
					//Si el nombre del convenio es diferente se deben de mostrar los datos a partir del convenio
					else
					{
						datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
						datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
						datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("convenio_"+i)+"\n");
						datos.append(mapa.get("ingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("fechaegreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valor_"+i)+""))+", "+mapa.get("usuario_"+i)+"\n");
						//Voy sumando el Subtotal por Convenio
						subTotalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Voy sumando el Subtotal por Via
						subTotalIngresoVia = subTotalIngresoVia + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Voy sumando el Total
						totalIngreso = totalIngreso + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
						//Reseteamos el contador de facturas por subtotal de convenio y contamos la nueva factura
						canSubTotalIngresoConvenio = 0;
						canSubTotalIngresoConvenio++;
						canSubTotalIngresoVia++;
						canTotalIngreso++;					
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
					 	{
							datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
							datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
							datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
							datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
					 	}
					}
				}
				//Si el tipo paciente viene diferente se debe incluir un nuevo regsitro incluyendo el nuevo tipo paciente 
				else
				{
					datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
					datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
					datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
					datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
					datos.append(mapa.get("nomviaingreso_"+i)+" - "+mapa.get("tipopaciente_"+i)+"\n");
					datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("convenio_"+i)+"\n");
					datos.append(mapa.get("ingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("fechaegreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valor_"+i)+""))+", "+mapa.get("usuario_"+i)+"\n");
					//Voy sumando el Subtotal por Convenio
					subTotalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
					//Voy sumando el Subtotal por Via de Ingreso
					subTotalIngresoVia = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
					//Voy sumando el Total
					totalIngreso = totalIngreso + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
					//Reseteamos los contadores y contamos la nueva factura
					canSubTotalIngresoConvenio = 0;
					canSubTotalIngresoVia = 0;
					canSubTotalIngresoConvenio++;
					canSubTotalIngresoVia++;
					canTotalIngreso++;
					//Si no existen mas registros se muestran los totales respectivos
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				 	{
						datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
						datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
						datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
						datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
				 	}
				}
			}
			//Si el nombre de la via de ingreso es diferente se debe incluir los datos a partir de la via de ingreso incluyendo el respectivo tipo paciente
			else
			{
				datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
				datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
				datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
				datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
				datos.append(mapa.get("nomviaingreso_"+i)+" - "+mapa.get("tipopaciente_"+i)+"\n");
				datos.append(mapa.get("codconvenio_"+i)+" - "+mapa.get("convenio_"+i)+"\n");
				datos.append(mapa.get("ingreso_"+i)+", "+mapa.get("fechaingreso_"+i)+", "+mapa.get("fechaegreso_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("nombrepaciente_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valor_"+i)+""))+", "+mapa.get("usuario_"+i)+"\n");
				//Voy sumando el Subtotal por Convenio
				subTotalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Voy sumando el Subtotal por Via de Ingreso
				subTotalIngresoVia = Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Voy sumando el Total
				totalIngreso = totalIngreso + Utilidades.convertirADouble(mapa.get("valor_"+i)+"");
				//Reseteamos los contadores y contamos la nueva factura
				canSubTotalIngresoConvenio = 0;
				canSubTotalIngresoVia = 0;
				canSubTotalIngresoConvenio++;
				canSubTotalIngresoVia++;
				canTotalIngreso++;
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
					datos.append("Subtotal Ingresos por Convenio: "+canSubTotalIngresoConvenio+"\n");
					datos.append("Valor Parcial por Convenio: "+UtilidadTexto.formatearExponenciales(subTotalIngresoConvenio)+"\n");
					datos.append("Subtotal Ingresos por Vía de Ingreso: "+canSubTotalIngresoVia+"\n");
					datos.append("Valor Parcial por Vía de Ingreso: "+UtilidadTexto.formatearExponenciales(subTotalIngresoVia)+"\n");
			 	}
			}
		}
		//Imprimimos el ultimo campo que seria el Total General
		datos.append("Total Ingresos: "+canTotalIngreso+"\n");
		datos.append("Valor Total: "+UtilidadTexto.formatearExponenciales(totalIngreso)+"\n");
		return datos;
	}
	
}