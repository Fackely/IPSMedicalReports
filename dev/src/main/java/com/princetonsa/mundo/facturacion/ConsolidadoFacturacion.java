package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.ConsolidadoFacturacionForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsolidadoFacturacionDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class ConsolidadoFacturacion
{

	/**
     * Constructor de la Clase
     */
    public ConsolidadoFacturacion()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto,para trabajar con la fuente de datos
	 */
	private static ConsolidadoFacturacionDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getConsolidadoFacturacionDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que llena el HashMap con los datos de Consolidados
	 * de Facturacion
	 * @param con
	 * @param forma
	 * @return
	 */
	public static HashMap consultarConsolidadoFacturacion(Connection con,ConsolidadoFacturacionForm forma)
	{
		HashMap criterios = new HashMap();
		criterios.put("facturaInicial",forma.getFacturaInicial());
		criterios.put("facturaFinal",forma.getFacturaFinal());
		criterios.put("fechaInicial",forma.getFechaInicial());
		criterios.put("fechaFinal",forma.getFechaFinal());
		criterios.put("convenio",forma.getConvenioSeleccionado());
		criterios.put("contrato",forma.getContratoSeleccionado());
		criterios.put("viaIngreso",forma.getViaIngreso());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsolidadoFacturacionDao().consultarConsolidadoFacturacion(con,criterios);
	}
	
	/**
	 * Metodo que inserta el log tipo base de datos
	 * para el consolidado de facturacion
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void insertarLog(Connection con, ConsolidadoFacturacionForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("usuario", usuario.getLoginUsuario());
		if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaImpresion))
		{
			criterios.put("tipoSalida", "Impresion");
			criterios.put("ruta", "");
		}
		else if(forma.getTipoSalida().equals(ConstantesIntegridadDominio.acronimoTipoSalidaArchivo))
		{
			criterios.put("tipoSalida", "Archivo Plano");
			criterios.put("ruta", forma.getPathArchivoTxt());
		}
		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("facturaInicial",forma.getFacturaInicial());
		criterios.put("facturaFinal",forma.getFacturaFinal());
		criterios.put("fechaInicial",forma.getFechaInicial());
		criterios.put("fechaFinal",forma.getFechaFinal());
		criterios.put("convenio",forma.getConvenioSeleccionado());
		criterios.put("contrato",forma.getContratoSeleccionado());
		criterios.put("viaIngreso",forma.getViaIngreso());
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsolidadoFacturacionDao().insertarLog(con,criterios);
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano de Consolidado
	 * de facturacion
	 * @param mapa
	 * @param nombreReporte
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaConsolidadoFacturacion(HashMap<String, Object> mapa, String nombreReporte, String fechaReporte, String encabezado)
	{
		StringBuffer datos = new StringBuffer();
		double totalIngresoConvenio = 0, totalIngresoPaciente = 0, totalIngresoTotal = 0;
		double totalContratoConvenio = 0, totalContratoPaciente = 0, totalContratoTotal = 0;
		double totalConvenioConvenio = 0, totalConvenioPaciente = 0, totalConvenioTotal = 0;
		int contadorFacturas = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("FILTRO CONSULTA: "+fechaReporte+"\n");
		datos.append(encabezado+"\n");
		
		//Organizamos los datos para generar el Archivo Plano
		for(int i=0; i<Utilidades.convertirAEntero(mapa.get("numRegistros")+""); i++)
		{
			if(i == 0)
			{
				datos.append(mapa.get("nomconvenio_"+i)+"\n");
				datos.append("CONTRATO No. "+mapa.get("numcontrato_"+i)+"\n");
				datos.append(mapa.get("factura_"+i)+", "+mapa.get("fechafactura_"+i)+", "+mapa.get("viaingreso_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("estrato_"+i)+", "+mapa.get("ficha_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy Sumando los valores totales del ingreso
				totalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalIngresoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalIngresoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando los valores totales por contrato
				totalContratoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalContratoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalContratoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando los valores totales por convenio
				totalConvenioConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalConvenioPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalConvenioTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Contador facturas
				contadorFacturas++;
				//Si no existen mas registros se muestran los totales respectivos
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
			 	{
			 		datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+i)+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
			 		datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
			 		datos.append("Total por Convenio "+mapa.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");
			 	}
			}
			//Si el nombre del convenio vienen iguales se deben de mostrar los otros datos sin el convenio
			else if ((mapa.get("convenio_"+i)+"").equals(mapa.get("convenio_"+(i-1))+""))
			{
				//Si el nombre del contrato vienen iguales se deben de mostrar los otros datos sin el convenio y sin el contrato
				if ((mapa.get("numcontrato_"+i)+"").equals(mapa.get("numcontrato_"+(i-1))+""))
				{
					//Si la via de ingreso es diferente se suman los valores totales por convenio, por contrato y 
					//por ingreso del ultimo registro y se imprime el valor actual del total por ingreso
					if(!(mapa.get("viaingreso_"+i)+"").equals(mapa.get("viaingreso_"+(i-1))+""))
					{
						datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+(i-1))+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
						datos.append(mapa.get("factura_"+i)+", "+mapa.get("fechafactura_"+i)+", "+mapa.get("viaingreso_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("estrato_"+i)+", "+mapa.get("ficha_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
						//Voy sumando los valores totales por contrato
						totalContratoConvenio = totalContratoConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
						totalContratoPaciente = totalContratoPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalContratoTotal = totalContratoTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Voy sumando los valores totales por convenio
						totalConvenioConvenio = totalConvenioConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
						totalConvenioPaciente = totalConvenioPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalConvenioTotal = totalConvenioTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Reseteamos los valores totales del ingreso para realizar la sumatoria por la nueva via de ingreso
						totalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");;
						totalIngresoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalIngresoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Reseteamos el Contador facturas
						contadorFacturas = 0;
						//Contador facturas
						contadorFacturas++;
						if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
						{
							datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+i)+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
							datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
					 		datos.append("Total por Convenio "+mapa.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");
						}
					}
					//La via de ingreso es la misma y solo se deben de sumar los totales por ingreso, por contrato y por convenio y se imprime
					//el registro nuevo sin la informacion de convenio y contrato
					else
					{
						datos.append(mapa.get("factura_"+i)+", "+mapa.get("fechafactura_"+i)+", "+mapa.get("viaingreso_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("estrato_"+i)+", "+mapa.get("ficha_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
						//Voy Sumando los valores totales del ingreso
						totalIngresoConvenio = totalIngresoConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
						totalIngresoPaciente = totalIngresoPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalIngresoTotal = totalIngresoTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Voy Sumando los valores totales por contrato
						totalContratoConvenio = totalContratoConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
						totalContratoPaciente = totalContratoPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalContratoTotal = totalContratoTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Voy sumando los valores totales por convenio
						totalConvenioConvenio = totalConvenioConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
						totalConvenioPaciente = totalConvenioPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
						totalConvenioTotal = totalConvenioTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
						//Contador facturas
						contadorFacturas++;
						if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
						{
							datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+i)+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
							datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
							datos.append("Total por Convenio "+mapa.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");
						}
					}
				}
				//El numero del contrato es diferente y el convenio es el mismo. Se deben imprimir los valores del convenio con la informacion del nuevo contrato
				else
				{
					datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+(i-1))+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
					datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
					datos.append("CONTRATO No. "+mapa.get("numcontrato_"+i)+"\n");
					datos.append(mapa.get("factura_"+i)+", "+mapa.get("fechafactura_"+i)+", "+mapa.get("viaingreso_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("estrato_"+i)+", "+mapa.get("ficha_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
					//Voy sumando los valores totales por convenio
					totalConvenioConvenio = totalConvenioConvenio + Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
					totalConvenioPaciente = totalConvenioPaciente + Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
					totalConvenioTotal = totalConvenioTotal + Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Voy sumando los valores totales por contrato
					totalContratoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
					totalContratoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
					totalContratoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Reseteamos los valores totales del ingreso para realizar la sumatoria por la nueva via de ingreso
					totalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");;
					totalIngresoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
					totalIngresoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
					//Reseteamos el Contador facturas
					contadorFacturas = 0;
					//Contador facturas
					contadorFacturas++;
					if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
					{
						datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+i)+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
						datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
						datos.append("Total por Convenio "+mapa.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");
					}
				}
			}
			//Existe un nuevo convenio. Por lo consiguiente se deben totalizar todos los rompimientos del convenio anterior
			//Y mostrar la informacion del nuevo convenio
			else
			{
				//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
				datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+(i-1))+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
				datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
				datos.append("Total por Convenio "+mapa.get("nomconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");//Reseteamos el Contador facturas
				//Reseteo el contador de facturas para manejar las facturas del nuevo convenio
				contadorFacturas = 0;
				datos.append(mapa.get("nomconvenio_"+i)+"\n");
				datos.append("CONTRATO No. "+mapa.get("numcontrato_"+i)+"\n");
				datos.append(mapa.get("factura_"+i)+", "+mapa.get("fechafactura_"+i)+", "+mapa.get("viaingreso_"+i)+", "+mapa.get("paciente_"+i)+", "+mapa.get("idpaciente_"+i)+", "+mapa.get("estrato_"+i)+", "+mapa.get("ficha_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+""))+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapa.get("valortotal_"+i)+""))+"\n");
				//Voy Sumando los valores totales del ingreso
				totalIngresoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalIngresoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalIngresoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando los valores totales por contrato
				totalContratoConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalContratoPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalContratoTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Voy sumando los valores totales por convenio
				totalConvenioConvenio = Utilidades.convertirADouble(mapa.get("valorconvenio_"+i)+"");
				totalConvenioPaciente = Utilidades.convertirADouble(mapa.get("valorpaciente_"+i)+"");
				totalConvenioTotal = Utilidades.convertirADouble(mapa.get("valortotal_"+i)+"");
				//Contador facturas
				contadorFacturas++;
				if(i == (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") - 1))
				{
					datos.append("Subtotal por Vía de Ingreso "+mapa.get("viaingreso_"+i)+", Facturas "+contadorFacturas+", "+UtilidadTexto.formatearExponenciales(totalIngresoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalIngresoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalIngresoTotal)+"\n");
					datos.append("Subtotal por Contrato "+mapa.get("numcontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalContratoConvenio)+", "+UtilidadTexto.formatearExponenciales(totalContratoPaciente)+", "+UtilidadTexto.formatearExponenciales(totalContratoTotal)+"\n");
					datos.append("Total por Convenio "+mapa.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenioConvenio)+", "+UtilidadTexto.formatearExponenciales(totalConvenioPaciente)+", "+UtilidadTexto.formatearExponenciales(totalConvenioTotal)+"\n");
				}
			}
		}
		
		return datos;
	}
	
	/**
	 * 
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param viaIngreso
	 * @param convenio
	 * @param contrato
	 * @return
	 */
	public static String obtenerCondiciones(String facturaInicial, String facturaFinal, String fechaInicial, String fechaFinal, String viaIngreso, String convenio, String contrato) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsolidadoFacturacionDao().obtenerCondiciones( facturaInicial,  facturaFinal,  fechaInicial,  fechaFinal,  viaIngreso,  convenio,  contrato);
	}
	
}