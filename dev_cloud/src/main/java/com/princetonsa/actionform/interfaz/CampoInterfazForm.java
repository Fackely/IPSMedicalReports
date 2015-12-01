/*
 * Creado en Apr 26, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.interfaz;

import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;

public class CampoInterfazForm extends ValidatorForm
{
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	/**
	 * Nombre del archivo plano de salida
	 */
	private String nombreArchivoSalida;
	
	/**
	 * Nombre anterior del archivo plano de salida
	 */
	private String nombreArchivoSalidaAnt;
	
	/**
	 * Path de ubicación del archivo de salida.
	 */
	private String pathArchivoSalida;
	
	/**
	 * Path anterior de ubicación del archivo de salida.
	 */
	private String pathArchivoSalidaAnt;
	
	/**
	 * Nombre del archivo plano de inconsistencias
	 */
	private String nombreArchivoInconsistencias;
	
	/**
	 * Nombre anterior del archivo plano de inconsistencias
	 */
	private String nombreArchivoInconsistenciasAnt;
	
	/**
	 * Path de ubicación del archivo de inconsistencias
	 */
	private String pathArchivoInconsistencias;
	
	/**
	 * Path anterior de ubicación del archivo de inconsistencias.
	 */
	private String pathArchivoInconsistenciasAnt;
	
	/**
	 * Caracter utilizado para la separación de los campos
	 */
	private String separadorCampos;
	
	/**
	 * Caracter anterior utilizado para la separación de los campos
	 */
	private String separadorCamposAnt;
	
	/**
	 * Separador de los decimales en cifras de valor
	 */
	private int separadorDecimales;
	
	/**
	 * Separador anterior de los decimales en cifras de valor
	 */
	private int separadorDecimalesAnt;
	
	/**
	 * Nombre del tipo de separador de decimales para poder mostrarlo
	 * en el log 
	 */
	private String nombreSeparadorDecimales;
	
	/**
	 * Nombre anterior del tipo de separador de decimales para poder mostrarlo
	 * en el log 
	 */
	private String nombreSeparadorDecimalesAnt;
	
	/**
	 * Identificador fin de archivo
	 */
	private String identificadorFinArchivo;
	
	/**
	 * Identificador anterior fin de archivo
	 */
	private String identificadorFinArchivoAnt;
	
	/**
	 * Presenta Devolución Paciente en el mismo campo del valor Paciente
	 */
	private String presentaDevolucionPaciente;
	
	/**
	 * Anterior Presenta Devolución Paciente en el mismo campo del valor Paciente
	 */
	private String presentaDevolucionPacienteAnt;
	
	/**
	 * Campo para indicar si en los casos de devolución paciente se muestra el valor con negativo
	 */
	private String valorNegativoDevolPaciente;
	
	/**
	 * Anterior Campo para indicar si en los casos de devolución paciente se muestra el valor con negativo
	 */
	private String valorNegativoDevolPacienteAnt;
	
	/**
	 * Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Débito
	 */
	private String descripcionDebito;
	
	/**
	 * Anterior Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Débito
	 */
	private String descripcionDebitoAnt;
	
	/**
	 * Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Crédito
	 */
	private String descripcionCredito;
	
	/**
	 * Anterior Descripción  que se debe presentar en el archivo plano para el tipo de movimiento Crédito
	 */
	private String descripcionCreditoAnt;
	
	/**
	 * Mapa para guardar información de parametrización de cada uno de los campos
	 */
	private HashMap mapa;
	
	/**
	 * Campo para indicar si hay información general de parametrización
	 */
	private boolean hayInfoGral;
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.nombreArchivoSalida="";
		this.nombreArchivoSalidaAnt="";
		this.pathArchivoSalida="";
		this.pathArchivoSalidaAnt="";
		this.nombreArchivoInconsistencias="";
		this.nombreArchivoInconsistenciasAnt="";
		this.pathArchivoInconsistencias="";
		this.pathArchivoInconsistenciasAnt="";
		this.separadorCampos="";
		this.separadorCamposAnt="";
		this.separadorDecimales=-1;
		this.separadorDecimalesAnt=-1;
		this.nombreSeparadorDecimales="";
		this.nombreSeparadorDecimalesAnt="";
		this.identificadorFinArchivo="";
		this.identificadorFinArchivoAnt="";
		this.presentaDevolucionPaciente="";
		this.presentaDevolucionPacienteAnt="";
		this.valorNegativoDevolPaciente="";
		this.valorNegativoDevolPacienteAnt="";
		this.descripcionDebito="";
		this.descripcionDebitoAnt="";
		this.descripcionCredito="";
		this.descripcionCreditoAnt="";
		this.mapa=new HashMap();
		
		this.hayInfoGral=false;
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("guardar"))
			{
				if (!UtilidadCadena.noEsVacio(this.nombreArchivoSalida))
					errores.add("Nombre Archivo Salida",  new ActionMessage("errors.required","El Nombre del archivo plano de salida"));
				
				if (!UtilidadCadena.noEsVacio(this.pathArchivoSalida))
					errores.add("Path Archivo Salida",  new ActionMessage("errors.required","El Path Ubicación archivo de salida"));
				
				if (!UtilidadCadena.noEsVacio(this.nombreArchivoInconsistencias))
					errores.add("Nombre Archivo Inconsistencias",  new ActionMessage("errors.required","El Nombre del archivo de inconsistencias"));
				
				if (!UtilidadCadena.noEsVacio(this.pathArchivoInconsistencias))
					errores.add("Paht Archivo Inconsistencias",  new ActionMessage("errors.required","El Path Ubicación archivo de inconsistencias"));
				
				if (!UtilidadCadena.noEsVacio(this.separadorCampos))
					errores.add("Separador Campos",  new ActionMessage("errors.required","El Separador de los campos"));
				
				if (this.separadorDecimales == -1)
					errores.add("Separador Decimales",  new ActionMessage("errors.required","El Separador de los decimales en cifras de valor"));
				
				/*if (!UtilidadCadena.noEsVacio(this.facturasAnuladas))
					errores.add("Facturas Anuladas",  new ActionMessage("errors.required","Incluye facturas anuladas"));*/
				
				if (!UtilidadCadena.noEsVacio(this.presentaDevolucionPaciente))
					errores.add("Presentar Devolucion Paciente",  new ActionMessage("errors.required","Presentar Devolución Paciente"));
				
				if (!UtilidadCadena.noEsVacio(this.valorNegativoDevolPaciente))
					errores.add("Valor Negativo",  new ActionMessage("errors.required","Valor negativo en devolución paciente "));
				
				if (!UtilidadCadena.noEsVacio(this.descripcionDebito))
					errores.add("Descripción Débito",  new ActionMessage("errors.required","La Descripción del movimiento débito"));
				
				if (!UtilidadCadena.noEsVacio(this.descripcionCredito))
					errores.add("Descripción Crédito",  new ActionMessage("errors.required","La Descripción del movimiento crédito"));
				
				/*if (!UtilidadCadena.noEsVacio(this.agruparFacturasValor))
					errores.add("Agrupar facturas valor",  new ActionMessage("errors.required","Agrupar las facturas por campos tipo valor"));*/
				
				//-------Se valida que el nombre del archivo de salidad sea diferente al nombre del archivo de inconsistencias-------//
				if (UtilidadCadena.noEsVacio(this.nombreArchivoSalida) && UtilidadCadena.noEsVacio(this.nombreArchivoInconsistencias))
					if (this.nombreArchivoSalida.equals(this.nombreArchivoInconsistencias))
						errores.add("Diferente nombre",  new ActionMessage("error.interfaz.camposInterfaz.igualNombreArchivoSalidaInconsistencias"));
				
				//-------------------SE VERIFICA LA INFORMACIÓN DE LOS CAMPOS REGISTROS DE INTERFAZ ------------------------//
				if(this.getMapa("codsCampoInterfaz") != null)
					{
						String codsCampoInterfaz=this.getMapa("codsCampoInterfaz")+"";
						String[] vecNuevosCampos=codsCampoInterfaz.split("-");
						Vector vecOrdenCampo=new Vector();
						
						int nroTipoValores=0;
																		
						for (int i=0; i<vecNuevosCampos.length; i++)
							{
							int codFilaCampo=Integer.parseInt(vecNuevosCampos[i]);
							//----------Valor del tipo de campo interfaz--------//
							int tipoCampoInterfaz=Integer.parseInt(this.getMapa("tipo_campo_interfaz_"+codFilaCampo)+"");
							
							if (tipoCampoInterfaz == -1)
								errores.add("Tipo Campo",  new ActionMessage("errors.required","El Tipo de Campo del registro Nro ["+(codFilaCampo+1)+"]"));
							else if (tipoCampoInterfaz==ConstantesBD.codigoTipoCampoValores)
									nroTipoValores++;
														
							//---------Valor del orden del campo-------------------//
							if (!UtilidadCadena.noEsVacio(this.getMapa("orden_campo_"+codFilaCampo)+""))
								errores.add("orden campo",  new ActionMessage("errors.required","El Orden Campo  del registro Nro ["+(codFilaCampo+1)+"]"));
							else
								vecOrdenCampo.add(this.getMapa("orden_campo_"+codFilaCampo)+"");
								
							//----------Valor del indicativo si es requerido ------------------//
							String indicativoRequerido = (String)this.getMapa("indicativo_requerido_"+codFilaCampo);
							
							if (!UtilidadCadena.noEsVacio(indicativoRequerido))
								errores.add("Indicativo requerido",  new ActionMessage("errors.required","El Indicativo Requerido  del registro Nro ["+(codFilaCampo+1)+"]"));
							}//for
						
						//---------------Se verifica que al menos se haya parametrizado un campo como tipo de campo valores ----------//
						  if (nroTipoValores == 0)
							  errores.add("Parametrizado Valores",  new ActionMessage("error.interfaz.camposInterfaz.parametrizadoTipoCampoValores"));
						
						//----------Se verifica que el orden campo no esté repetido ----------------------------//
						String erroresOrdenMostrados="";
						for (int i=0; i<vecNuevosCampos.length; i++)
							{
							int codFilaCampo=Integer.parseInt(vecNuevosCampos[i]);
							
							if (UtilidadCadena.noEsVacio(this.getMapa("orden_campo_"+codFilaCampo)+""))
								{
									int cont=0;
									String ordenCampo=(String)this.getMapa("orden_campo_"+codFilaCampo);
									
									for (int j=0; j<vecOrdenCampo.size(); j++)
										{
											if (ordenCampo.equals(vecOrdenCampo.elementAt(j)))
											{
												cont++;
											}
										}//for
									if (cont > 1 && erroresOrdenMostrados.indexOf("["+ordenCampo+"]")==-1)
										{
											errores.add("Orden Campo Repetido",  new ActionMessage("error.interfaz.camposInterfaz.ordenCampoRepetido", ordenCampo));
											erroresOrdenMostrados="["+ordenCampo+"]";
										}
								}//if
							}//for
						
					}//if codsCampoInterfaz!=null
				
									
				//-------------------------------------------Validación de los campos de registros de interfaz ---------------------------------------------//
				
			}
		
		return errores;
		
	}

	//---------------------------------------------------- SETS Y GETS -------------------------------------------------------------------------//
	/**
	 * @return Retorna the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

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
	 * @return Retorna the descripcionCreditoAnt.
	 */
	public String getDescripcionCreditoAnt()
	{
		return descripcionCreditoAnt;
	}

	/**
	 * @param descripcionCreditoAnt The descripcionCreditoAnt to set.
	 */
	public void setDescripcionCreditoAnt(String descripcionCreditoAnt)
	{
		this.descripcionCreditoAnt = descripcionCreditoAnt;
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
	 * @return Retorna the descripcionDebitoAnt.
	 */
	public String getDescripcionDebitoAnt()
	{
		return descripcionDebitoAnt;
	}

	/**
	 * @param descripcionDebitoAnt The descripcionDebitoAnt to set.
	 */
	public void setDescripcionDebitoAnt(String descripcionDebitoAnt)
	{
		this.descripcionDebitoAnt = descripcionDebitoAnt;
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
	 * @return Retorna the identificadorFinArchivoAnt.
	 */
	public String getIdentificadorFinArchivoAnt()
	{
		return identificadorFinArchivoAnt;
	}

	/**
	 * @param identificadorFinArchivoAnt The identificadorFinArchivoAnt to set.
	 */
	public void setIdentificadorFinArchivoAnt(String identificadorFinArchivoAnt)
	{
		this.identificadorFinArchivoAnt = identificadorFinArchivoAnt;
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
	 * @return Retorna the nombreArchivoInconsistenciasAnt.
	 */
	public String getNombreArchivoInconsistenciasAnt()
	{
		return nombreArchivoInconsistenciasAnt;
	}

	/**
	 * @param nombreArchivoInconsistenciasAnt The nombreArchivoInconsistenciasAnt to set.
	 */
	public void setNombreArchivoInconsistenciasAnt(
			String nombreArchivoInconsistenciasAnt)
	{
		this.nombreArchivoInconsistenciasAnt = nombreArchivoInconsistenciasAnt;
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
	 * @return Retorna the nombreArchivoSalidaAnt.
	 */
	public String getNombreArchivoSalidaAnt()
	{
		return nombreArchivoSalidaAnt;
	}

	/**
	 * @param nombreArchivoSalidaAnt The nombreArchivoSalidaAnt to set.
	 */
	public void setNombreArchivoSalidaAnt(String nombreArchivoSalidaAnt)
	{
		this.nombreArchivoSalidaAnt = nombreArchivoSalidaAnt;
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
	 * @return Retorna the pathArchivoInconsistenciasAnt.
	 */
	public String getPathArchivoInconsistenciasAnt()
	{
		return pathArchivoInconsistenciasAnt;
	}

	/**
	 * @param pathArchivoInconsistenciasAnt The pathArchivoInconsistenciasAnt to set.
	 */
	public void setPathArchivoInconsistenciasAnt(
			String pathArchivoInconsistenciasAnt)
	{
		this.pathArchivoInconsistenciasAnt = pathArchivoInconsistenciasAnt;
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
	 * @return Retorna the pathArchivoSalidaAnt.
	 */
	public String getPathArchivoSalidaAnt()
	{
		return pathArchivoSalidaAnt;
	}

	/**
	 * @param pathArchivoSalidaAnt The pathArchivoSalidaAnt to set.
	 */
	public void setPathArchivoSalidaAnt(String pathArchivoSalidaAnt)
	{
		this.pathArchivoSalidaAnt = pathArchivoSalidaAnt;
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
	 * @return Retorna the presentaDevolucionPacienteAnt.
	 */
	public String getPresentaDevolucionPacienteAnt()
	{
		return presentaDevolucionPacienteAnt;
	}

	/**
	 * @param presentaDevolucionPacienteAnt The presentaDevolucionPacienteAnt to set.
	 */
	public void setPresentaDevolucionPacienteAnt(
			String presentaDevolucionPacienteAnt)
	{
		this.presentaDevolucionPacienteAnt = presentaDevolucionPacienteAnt;
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
	 * @return Retorna the separadorCamposAnt.
	 */
	public String getSeparadorCamposAnt()
	{
		return separadorCamposAnt;
	}

	/**
	 * @param separadorCamposAnt The separadorCamposAnt to set.
	 */
	public void setSeparadorCamposAnt(String separadorCamposAnt)
	{
		this.separadorCamposAnt = separadorCamposAnt;
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
	 * @return Retorna the separadorDecimalesAnt.
	 */
	public int getSeparadorDecimalesAnt()
	{
		return separadorDecimalesAnt;
	}

	/**
	 * @param separadorDecimalesAnt The separadorDecimalesAnt to set.
	 */
	public void setSeparadorDecimalesAnt(int separadorDecimalesAnt)
	{
		this.separadorDecimalesAnt = separadorDecimalesAnt;
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
	 * @return Retorna the valorNegativoDevolPacienteAnt.
	 */
	public String getValorNegativoDevolPacienteAnt()
	{
		return valorNegativoDevolPacienteAnt;
	}

	/**
	 * @param valorNegativoDevolPacienteAnt The valorNegativoDevolPacienteAnt to set.
	 */
	public void setValorNegativoDevolPacienteAnt(
			String valorNegativoDevolPacienteAnt)
	{
		this.valorNegativoDevolPacienteAnt = valorNegativoDevolPacienteAnt;
	}

	/**
	 * @return Retorna the hayInfoGral.
	 */
	public boolean isHayInfoGral()
	{
		return hayInfoGral;
	}

	/**
	 * @param hayInfoGral The hayInfoGral to set.
	 */
	public void setHayInfoGral(boolean hayInfoGral)
	{
		this.hayInfoGral = hayInfoGral;
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

	/**
	 * @return Retorna the nombreSeparadorDecimalesAnt.
	 */
	public String getNombreSeparadorDecimalesAnt()
	{
		return nombreSeparadorDecimalesAnt;
	}

	/**
	 * @param nombreSeparadorDecimalesAnt The nombreSeparadorDecimalesAnt to set.
	 */
	public void setNombreSeparadorDecimalesAnt(String nombreSeparadorDecimalesAnt)
	{
		this.nombreSeparadorDecimalesAnt = nombreSeparadorDecimalesAnt;
	}

	
	
}
