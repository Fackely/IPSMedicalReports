package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.FacturadosPorConvenioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.FacturadosPorConvenioDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class FacturadosPorConvenio
{

	/**
     * Constructor de la Clase
     */
    public FacturadosPorConvenio()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto,para trabajar con la fuente de datos
	 */
	private static FacturadosPorConvenioDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getFacturadosPorConvenioDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * Metodo que llena el HashMap con los datos de
	 * Servicios/Articulos Facturados por Convenio
	 * por servicios
	 * @param con
	 * @param forma
	 * @param porServArt Servicios ====> True; Articulos ====> False
	 * @return
	 */
	public static HashMap consultarFacturadosPorConvenio(Connection con, FacturadosPorConvenioForm forma, boolean porSerArt)
	{
		HashMap criterios = new HashMap();
		criterios.put("fechaInicial",forma.getFechaInicial());
		criterios.put("fechaFinal",forma.getFechaFinal());
		criterios.put("convenio",forma.getConvenioSeleccionado());
		criterios.put("contrato",forma.getContratoSeleccionado());
		criterios.put("radioServicio",forma.getRadioServicios());
		criterios.put("radioArticulo",forma.getRadioArticulos());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturadosPorConvenioDao().consultarFacturadosPorConvenio(con, criterios, porSerArt);
	}
	
	/**
	 * Metodo que inserta el log tipo base de datos
	 * para el reporte servicios/articulos facturados
	 * por convenio
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	public void insertarLog(Connection con, FacturadosPorConvenioForm forma, UsuarioBasico usuario)
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
		criterios.put("fechaInicial",forma.getFechaInicial());
		criterios.put("fechaFinal",forma.getFechaFinal());
		criterios.put("convenio",forma.getConvenioSeleccionado());
		criterios.put("contrato",forma.getContratoSeleccionado());
		criterios.put("checkServicios", forma.getCheckServicios());
		criterios.put("checkArticulos", forma.getCheckArticulos());
		DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFacturadosPorConvenioDao().insertarLog(con,criterios);
	}
	
	/**
	 * Metodo que organiza los datos del mapa para generar el archivo plano
	 * de Servicios/Articulos Facturados por Convenio
	 * @param mapaServicios
	 * @param mapaArticulos 
	 * @param nombreReporte
	 * @param string 
	 * @param convenio 
	 * @param radioArticulos 
	 * @param radioServicios 
	 * @param string 
	 * @param fechaReporte
	 * @param encabezado
	 * @return
	 */
	public static StringBuffer cargarMapaFacturadosPorConvenio(HashMap mapaServicios, HashMap mapaArticulos, String nombreReporte, String fechaInicial, String fechaFinal, String encabezadoServicios, String encabezadoArticulos, String convenio, String contrato, String radioArticulos, String radioServicios)
	{
		StringBuffer datos = new StringBuffer();
		double totalValorTotal = 0, totalConvenio = 0;
		datos.append("NOMBRE REPORTE: "+nombreReporte+"\n");
		datos.append("PERIODO: "+fechaInicial+" al "+fechaFinal+"\n");
		if(UtilidadCadena.noEsVacio(convenio))
			datos.append("CONVENIO: "+convenio+"\n");
		else
			datos.append("CONVENIO: Todos\n");
		if(UtilidadCadena.noEsVacio(contrato))
			datos.append("CONTRATO: "+contrato+"\n");
		else
			datos.append("CONTRATO: Todos\n");
		
		if(Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") > 0)
		{
			//******************************************************INICIO SERVICIOS*****************************************************************
			datos.append(encabezadoServicios+"\n");
			//Se valida si el rompimiento es por servicio gracias a que usa otro formato distinto a la naturaleza y al de grupo
			if(radioServicios.equals("3"))
			{
				//Organizamos los datos para generar el Archivo Plano. Primero el mapa de Servicios
				for(int i=0; i<Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+""); i++)
				{
					if(i == 0)
					{
						datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
						datos.append(mapaServicios.get("codrompimiento_"+i)+" "+mapaServicios.get("nomrompimiento_"+i)+",  "+mapaServicios.get("codigopropietario_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
						//Voy Sumando los valores totales del ingreso
						totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Voy sumando el total por convenio
						totalConvenio = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si el nombre del convenio y el contrato vienen iguales se deben de mostrar los otros datos sin el convenio y el contrato
					else if ((mapaServicios.get("desccontrato_"+i)+"").equals(mapaServicios.get("desccontrato_"+(i-1))+""))
					{
						//Suma el valor total para los otros registros del mismo convenio y el mismo contrato
						totalValorTotal = totalValorTotal + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Suma el total por convenio
						totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						datos.append(mapaServicios.get("codrompimiento_"+i)+" "+mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("codigopropietario_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si viene un nuevo convenio y contrato
					else
					{
						if((mapaServicios.get("convenio_"+i)+"").equals(mapaServicios.get("convenio_"+(i-1))+"") && !(mapaServicios.get("contrato_"+i)+"").equals(mapaServicios.get("contrato_"+(i-1))+""))
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
							datos.append(mapaServicios.get("codrompimiento_"+i)+" "+mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("codigopropietario_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
						else
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
							datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
							datos.append(mapaServicios.get("codrompimiento_"+i)+" "+mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("codigopropietario_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
					}
				}
			}
			else
			{
				//Organizamos los datos para generar el Archivo Plano. Primero el mapa de Servicios
				for(int i=0; i<Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+""); i++)
				{
					if(i == 0)
					{
						datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
						datos.append(mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
						//Voy Sumando los valores totales del ingreso
						totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Voy sumando el total por convenio
						totalConvenio = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si el nombre del convenio y el contrato vienen iguales se deben de mostrar los otros datos sin el convenio y el contrato
					else if ((mapaServicios.get("desccontrato_"+i)+"").equals(mapaServicios.get("desccontrato_"+(i-1))+""))
					{
						//Suma el valor total para los otros registros del mismo convenio y el mismo contrato
						totalValorTotal = totalValorTotal + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						//Suma el total por convenio
						totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
						datos.append(mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si viene un nuevo convenio y contrato
					else
					{
						if((mapaServicios.get("convenio_"+i)+"").equals(mapaServicios.get("convenio_"+(i-1))+"") && !(mapaServicios.get("contrato_"+i)+"").equals(mapaServicios.get("contrato_"+(i-1))+""))
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
							datos.append(mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");//Voy Sumando los valores totales del ingreso
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
						else
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
							datos.append(mapaServicios.get("desccontrato_"+i)+"\n");
							datos.append(mapaServicios.get("nomrompimiento_"+i)+", "+mapaServicios.get("viaingreso_"+i)+", "+mapaServicios.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+""))+"\n");//Voy Sumando los valores totales del ingreso
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = Utilidades.convertirADouble(mapaServicios.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaServicios.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaServicios.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaServicios.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
					}
				}
			}
			//*******************************************************FIN SERVICIOS*******************************************************************
		}
		
		if(Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") > 0)
		{
			//******************************************************INICIO ARTICULOS*****************************************************************
			datos.append(encabezadoArticulos+"\n");
			//Se valida si el rompimiento es por articulo gracias a que usa otro formato distinto a la clase de inventario y a la naturaleza
			if(radioArticulos.equals("3"))
			{
				//Organizamos los datos para generar el Archivo Plano. Segundo el mapa de Articulos
				for(int i=0; i<Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+""); i++)
				{
					if(i == 0)
					{
						datos.append(mapaArticulos.get("desccontrato_"+i)+"\n");
						datos.append(mapaArticulos.get("codrompimiento_"+i)+" "+mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("concentracion_"+i)+", "+mapaArticulos.get("forma_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
						//Voy Sumando los valores totales del ingreso
						totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						//Voy sumando el total por convenio
						totalConvenio = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si el nombre del convenio y el contrato vienen iguales se deben de mostrar los otros datos sin el convenio y el contrato
					else if ((mapaArticulos.get("desccontrato_"+i)+"").equals(mapaArticulos.get("desccontrato_"+(i-1))+""))
					{
						//Suma el valor total para los otros registros del mismo convenio y el mismo contrato
						totalValorTotal = totalValorTotal + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						//Suma el total por convenio
						totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");						
						datos.append(mapaArticulos.get("codrompimiento_"+i)+" "+mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("concentracion_"+i)+", "+mapaArticulos.get("forma_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si viene un nuevo convenio y contrato
					else
					{
						if((mapaArticulos.get("convenio_"+i)+"").equals(mapaArticulos.get("convenio_"+(i-1))+"") && !(mapaArticulos.get("contrato_"+i)+"").equals(mapaArticulos.get("contrato_"+(i-1))+""))
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append(mapaArticulos.get("desccontrato_"+i)+"\n");
							datos.append(mapaArticulos.get("codrompimiento_"+i)+" "+mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("concentracion_"+i)+", "+mapaArticulos.get("forma_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
						else
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
							datos.append(mapaArticulos.get("desccontrato_"+i)+"\n");
							datos.append(mapaArticulos.get("codrompimiento_"+i)+" "+mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("concentracion_"+i)+", "+mapaArticulos.get("forma_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
					}
				}
			}
			else
			{
				//Organizamos los datos para generar el Archivo Plano. Segundo el mapa de Articulos
				for(int i=0; i<Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+""); i++)
				{
					if(i == 0)
					{
						datos.append(mapaArticulos.get("desccontrato_"+i)+"\n");
						datos.append(mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
						//Voy Sumando los valores totales del ingreso
						totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						//Voy sumando el total por convenio
						totalConvenio = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");						
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si el nombre del convenio y el contrato vienen iguales se deben de mostrar los otros datos sin el convenio y el contrato
					else if ((mapaArticulos.get("desccontrato_"+i)+"").equals(mapaArticulos.get("desccontrato_"+(i-1))+""))
					{
						//Suma el valor total para los otros registros del mismo convenio y el mismo contrato
						totalValorTotal = totalValorTotal + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						//Suma el total por convenio
						totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
						datos.append(mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
						//Si no existen mas registros se muestran los totales respectivos
						if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						{
					 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
					 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						}
					}
					//Si viene un nuevo convenio y contrato
					else
					{
						if((mapaArticulos.get("convenio_"+i)+"").equals(mapaArticulos.get("convenio_"+(i-1))+"") && !(mapaArticulos.get("contrato_"+i)+"").equals(mapaArticulos.get("contrato_"+(i-1))+""))
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append(mapaArticulos.get("desccontrato_"+(i-1))+"\n");
							datos.append(mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = totalConvenio + Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
						else
						{
							//Imprimo el Total por contrato y convenio antes de imprimir el otro registro
							datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
							datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+(i-1))+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
							datos.append(mapaArticulos.get("desccontrato_"+(i-1))+"\n");
							datos.append(mapaArticulos.get("nomrompimiento_"+i)+", "+mapaArticulos.get("viaingreso_"+i)+", "+mapaArticulos.get("cantidad_"+i)+", "+UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+""))+"\n");
							//Voy Sumando los valores totales del ingreso
							totalValorTotal = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Suma el total por convenio
							totalConvenio = Utilidades.convertirADouble(mapaArticulos.get("valortotal_"+i)+"");
							//Si es la ultima fila con mas de un registro por contrato se muestra el total de la ultima via ingreso y se realiza el totalizado por el convenio
						 	if(i == (Utilidades.convertirAEntero(mapaArticulos.get("numRegistros")+"") - 1))
						 	{
						 		datos.append("Subtotal por Convenio "+mapaArticulos.get("desccontrato_"+i)+", "+UtilidadTexto.formatearExponenciales(totalValorTotal)+"\n");
						 		datos.append("Total Convenio "+mapaArticulos.get("nomconvenio_"+i)+", "+UtilidadTexto.formatearExponenciales(totalConvenio)+"\n");
						 	}
						}
					}
				}
			}
			//*******************************************************FIN ARTICULOS*******************************************************************
		}
		return datos;
	}
	
}