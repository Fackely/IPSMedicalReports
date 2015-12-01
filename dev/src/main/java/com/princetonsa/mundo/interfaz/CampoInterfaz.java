/*
 * Creado en Apr 26, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import java.util.HashMap;
import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.interfaz.CampoInterfazForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.CampoInterfazDao;
import com.princetonsa.mundo.UsuarioBasico;

public class CampoInterfaz
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CampoInterfaz.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CampoInterfazDao  campoInterfazDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Nombre del archivo plano de salida
	 */
	private String nombreArchivoSalida;
	
	/**
	 * Path de ubicación del archivo de salida.
	 */
	private String pathArchivoSalida;
	
	/**
	 * Nombre del archivo plano de inconsistencias
	 */
	private String nombreArchivoInconsistencias;
	
	/**
	 * Path de ubicación del archivo de inconsistencias
	 */
	private String pathArchivoInconsistencias;
	
	/**
	 * Caracter utilizado para la separación de los campos
	 */
	private String separadorCampos;
	
	/**
	 * Separador de los decimales en cifras de valor
	 */
	private int separadorDecimales;
	
	/**
	 * Nombre del tipo de separador de decimales para poder mostrarlo
	 * en el log 
	 */
	private String nombreSeparadorDecimales;
	
	/**
	 * Identificador fin de archivo
	 */
	private String identificadorFinArchivo;
	
	/**
	 * Presenta Devolución Paciente en el mismo campo del valor Paciente
	 */
	private String presentaDevolucionPaciente;
	
	/**
	 * Campo para indicar si en los casos de devolución paciente se muestra el valor con negativo
	 */
	private String valorNegativoDevolPaciente;
	
	/**
	 * Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Débito
	 */
	private String descripcionDebito;
	
	/**
	 * Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Crédito
	 */
	private String descripcionCredito;
	
	/**
	 * Mapa para guardar información de parametrización de cada uno de los campos
	 */
	private HashMap mapa;
//	---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public CampoInterfaz ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			campoInterfazDao = myFactory.getCampoInterfazDao();
			wasInited = (campoInterfazDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método para cargar la información general de la parametrización de campos interfaz 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public boolean cargarInformacionGral (Connection con, int institucion)
	{
		CampoInterfazDao campointerfazDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCampoInterfazDao();
		Collection coleccion=null;
		
		try
		{
			coleccion=campointerfazDao.cargarInformacionGral(con, institucion);
			Iterator ite=coleccion.iterator();
			
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				this.nombreArchivoSalida =(col.get("nombre_archivo_salida")+"").equals("null")  ? "" : (col.get("nombre_archivo_salida")+"");
				this.pathArchivoSalida =(col.get("path_archivo_salida")+"").equals("null")  ? "" : (col.get("path_archivo_salida")+"");
				this.nombreArchivoInconsistencias =(col.get("nombre_archivo_incons")+"").equals("null")  ? "" : (col.get("nombre_archivo_incons")+"");
				this.pathArchivoInconsistencias =(col.get("path_archivo_incons")+"").equals("null")  ? "" : (col.get("path_archivo_incons")+"");
				this.separadorCampos =(col.get("separador_campos")+"").equals("null")  ? "" : (col.get("separador_campos")+"");
				this.separadorDecimales = Integer.parseInt(col.get("separador_decimales")+"");
				this.nombreSeparadorDecimales =(col.get("nom_separador_decimales")+"").equals("null")  ? "" : (col.get("nom_separador_decimales")+"");
				this.identificadorFinArchivo =(col.get("identificador_fin_archivo")+"").equals("null")  ? "" : (col.get("identificador_fin_archivo")+"");
				
				if (UtilidadTexto.getBoolean(col.get("pres_devolucion_paciente")+""))
					this.presentaDevolucionPaciente=ValoresPorDefecto.getValorTrueParaConsultas();
				else
					this.presentaDevolucionPaciente=ValoresPorDefecto.getValorFalseParaConsultas();
				
				if (UtilidadTexto.getBoolean(col.get("valor_negativo_devol_pac")+""))
					this.valorNegativoDevolPaciente=ValoresPorDefecto.getValorTrueParaConsultas();
				else
					this.valorNegativoDevolPaciente=ValoresPorDefecto.getValorFalseParaConsultas();
				
				this.descripcionDebito =(col.get("descripcion_debito")+"").equals("null")  ? "" : (col.get("descripcion_debito")+"");
				this.descripcionCredito=(col.get("descripcion_credito")+"").equals("null")  ? "" : (col.get("descripcion_credito")+"");
				
				return true;
			}
			else
			{
			 return false;	
			}
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la información general (CampoInterfaz)" +e.toString());
		  coleccion=null;
		}
		 return false;		
	}
	
	/**
	 * Método para guardar la información general de la parametrización de los campos de interfaz
	 * @param con
	 * @param usuario
	 * @param forma
	 * @throws SQLException 
	 */
	public boolean insertarActualizarInfoGral(Connection con, UsuarioBasico usuario, CampoInterfazForm forma) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
				
		if (campoInterfazDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CampoInterfazDao - insertarModificarInfoGral )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//------------Si HayInfoGral está en true es porque hay información general ya ingresada entonces se debe modificar -------------//
		if (forma.isHayInfoGral())
		{
			//-----------Se verifica si se cambió algo en la información general para actualizar --------------//
			if (!forma.getNombreArchivoSalida().equals(forma.getNombreArchivoSalidaAnt()) || !forma.getPathArchivoSalida().equals(forma.getPathArchivoSalidaAnt()) 
			   || !forma.getNombreArchivoInconsistencias().equals(forma.getNombreArchivoInconsistenciasAnt()) || !forma.getPathArchivoInconsistencias().equals(forma.getPathArchivoInconsistenciasAnt())
			   || !forma.getSeparadorCampos().equals(forma.getSeparadorCamposAnt()) || forma.getSeparadorDecimales()!=forma.getSeparadorDecimalesAnt()
			   || !forma.getIdentificadorFinArchivo().equals(forma.getIdentificadorFinArchivoAnt()) 
			   || !forma.getPresentaDevolucionPaciente().equals(forma.getPresentaDevolucionPacienteAnt()) || !forma.getValorNegativoDevolPaciente().equals(forma.getValorNegativoDevolPacienteAnt())
			   || !forma.getDescripcionDebito().equals(forma.getDescripcionDebitoAnt()) || !forma.getDescripcionCredito().equals(forma.getDescripcionCreditoAnt()))
			{
				
				//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
				   StringBuffer log=new StringBuffer();
				   log.append("\n=================MODIFICACIÓN PARAMETRIZACIÓN CAMPOS INTERFAZ====================");
				   log.append("\n INSTITUCION :  " +usuario.getInstitucion()+"\n");
				   log.append("\n ********* INFORMACIÓN GENERAL DEL ARCHIVO ********** \n");
				   
				   if (!forma.getNombreArchivoSalida().equals(forma.getNombreArchivoSalidaAnt()))
				   {
					   log.append("\n NOMBRE DEL ARCHIVO PLANO DE SALIDA ANTERIOR :"+forma.getNombreArchivoSalidaAnt());
					   log.append("\n NOMBRE DEL ARCHIVO PLANO DE SALIDA NUEVO :"+forma.getNombreArchivoSalida()+"\n");
				   }
				   
				   if (!forma.getPathArchivoSalida().equals(forma.getPathArchivoSalidaAnt()))
				   {
					   log.append("\n PATH UBICACIÓN ARCHIVO DE SALIDA ANTERIOR :"+forma.getPathArchivoSalidaAnt());
					   log.append("\n PATH UBICACIÓN ARCHIVO DE SALIDA NUEVO :"+forma.getPathArchivoSalida()+"\n");
				   }
				   
				   if (!forma.getNombreArchivoInconsistencias().equals(forma.getNombreArchivoInconsistenciasAnt()))
				   {
					   log.append("\n NOMBRE DEL ARCHIVO INCONSISTENCIAS ANTERIOR :"+forma.getNombreArchivoInconsistenciasAnt());
					   log.append("\n NOMBRE DEL ARCHIVO INCONSISTENCIAS NUEVO :"+forma.getNombreArchivoInconsistencias()+"\n");
				   }
				   
				   if (!forma.getPathArchivoInconsistencias().equals(forma.getPathArchivoInconsistenciasAnt()))
				   {
					   log.append("\n PATH UBICACIÓN ARCHIVO DE INCONSISTENCIAS ANTERIOR :"+forma.getPathArchivoInconsistenciasAnt());
					   log.append("\n PATH UBICACIÓN ARCHIVO DE INCONSISTENCIAS NUEVO :"+forma.getPathArchivoInconsistencias()+"\n");
				   }
				   
				   if (!forma.getSeparadorCampos().equals(forma.getSeparadorCamposAnt()))
				   {
					   log.append("\n SEPARADOR DE LOS CAMPOS ANTERIOR :"+forma.getSeparadorCamposAnt());
					   log.append("\n SEPARADOR DE LOS CAMPOS NUEVO :"+forma.getSeparadorCampos()+"\n");
				   }
				   
				   if (forma.getSeparadorDecimales()!=forma.getSeparadorDecimalesAnt())
				   {
					   log.append("\n SEPARADOR DE LOS DECIMALES ANTERIOR :"+forma.getNombreSeparadorDecimalesAnt());
					   log.append("\n SEPARADOR DE LOS DECIMALES NUEVO :"+forma.getNombreSeparadorDecimales()+"\n");
				   }
				   
				   if (!forma.getIdentificadorFinArchivo().equals(forma.getIdentificadorFinArchivoAnt()))
				   {
					   log.append("\n IDENTIFICAR DE FIN DE ARCHIVO ANTERIOR :"+forma.getIdentificadorFinArchivoAnt());
					   log.append("\n IDENTIFICAR DE FIN DE ARCHIVO  NUEVO :"+forma.getIdentificadorFinArchivo()+"\n");
				   }
				   
				 /*  if (!forma.getFacturasAnuladas().equals(forma.getFacturasAnuladasAnt()))
				   {
					   if (UtilidadTexto.getBoolean(forma.getFacturasAnuladasAnt()))
						    log.append("\n INCLUYE FACTURAS ANULADAS ANTERIOR : [SI]");
					   else
						   log.append("\n INCLUYE FACTURAS ANULADAS ANTERIOR : [NO]");
					   
					   if (UtilidadTexto.getBoolean(forma.getFacturasAnuladas()))
						    log.append("\n INCLUYE FACTURAS ANULADAS NUEVA : [SI] \n");
					   else
						   log.append("\n INCLUYE FACTURAS ANULADAS NUEVA : [NO] \n");
				   }*/
				   
				   if (!forma.getPresentaDevolucionPaciente().equals(forma.getPresentaDevolucionPacienteAnt()))
				   {
					   if (UtilidadTexto.getBoolean(forma.getPresentaDevolucionPacienteAnt()))
						    log.append("\n PRESENTAR DEVOLUCIÓN PACIENTE ANTERIOR : [SI]");
					   else
						   log.append("\n PRESENTAR DEVOLUCIÓN PACIENTE ANTERIOR : [NO]");
					   
					   if (UtilidadTexto.getBoolean(forma.getPresentaDevolucionPaciente()))
						    log.append("\n PRESENTAR DEVOLUCIÓN PACIENTE NUEVA : [SI] \n");
					   else
						   log.append("\n PRESENTAR DEVOLUCIÓN PACIENTE NUEVA : [NO] \n");
				   }
				   
				   if (!forma.getValorNegativoDevolPaciente().equals(forma.getValorNegativoDevolPacienteAnt()))
				   {
					   if (UtilidadTexto.getBoolean(forma.getValorNegativoDevolPacienteAnt()))
						    log.append("\n VALOR NEGATIVO EN DEVOLUCIÓN PACIENTE ANTERIOR : [SI]");
					   else
						   log.append("\n VALOR NEGATIVO EN DEVOLUCIÓN PACIENTE ANTERIOR: [NO]");
					   
					   if (UtilidadTexto.getBoolean(forma.getValorNegativoDevolPaciente()))
						    log.append("\n VALOR NEGATIVO EN DEVOLUCIÓN PACIENTE NUEVA : [SI] \n");
					   else
						   log.append("\n VALOR NEGATIVO EN DEVOLUCIÓN PACIENTE NUEVA : [NO] \n");
				   }
				   
				   if (!forma.getDescripcionDebito().equals(forma.getDescripcionDebitoAnt()))
				   {
					   log.append("\n DESCRIPCIÓN MOVIMIENTO DÉBITO ANTERIOR :"+forma.getDescripcionDebitoAnt());
					   log.append("\n DESCRIPCIÓN MOVIMIENTO DÉBITO NUEVO :"+forma.getDescripcionDebito()+"\n");
				   }
				   
				   if (!forma.getDescripcionCredito().equals(forma.getDescripcionCreditoAnt()))
				   {
					   log.append("\n DESCRIPCIÓN MOVIMIENTO CRÉDITO ANTERIOR :"+forma.getDescripcionCreditoAnt());
					   log.append("\n DESCRIPCIÓN MOVIMIENTO CRÉDITO NUEVO :"+forma.getDescripcionCredito()+"\n");
				   }
				   
				   /*if (!forma.getAgruparFacturasValor().equals(forma.getAgruparFacturasValorAnt()))
				   {
					   if (UtilidadTexto.getBoolean(forma.getAgruparFacturasValorAnt()))
						    log.append("\n AGRUPAR LAS FACTURAS POR CAMPO TIPO VALOR ANTERIOR : [SI]");
					   else
						   log.append("\n AGRUPAR LAS FACTURAS POR CAMPO TIPO VALOR ANTERIOR: [NO]");
					   
					   if (UtilidadTexto.getBoolean(forma.getAgruparFacturasValor()))
						    log.append("\n AGRUPAR LAS FACTURAS POR CAMPO TIPO VALOR NUEVA : [SI] \n");
					   else
						   log.append("\n AGRUPAR LAS FACTURAS POR CAMPO TIPO VALOR NUEVA : [NO] \n");
				   }*/
				    
				   log.append("\n========================================================================");
				   //-Generar el log 
				   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactParamInfoGralCamposInterfazModCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
				
				resp=campoInterfazDao.insertarActualizarInfoGral (con, false, usuario.getCodigoInstitucionInt(), this.nombreArchivoSalida, this.pathArchivoSalida, this.nombreArchivoInconsistencias, this.pathArchivoInconsistencias, this.separadorCampos, this.separadorDecimales, this.identificadorFinArchivo, this.presentaDevolucionPaciente, this.valorNegativoDevolPaciente, this.descripcionDebito, this.descripcionCredito);
			}
		}
		else
		{
			resp=campoInterfazDao.insertarActualizarInfoGral (con, true, usuario.getCodigoInstitucionInt(), this.nombreArchivoSalida, this.pathArchivoSalida, this.nombreArchivoInconsistencias, this.pathArchivoInconsistencias, this.separadorCampos, this.separadorDecimales, this.identificadorFinArchivo, this.presentaDevolucionPaciente, this.valorNegativoDevolPaciente, this.descripcionDebito, this.descripcionCredito);
		}
		
		//------Si existió algún error se aborta la transacción sino se finaliza normalmente la transacción----//
		if (!inicioTrans || resp < 0)
			{
				myFactory.abortTransaction(con);
				return false;
			}
		else
			{
			    myFactory.endTransaction(con);
			}
		
		return true;
		
	}
	
	/**
	 * Método para consultar los campos de interfaz parametrizados hasta el momento para 
	 * la institución
	 * @param con
	 * @param institucion
	 * @return HashMap 
	 */
	
	public HashMap consultarCamposInterfaz(Connection con, int institucion)
	{
		return campoInterfazDao.consultarCamposInterfaz(con, institucion);
	}
	
	/**
	 * Método para insertar/actualizar la información ingresada en la segunda sección
	 * de campos de interfaz
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarActualizarCamposInterfaz(Connection con, UsuarioBasico usuario) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
		boolean error=false;
				
		if (campoInterfazDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CampoInterfazDao - insertarActualizarCamposInterfaz )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);	
		
		if(this.getMapa("codsCampoInterfaz") != null)
		{
			String codsCampoInterfaz=this.getMapa("codsCampoInterfaz")+"";
			String[] vecNuevosCampos=codsCampoInterfaz.split("-");
			String actualizar=this.getMapa("actualizar")+"";
			String[] vecActualizar=actualizar.split("-");
			
			
			for (int i=0; i<vecNuevosCampos.length; i++)
				{
				//-------- Codigo de la fila del nuevo artículo-------------//
				int codFilaCampo=Integer.parseInt(vecNuevosCampos[i]);
				int codAccionActualizar=Integer.parseInt(vecActualizar[i]);
				
				//----------Valor del tipo de campo interfaz--------//
				int tipoCampoInterfaz=Integer.parseInt(this.getMapa("tipo_campo_interfaz_"+codFilaCampo)+"");
				
				//---------Valor del orden del campo-------------------//
				int ordenCampo=Integer.parseInt(this.getMapa("orden_campo_"+codFilaCampo)+"");
				
				//----------Valor del indicativo si es requerido ------------------//
				String indicativoRequerido = (String)this.getMapa("indicativo_requerido_"+codFilaCampo);
				
				//--------------Se verifica si se debe actualizar o insertar el nuevo campo ----------//
				if (codAccionActualizar==1)
				{
					//----------------Se verifica si se cambió algún valor en el registro del campo para realizar la actualización---------//
					int tipoCampoInterfazAnt=Integer.parseInt(this.getMapa("tipo_campo_interfazAnt_"+codFilaCampo)+"");
					int ordenCampoAnt=Integer.parseInt(this.getMapa("orden_campoAnt_"+codFilaCampo)+"");
					String indicativoRequeridoAnt = (String)this.getMapa("indicativo_requeridoAnt_"+codFilaCampo);
										
					//------------Se verifica si cambió algún valor del campo y se actualiza -------------//
					if (tipoCampoInterfazAnt != tipoCampoInterfaz || ordenCampoAnt != ordenCampo || !indicativoRequeridoAnt.equals(indicativoRequerido))
						{
						//-----------------------------------------------GENERACION DEL LOG AL MODIFICAR --------------------------------------------------//
						   StringBuffer log=new StringBuffer();
						   log.append("\n===============MODIFICACIÓN PARAMETRIZACIÓN CAMPOS INTERFAZ================");
						   log.append("\n INSTITUCION :  " +usuario.getInstitucion()+"\n");
						   log.append("\n ********* CAMPOS DE LOS REGISTROS ********** \n");
						   
						   if (tipoCampoInterfazAnt != tipoCampoInterfaz)
							   {
							   		String nombreTipoCampoAnt = (String)this.getMapa("nombre_tipo_campoAnt_"+codFilaCampo);
							   		String nombreTipoCampoNuevo = (String)this.getMapa("nombre_tipo_campo_"+codFilaCampo);
								   
							   		log.append("\n TIPO DE CAMPO ANTERIOR :"+nombreTipoCampoAnt);
								    log.append("\n TIPO DE CAMPO NUEVO :"+nombreTipoCampoNuevo+"\n");
							   }
						   
						   if (ordenCampoAnt != ordenCampo)
							   {
								   log.append("\n ORDEN CAMPO ANTERIOR :"+ordenCampoAnt);
								   log.append("\n ORDEN CAMPO NUEVO :"+ordenCampo+"\n");
							   }
						   
						   if (!indicativoRequeridoAnt.equals(indicativoRequerido))
							   {
								   if (UtilidadTexto.getBoolean(indicativoRequeridoAnt))
									    log.append("\n INDICATIVO REQUERIDO ANTERIOR : [SI]");
								   else
									   log.append("\n INDICATIVO REQUERIDO ANTERIOR : [NO]");
								   
								   if (UtilidadTexto.getBoolean(indicativoRequerido))
									    log.append("\n INDICATIVO REQUERIDO NUEVA : [SI] \n");
								   else
									   log.append("\n INDICATIVO REQUERIDO NUEVA : [NO] \n");
							   }
						
						   log.append("\n==================================================================");
						   //-Generar el log 
						   LogsAxioma.enviarLog(ConstantesBD.logInterfazFactParamInfoGralCamposInterfazModCodigo, log.toString(), ConstantesBD.tipoRegistroLogModificacion, usuario.getLoginUsuario());
						
						resp=campoInterfazDao.insertarActualizarCamposInterfaz (con, false, usuario.getCodigoInstitucionInt(), tipoCampoInterfaz, ordenCampo, indicativoRequerido, ordenCampoAnt);
						if (resp <0)
							{
								error=true;
								break;
							}
						}
				}
				else
				{
					//-----------Se inserta el nuevo campo parametrizado-----------------------//
					resp=campoInterfazDao.insertarActualizarCamposInterfaz (con, true, usuario.getCodigoInstitucionInt(), tipoCampoInterfaz, ordenCampo, indicativoRequerido, -1);
					if (resp <0)
						{
							error=true;
							break;
						}
				}
				
				//----------
					
				}//for
		}//if codsCampoInterfaz!=null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
			{
				myFactory.abortTransaction(con);
				return false;
			}
		else
			{
			    myFactory.endTransaction(con);
			}
		
		return true;
	}
//	---------------------------------------------------- SETS Y GETS -------------------------------------------------------------------------//
	
	/**
	 * @return Retorna the descripcionCredito.
	 */
	public String getDescripcionCredito()
	{
		return descripcionCredito;
	}

	/**
	 * @param descripcionCredito The descripcionCredito to set.
	 */
	public void setDescripcionCredito(String descripcionCredito)
	{
		this.descripcionCredito = descripcionCredito;
	}

	/**
	 * @return Retorna the descripcionDebito.
	 */
	public String getDescripcionDebito()
	{
		return descripcionDebito;
	}

	/**
	 * @param descripcionDebito The descripcionDebito to set.
	 */
	public void setDescripcionDebito(String descripcionDebito)
	{
		this.descripcionDebito = descripcionDebito;
	}

	/**
	 * @return Retorna the identificadorFinArchivo.
	 */
	public String getIdentificadorFinArchivo()
	{
		return identificadorFinArchivo;
	}

	/**
	 * @param identificadorFinArchivo The identificadorFinArchivo to set.
	 */
	public void setIdentificadorFinArchivo(String identificadorFinArchivo)
	{
		this.identificadorFinArchivo = identificadorFinArchivo;
	}

	/**
	 * @return Retorna the mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}

	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}

	/**
	 * @return Retorna the nombreArchivoInconsistencias.
	 */
	public String getNombreArchivoInconsistencias()
	{
		return nombreArchivoInconsistencias;
	}

	/**
	 * @param nombreArchivoInconsistencias The nombreArchivoInconsistencias to set.
	 */
	public void setNombreArchivoInconsistencias(String nombreArchivoInconsistencias)
	{
		this.nombreArchivoInconsistencias = nombreArchivoInconsistencias;
	}

	/**
	 * @return Retorna the nombreArchivoSalida.
	 */
	public String getNombreArchivoSalida()
	{
		return nombreArchivoSalida;
	}

	/**
	 * @param nombreArchivoSalida The nombreArchivoSalida to set.
	 */
	public void setNombreArchivoSalida(String nombreArchivoSalida)
	{
		this.nombreArchivoSalida = nombreArchivoSalida;
	}

	/**
	 * @return Retorna the pathArchivoInconsistencias.
	 */
	public String getPathArchivoInconsistencias()
	{
		return pathArchivoInconsistencias;
	}

	/**
	 * @param pathArchivoInconsistencias The pathArchivoInconsistencias to set.
	 */
	public void setPathArchivoInconsistencias(String pathArchivoInconsistencias)
	{
		this.pathArchivoInconsistencias = pathArchivoInconsistencias;
	}

	/**
	 * @return Retorna the pathArchivoSalida.
	 */
	public String getPathArchivoSalida()
	{
		return pathArchivoSalida;
	}

	/**
	 * @param pathArchivoSalida The pathArchivoSalida to set.
	 */
	public void setPathArchivoSalida(String pathArchivoSalida)
	{
		this.pathArchivoSalida = pathArchivoSalida;
	}

	/**
	 * @return Retorna the presentaDevolucionPaciente.
	 */
	public String getPresentaDevolucionPaciente()
	{
		return presentaDevolucionPaciente;
	}

	/**
	 * @param presentaDevolucionPaciente The presentaDevolucionPaciente to set.
	 */
	public void setPresentaDevolucionPaciente(String presentaDevolucionPaciente)
	{
		this.presentaDevolucionPaciente = presentaDevolucionPaciente;
	}

	/**
	 * @return Retorna the separadorCampos.
	 */
	public String getSeparadorCampos()
	{
		return separadorCampos;
	}

	/**
	 * @param separadorCampos The separadorCampos to set.
	 */
	public void setSeparadorCampos(String separadorCampos)
	{
		this.separadorCampos = separadorCampos;
	}

	/**
	 * @return Retorna the separadorDecimales.
	 */
	public int getSeparadorDecimales()
	{
		return separadorDecimales;
	}

	/**
	 * @param separadorDecimales The separadorDecimales to set.
	 */
	public void setSeparadorDecimales(int separadorDecimales)
	{
		this.separadorDecimales = separadorDecimales;
	}

	/**
	 * @return Retorna the valorNegativoDevolPaciente.
	 */
	public String getValorNegativoDevolPaciente()
	{
		return valorNegativoDevolPaciente;
	}

	/**
	 * @param valorNegativoDevolPaciente The valorNegativoDevolPaciente to set.
	 */
	public void setValorNegativoDevolPaciente(String valorNegativoDevolPaciente)
	{
		this.valorNegativoDevolPaciente = valorNegativoDevolPaciente;
	}

	/**
	 * @return Retorna the nombreSeparadorDecimales.
	 */
	public String getNombreSeparadorDecimales()
	{
		return nombreSeparadorDecimales;
	}

	/**
	 * @param nombreSeparadorDecimales The nombreSeparadorDecimales to set.
	 */
	public void setNombreSeparadorDecimales(String nombreSeparadorDecimales)
	{
		this.nombreSeparadorDecimales = nombreSeparadorDecimales;
	}

	

	
}
