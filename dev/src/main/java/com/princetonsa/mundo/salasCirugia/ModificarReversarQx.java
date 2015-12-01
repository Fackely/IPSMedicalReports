/*
 * Nov 30, 2005
 *
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ModificarReversarQxDao;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 *
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Modificar/Reversar Liquidación Quirúrgica
 */
public class ModificarReversarQx 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ModificarReversarQx.class);
	
	/**
	 * DAO para el manejo de ModificarReversarQxDao
	 */ 
	ModificarReversarQxDao modificarDao = null;
	
	//************ATRIBUTOS******************************************
	
	/**
	 * Código Axioma de la órden Cx.
	 */
	private int numeroSolicitud;
	
	/**
	 * Fecha Inicial de la cirugia
	 */
	private String fechaCirugia;
	
	/**
	 * Login del usuario
	 */
	private String usuario;
	
	/**
	 * Motivo de la reversión
	 */
	private String motivo;
	
	//**************************************************************
	//*********INICIALIZADORES & CONSTRUCTORES**************************
	/**
	 * Constructor
	 */
	public ModificarReversarQx() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.numeroSolicitud = 0;
		this.fechaCirugia = "";
		this.usuario = "";
		this.motivo = "";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (modificarDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			modificarDao = myFactory.getModificarReversarQxDao();
		}		
	}
	//*******************************************************************+
	//******************MÉTODOS***************************************
	
	/**
	 * Método implementado para modificar la liquidacion de una orden quirurgica
	 */
	public boolean modificar(Connection con,HashMap encabezado,HashMap cirugias,HashMap asocios)
	{
		//variables auxiliares
		String auxS0 = "";
		int auxI0 = 0;
		int auxI1 = 0;
		int auxI2 = 0;
		int codigoEnc = 0;
		double auxD0 = 0.0;
		double auxD1 = 0.0;
		boolean exito = true;
		boolean huboModificacion = false;
		boolean huboModificacionProfesional = false;
		
		//*******ACTUALIZACIÓN DEL NÚMERO DE AUTORIZACION***************************
		/*
		Solicitud solicitud = new Solicitud();
		auxS0 = encabezado.get("autorizacion") + "";
		//se verifica si el numero de autorizacion está lleno
		if(!auxS0.equals(""))
			solicitud.actualizarNumeroAutorizacionTransaccional(con,auxS0,this.numeroSolicitud,ConstantesBD.inicioTransaccion);
		else
			UtilidadBD.iniciarTransaccion(con);
		*/
		//****************************************************************************
		
		//*****SE VERIFICA SI HUBO MODIFICACION****************************************
		auxI0 = Integer.parseInt(cirugias.get("numRegistros")+"");
		//iteración de cirugias
		for(int i=0;i<auxI0;i++)
		{
			//obtener numero de asocios de la cirugia
			auxI1 = Integer.parseInt(asocios.get("numAsocios_"+i)+"");
			
			//iteracion de asocios
			for(int j=0;j<auxI1;j++)
			{
				auxD0 = Double.parseDouble(asocios.get("valorAsocio_"+i+"_"+j)+"");
				auxD1 = Double.parseDouble(asocios.get("valorInicialAsocio_"+i+"_"+j)+"");
				
				//se verifica si el valor fue modificado
				if(auxD0!=auxD1)
					huboModificacion = true;
				
				//Se verifica si se modificó profesional o especialidad
				if(
						(Utilidades.convertirAEntero(asocios.get("codigoMedicoAsocio_"+i+"_"+j).toString())>0&&UtilidadTexto.isEmpty(asocios.get("nombreMedicoAsocio_"+i+"_"+j).toString()))
						||
						(Utilidades.convertirAEntero(asocios.get("codigoEspecialidad_"+i+"_"+j).toString())>0&&UtilidadTexto.isEmpty(asocios.get("nombreEspecialidad_"+i+"_"+j).toString()))
					)
					huboModificacionProfesional = true;
			}	
		}
		//*****************************************************************************
		
		if(huboModificacion || huboModificacionProfesional)
		{
			//******SE INSERTA ENCABEZADO DE LOG MODIFICACION******************************
			//Solo se inserta si hubo modificación de tarifa
			if(huboModificacion)
			{
				codigoEnc = modificarDao.insertarEncabezadoLog(con,
						this.numeroSolicitud,
						this.fechaCirugia,
						ConstantesBD.codigoTipoCambioModificacion,
						this.usuario,
						"",
						ConstantesBD.continuarTransaccion);
					//se verifica éxito de transaccion
					if(codigoEnc<=0)
						exito = false;
			}
			//*****************************************************************************
			
			//******SE ACTUALIZA EL VALOR DE LOS ASOCIOS*********************************
			if(exito)
			{
				auxI0 = Integer.parseInt(cirugias.get("numRegistros")+"");
				//iteración de cirugias
				for(int i=0;i<auxI0;i++)
				{
					//obtener numero de asocios de la cirugia
					auxI1 = Integer.parseInt(asocios.get("numAsocios_"+i)+"");
					
					//iteracion de asocios
					for(int j=0;j<auxI1;j++)
					{
						auxD0 = Double.parseDouble(asocios.get("valorAsocio_"+i+"_"+j)+"");
						auxD1 = Double.parseDouble(asocios.get("valorInicialAsocio_"+i+"_"+j)+"");
						
						//se verifica si el valor fue modificado
						if(auxD0!=auxD1)
						{
							//se actualiza valor del asocio en la estructura antigua
							auxI2 = modificarDao.actualizarValorAsocio(con,
								Integer.parseInt(asocios.get("codigoAxiomaAsocio_"+i+"_"+j)+""),
								auxD0,
								asocios.get("tipoServicioAsocio_"+i+"_"+j).toString(),
								ConstantesBD.continuarTransaccion);
							
							//se verifica exito de la actualización
							if(auxI2<=0)
								exito = false;
							
							//se actualiza valor del asocio en la estructura de cargos
							auxI2 = actualizarValorAsocioEnCargos(
								con, 
								this.numeroSolicitud+"", 
								cirugias.get("codigoServicio_"+i).toString(), 
								asocios.get("codigoAxiomaAsocio_"+i+"_"+j).toString(), 
								asocios.get("tipoServicioAsocio_"+i+"_"+j).toString(),
								asocios.get("valorAsocio_"+i+"_"+j).toString()
							);
							
							//se verifica exito de la actualización
							if(auxI2<=0)
								exito = false;
							
							//**********INSERCION DEL DETALLE DEL LOG******************************
							auxI2 = modificarDao.insertarDetalleLog(con,
									codigoEnc,
									Integer.parseInt(cirugias.get("numeroServicio_"+i)+""),
									Integer.parseInt(cirugias.get("codigoServicio_"+i)+""),
									cirugias.get("descripcionServicio_"+i)+"",
									Integer.parseInt(asocios.get("codigoServicioAsocio_"+i+"_"+j)+""),
									asocios.get("nombreServicioAsocio_"+i+"_"+j)+"",
									Integer.parseInt(asocios.get("codigoAsocio_"+i+"_"+j)+""),
									asocios.get("nombreAsocio_"+i+"_"+j)+"",
									Double.parseDouble(asocios.get("valorInicialAsocio_"+i+"_"+j)+""),
									Double.parseDouble(asocios.get("valorAsocio_"+i+"_"+j)+""),
									asocios.get("codigoAxiomaAsocio_"+i+"_"+j).toString(), 
									asocios.get("tipoServicioAsocio_"+i+"_"+j).toString(),
									ConstantesBD.continuarTransaccion);
								
								//se verifica éxito de transaccion
								if(auxI2<=0)
									exito = false;
							//*********************************************************************
								
							
						}
						
						//**************SE MODIFICA EL PROFESIONAL Y/O ESPECIALIDAD SI SE NECESITABA**************
						if(
								(Utilidades.convertirAEntero(asocios.get("codigoMedicoAsocio_"+i+"_"+j).toString())>0&&UtilidadTexto.isEmpty(asocios.get("nombreMedicoAsocio_"+i+"_"+j).toString()))
								||
								(Utilidades.convertirAEntero(asocios.get("codigoEspecialidad_"+i+"_"+j).toString())>0&&UtilidadTexto.isEmpty(asocios.get("nombreEspecialidad_"+i+"_"+j).toString()))
							)
							if(actualizarProfesionalEspecialidadAsocio(
								con, 
								Utilidades.convertirAEntero(asocios.get("codigoMedicoAsocio_"+i+"_"+j).toString()), 
								Utilidades.convertirAEntero(asocios.get("codigoEspecialidad_"+i+"_"+j).toString()), 
								asocios.get("codigoAxiomaAsocio_"+i+"_"+j).toString())<=0)
								exito = false;
						//*****************************************************************************************
					}	
				}
			}
			//*****************************************************************************
		}
		
		
		
		if(!exito)
			UtilidadBD.abortarTransaccion(con);
		else
			UtilidadBD.finalizarTransaccion(con);
		
		return exito;
	}
	
	
	/**
	 * Método implementado para reversar la liquidacion de una
	 * orden Quirúrgica
	 * @param con
	 * @param cirugias
	 * @param asocios
	 * @return
	 */
	public boolean reversar(Connection con,HashMap cirugias,HashMap asocios)
	{
		int auxI0 = 0;
		int auxI1 = 0;
		int auxI2 = 0;
		int codigoEnc = 0;
		boolean exito = true;
		logger.info("numero de solicitud=> "+numeroSolicitud);
		//*******ACTUALIZAR ESTADO FACTURACION SOLICITUD******************
		
		//Comentado por presentar problema cuando se reversa una solicitud de Cargo Directo de Cirugia
		
		/*Solicitud solicitud = new Solicitud();
		ResultadoBoolean resultado = solicitud.cambiarEstadosSolicitudTransaccional(
			con,this.numeroSolicitud,
			ConstantesBD.codigoEstadoFPendiente,
			ConstantesBD.codigoEstadoHCInterpretada,
			ConstantesBD.inicioTransaccion);
		//se verifica exito de transaccion
		if(!resultado.isTrue())
			exito = false;
		//****************************************************************
		logger.info("Exito actualizacion=> "+resultado.isTrue());
		*/
		
		//**********REVERSAR LIQUIDACION Qx*******************************
		auxI0 = modificarDao.reversarLiquidacionQx(
				con,this.numeroSolicitud,ConstantesBD.continuarTransaccion);
		//se verifica éxito de transaccion
		if(auxI0<=0)
			exito = false;
		//**************************************************************
		logger.info("Exito reversar=> "+auxI0);
		
		//********SE REVERSA EL INDICADOR DEL CONSUMO DE MATERIALES************
		if(LiquidacionServicios.actualizarIndicadorConsumoMaterialesSolicitud(con, this.numeroSolicitud+"", false)<=0)
			exito = false;
		//**********************************************************************
		
		//*************INSERTAR ENCABEZADO LOG REVERSION*********************
		codigoEnc = modificarDao.insertarEncabezadoLog(con,
			this.numeroSolicitud,
			this.fechaCirugia,
			ConstantesBD.codigoTipoCambioReversion,
			this.usuario,
			this.motivo,
			ConstantesBD.continuarTransaccion);
		//se verifica éxito de transaccion
		if(codigoEnc<=0)
			exito = false;
		//*******************************************************************
		logger.info("Exito encabezado Log=> "+codigoEnc);
		
		//*************INSERTAR DETALLE LOG DE REVERSION************************
		if(exito)
		{
			auxI0 = Integer.parseInt(cirugias.get("numRegistros")+"");
			//iteracion de cirugias
			for(int i=0;i<auxI0;i++)
			{
				auxI1 = Integer.parseInt(asocios.get("numAsocios_"+i)+"");
				//iteracion de asocios de la cirugia
				for(int j=0;j<auxI1;j++)
				{
					auxI2 = modificarDao.insertarDetalleLog(con,
						codigoEnc,
						Integer.parseInt(cirugias.get("numeroServicio_"+i)+""),
						Integer.parseInt(cirugias.get("codigoServicio_"+i)+""),
						cirugias.get("descripcionServicio_"+i)+"",
						Integer.parseInt(asocios.get("codigoServicioAsocio_"+i+"_"+j)+""),
						asocios.get("nombreServicioAsocio_"+i+"_"+j)+"",
						Integer.parseInt(asocios.get("codigoAsocio_"+i+"_"+j)+""),
						asocios.get("nombreAsocio_"+i+"_"+j)+"",
						Double.parseDouble(asocios.get("valorInicialAsocio_"+i+"_"+j)+""),
						-1,
						asocios.get("codigoAxiomaAsocio_"+i+"_"+j).toString(), 
						asocios.get("tipoServicioAsocio_"+i+"_"+j).toString(),
						ConstantesBD.continuarTransaccion);
					
					logger.info("Exito insercion detalle Log["+i+","+j+"]=> "+codigoEnc);
					//se verifica éxito de transaccion
					if(auxI2<=0)
						exito = false;
				}
			}
		}
		
		//**************************************************************
		
		if(!exito)
			UtilidadBD.abortarTransaccion(con);
		else
			UtilidadBD.finalizarTransaccion(con);
		
		
		return exito;
	}
	
	/**
	 * Método que genera el LOG de reversión liquidacion Qx.
	 * @param cirugias
	 * @param asocios
	 * @param encabezado
	 */
	public void generarLogReversion(HashMap cirugias,HashMap asocios,HashMap encabezado)
	{
		//variables auxiliares
		int auxI0 = 0;
		if(cirugias.get("numRegistros") != null){
			auxI0=Integer.parseInt(cirugias.get("numRegistros")+"");
		}
		int auxI1 = 0;
		
		String  log="\n            =========INFORMACION ORIGINAL LIQUIDACIÓN QX======== " +
					"\n*  Órden [" +encabezado.get("orden")+"] " +
					"\n*  Fecha/Hora Grabación [" +UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()+"] " +
					"\n*  Usuario [" +this.usuario+"] ";
		
		//iteracion de cirugias
		for(int i=0;i<auxI0;i++)
		{
			log += "\n\n" +
				   "\n  *** CIRUGÍA Nº "+cirugias.get("numeroServicio_"+i)+" ***"+
				   "\n      *  Esquema Tarifario ["+cirugias.get("nombreEsquemaTarifario_"+i)+"]"+
				   "\n      *  Tipo Cirugía ["+cirugias.get("nombreTipoCirugia_"+i)+"]"+
				   "\n      *  Servicio - Cirugía ["+cirugias.get("descripcionServicio_"+i)+"]"+
				   "\n      *  Grupo ó UVR ["+cirugias.get("grupoUvr_"+i)+"]"+
				   "\n      *  Médico ["+cirugias.get("nombreMedicoCx_"+i)+"]"+
				   "\n      *  Vía ["+cirugias.get("nombreViaCx_"+i)+"]";
			
			auxI1 = Integer.parseInt(asocios.get("numAsocios_"+i)+"");
			
			//iteracion de asocios
			for(int j=0;j<auxI1;j++)
			{
				log += "\n\n\n      * "+asocios.get("nombreAsocio_"+i+"_"+j)+
				" : $ "+UtilidadTexto.formatearValores(asocios.get("valorInicialAsocio_"+i+"_"+j)+"",2,true,true)+
				"\n        "+asocios.get("nombreMedicoAsocio_"+i+"_"+j)+"   "+
					asocios.get("nombreEspecialidad_"+i+"_"+j)+"   "+
					asocios.get("nombrePool_"+i+"_"+j)+
				"\n         Servicio - Asocio ["+asocios.get("nombreServicioAsocio_"+i+"_"+j)+"] ";
			}
			
			log += "\n  **********************************************************************";
			
		}
        log+="\n========================================> FUE ELIMINADO\n\n\n " ;	
		
		
		LogsAxioma.enviarLog(ConstantesBD.logReversionLiquidacionQxCodigo,log,ConstantesBD.tipoRegistroLogEliminacion,this.usuario);
	}
	
	
	
	/**
	 * Método que realiza la busqueda de los LOG modificacion/reversion Qx.
	 * de la BD
	 * @param con
	 * @param tipoCambio
	 * @param usuario
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @param centroAtencion
	 * @return
	 */
	public HashMap busquedaGeneralLOG(
			Connection con,int tipoCambio,String usuario,String fechaInicial,
			String fechaFinal,int ordenInicial,int ordenFinal,
			String tipoIdentificacion,String numeroIdentificacion,int centroAtencion)
	{
		HashMap registros=modificarDao.busquedaGeneralLOG(con,tipoCambio,usuario,fechaInicial,fechaFinal,ordenInicial,ordenFinal,tipoIdentificacion,numeroIdentificacion,centroAtencion);
		return this.prepararDatosMapa(registros);
	}
	
	
	/**
	 * Método usado para preparar los datos de cada registro
	 * de un mapa para que tengan la representación ideal en la vista
	 * @param mapa
	 * @return
	 */
	private HashMap prepararDatosMapa(HashMap mapa) 
	{
		int numRegistros=0;
		//se verifica estado del mapa
		if(mapa!=null)
		{
			numRegistros=Integer.parseInt(mapa.get("numRegistros")+"");
			for(int i=0;i<numRegistros;i++)
			{
				//preparación de la hora a 5 caracteres en el campo de fecha_orden
				String[] fechaOrden=(mapa.get("fecha_cambio_"+i)+"").split(" ");
				mapa.put("fecha_cambio_"+i,fechaOrden[0]+" "+UtilidadFecha.convertirHoraACincoCaracteres(fechaOrden[1]));
			}
		}
		
		return mapa;
	}
	
	/**
	 * Método que consulta el detalle del LOG Modificar/Reversar Qx.
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap getDetalleLog(Connection con,int codigoRegistro)
	{
		return modificarDao.getDetalleLog(con,codigoRegistro);
	}
	
	/**
	 * Método implementado para actualizar el pool de un asocio
	 * @param con
	 * @param codigo
	 * @param pool
	 * @return
	 */
	public int actualizarPoolAsocio(Connection con,int codigo,int pool)
	{
		return modificarDao.actualizarPoolAsocio(con,codigo,pool);
	}
	
	/**
	 * Método implementado para cargar los convenios de un asocio y su correspondiente valor del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarConveniosAsocio(Connection con,String numeroSolicitud,String servicioCx,String consecutivoAsocio,String tipoServicio)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		campos.put("servicioCx",servicioCx);
		campos.put("consecutivoAsocio",consecutivoAsocio);
		campos.put("tipoServicio",tipoServicio);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getModificarReversarQxDao().cargarConveniosAsocio(con, campos);
		
	}
	
	/**
	 * Método que realiza la actualizacion del cargo de un asocio
	 * @param con
	 * @param string 
	 * @param campos
	 * @return
	 */
	private int actualizarValorAsocioEnCargos(Connection con,String numeroSolicitud,String servicioCx,String consecutivoAsocio, String tipoServicioAsocio,String valorAsocio)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud", numeroSolicitud);
		campos.put("servicioCx", servicioCx);
		campos.put("consecutivoAsocio", consecutivoAsocio);
		campos.put("tipoServicioAsocio", tipoServicioAsocio);
		campos.put("valorAsocio", valorAsocio);
		return modificarDao.actualizarValorAsocioEnCargos(con, campos);
	}
	
	/**
	 * Método para actualizar el profesional y/o la especialidad del asocio
	 * @param con
	 * @param campos
	 * @return
	 */
	private int actualizarProfesionalEspecialidadAsocio(Connection con,int codigoProfesional,int codigoEspecialidad,String consecutivoAsocio)
	{
		HashMap campos =new HashMap();
		campos.put("codigoProfesional",codigoProfesional);
		campos.put("codigoEspecialidad",codigoEspecialidad);
		campos.put("consecutivoAsocio",consecutivoAsocio);
		return modificarDao.actualizarProfesionalEspecialidadAsocio(con, campos);
	}
	//********************************************************************
	//**************GETTERS & SETTERS*************************************
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the fechaCirugia.
	 */
	public String getFechaCirugia() {
		return fechaCirugia;
	}
	/**
	 * @param fechaCirugia The fechaCirugia to set.
	 */
	public void setFechaCirugia(String fechaCirugia) {
		this.fechaCirugia = fechaCirugia;
	}
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	/**
	 * @return Returns the motivo.
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * @param motivo The motivo to set.
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}
