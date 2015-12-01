package com.princetonsa.action.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.carteraPaciente.ApliPagosCarteraPacienteForm;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.carteraPaciente.ApliPagosCarteraPaciente;



/**
 * @author Julio Hernandezy
 * */
public class ApliPagosCarteraPacienteAction extends Action
{	
	
	Logger logger = Logger.getLogger(ApliPagosCarteraPacienteAction.class);
	
	
	/**
	 * Metodo execute del action
	 * */	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form, 
								 HttpServletRequest request, 
								 HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try{
		
		 if (form instanceof ApliPagosCarteraPacienteForm) 
		 {
			 
			 
			 con = UtilidadBD.abrirConexion();
			 
			 if(con == null)
			 {
				 request.setAttribute("CodigoDescripcionError","erros.problemasBd");
				 return mapping.findForward("paginaError");
			 }
			 
			 UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");			 
			 //paciente cargado en sesion 
			 PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			 
			 ApliPagosCarteraPacienteForm forma = (ApliPagosCarteraPacienteForm)form;
			 String estado = forma.getEstado();
			 ActionErrors errores = new ActionErrors();
			 		 
			 logger.info("-------------------------------------");
			 logger.info("Valor del Estado >> "+forma.getEstado());
			 logger.info("Valor de la conexion  >> "+con);
			 logger.info("-------------------------------------");
			 
			 if(estado == null)
			 {
				 forma.reset();
				 logger.warn("Estado no Valido dentro del Flujo de Aplicación Pagos Cartera Paciente (null)");				 
				 request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("paginaError");
			 }
			 
			 //Estado empezar
			 if (estado.equals("empezar"))
			 {
				 forma.reset();
				 forma.setListaRecibosCaja(ApliPagosCarteraPaciente.consultarRecibosCaja(con));
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("principal");
			 }
			 
			 //Estado busquedaAvanzadaRC
			 if (estado.equals("busquedaAvanzadaRC"))
			 {
				 forma.reset();
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("busqueda");
			 }
			 
			 //Estado buscarRCFiltrados
			 if (estado.equals("buscarRCFiltrados"))
			 {
				 //Se resetea el array que se muestra al principio
				 forma.resetListadoRC();
				 	
				 if (!forma.getFechaInicialGen().equals(""))
				 {
					 if (!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicialGen()))
					 { 
						 errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Fecha Inicial de Generación "+forma.getFechaInicialGen()));
						 saveErrors(request, errores);
					 }
				 }
				 
				 if (!forma.getFechaFinalGen().equals(""))
				 {
					 if (!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaFinalGen()))
					 {	
						 errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Fecha Final de Generación "+forma.getFechaFinalGen()));
						 saveErrors(request, errores);
					 }
				 }
				 
				 if (!forma.getFechaInicialGen().equals("")&&forma.getFechaFinalGen().equals(""))
				 {
					 errores.add("", new ActionMessage("errors.required", "Fecha Final de generación "));
					 saveErrors(request, errores);
				 }
				 
				 if (forma.getFechaInicialGen().equals("")&&!forma.getFechaFinalGen().equals(""))
				 {
					 errores.add("", new ActionMessage("errors.required", "Fecha Inicial de generación "));
					 saveErrors(request, errores);
				 }
				 	
				 if (!forma.getFechaInicialGen().equals("")&&!forma.getFechaFinalGen().equals(""))
				 {
					if (UtilidadFecha.esFechaValidaSegunAp(forma.getFechaInicialGen())&&UtilidadFecha.esFechaValidaSegunAp(forma.getFechaFinalGen()))
					{
						if (UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaFinalGen(),forma.getFechaInicialGen()))
						 {
							errores.add("",new ActionMessage("errors.notEspecific", "La fecha de generación final debe ser mayor que la fecha de generación inicial"));
							saveErrors(request, errores);
						 }
					}
				 }
				
				 if (forma.getDocFinal().equals("")&&!forma.getDocInicial().equals(""))
				 {
					 errores.add("", new ActionMessage("errors.required", "Documento Final "));
					 saveErrors(request, errores);
				 }
				 
				 if (!forma.getDocFinal().equals("")&&forma.getDocInicial().equals(""))
				 {
					 errores.add("", new ActionMessage("errors.required", "Documento Inicial "));
					 saveErrors(request, errores);
				 }
				 
				 if (!forma.getDocFinal().equals("")&&!forma.getDocInicial().equals(""))
				 {
					if (Utilidades.convertirAEntero(forma.getDocFinal())<Utilidades.convertirAEntero(forma.getDocInicial()))
					{
						errores.add("",new ActionMessage("errors.notEspecific", "El documento final debe ser mayor que el documento inicial "));
						saveErrors(request, errores);
					}
				 }
				
				if (errores.isEmpty())
				 {
					//Se agregan al mapa los filtros
					 forma.getFiltros().put("tipo",forma.getTipo());
					 forma.getFiltros().put("fechaFinalGen",forma.getFechaFinalGen());
					 forma.getFiltros().put("fechaInicialGen",forma.getFechaInicialGen());
					 forma.getFiltros().put("docInicial",forma.getDocInicial());
					 forma.getFiltros().put("docFinal",forma.getDocFinal());
					 //Se llena el arraylist con la busqueda por filtros
					 forma.setListaRecibosCaja(ApliPagosCarteraPaciente.consultarRecibosCajaFiltros(con,forma.getFiltros()));
				 }
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("busqueda");
			 }
			 
			 //Estado busquedaDocumentos
			 if (estado.equals("busquedaDocumentos"))
			 {
				 int indice=Utilidades.convertirAEntero(forma.getIndiceAplicacionPago());
				 String deudor=forma.getListaRecibosCaja().get(indice).getCodDeudor();
				 String nroRC=forma.getListaRecibosCaja().get(indice).getConsecutivo();
				 
				 //Se buscan los documentos de garantía
				 forma.setListadoDocsGarantia(ApliPagosCarteraPaciente.consultarDocsFarantia(con,nroRC,deudor));
				 
				 UtilidadBD.cerrarConexion(con);
				 return mapping.findForward("busquedaDocs");
			 }
			 
			 
			 
			 //Estado aplicarPagosDocs
			 if (estado.equals("aplicarPagosDocs"))
			 {
				 //Se resetean los valores necesarios para omenzar la aplicación
				 forma.resetAplicacion();
				 
				 //********Validaciones para la fecha de aplciacion
				 if (!forma.getFechaAplicacion().equals(""))
				 {
					 if (!UtilidadFecha.esFechaValidaSegunAp(forma.getFechaAplicacion()))
					 {
						 errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Fecha de Aplicación "+forma.getFechaFinalGen()));
						 saveErrors(request, errores);
					 }
				 }
				 
				 if (!forma.getFechaAplicacion().equals(""))
				 {
					 if (UtilidadFecha.esFechaValidaSegunAp(forma.getFechaAplicacion()))
					 {
					 	if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaAplicacion(), UtilidadFecha.getFechaActual()))
						 {
							errores.add("",new ActionMessage("errors.notEspecific", "La fecha de Aplicación debe ser menor ó igual a la fecha actual"));
							saveErrors(request, errores); 
						 }
					 }
				 }
				 
				 if (forma.getFechaAplicacion().equals(""))
				 {
					 errores.add("", new ActionMessage("errors.required", "La fecha de Aplicación "));
					 saveErrors(request, errores);
				 }
				 
				 //*********Fin Validaciones fecha de aplicación
				 
				 
				 //Se inicializan las variables a usar
				 int indice=Utilidades.convertirAEntero(forma.getIndiceAplicacionPago());
				 double saldo=forma.getListaRecibosCaja().get(indice).getSaldo();
				 int numRegsGarantia=forma.getListadoDocsGarantia().size();
				 boolean errorValorMayor=false;
				 double totalAAplicar=0; 
				 
				 //Se suma el total de los valores a aplicar
				 for (int i=0;i<numRegsGarantia;i++)
				 {
					 //Se suma el total de los valores que cubrirá el RC para el doc de garantia
					 if (!forma.getValorAplicar("valor_"+i).toString().equals(""))
						 totalAAplicar+=Utilidades.convertirADouble(forma.getValorAplicar("valor_"+i).toString());
					 
					 //Se mira si hay número de cuotas par aplicar el pago
					 if (forma.getListadoDocsGarantia().get(i).getCuotas()<=0)
					 {
						errores.add("",new ActionMessage("errors.notEspecific", "No se puede aplicar pago para el documento "+forma.getListadoDocsGarantia().get(i).getNumeroDocumento()+" porque no tiene número de cuotas"));
						saveErrors(request, errores); 
					 }
					 
					 //Se mira que haya un valor a aplicar
					 if (forma.getValorAplicar("valor_"+i).toString().equals(""))
					 {
						 errores.add("", new ActionMessage("errors.required", "Para el documento "+forma.getListadoDocsGarantia().get(i).getNumeroDocumento()+" un valor a aplicar"));
						 saveErrors(request, errores);
					 }
					 
					 if (Utilidades.convertirADouble(forma.getValorAplicar("valor_"+i).toString())>forma.getListadoDocsGarantia().get(i).getSaldoActual())
					 {
						 errores.add("", new ActionMessage("errors.notEspecific", "Para el Documento documento "+forma.getListadoDocsGarantia().get(i).getNumeroDocumento()+" No se puede aplicar el valor de "+forma.getValorAplicar("valor_"+i)+" ya que tiene que ser menor o igual al valor del Saldo Actual"));
						 saveErrors(request, errores);
					 }
				 }
				 
				 //Se mira de que no se vaya a aplicar un valor mayor al saldo del recibo de caja
				 if (totalAAplicar>saldo)
					 errorValorMayor=true;
				 
				 //Si no hay errores se comienza a ingresar la aplicación
				 if (errores.isEmpty())
				 {
					 for (int i=0;i<numRegsGarantia;i++)
					 {			 
						 //Se llena el DTO de aplicacion pagos
						 DtoAplicacPagosCarteraPac dto= new DtoAplicacPagosCarteraPac();
						 dto.setDatosFinanciacion(forma.getListadoDocsGarantia().get(i).getCodigoDatosFinanciacion());
						 dto.setValor(Utilidades.convertirADouble(forma.getValorAplicar("valor_"+i).toString()));
						 dto.setFechaAplicacion(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAplicacion()));
						 dto.setUsuario(usuario.getLoginUsuario());
						 dto.setNumeroDocumento(forma.getListaRecibosCaja().get(indice).getConsecutivo());
						 dto.setTipoDocumento(forma.getListaRecibosCaja().get(indice).getTipoDocumento());
						 dto.setInstitucion(usuario.getCodigoInstitucionInt());
						 dto.setFecha(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						 dto.setHora(UtilidadFecha.getHoraActual());
						 
						 //Se consultan las cuotas de datos financiacion
						 forma.setListaCuotasDatosFin(ApliPagosCarteraPaciente.consultaCuotasDatosFin(con,dto.getDatosFinanciacion()+""));
						 //El número de registros del array de Cuotas
						 int numRegsCuotas=forma.getListaCuotasDatosFin().size();
						 
						 //Si no hay cuotas a aplicar no se puede hacer el pago y se muestra el error
						 if (numRegsCuotas==0)
						 {
							errores.add("",new ActionMessage("errors.notEspecific", "No hay cuotas para pagar ó la deuda está paga para el documento "+dto.getNumeroDocumento()+" porfavor revisar en al funcionalidad de Consultar/Refinanciaciar Cuotas Cartera paciente."));
							saveErrors(request, errores);
							break;
						 }
						 
						 //Se organizan los valores para ver cuales se pueden aplicar y cuales no y si para ver el verdadero total a aplicar
						 //Variable para guardar el total a sumar y el verdadero valor de la cuota
						BigDecimal valorAplicar = new BigDecimal(dto.getValor());
						//Variable apra gaurdar temp el valor de la cuota
						BigDecimal valorCuota= new BigDecimal(0);
						//Variable para sumar las cuotas
						BigDecimal sumaTotalCuotas = new BigDecimal(0);
						
						
						//Se marcan las cuotas que se peuden aplicar y se gaurda el valor a pagar
						 for (int k=0;k<numRegsCuotas;k++)
						{		
							 valorCuota=forma.getListaCuotasDatosFin().get(k).getValorCuota();
							 
							 //Variable Bigdecimal para el valor de 0
							 BigDecimal cero=new BigDecimal(0);
							 
							 //Si el valor a aplicar es mayor a la cuota a aplicar
							 if (valorCuota.compareTo(valorAplicar)<=0
									 &&(Utilidades.convertirADouble(valorAplicar.toString())!=Utilidades.convertirADouble(cero.toString())))
							 {
								logger.info("MAYOR QUE");
								sumaTotalCuotas = sumaTotalCuotas.add(valorCuota);
								valorAplicar=valorAplicar.subtract(valorCuota);
						 	 	forma.getListaCuotasDatosFin().get(k).setValorAplicar(valorCuota);
						 	 	forma.getListaCuotasDatosFin().get(k).setAplicar(true);
						 	 	logger.info("valor cuota-->"+valorCuota);
						 	 	logger.info("valor aplicar-->"+valorAplicar);
						 	 	logger.info("TOTAL DE cuotas-->"+sumaTotalCuotas);
							 }
							//Si el valor a aplicar es menor a la cuota a aplicar
							 if (valorCuota.compareTo(valorAplicar)>0
									 &&(Utilidades.convertirADouble(valorAplicar.toString())!=Utilidades.convertirADouble(cero.toString())))
							 {
								logger.info("MENOR QUE");
								sumaTotalCuotas=sumaTotalCuotas.add(valorAplicar);
								forma.getListaCuotasDatosFin().get(k).setValorAplicar(valorAplicar);
								valorAplicar=valorAplicar.subtract(valorAplicar);
								forma.getListaCuotasDatosFin().get(k).setAplicar(true);
								logger.info("valor cuota-->"+valorCuota);
						 	 	logger.info("valor aplicar-->"+valorAplicar);
						 	 	logger.info("TOTAL DE cuotas-->"+sumaTotalCuotas);
							 }
							 else
							 {
								 forma.getListaCuotasDatosFin().get(k).setAplicar(false);
							 }
						}
						 
						 //Se determina el verdadero valor a aplicar para ponerlo en el encabezado
						 dto.setValor(Utilidades.convertirADouble(sumaTotalCuotas.toString()));
						 
						
						 //Si existen cuotas se crea el encabezado
						 if (numRegsCuotas>0)
						 {
							 //******Se genera el encabezado de la aplicación y se obtiene el valor del codigo pk
							 double codigoPkInsertado=ApliPagosCarteraPaciente.ingresarEncabezadoAplicPagos(con,dto);
							 if (codigoPkInsertado>0)
							 {
								 
								 int codigoPk;
								 int datosFinanciacion;
								 valorAplicar= new BigDecimal(dto.getValor());
								 
								 //Si se genera el encabezado se empiezan a aplicar los pagos para cada una de las cuotas asociadas al dato de financiacion
								 for (int j=0;j<numRegsCuotas;j++)
								 {
									 //Se guardan los datos en variables locales
									 codigoPk=forma.getListaCuotasDatosFin().get(j).getCodigoPK();
									 datosFinanciacion=forma.getListaCuotasDatosFin().get(j).getDatosFinanciacion();
									 valorCuota=forma.getListaCuotasDatosFin().get(j).getValorCuota();
									 
									 //Si la cuota se marcó para aplicar
									 BigDecimal cero=new BigDecimal(0);
									 if (forma.getListaCuotasDatosFin().get(j).isAplicar())
									 {
										 //Se arma el Dto para el detalle de aplicacion de pagos
										 DtoDetApliPagosCarteraPac dtoDetApli = new DtoDetApliPagosCarteraPac();
										 dtoDetApli.setCuotaDatoFinanciacion(forma.getListaCuotasDatosFin().get(j).getCodigoPK());
										 dtoDetApli.setValor(forma.getListaCuotasDatosFin().get(j).getValorAplicar());
										 //Se asigna el valor de la secuencia del encabezado ingresado
										 dtoDetApli.setApliPagoCarteraPac(codigoPkInsertado);
										 
										 //Se genera el detalle de pagos
										 if (ApliPagosCarteraPaciente.ingresarDetApliPagosCarteraPac(con,dtoDetApli))
										 {
											 logger.info("SE GENERO CORRECTAMENTE EL DETALLE!");
										 }
									 }
								 }
							 }
						 }
						 //Se llena el arraylist de arraylist para las apliaciones creadas
						 forma.getListaAplicacionesCreadas().add(ApliPagosCarteraPaciente.consultarAplicacion(con)); 
					 }
				 }
				 
				 //Se pasa a estado cancelado los documentos de garantia que estén completamente cancelados
				  String deudor=forma.getListaRecibosCaja().get(indice).getCodDeudor();
				  String nroRC=forma.getListaRecibosCaja().get(indice).getCodDeudor();
				 //Se buscan los documentos de garantía
				 forma.setListadoDocsGarantia(ApliPagosCarteraPaciente.consultarDocsFarantia(con,nroRC,deudor));
				 int tamanioListadoDG=forma.getListadoDocsGarantia().size();
				 
				 //Se recorre el DTO en busqueda de documentos que ya esten saldados
				 for (int m=0;m<tamanioListadoDG;m++)
				 {
					 HashMap datosDoc=new HashMap();
					 
					 if (forma.getListadoDocsGarantia().get(m).getSaldoActual()==0)
					 {
						logger.info("ENTRE A INACTIVAR!!!!!");
						datosDoc.put("ingreso", forma.getListadoDocsGarantia().get(m).getIngresoDocumento());
						datosDoc.put("tipodocumento", forma.getListadoDocsGarantia().get(m).getTipoDocumento());
						datosDoc.put("consecutivo", forma.getListadoDocsGarantia().get(m).getNumeroDocumento());
						datosDoc.put("anio", forma.getListadoDocsGarantia().get(m).getAnioDocumento());
						ApliPagosCarteraPaciente.cancelarDocGarantia(con,datosDoc);
						
					 }
						 
				 }
				 
				 UtilidadBD.cerrarConexion(con);
				 if(errores.isEmpty())
					 return mapping.findForward("detalleAplicacion");
				 else
					 return mapping.findForward("busquedaDocs");
			 }
		 }
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		 return null;
	}
}