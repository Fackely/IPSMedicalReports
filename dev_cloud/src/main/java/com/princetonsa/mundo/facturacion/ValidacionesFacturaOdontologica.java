package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ElementoApResource;
import util.RangosConsecutivos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.dto.facturacion.DtoControlAnticiposContrato;
import com.princetonsa.dto.facturacion.DtoProfesionalSaludSinValorHonorarios;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.princetonsa.dto.facturacion.DtoSolicitudesResponsableFacturaOdontologica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.ConsecutivosCentroAtencion;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.impl.facturacion.ControlAnticiposContratoMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo;
import com.servinte.axioma.orm.ControlAnticiposContrato;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Manejo de errores de la factura Odontologica
 * @author axioma
 *
 */
public class ValidacionesFacturaOdontologica 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ValidacionesFactura.class);
	
	/**
	 * Manejo de los errores
	 */
	private ArrayList<ElementoApResource> erroresFactura;
	
	/**
	 * Manejo de los warnings 
	 */
	private ArrayList<ElementoApResource> warningsFactura;
	
	/**
	 * consutructor
	 *
	 */
	public ValidacionesFacturaOdontologica()
	{
		this.clean();
	}
	
	/**
	 * Mï¿½todo que limpia los atributos de esta clase
	 *
	 */
	public void clean ()
	{
		this.erroresFactura=new ArrayList<ElementoApResource>();
		this.warningsFactura= new ArrayList<ElementoApResource>();
	}
	
	/**
	 * Metodo para validar que el paciente este cargado
	 * @param paciente
	 * @return
	 */
	public boolean esPacienteCargado(PersonaBasica paciente)
	{
		boolean retorna= true;
		if(paciente.getCodigoPersona()<1 || paciente.getCodigoTipoIdentificacionPersona().equals("") )
		{
			this.setErrorFactura("errors.paciente.noCargado", new ArrayList<String>());
			retorna=false;
		}
		return retorna;
	}
	
	/**
	 * Metodo para validar la existencia de los consecutivos de factura, 
	 * 1. X CENTRO ATENCION
	 * 2. X MULTIEMPRESA
	 * 3. X INSTITUCION
	 *  
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return
	 */
	public boolean existeConsecutivoFactura(int codigoInstitucion, int centroAtencion) 
	{
		boolean retorna= true;
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(codigoInstitucion))
		{
			logger.info("X CENTRO ATENCION");
			retorna = validarConsecutivoXCentroAtencion(centroAtencion);
		}
		//2. EVALUAMOS SI LA INSTITUCION ES MULTIEMPRESA
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)))
		{
			logger.info("X MULTIEMPRESA");
			retorna = validarConsecutivoXMultiempresa(centroAtencion);
		}
		//3. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{
			logger.info("X INSTITUCION");
			retorna = validarConsecutivoXInstitucion(codigoInstitucion);
		}
		return retorna;
	}

	/**
	 * 
	 * @param con
	 * @param usuario
	 * @param tipoFuenteDatos 
	 * @return
	 */
	public BigDecimal obtenerSiguienteConsecutivoFactura(Connection con,UsuarioBasico usuario, int tipoFuenteDatos)
	{
		BigDecimal consecutivo= BigDecimal.ZERO;
		String nombreConsecutivo=ConstantesBD.nombreConsecutivoFacturas;
		ArrayList<BigDecimal> listaValoresConsecutivos=new ArrayList<BigDecimal>();
		HibernateUtil.beginTransaction();
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO CENTRO DE ATENCION
		if(tipoFuenteDatos == 0)
		{
			logger.info("X CENTRO ATENCION");
			consecutivo= ConsecutivosCentroAtencion.incrementarConsecutivoXCentroAtencion(usuario.getCodigoCentroAtencion(), nombreConsecutivo, usuario.getCodigoInstitucionInt());
		}
		//2. EVALUAMOS SI LA INSTITUCION ES MULTIEMPRESA
		else if(tipoFuenteDatos == 1)
		{
			logger.info("X MULTIEMPRESA");
			ValidacionesFactura validaciones = new ValidacionesFactura();
			double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(usuario.getCodigoCentroAtencion());
			consecutivo= new BigDecimal(validaciones.obtenerSiguientePosibleNumeroFacturaMultiempresa(con, empresaInstitucion));
			if(!ValidacionesFactura.incrementarConsecutivoFacturaMultiempresa(con, empresaInstitucion, 1))
			{
				consecutivo= BigDecimal.ZERO;
			}
		}
		//3. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{
			logger.info("X INSTITUCION");
			boolean consecutivoUsado=false;
			consecutivo = new BigDecimal( Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt())));
			consecutivoUsado=UtilidadesFacturacion.esConsecutvioAsignadoFactura(usuario.getCodigoInstitucionInt(),String.valueOf(consecutivo.longValue()));
			if(consecutivoUsado)
			{
				listaValoresConsecutivos.add(consecutivo);
				int contIntentos=0;
				consecutivo = new BigDecimal(0.0);
				while(consecutivo.doubleValue()<=0&&contIntentos<100)
				{
					consecutivo = new BigDecimal( Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoFacturas, usuario.getCodigoInstitucionInt())) );
					contIntentos++;
					if(UtilidadesFacturacion.esConsecutvioAsignadoFactura(usuario.getCodigoInstitucionInt(),consecutivo+""))
					{
						listaValoresConsecutivos.add(consecutivo);
						consecutivo = new BigDecimal(0.0);
					}
				}
				
			}
			
			this.actualizarUsoConsecutivos(con, listaValoresConsecutivos, usuario.getCodigoInstitucionInt());
			
			if(consecutivo.intValue()<=0)
			{
				logger.warn("Transacción Abortada");
				HibernateUtil.abortTransaction();
			    return consecutivo;
			}
			HibernateUtil.endTransaction();
		}
		return consecutivo;
	}
	
	/**
	 * Este método se encarga de actualizar los consecutivos en la tabla uso_consecutivos
	 * que ya han sido usados 
	 * @param con 
	 * @param listaValoresConsecutivos lista que almacena los valores a actualizar
	 * @param institucion código de la institución
	 */
	private void actualizarUsoConsecutivos(Connection con, ArrayList<BigDecimal> listaValoresConsecutivos, int institucion) 
	{
		UtilidadBD.iniciarTransaccion(con);
		for (int i=0;i<listaValoresConsecutivos.size();i++) 
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturas, institucion, listaValoresConsecutivos.get(i).intValue()+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		}
		UtilidadBD.finalizarTransaccion(con);
		
	}
	
	/**
	 * @param centroAtencion
	 * @param retorna
	 * @return
	 */
	private boolean validarConsecutivoXCentroAtencion(int centroAtencion) 
	{
		boolean retorna= true;
		BigDecimal valorActualConsecutivo= ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(centroAtencion, ConstantesBD.nombreConsecutivoFacturas, UtilidadFecha.getMesAnioDiaActual("anio"));
		
		if(valorActualConsecutivo.doubleValue()<=0)
		{
			//error.paciente.faltaDefinirConsecutivo = Falta definir el consecutivo para {0} del paciente. Por favor verifique [ac-024]
			this.setErrorFactura("error.paciente.faltaDefinirConsecutivo", "Facturas por Centro de Atencion");
			retorna=false;
		}
		return retorna;
	}

	/**
	 * @param centroAtencion
	 * @param retorna
	 * @return
	 */
	private boolean validarConsecutivoXMultiempresa(int centroAtencion) 
	{
		boolean retorna= true;
		double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(centroAtencion);
		ValidacionesFactura validacionesFact= new ValidacionesFactura();
		BigDecimal valorActualConsecutivo= validacionesFact.obtenerSiguientePosibleNumeroFacturaMultiempresa(empresaInstitucion);
		
		if(valorActualConsecutivo.doubleValue()<=0)
		{
			this.setErrorFactura("error.paciente.faltaDefinirConsecutivo", "Facturas por Empresa Institucion");
			retorna=false;
		}
		return retorna;
	}

	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	private boolean validarConsecutivoXInstitucion(int codigoInstitucion) 
	{
		boolean retorna= true;
		Connection con= UtilidadBD.abrirConexion();
		BigDecimal valorActualConsecutivo= new BigDecimal(Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoFacturas, codigoInstitucion)));
		UtilidadBD.closeConnection(con);
		
		if(valorActualConsecutivo.doubleValue()<=0)
		{
			this.setErrorFactura("error.paciente.faltaDefinirConsecutivo", "Facturas por Instituciï¿½n");
			retorna=false;
		}
		return retorna;
	}
	
	/**
	 * Metodo para evaluar si el consecutvio esta entre los rangos validos autorizados por la DIAN
	 * @param codigoInstitucionInt
	 * @param codigoCentroAtencion
	 * @return
	 */
	public boolean esConsecutivoEnRangoAutorizado(int codigoInstitucion, int centroAtencion) 
	{
		boolean retorna= true;
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO ES X INSTITUCION O X CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(codigoInstitucion))
		{
			BigDecimal valorActualConsecutivo= ConsecutivosCentroAtencion.obtenerValorActualConsecutivo(centroAtencion, ConstantesBD.nombreConsecutivoFacturas, UtilidadFecha.getMesAnioDiaActual("anio"));
			retorna = validarRangoConsecutivoXCentroAtencion(centroAtencion, valorActualConsecutivo);
		}
		//2. EVALUAMOS SI LA INSTITUCION ES MULTIEMPRESA
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)))
		{
			ValidacionesFactura validacionesFact= new ValidacionesFactura();
			double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(centroAtencion);
			BigDecimal valorActualConsecutivo= validacionesFact.obtenerSiguientePosibleNumeroFacturaMultiempresa(empresaInstitucion);
			retorna = validarRangoConsecutivoXMultiempresa(Utilidades.convertirAEntero(empresaInstitucion+""), valorActualConsecutivo);
		}
		//3. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{	 
			Connection con= UtilidadBD.abrirConexion();
			BigDecimal valorActualConsecutivo= new BigDecimal(Utilidades.convertirAEntero(UtilidadBD.obtenerValorActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoFacturas, codigoInstitucion)));
			UtilidadBD.closeConnection(con);
			retorna = validarRangoConsecutivoXInstitucion(codigoInstitucion, valorActualConsecutivo);
		}
		return retorna;
	}

	/**
	 * Metodo para evaluar si el consecutvio esta entre los rangos validos autorizados por la DIAN
	 * @param codigoInstitucionInt
	 * @param codigoCentroAtencion
	 * @return
	 */
	public boolean esConsecutivoEnRangoAutorizadoDadoValor(int codigoInstitucion, int centroAtencion, BigDecimal valorActualConsecutivo) 
	{
		boolean retorna= true;
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO ES X INSTITUCION O X CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(codigoInstitucion))
		{
			retorna = validarRangoConsecutivoXCentroAtencion(centroAtencion, valorActualConsecutivo);
		}
		//2. EVALUAMOS SI LA INSTITUCION ES MULTIEMPRESA
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)))
		{
			double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(centroAtencion);
			retorna = validarRangoConsecutivoXMultiempresa(Utilidades.convertirAEntero(empresaInstitucion+""), valorActualConsecutivo);
		}
		//3. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{	 
			retorna = validarRangoConsecutivoXInstitucion(codigoInstitucion, valorActualConsecutivo);
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param forma
	 * @param usado
	 * @param finalizado
	 * @param institucion
	 */
	public static void liberarConsecutivos(Connection con, ArrayList<Double> consecutivosInstitucion , int institucion) 
	{
		if(!ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(institucion)
			&& !UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(institucion)))
		{
			for(Double consecutivo: consecutivosInstitucion)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturas, institucion, consecutivo+"", ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			}
		}	
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usado
	 * @param finalizado
	 * @param institucion
	 */
	public static void finalizarConsecutivos(Connection con, ArrayList<Double> consecutivosInstitucion , int institucion) 
	{
		if(!ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(institucion)
				&& !UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(institucion)))
		{
			for(Double consecutivo: consecutivosInstitucion)
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoFacturas, institucion, consecutivo+"", ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
		}	
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @param centroAtencion
	 * @return
	 */
	public RangosConsecutivos obtenerRangosFactura(int codigoInstitucion, int centroAtencion)
	{
		RangosConsecutivos rango= null;
		//1. PRIMERO IDENTIFICAMOS SI EL CONSECUTIVO ES X INSTITUCION O X CENTRO DE ATENCION
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(codigoInstitucion))
		{
			rango= CentroAtencion.obtenerRangosFacturacionXCentroAtencion(centroAtencion);
		}
		//2. EVALUAMOS SI LA INSTITUCION ES MULTIEMPRESA
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion)))
		{
			double empresaInstitucion= CentroAtencion.obtenerEmpresaInstitucionCentroAtencion(centroAtencion);
			rango= ValidacionesFactura.obtenerRangoInicialFinalFacturaMultiempresa(empresaInstitucion);
		}
		//3. DE LO CONTRARIO TOMAMOS EL CONSECUTIVO X INSTITUION
		else 
		{	 
			rango= ParametrizacionInstitucion.obtenerRangosFacturacionXInstitucion(codigoInstitucion);
		}
		return rango;
	}
	
	/**
	 * 
	 * @param centroAtencion
	 * @return
	 */
	private boolean validarRangoConsecutivoXCentroAtencion(int centroAtencion, BigDecimal valorActualConsecutivo) 
	{
		boolean retorna= true;
		
		logger.info("valorActualConsecutivo--->"+valorActualConsecutivo);
		
		RangosConsecutivos rango= CentroAtencion.obtenerRangosFacturacionXCentroAtencion(centroAtencion);
		
		if(rango==null || (rango.getRangoInicial().doubleValue()<=0 && rango.getRangoFinal().doubleValue()<=0) || (rango.getRangoInicial().doubleValue()<0 || rango.getRangoFinal().doubleValue()<0))
		{
			//Falta definir el rango de {0}. Por favor verifique. [bi-008-1]
			this.setErrorFactura("error.facturacion.rangoVacio", "facturaciï¿½n por centro de atenciï¿½n para la Generaciï¿½n de Factura");
			retorna=false;
		}
		else
		{
			if(valorActualConsecutivo.doubleValue()>0 && !rango.estaEnRango(valorActualConsecutivo))
			{
				//El Consecutivo de Facturacion {0} no estï¿½ dentro del Rango de Facturaciï¿½n Autorizado {1}. Por favor Verifique. [bi-009-1]
				ArrayList<String> atributos= new ArrayList<String>();
				atributos.add(valorActualConsecutivo+"");
				atributos.add(rango.toString());
				this.setErrorFactura("error.facturacion.rangoLimite1", atributos);
				retorna=false;
			}
		}	
		return retorna;
	}

	/**
	 * 
	 * @param centroAtencion
	 * @return
	 */
	private boolean validarRangoConsecutivoXMultiempresa(int empresaInstitucion, BigDecimal valorActualConsecutivo) 
	{
		boolean retorna= true;
		
		RangosConsecutivos rango= ValidacionesFactura.obtenerRangoInicialFinalFacturaMultiempresa(empresaInstitucion);
		
		if(rango==null || (rango.getRangoInicial().doubleValue()<=0 && rango.getRangoFinal().doubleValue()<=0) || (rango.getRangoInicial().doubleValue()<0 || rango.getRangoFinal().doubleValue()<0))
		{
			//Falta definir el rango de {0}. Por favor verifique. [bi-008-1]
			this.setErrorFactura("error.facturacion.rangoVacio", "facturaciï¿½n por empresa instituciï¿½n para la Generaciï¿½n de Factura");
			retorna=false;
		}
		else
		{
			if(valorActualConsecutivo.doubleValue()>0 && !rango.estaEnRango(valorActualConsecutivo))
			{
				//El Consecutivo de Facturacion {0} no estï¿½ dentro del Rango de Facturaciï¿½n Autorizado {1}. Por favor Verifique. [bi-009-1]
				ArrayList<String> atributos= new ArrayList<String>();
				atributos.add(valorActualConsecutivo+"");
				atributos.add(rango.toString());
				this.setErrorFactura("error.facturacion.rangoLimite1", atributos);
				retorna=false;
			}
		}	
		return retorna;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	private boolean validarRangoConsecutivoXInstitucion(int codigoInstitucion, BigDecimal valorActualConsecutivo) 
	{
		boolean retorna= true;
		RangosConsecutivos rango= ParametrizacionInstitucion.obtenerRangosFacturacionXInstitucion(codigoInstitucion);
		
		if(rango==null || (rango.getRangoInicial().doubleValue()<=0 && rango.getRangoFinal().doubleValue()<=0) || (rango.getRangoInicial().doubleValue()<0 || rango.getRangoFinal().doubleValue()<0))
		{
			//Falta definir el rango de {0}. Por favor verifique. [bi-008-1]
			this.setErrorFactura("error.facturacion.rangoVacio", "facturaciï¿½n por instituciï¿½n para la Generaciï¿½n de Factura");
			retorna=false;
		}
		else
		{
			if(valorActualConsecutivo.doubleValue()>0 && rango.estaEnRango(valorActualConsecutivo))
			{
				//El Consecutivo de Facturacion {0} no estï¿½ dentro del Rango de Facturaciï¿½n Autorizado {1}. Por favor Verifique. [bi-009-1]
				ArrayList<String> atributos= new ArrayList<String>();
				atributos.add(valorActualConsecutivo+"");
				atributos.add(rango.toString());
				this.setErrorFactura("error.facturacion.rangoLimite1", atributos);
				retorna=false;
			}
		}	
		return retorna;
	
	}
	
	/**
	 * Metodo para validar si los contratos estan o no vencidos
	 * @param cuenta
	 * @param contratos
	 * @param institucion
	 * @return
	 */
	public boolean esContratosVigentesYTopeValido(BigDecimal cuenta, Vector contratos, int institucion)
	{
		boolean retorna= false;
		String validarContratosVencidos=ValoresPorDefecto.getValidarContratosVencidos(institucion);
		boolean validarContratosBool=UtilidadTexto.getBoolean(validarContratosVencidos);;
		if(UtilidadTexto.isEmpty(validarContratosVencidos))
		{
			ElementoApResource elem=new ElementoApResource("error.facturacion.parametroContratosVencidos");
			this.erroresFactura.add(elem);
			retorna= true;
		}
		
		if(!retorna)
		{	
			Connection con= UtilidadBD.abrirConexion();
			HashMap contratosVencidosMap= Contrato.obtenerContratosVencidosXCuenta(con, cuenta.toString(), contratos);
			UtilidadBD.closeConnection(con);
			
			for(int w1=0; w1<Integer.parseInt(contratosVencidosMap.get("numRegistros").toString()); w1++)
			{
				ArrayList<String> atributos= new ArrayList<String>();
				atributos.add(contratosVencidosMap.get("nombreconvenio_"+w1).toString()+" de la cuenta Nro "+cuenta);
				atributos.add(contratosVencidosMap.get("fechainicial_"+w1).toString());
				atributos.add(contratosVencidosMap.get("fechafinal_"+w1).toString());
				//EL CONTRATO DEL CONVENIO {0} A FACTURAR SE ENCUENTRA VENCIDO. Fecha Inicial: {1} Fecha Final: {2}. [bf-19]
				if(validarContratosBool)
				{	
					this.setErrorFactura("error.facturacion.convenioVencidoConFechas", atributos);
					retorna= true;
				}	
				else
				{	
					this.setWarningFactura("error.facturacion.convenioVencidoConFechas", atributos);
				}
			}
		}	
		
		if(contratos.size()>0)
		{
			Connection con= UtilidadBD.abrirConexion();
			
			HashMap contratosTopeCompleto= Contrato.obtenerContratosTopesCompletos(con, contratos, cuenta+"");
			UtilidadBD.closeConnection(con);
			
			for(int w=0; w<Integer.parseInt(contratosTopeCompleto.get("numRegistros").toString()); w++)
			{
				ArrayList<String> atributos= new ArrayList<String>();
				atributos.add(contratosTopeCompleto.get("nombreconvenio_"+w).toString());
				atributos.add(UtilidadTexto.formatearValores(contratosTopeCompleto.get("valor_"+w).toString()));
				atributos.add(UtilidadTexto.formatearValores(contratosTopeCompleto.get("acumulado_"+w).toString()));
				
				if(validarContratosBool)
				{	
					this.setErrorFactura("error.facturacion.convenioValorCompletoConValores", atributos);
					retorna=true;
				}	
				else
				{	
					this.setWarningFactura("error.facturacion.convenioValorCompletoConValores", atributos);
				}
			}
		}
		
		return retorna;
	}
	
	
	/**
	 * 
	 */
	public void setearErrorEstadosIngresosCuentaInvalido() 
	{
		this.setErrorFactura("errors.notEspecific", "No existe una cuenta activa con ingreso abierto o cerrado");
	}
	
	/**
	 * 
	 * @param listaResponsablesConSolicitudes
	 */
	public boolean esValorCargoTotalMayorCero(ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes) 
	{
		boolean existeResponsableParaFacturar= false;
		ArrayList<ElementoApResource> erroresOWarningsTemp= new ArrayList<ElementoApResource>();
		
		for(DtoResponsableFacturaOdontologica dto: listaResponsablesConSolicitudes)
		{
			if(dto.getValorTotalNetoCargosEstadoCargado().doubleValue()<=0)
			{
				ElementoApResource elem=new ElementoApResource("error.facturacion.totalCuentaCero");
				elem.agregarAtributo(dto.getConvenio().getNombre());
				erroresOWarningsTemp.add(elem);
			}
			else
			{
				existeResponsableParaFacturar= true;
			}
		}
		
		if(!existeResponsableParaFacturar)
		{
			//entonces agregamos el error
			for(int w=0; w<erroresOWarningsTemp.size(); w++)
			{	
				this.erroresFactura.add(erroresOWarningsTemp.get(w));
			}	
		}
		else
		{
			//entonces agregamos el warning
			for(int w=0; w<erroresOWarningsTemp.size(); w++)
			{	
				this.warningsFactura.add(erroresOWarningsTemp.get(w));
			}	
		}
		
		return existeResponsableParaFacturar;
	}
	
	/**
	 * (Simulacion errors ApplicationResources.properties)
	 */
	private void setErrorFactura (String llaveError, ArrayList<String> atributos)
	{
		ElementoApResource elem=new ElementoApResource(llaveError);
		for(String atributo: atributos)
		{	
			elem.agregarAtributo(atributo);
		}	
		this.erroresFactura.add(elem);
	}

	/**
	 * (Simulacion errors ApplicationResources.properties)
	 */
	private void setErrorFactura (String llaveError, String atributo)
	{
		ElementoApResource elem=new ElementoApResource(llaveError);
		elem.agregarAtributo(atributo);
		this.erroresFactura.add(elem);
	}
	
	/**
	 * 
	 * @param codigoPaciente 
	 * @param listaResponsablesConSolicitudes
	 */
	public boolean esValorAnticipoConvenioOSaldoPacienteCorrecto(int codigoPaciente, ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes) 
	{
		boolean retorna= true;
		Connection con=UtilidadBD.abrirConexion();
		for(DtoResponsableFacturaOdontologica dto: listaResponsablesConSolicitudes)
		{
			IControlAnticiposContratoMundo controlAnticiposContrato = new ControlAnticiposContratoMundo();
			ArrayList<ControlAnticiposContrato> listaControlAnticiposContrato = controlAnticiposContrato.determinarContratoRequiereAnticipo(dto.getContrato().getCodigo());
			if (!listaControlAnticiposContrato.isEmpty()) {
				if(dto.isPacientePagaAtencionXMontoCobro())
				{
					//se validan los abonos del paciente que el campo reserva abono sea mayor igual al total a pagar
					BigDecimal valorReservaAbono= AbonosYDescuentos.obtenerValorReservadoAbono(codigoPaciente);
					
					if(valorReservaAbono.doubleValue() < dto.getValorTotalNetoCargosEstadoCargado().doubleValue())
					{
						ArrayList<String> atributos= new ArrayList<String>();
						atributos.add(dto.getConvenio().getNombre().toUpperCase());
						atributos.add(UtilidadTexto.formatearValores(valorReservaAbono+""));
						atributos.add(UtilidadTexto.formatearValores(dto.getValorTotalNetoCargosEstadoCargado()+""));
						this.setErrorFactura("error.facturacion.reservaInsuficienteAbono", atributos);
						retorna= false;
					}
				}
				else
				{
					//verificamos si controla anticipos
					if(dto.isControlaAnticipos())
					{
						BigDecimal valorAnticipoRespervadoPresupuestoContratado= BigDecimal.ZERO;
						ArrayList<DtoControlAnticiposContrato> array= Contrato.obtenerControlAnticipos(con, dto.getContrato().getCodigo());
						if(array.size()>0)
						{
							valorAnticipoRespervadoPresupuestoContratado=  new BigDecimal(array.get(0).getValorAnticipoReservadoConvenio());
						}
						
						Log4JManager.info("ValorAnticipoPresupuestoContratado->"+valorAnticipoRespervadoPresupuestoContratado.doubleValue());
						Log4JManager.info("Valor Total A Pagar factura >>"+dto.getValorTotalNetoCargosEstadoCargado().doubleValue());
						
						if(valorAnticipoRespervadoPresupuestoContratado.doubleValue() < dto.getValorTotalNetoCargosEstadoCargado().doubleValue())
						{
							ArrayList<String> atributos= new ArrayList<String>();
							atributos.add(dto.getConvenio().getNombre().toUpperCase());
							atributos.add(UtilidadTexto.formatearValores(valorAnticipoRespervadoPresupuestoContratado+""));
							atributos.add(UtilidadTexto.formatearValores(dto.getValorTotalNetoCargosEstadoCargado()+""));
							this.setErrorFactura("error.facturacion.reservaInsuficienteAnticipo", atributos);
							retorna= false;
						}
							
					}
				}
			}
		}
		UtilidadBD.closeConnection(con);
		return retorna;
	}
	
	/**
	 * Metodo para validar que las solicitudes a facturar tenga pool asociado
	 * @param listaResponsablesConSolicitudes
	 */
	public boolean validacionesPoolesYLiquidacionHonorarios(	ArrayList<DtoResponsableFacturaOdontologica> listaResponsablesConSolicitudes, int institucion) 
	{
		boolean retorna= true;
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValoresDefectoValidarPoolesFact(institucion)))
		{
			ArrayList<String> array = new ArrayList<String>();
			ArrayList<String> arrayPooles = new ArrayList<String>();
			DtoProfesionalSaludSinValorHonorarios dtoProfesional;

			for(DtoResponsableFacturaOdontologica dtoEncabezado: listaResponsablesConSolicitudes)
			{
				int i=0;
				for(DtoSolicitudesResponsableFacturaOdontologica dtoDetalle: dtoEncabezado.getListaSolicitudes())
				{
					//se debe validar que el medico tenga tipo de liquidacion
					if(dtoDetalle.getTipoLiquidacionMedico().isEmpty()
						&&	dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudMedicamentos 
						&&	dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
						&&  dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudEstancia
						&& 	!array.contains(dtoDetalle.getMedicoResponde().getNombre()))
					{
						array.add(dtoDetalle.getMedicoResponde().getNombre());
						/*this.setErrorFactura("errors.notEspecific", "Debe ingresar el Tipo de Liquidaciï¿½n para el mï¿½dico "+dtoDetalle.getMedicoResponde().getNombre());
						retorna=false;*/
						if(dtoDetalle.getTipoLiquidacionMedico().isEmpty()) {
							dtoProfesional = new DtoProfesionalSaludSinValorHonorarios();

							dtoProfesional.setFechaLiquidacionHonorarios(UtilidadFecha.getFechaActualTipoBD());
							dtoProfesional.setHoraLiquidacionHonorarios(UtilidadFecha.getHoraActual());
							dtoProfesional.setNombreProfesional(dtoDetalle.getMedicoResponde().getNombre());
							dtoProfesional.setNumeroSolicitud(dtoEncabezado.getListaSolicitudes().get(i).getDetalleCargo().getNumeroSolicitud());
							dtoProfesional.setCodigoServicio(dtoEncabezado.getListaSolicitudes().get(i).getDetalleCargo().getCodigoServicio());

							// INSERTAMOS EL LOG PARA PROFESIONALES DE LA SALUD A LOS CUALES NO SE LES VA A GENERAR VALOR DE HONORARIOS
							Connection con= UtilidadBD.abrirConexion();
							UtilidadBD.iniciarTransaccion(con);
							//if(!FacturaOdontologica.insertarFacturas(con, listaFacturas)){
							if (!FacturaOdontologica.insertarLogProfesionalesSaludNoHonorarios(
									con, dtoProfesional)) {
								this.setErrorFactura("errors.notEspecific",
										"No se puede almacenar el log de profesional de la salud, intente de nuevo.");
								retorna = false;
							}
						}
					}
					else if(dtoDetalle.getTipoLiquidacionMedico().equals(ConstantesIntegridadDominio.acronimoPool))
					{
						//si el tipo de liquidacion es pool y el pool es menor igual que cero entonces debe sacar error
						if(dtoDetalle.getPool().getCodigo()<=0)
						{
							if(dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudMedicamentos 
								&&	dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
								&&  dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudEstancia)
							{
								ArrayList<String> atributos= new ArrayList<String>();
								atributos.add(dtoDetalle.getConsecutivoSolicitud());
								atributos.add(Utilidades.obtenerNombreTipoSolicitud(dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()+""));
								atributos.add(dtoDetalle.getMedicoResponde().getNombre());
								this.setErrorFactura("error.facturacion.solicitudSinPool", atributos);
								retorna=false;
							}
						}
						// de lo contrario validamos el porcentaje de participacion
						else
						{
							if(	dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudMedicamentos 
								&&	dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos
								&&  dtoDetalle.getDetalleCargo().getCodigoTipoSolicitud()!=ConstantesBD.codigoTipoSolicitudEstancia
								&& 	!arrayPooles.contains(dtoDetalle.getPool().getNombre())
								&&  (dtoDetalle.getPorcentajeParticipacionPool()<=0))
							{
								arrayPooles.add(dtoDetalle.getPool().getNombre());
								this.setErrorFactura("errors.notEspecific", "Falta parametrizaciï¿½n de porcentaje o valor pooles para el pool "+dtoDetalle.getPool().getNombre()+". Por favor verifique");
								retorna=false;
							}
						}
					}	
					i++;
				}
			}
		}	
		return retorna;
	}
	
	
	
	/**
	 * Método que se encarga de consultar la información necesaria para la
	 * generación de la Factura dependiendo de la parametrización del sistema.
	 * 
	 * tipoFuenteDatos = 0 ----> Centro de Atención
	 * tipoFuenteDatos = 1 ----> Empresa Institución
	 * tipoFuenteDatos = 2 ----> Institución
	 * 
	 * @param historicoEncabezado 
	 * @param tipoFuenteDatos
	 * @param codigoFuenteInformacion
	 */
	public void obtenerDatosParametrizacionParaFactura (HistoricoEncabezado historicoEncabezado, int tipoFuenteDatos, int codigoFuenteInformacion){
		
		ICentroAtencionMundo centroAtencionMundo = AdministracionFabricaMundo.crearCentroAtencionMundo();
		
		com.servinte.axioma.orm.CentroAtencion centroAtencion = centroAtencionMundo.buscarPorCodigo(codigoFuenteInformacion);
		
		if(centroAtencion!=null){
			
			/*
			 * Sección Encabezado
			 *
			 */
			if (tipoFuenteDatos == 0 || tipoFuenteDatos == 1){
			
				historicoEncabezado.setNombreInstitucion(centroAtencion.getEmpresasInstitucion().getRazonSocial());
				historicoEncabezado.setNitInstitucion(centroAtencion.getEmpresasInstitucion().getNit());
				historicoEncabezado.setDigitoVerificacion(centroAtencion.getEmpresasInstitucion().getDigitoVerificacion()+"");
				historicoEncabezado.setActividadEconomica(centroAtencion.getEmpresasInstitucion().getActividadEco());
				historicoEncabezado.setPiePagina(centroAtencion.getEmpresasInstitucion().getPie());
				historicoEncabezado.setEncabezado(centroAtencion.getEmpresasInstitucion().getEncabezado());
				historicoEncabezado.setSucursal(centroAtencion.getDescripcion());
				historicoEncabezado.setTipoIdentificacionInst(centroAtencion.getEmpresasInstitucion().getTiposIdentificacion().getNombre());
				historicoEncabezado.setCiudadInstitucion(centroAtencion.getEmpresasInstitucion().getCiudades().getDescripcion());
				historicoEncabezado.setDireccionInstitucion(centroAtencion.getEmpresasInstitucion().getDireccion());
				historicoEncabezado.setTelefonoInstitucion(centroAtencion.getEmpresasInstitucion().getTelefono());
				historicoEncabezado.setLogoInstitucion(centroAtencion.getEmpresasInstitucion().getLogo());
			}else if (tipoFuenteDatos == 2){
				
				historicoEncabezado.setNombreInstitucion(centroAtencion.getInstituciones().getRazonSocial());
				historicoEncabezado.setNitInstitucion(centroAtencion.getInstituciones().getNit());
				historicoEncabezado.setDigitoVerificacion(centroAtencion.getInstituciones().getDigitoVerificacion()+"");
				historicoEncabezado.setActividadEconomica(centroAtencion.getInstituciones().getActividadEco());
				historicoEncabezado.setPiePagina(centroAtencion.getInstituciones().getPie());
				historicoEncabezado.setEncabezado(centroAtencion.getInstituciones().getEncabezado());
				historicoEncabezado.setSucursal(centroAtencion.getDescripcion());
				historicoEncabezado.setTipoIdentificacionInst(centroAtencion.getInstituciones().getTiposIdentificacion().getNombre());
				historicoEncabezado.setCiudadInstitucion(centroAtencion.getInstituciones().getCiudades().getDescripcion());
				historicoEncabezado.setDireccionInstitucion(centroAtencion.getInstituciones().getDireccion());
				historicoEncabezado.setTelefonoInstitucion(centroAtencion.getInstituciones().getTelefono());
				historicoEncabezado.setLogoInstitucion(centroAtencion.getInstituciones().getLogo());
			}
			
			/*
			 * Información por Centro de Atención
			 */
			if(tipoFuenteDatos == 0){
				
				if(centroAtencion!=null){
					
					historicoEncabezado.setRangoInicial(centroAtencion.getRgoInicFact()+"");
					historicoEncabezado.setRangoFinal(centroAtencion.getRgoFinFact()+"");
					historicoEncabezado.setResolucion(centroAtencion.getResolucion());
					historicoEncabezado.setPrefijoFactura(centroAtencion.getPrefFactura());
				}
				
			}else if (tipoFuenteDatos == 1){
				
				/*
				 * Información por Institucion - Multiempresa
				 */
				
				historicoEncabezado.setRangoInicial(centroAtencion.getEmpresasInstitucion().getRgoInicFact()+"");
				historicoEncabezado.setRangoFinal(centroAtencion.getEmpresasInstitucion().getRgoFinFact()+"");
				historicoEncabezado.setResolucion(centroAtencion.getEmpresasInstitucion().getResolucion());
				historicoEncabezado.setPrefijoFactura(centroAtencion.getEmpresasInstitucion().getPrefFactura());
	
				
			}else if (tipoFuenteDatos == 2){
				
				/*
				 * Información por Institución
				 */

				historicoEncabezado.setRangoInicial(centroAtencion.getInstituciones().getRgoInicFact()+"");
				historicoEncabezado.setRangoFinal(centroAtencion.getInstituciones().getRgoFinFact()+"");
				historicoEncabezado.setResolucion(centroAtencion.getInstituciones().getResolucion());
				historicoEncabezado.setPrefijoFactura(centroAtencion.getInstituciones().getPrefFactura());
			}
		}
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	/**
	 * Método que determina de donde se debe obtener la información para
	 * la generación de la factura.
	 * 
	 * @param usuario
	 * @return
	 */
	public int obtenerTipoFuenteDatosFactura(UsuarioBasico usuario)
	{
		//1. Maneja Consecutivo Factura por Centro de Atención
		if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(usuario.getCodigoInstitucionInt()))
		{
			logger.info("X CENTRO ATENCION");
			
			return 0;
		}
		//2. Institución es multiempresa
		else if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuario.getCodigoInstitucionInt())))
		{
			return 1;
		}
		//3. Institución
		else 
		{
			logger.info("X INSTITUCION");
			return 2;
		}
	}
	
	/**
	 * @return the erroresFactura
	 */
	public ArrayList<ElementoApResource> getErroresFactura() {
		return erroresFactura;
	}

	/**
	 * @param erroresFactura the erroresFactura to set
	 */
	public void setErroresFactura(ArrayList<ElementoApResource> erroresFactura) {
		this.erroresFactura = erroresFactura;
	}

	/**
	 * @return the warningsFactura
	 */
	public ArrayList<ElementoApResource> getWarningsFactura() {
		return warningsFactura;
	}

	/**
	 * @param warningsFactura the warningsFactura to set
	 */
	public void setWarningsFactura(ArrayList<ElementoApResource> warningsFactura) {
		this.warningsFactura = warningsFactura;
	}

	
	/**
	 * (Simulacion errors ApplicationResources.properties)
	 */
	private void setWarningFactura (String llaveError, ArrayList<String> atributos)
	{
		ElementoApResource elem=new ElementoApResource(llaveError);
		for(String atributo: atributos)
		{	
			elem.agregarAtributo(atributo);
		}	
		this.warningsFactura.add(elem);
	}
}
