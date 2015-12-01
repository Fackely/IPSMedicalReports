	package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.GlosasDao;
import com.princetonsa.dto.glosas.DtoConceptoGlosa;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Anexo 686 Registro Auditoría
 * Creado el 6 Enero de 2009
 * @author Felipe Pérez Granda - Sebastián Gómez
 * @mail lfperez@princetonsa.com - sgomez@princetonsa.com
 */

public class Glosas
{
	/* *******************
	 * Atributos de Logger
	 * *******************/
	public static Logger logger = Logger.getLogger(Glosas.class);
	
	public static String[] indicesListadoFacturas = {
		"codigo_",
		"consecutivoFactura_",
		"valorTotal_",
		"fecha_",
		"fechaBd_",
		"nombreViaIngreso_",
		"tipoPaciente_",
		"codigoViaIngreso_",
		"nombrePaciente_",
		"nombreConvenio_",
		"codigoConvenio_",
		"codigoContrato_",
		"observaciones_",
		"codigoAuditoria_",
		"seleccionado_",
		"auditada_",
		"externa_"};
	
	public static String[] indicesListadoFacturasAuditadas = {
		"codigoGlosa_",
		"consecutivoFactura_",
		"fechaElaboracionBd_",
		"fechaElaboracion_",
		"fechaAuditoriaBd_",
		"fechaAuditoria_",
		"nombrePaciente_",
		"nombreConvenio_" +
		"codigoConvenio_",
		"preGlosa_",
		"valorPreGlosa_",
		"valorTotal_"
	};
	
	public static String[] indicesSolicitudesFacturas = {
		"valor_",
		 "consecutivoArticuloServicio_",
		 "descripcionArticuloServicio_",
		 "serv_",
		 "fechaSolicitud_",
		 "solicitud_",
		 "arti_",
		 "codigoArticuloServicio_",
		 "cantidad_",
		 "nombreTipoSolicitud_",
		 "codigoTipoSolicitud_",
		 "detFacturaSolicitud_",
		 "consecutivoSolicitud_",
		 "seleccionado_"
	};
	
	
	/* ***********************
	 * Fin Atributos de Logger
	 * ***********************/
	
	
	/**
	 * Constructor de Glosas
	 */
	public Glosas()
	{
		this.init(System.getProperty("TIPOBD"));
		this.clean();
	}
	
	
	/**
	 * DAO de Glosas
	 */
	private GlosasDao glosasDao = null;
	
	/**
	 * DTo de Glosas
	 */
	private DtoGlosa glosa = new DtoGlosa();
	
	/**
	 * Manejo de los errores
	 */
	private ActionErrors errores = new ActionErrors();
	
	/**
	 * Método que inicializa los datos de la glosa
	 */
	public void clean()
	{
		this.glosa = new DtoGlosa();
		this.errores = new ActionErrors();
	}
	
	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (glosasDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			glosasDao = myFactory.getGlosasDao();
		}
			
	}
	
	/*
	 * Inicialización del DAO Glosas
	 */

	private static GlosasDao glosasDao() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGlosasDao();
	}
	
	/**
	 * Método encargado de consultar las facturas
	 * @author Felipe Pérez Granda - Sebastián Gómez
	 * @param connection
	 * @param criterios
	 * @return HashMap
	 */
	public HashMap<String, Object> consultarFacturas (Connection connection, HashMap<String, Object> criterios)
	{
		return glosasDao().consultarFacturas(connection, criterios);
	}
	
	/**
	 * Método implementado para cargar los datos de una glosa
	 * @author Sebastián Gómez R. - Felipe Pérez Granda
	 * @param connection
	 * @param criterios
	 * @return DtoGlosa
	 */
	public DtoGlosa cargarGlosa (Connection connection, HashMap<String, Object> criterios, UsuarioBasico usuario)
	{
		return glosasDao().cargarGlosa(connection, criterios, usuario );
	}
	
	/**
	 * Método para cargar una glosa sobre el flujo de auditoria
	 * @param con
	 * @param criterios
	 * @param registrarGlosa: para saber si el cargar es para el flujo de registrar o auditar glosas
	 * @return
	 */
	public DtoGlosa cargarGlosaAuditoria(Connection con, String codigoAuditoria, String codigoFactura,UsuarioBasico usuario,boolean registrarGlosa)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigo", codigoAuditoria);
		criterios.put("codigoTarifarioOficial", ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(usuario.getCodigoInstitucionInt()));
		criterios.put("auditoria", codigoFactura);
		
		DtoGlosa glosa = glosasDao.cargarGlosa(con, criterios, usuario);
		
		//*********SE PREPARAN LOS DATOS PARA LA AUDITORIA*******************
		//1) Se verifica si la factura ya tiene conceptos, si no tiene se instancia uno vacío
		if(glosa.getFacturas().get(0).getConceptos().size()==0)
		{
			DtoConceptoGlosa conceptoDevolucion = new DtoConceptoGlosa();
			conceptoDevolucion.setTipo(ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion);
			conceptoDevolucion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			//Se añade un concepto devolucion vacìo por defecto
			glosa.getFacturas().get(0).getConceptos().add(conceptoDevolucion);
			
		}
		else
		{
			if(glosa.getFacturas().get(0).getConceptos().get(0).getTipo().equals(ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion))
			{
				glosa.getFacturas().get(0).setChequeoDevolucion(true);
				glosa.setDevolucionActiva(true);
			}
			else
			{
				glosa.getFacturas().get(0).setChequeoDevolucion(false);
				glosa.setDevolucionActiva(false);
			}		
		}
		
		//2) Se cargan todos los conceptos tipo glosa
		glosa.setConceptos(Utilidades.obtenerConceptosGlosa(con, registrarGlosa?"":ConstantesIntegridadDominio.acronimoTipoGlosaGlosa, usuario.getCodigoInstitucionInt()));
		//*******************************************************************
		
		return glosa;
	}
	
	/**
	 * Método para auditar las facturas seleccionadas
	 * @param con
	 * @param listadoFacturas
	 * @param usuario
	 * @return
	 */
	public ResultadoBoolean auditarFacturasSeleccionadas(Connection con,HashMap<String,Object> listadoFacturas,UsuarioBasico usuario)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		
		for(int i=0;i<Utilidades.convertirAEntero(listadoFacturas.get("numRegistros")+"");i++)
		{
			//Mientras que el resultado sea exitoso se prosigue con la inserciòn de las glosas
			if(resultado.isTrue())
			{
			
				//SE revisa que esté seleccionada la factura y que no sea externa
				//logger.info("seleccionado_"+i+": "+listadoFacturas.get("seleccionado_"+i));
				//logger.info("externa_"+i+": "+listadoFacturas.get("externa_"+i));
				if(UtilidadTexto.getBoolean(listadoFacturas.get("seleccionado_"+i).toString())&&!UtilidadTexto.getBoolean(listadoFacturas.get("externa_"+i).toString()))
				{	
					//Se llena el Dto de glosa
					DtoGlosa glosa = new DtoGlosa();
					
					glosa.setCodigoConvenio(Utilidades.convertirAEntero(listadoFacturas.get("codigoConvenio_"+i).toString()));
					String obs=UtilidadTexto.agregarTextoAObservacionFechaGrabacion("", listadoFacturas.get("observaciones_"+i).toString(), usuario, true);
					glosa.setObservaciones(obs);
					glosa.setUsuarioAuditor(usuario);
					glosa.setFechaAuditoria(UtilidadFecha.getFechaActual(con));
					glosa.setHoraAuditoria(UtilidadFecha.getHoraActual(con));
					glosa.setUsuarioModificacion(usuario);
					glosa.setEstado(ConstantesIntegridadDominio.acronimoEstadoAuditado);
					glosa.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
					glosa.setConsecutivoContrato(listadoFacturas.get("codigoContrato_"+i).toString());
					
					//Se llena el detalle de la factura de la glosa
					DtoFacturaGlosa facturaGlosa = new DtoFacturaGlosa();
					facturaGlosa.setCodigoFactura(listadoFacturas.get("codigo_"+i).toString());
					facturaGlosa.setUsuarioModificacion(usuario);
					facturaGlosa.setCodigoContrato(Utilidades.convertirAEntero(listadoFacturas.get("codigoContrato_"+i).toString()));
					facturaGlosa.setSaldoFacturaStr(listadoFacturas.get("valorTotal_"+i).toString());
					facturaGlosa.setSaldoFactura(Utilidades.convertirADouble(listadoFacturas.get("valorTotal_"+i).toString()));
					facturaGlosa.setFechaElaboracionFactura(listadoFacturas.get("fecha_"+i).toString());
					
					glosa.getFacturas().add(facturaGlosa);
					
					//Se inserta la glosa
					resultado = glosasDao.guardarGlosa(con, glosa, "");
					
				}
			}
		}
		
		return resultado;
	}
	
	
	/**
	 * Método para procesar el cambio de concepto devolucion
	 * @param con
	 * @param glosa
	 * @return
	 */
	public DtoGlosa procesarCambioConceptoDevolucion(Connection con,DtoGlosa glosa, String forma)
	{
		int posFactura = glosa.getPosicionFactura();
		glosa.setDevolucionActiva(false);
		
		//Se verifica si se chequeó una devolucion
		if(glosa.getFacturas().get(posFactura).getChequeoDevolucion())
		{
			//Si ya se había seleccionado un concepto se cargan todas las solicitudes de la factura
			if(!glosa.getFacturas().get(posFactura).getConceptos().get(0).getCodigoConcepto().equals(""))
			{
				glosa = glosasDao.cargarGlosaDevolucion(con, glosa, forma);
				glosa.setDevolucionActiva(true);
			}
			else
			{
				//Se elimina el detalle de la factura
				for(DtoDetalleFacturaGlosa detalle:glosa.getFacturas().get(posFactura).getDetalle())
						detalle.setEliminado(true);
				
				//Se elimnan los conceptos de la factura
				for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
					concepto.setEliminado(true);
			}
		}
		else
		{
			//Se elimina el detalle de la factura
			for(DtoDetalleFacturaGlosa detalle:glosa.getFacturas().get(posFactura).getDetalle())
					detalle.setEliminado(true);
			
			//Se elimnan los conceptos de la factura
			for(DtoConceptoGlosa concepto:glosa.getFacturas().get(posFactura).getConceptos())
				concepto.setEliminado(true);
		}
		
		return glosa;
	}
	
	
	/**
	 * Método para cargar las solicitudes de la factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap<String, Object> cargarSolicitudesFactura(Connection con,int codigoFactura,String codigosDetalleFactura,int codigoTarifarioOficial,int numeroSolicitud,String codigoServicio,String descripcionServicio,int codigoArticulo,String descripcionArticulo, String codigoEstandarBusquedaArticulo)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codigoFactura", codigoFactura);
		criterios.put("codigosDetalleFactura", codigosDetalleFactura);
		criterios.put("codigoTarifarioOficial", codigoTarifarioOficial);
		criterios.put("numeroSolicitud", numeroSolicitud);
		criterios.put("codigoServicio", codigoServicio);
		criterios.put("descripcionServicio", descripcionServicio);
		criterios.put("codigoArticulo", codigoArticulo);
		criterios.put("descripcionArticulo", descripcionArticulo);
		criterios.put("codigoEstandarBusquedaArticulo", codigoEstandarBusquedaArticulo);
		
		return glosasDao.cargarSolicitudesFactura(con, criterios);
	}
	
	/**
	 * Método implementado para agregar mas detalles de la factura como resultado de la
	 * busqueda avanzada de solicitudes
	 * @param con 
	 * @param glosa
	 * @return
	 */
	public DtoGlosa agregarNuevosDetalleFacturaBusqueda(Connection con, DtoGlosa glosa)
	{
		int posicionFactura = glosa.getPosicionFactura();
		
		for(int i=0;i<glosa.getNumBusquedaSolicitudes();i++)
			if(UtilidadTexto.getBoolean(glosa.getBusquedaSolicitudes("seleccionado_"+i).toString()))
			{
				DtoDetalleFacturaGlosa detalle = new DtoDetalleFacturaGlosa();
				detalle.setCodigoDetalleFacturaSolicitud(glosa.getBusquedaSolicitudes("detFacturaSolicitud_"+i).toString());
				detalle.setNumeroSolicitud(glosa.getBusquedaSolicitudes("solicitud_"+i).toString());
				detalle.setConsecutivoOrden(glosa.getBusquedaSolicitudes("consecutivoSolicitud_"+i).toString());
				detalle.setCodigoTipoSolicitud(Integer.parseInt(glosa.getBusquedaSolicitudes("codigoTipoSolicitud_"+i).toString()));
				detalle.setNombreTipoSolicitud(glosa.getBusquedaSolicitudes("nombreTipoSolicitud_"+i).toString());
				detalle.setDescripcionServicioArticulo(glosa.getBusquedaSolicitudes("descripcionArticuloServicio_"+i).toString());
				detalle.setCodigoServicioArticulo(glosa.getBusquedaSolicitudes("consecutivoArticuloServicio_"+i).toString());
				detalle.setCantidad(Integer.parseInt(glosa.getBusquedaSolicitudes("cantidad_"+i).toString()));
				detalle.setCantidadGlosa(detalle.getCantidad());
				detalle.setValorStr(UtilidadTexto.formatearExponenciales(Utilidades.convertirADouble(glosa.getBusquedaSolicitudes("valor_"+i).toString())));
				detalle.setValor(Utilidades.convertirADouble((detalle.getValorStr())));				
				detalle.setValorGlosaStr(detalle.getValorStr());
				detalle.setValorGlosa(detalle.getValor());
				
				if(detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudMedicamentos||detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos)
					detalle.setEsServicio(false);
				else
				{
					if(detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia && !(glosa.getBusquedaSolicitudes("arti_"+i)+"").equals(null) && !(glosa.getBusquedaSolicitudes("arti_"+i)+"").equals(""))
						detalle.setEsServicio(false);
					else
						detalle.setEsServicio(true);
				}
				if(!(glosa.getBusquedaSolicitudes("arti_"+i)+"").equals(null) && !(glosa.getBusquedaSolicitudes("arti_"+i)+"").equals(""))
					detalle.setEsArticulo(true);
				else
					detalle.setEsArticulo(false);
				if(detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia)
					detalle.setAsocios(
						glosasDao.cargarAsociosDetalleFactura(
							con, 
							detalle.getCodigoDetalleFacturaSolicitud(), 
							Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(glosa.getCodigoInstitucion()), true)
						)
					);
				glosa.getFacturas().get(posicionFactura).getDetalle().add(detalle);
			}
		
		return glosa;
	}
	
	/**
	 * Método para auditar la factura de la glosa
	 * @param con
	 * @param usuario
	 */
	public void guardarGlosa(Connection con,UsuarioBasico usuario,String remite)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		boolean esNueva = false; //para saber si la glosa es nueva
		
		//******Se realiza validaciones de la pre-glosa***************************
		this.validacionCampos(true, true, remite);
		//************************************************************************
		
		if(this.errores.isEmpty())
		{
			
			String consecutivo="";
			
			if(glosa.getGlosaSistema().equals(""))
			{
				//Se verifica que se tenga parametrizado consecutivo disponible de glosas
				consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoGlosas, usuario.getCodigoInstitucionInt());
				logger.info("valor del consecutivo: "+consecutivo);
				if(!UtilidadCadena.noEsVacio(consecutivo) || consecutivo.equals("-1"))
					errores.add("Falta consecutivo disponible",new ActionMessage("error.faltaDefinirConsecutivo","GLOSAS"));
				else if(Utilidades.convertirAEntero(consecutivo)==ConstantesBD.codigoNuncaValido)
					errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo de glosas"));
				
				esNueva = true;
			}
			else
				consecutivo = this.glosa.getGlosaSistema();
			
			//Se toma la fecha del sistema
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			this.glosa.setFechaRegistroGlosa(fechaSistema);
			this.glosa.setGlosaSistema(consecutivo);
			this.glosa.setConsecutivoContrato(glosa.getFacturas().get(0).getCodigoContrato()+"");
			//Dependiendo si se audita o se registra la glosa se asignan los campos correspondientes
			if(remite.equals("RegistroAuditoria"))
			{
				
				if(this.glosa.getGlosaEntidad().equals(""))
					this.glosa.setGlosaEntidad(consecutivo);
				if(this.glosa.getFechaNotificacion().equals(""))
					this.glosa.setFechaNotificacion(fechaSistema);
				this.glosa.setUsuarioGlosa(usuario);
				this.glosa.setEstado(ConstantesIntegridadDominio.acronimoEstadoGlosaAuditada);
			}
			else if(remite.equals("GenerarAuditoria"))
			{
				this.glosa.setGlosaEntidad(consecutivo);
				this.glosa.setFechaNotificacion(fechaSistema);
				this.glosa.setEstado(ConstantesIntegridadDominio.acronimoEstadoGlosaRegistrada);
			}
			
			this.glosa.setUsuarioModificacion(usuario);
			
			//Se asigna el usuario que modifica
			for(DtoFacturaGlosa factura:this.glosa.getFacturas())
				factura.setUsuarioModificacion(usuario);
			
			resultado = glosasDao.guardarGlosa(con, this.glosa, remite);
			
			//Si es exitoso
			if(resultado.isTrue())
			{
				//Se aumenta el consecutivo siguiente de glosas
				if(esNueva&&!UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoGlosas,usuario.getCodigoInstitucionInt(), consecutivo,ConstantesBD.acronimoSi, ConstantesBD.acronimoSi ))
				{
					resultado.setResultado(false);
					resultado.setDescripcion("Problemas aumentando el consecutivo de glosas. Proceso cancelado");
				}
			}
			
			if(!resultado.isTrue())
				this.errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
				
		}
		
	}
	
	/**
	 * 
	 * Método implementado para validar los campos requeridos para registrar la glosa
	 * @param validarDevolucion
	 * @param preGlosa
	 */
	private void validacionCampos(boolean validarDevolucion,boolean preGlosa, String remite)
	{
		int posicionFactura = 0, posicionDetalle = 0;
		
		//Se iteran las facturas de la glosa 
		for(DtoFacturaGlosa factura:this.glosa.getFacturas())
		{
			//*******SE VALIDA EL CONCEPTO DE DEVOLUCION*************************
			if(factura.getChequeoDevolucion()!=null&&factura.getChequeoDevolucion()&&validarDevolucion)
			{
				boolean existeDevolucion = false;
				for(DtoConceptoGlosa concepto:factura.getConceptos())
					if(!concepto.isEliminado()&&concepto.getTipo().trim().equals(ConstantesIntegridadDominio.acronimoTipoGlosaDevolucion)&&!concepto.getCodigoConcepto().equals(""))
						existeDevolucion = true;
				
				if(!existeDevolucion)
					this.errores.add("", new ActionMessage("errors.required","El concepto de devolución"));
			}
			//********************************************************************
			
			int numElementosDetalle = 0;
			double totalValorGlosa = 0;
			
			//Iteración del detalle de la factura
			for(DtoDetalleFacturaGlosa detalle:factura.getDetalle())
			{
				if(!detalle.isEliminado())
				{
					//Se valida que sea requerida la cantidad de la glosa
					if(detalle.getCantidadGlosa()==null||detalle.getCantidadGlosa().intValue()<=0)
						this.errores.add("", new ActionMessage("errors.integerMayorQue","La cantidad "+(preGlosa?"pre-glosa":"glosa")+" de "+detalle.getDescripcionServicioArticulo()+" en la orden "+detalle.getConsecutivoOrden(),"0"));
					else if(detalle.getCantidadGlosa().intValue()>detalle.getCantidad().intValue())
						this.errores.add("", new ActionMessage("errors.integerMenorIgualQue","La cantidad "+(preGlosa?"pre-glosa":"glosa")+" de "+detalle.getDescripcionServicioArticulo()+" en la orden "+detalle.getConsecutivoOrden(),"la cantidad : "+detalle.getCantidad()));
					
					//Validaciones del valor de la glosa
					if(detalle.getValorGlosaStr().trim().equals("")||Utilidades.convertirADouble(detalle.getValorGlosaStr())<=0)
						this.errores.add("", new ActionMessage("errors.MayorQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" de "+detalle.getDescripcionServicioArticulo()+" en la orden "+detalle.getConsecutivoOrden(),"0"));
					//-------Se comentarea validacion por Cambio en Documento---------//
					/*else if(Utilidades.convertirADouble(detalle.getValorGlosaStr())>Utilidades.convertirADouble(detalle.getValorStr()))
						this.errores.add("", new ActionMessage("errors.MenorIgualQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" de "+detalle.getDescripcionServicioArticulo()+" en la orden "+detalle.getConsecutivoOrden(),"el valor: "+UtilidadTexto.formatearValores(detalle.getValorStr())));*/
					//---------------------Fin---------------------------------------//
					
					//Validaciones del concepto
					int numConceptosDetalle = 0;
					for(DtoConceptoGlosa concepto:detalle.getConceptos())
						if(!concepto.isEliminado())
							numConceptosDetalle++;
					if(numConceptosDetalle<=0)
						this.errores.add("", new ActionMessage("errors.minimoCampos2","1 concepto","registrar "+detalle.getDescripcionServicioArticulo()+" de la orden "+detalle.getConsecutivoOrden()));
					
					//**********VALIDACIONES DE LOS ASOCIOS********************
					if(detalle.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudCirugia && !detalle.isEsArticulo())
						validacionCamposAsocio(posicionFactura,posicionDetalle,preGlosa,true, remite);
					//***********************************************************
					
					totalValorGlosa += Utilidades.convertirADouble(detalle.getValorGlosaStr(),true);
					numElementosDetalle++;
				}
				posicionDetalle++;
			}
			
			if(totalValorGlosa!=Utilidades.convertirADouble(factura.getValorGlosaFacturaStr(), true) && !remite.equals("RegistrarModificarGlosa"))
				this.errores.add("", new ActionMessage("errors.notEspecific","La sumatoria de los valores "+(preGlosa?"pre-glosa":"glosa")+" no concuerdan con el valor total "+(preGlosa?"pre-glosa":"glosa")+" de la factura. por favor verifique"));
			
			if(numElementosDetalle==0)
				this.errores.add("", new ActionMessage("errors.minimoCampos2","agregar un servicio o artículo","registrar la información"));
			
			posicionFactura++;
		}
		
		
		
		
	}

	/**
	 * Método implementado para validar los datos del detalle de un cirugia
	 * @param posicionFactura
	 * @param posicionDetalle
	 * @param preGlosa 
	 */
	public void validacionCamposAsocio(int posicionFactura, int posicionDetalle, boolean preGlosa, boolean detalleOrden, String remite) 
	{
		double valorGlosaDetalle = Utilidades.convertirADouble(this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getValorGlosaStr(), true);
		double sumatoriaGlosaDetalle = 0;
		
		for(DtoDetalleAsociosGlosa asocio:this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getAsocios())
		{
			String detalleAsocio = " asocio "+asocio.getNombreAsocio()+(asocio.getNombreProfesional().equals("")?"":" ("+asocio.getNombreProfesional()+")");
			if(detalleOrden)
				detalleAsocio += " en el servicio "+ this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getDescripcionServicioArticulo()+" de la orden "+this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getConsecutivoOrden();
			
			//Se valida que se haya ingresado toda la informacion de cantidad y valor
			if(
				asocio.getCantidadGlosa()!=null&&asocio.getCantidadGlosa().intValue()>0
				&&
				!asocio.getValorGlosaStr().trim().equals("")
			)
			{
				//Validacion de la cantidad
				if(asocio.getCantidadGlosa().intValue()>asocio.getCantidad().intValue())
					this.errores.add("", new ActionMessage("errors.MenorIgualQue","La cantidad "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"la cantidad: "+asocio.getCantidad()));
				
				//Validacion del valor 
				if(Utilidades.convertirADouble(asocio.getValorGlosaStr())<=0)
					this.errores.add("", new ActionMessage("errors.MayorQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"0"));
				//******************Se comentarea validacion por Cambio en Documento*****************//
				/*else if(Utilidades.convertirADouble(asocio.getValorGlosaStr())>Utilidades.convertirADouble(asocio.getValorStr()))
					this.errores.add("", new ActionMessage("errors.MenorIgualQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"el valor: "+UtilidadTexto.formatearValores(asocio.getValorStr())));*/
				//********************************Fin************************************************//				
				
				//Validacion de los conceptos
				if(asocio.getNumConceptosDefinitivos()<=0)
					this.errores.add("", new ActionMessage("errors.minimoCampos2","1 concepto","registrar el "+detalleAsocio));
			}
			else if(asocio.getCantidadGlosa()!=null&&asocio.getCantidadGlosa().intValue()>0
				&&
				asocio.getValorGlosaStr().trim().equals(""))
			{
				//Validacion de la cantidad
				if(asocio.getCantidadGlosa().intValue()>asocio.getCantidad().intValue())
					this.errores.add("", new ActionMessage("errors.MenorIgualQue","La cantidad "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"la cantidad: "+asocio.getCantidad()));
				
				this.errores.add("", new ActionMessage("errors.notEspecific","Registro incompleto. Falta ingresar el valor "+(preGlosa?"pre-glosa":"glosa")+" para el "+detalleAsocio));
				
				
				//Validacion de los conceptos
				if(asocio.getNumConceptosDefinitivos()<=0)
					this.errores.add("", new ActionMessage("errors.minimoCampos2","1 concepto","registrar el "+detalleAsocio));
				
			}
			else if((asocio.getCantidadGlosa()==null||asocio.getCantidadGlosa().intValue()<=0)
					&&
					!asocio.getValorGlosaStr().trim().equals(""))
			{
				//Validacion del valor 
				if(Utilidades.convertirADouble(asocio.getValorGlosaStr())<=0)
					this.errores.add("", new ActionMessage("errors.MayorQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"0"));
				else if(Utilidades.convertirADouble(asocio.getValorGlosaStr())>Utilidades.convertirADouble(asocio.getValorStr()))
					this.errores.add("", new ActionMessage("errors.MenorIgualQue","El valor "+(preGlosa?"pre-glosa":"glosa")+" del"+detalleAsocio,"el valor: "+UtilidadTexto.formatearValores(asocio.getValorStr())));
				
				this.errores.add("", new ActionMessage("errors.notEspecific","Registro incompleto. Falta ingresar la cantidad "+(preGlosa?"pre-glosa":"glosa")+" para el "+detalleAsocio));
				
				//Validacion de los conceptos
				if(asocio.getNumConceptosDefinitivos()<=0)
					this.errores.add("", new ActionMessage("errors.minimoCampos2","1 concepto","registrar el "+detalleAsocio));
			}
			else if((asocio.getCantidadGlosa()==null||asocio.getCantidadGlosa().intValue()<=0)
					&&
					asocio.getValorGlosaStr().trim().equals(""))
			{
				//Validacion de los conceptos
				if(asocio.getNumConceptosDefinitivos()>0)
					this.errores.add("", new ActionMessage("errors.notEspecific","Registro incompleto. Falta ingresar la cantidad y el valor "+(preGlosa?"pre-glosa":"glosa")+" para el "+detalleAsocio));
			}
			
			sumatoriaGlosaDetalle += Utilidades.convertirADouble(asocio.getValorGlosaStr(), true);
		}
		
		if(sumatoriaGlosaDetalle!=valorGlosaDetalle && !remite.equals("RegistrarModificarGlosa"))
			this.errores.add("", new ActionMessage("errors.notEspecific","La sumatoria de los valores "+(preGlosa?"pre-glosa":"glosa")+" de los asocios no concuerdan con el valor total "+(preGlosa?"pre-glosa":"glosa")+" de la orden "+this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getConsecutivoOrden()+" y servicio "+this.glosa.getFacturas().get(posicionFactura).getDetalle().get(posicionDetalle).getDescripcionServicioArticulo()+". por favor verifique"));
		
	}
	
	/**
	 * Método para consultar las facturas auditadas
	 */
	public static HashMap<String, Object> consultarFacturasAuditadas(Connection con,String fechaAuditoriaInicial,String fechaAuditoriaFinal,String facturaInicial,String facturaFinal,int codigoConvenio,int codigoContrato,String numeroPreGlosa,int codigoInstitucion, String action, ArrayList<Integer> listaConvenios)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("fechaAuditoriaInicial", fechaAuditoriaInicial);
		criterios.put("fechaAuditoriaFinal", fechaAuditoriaFinal);
		criterios.put("facturaInicial", facturaInicial);
		criterios.put("facturaFinal", facturaFinal);
		criterios.put("codigoConvenio", codigoConvenio);
		criterios.put("codigoContrato", codigoContrato);
		criterios.put("numeroPreGlosa", numeroPreGlosa);
		criterios.put("codigoInstitucion", codigoInstitucion);
		criterios.put("action", action);
		return glosasDao().consultarFacturasAuditadas(con, criterios, listaConvenios);
		
	}

	/**
	 * 
	 * @param usuario
	 * @param tipoUsuario
	 * @param activo
	 * @return
	 */
	public static ArrayList<Integer> cargarListadoConveniosUsuario(UsuarioBasico usuario, String tipoUsuario, boolean activo)
	{
		Connection con= UtilidadBD.abrirConexion();
		ArrayList<Integer> listadoConvenios= new ArrayList<Integer>();
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAuditor(usuario.getCodigoInstitucionInt())) && tipoUsuario.equals(ConstantesIntegridadDominio.acronimoTipoUsuarioAuditor))
		{
			ArrayList<HashMap<String, Object>> array= UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), tipoUsuario, activo);
			for(HashMap<String, Object> mapa: array)
			{
				listadoConvenios.add(Utilidades.convertirAEntero(mapa.get("codigoConvenio")+""));
			}
		}
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarUsuarioGlosa(usuario.getCodigoInstitucionInt())) && tipoUsuario.equals(ConstantesIntegridadDominio.acronimoTipoUsuarioGlosa))
		{
			ArrayList<HashMap<String, Object>> array= UtilidadesFacturacion.obtenerConvenioPorUsuario(con, usuario.getLoginUsuario(), tipoUsuario, activo);
			for(HashMap<String, Object> mapa: array)
			{
				listadoConvenios.add(Utilidades.convertirAEntero(mapa.get("codigoConvenio")+""));
			}
		}
		UtilidadBD.closeConnection(con);
		return listadoConvenios;
	}
	
	
	
	
	/**
	 * @return the glosa
	 */
	public DtoGlosa getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(DtoGlosa glosa) {
		this.glosa = glosa;
	}

	/**
	 * 
	 * @param codigoAuditoria
	 * @param observaciones
	 */
	public static boolean actualizarObservacionAuditoria(String codigoAuditoria, String observaciones) 
	{
		return glosasDao().actualizarObservacionAuditoria(codigoAuditoria, observaciones);
	}
}