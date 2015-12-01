/*
 * @(#)ServiciosForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01 
 *
 */

package com.princetonsa.actionform;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Forma para manejo presentación de la funcionalidad "Servicios"
 * 
 * @version 1.0 Nov 27, 2003
 */
public class ServiciosForm extends ValidatorForm
{

	/**
	 * Mapa con el que se capturarán todos los
	 * posibles servicios que existan y/o el usuario 
	 * haya decidido modificar
	 */
	private  HashMap valores = new HashMap();
	
	/**
	 * Código de la especialidad para la cual se 
	 * restringe este servicio
	 */
	private int especialidad;
	
	/**
	 * Código del sexo para el cual se restringe 
	 * este servicio
	 */
	private int sexo;

	/**
	 * Acrónimo del tipo de servicio
	 */
	private String tipoServicio;

	/**
	 * Acrónimo de la naturaleza del servicio
	 */
	private String naturalezaServicio;

	/**
	 * Boolean que indica si este servicio
	 * es Pos o no
	 */
	private boolean esPos;
	
	
	/**
	 * 
	 */
	private String posSubsidiado;
	
	/**
	 * Mapa que contiene la informacion de los formularios
	 * parametrizados por el servicio
	 */
	private HashMap<String, Object> formulario = new HashMap<String, Object>();
	
	/**
	 * Arreglo para el manejo de la parametrización delos formularios
	 */
	private ArrayList<DtoPlantillaServDiag> arregloFormularios = new ArrayList<DtoPlantillaServDiag>();
	
	/**
	 * Codigo del formulario
	 */
	private int codigoFormulario;
	
	/**
	 * nivel
	 */
	private String nivel;
	
	/**
	 * Código / Acronimo / Nombre de
	 * este servicio según Cups
	 */
	private String codigoCups;
	
	/**
	 * Descripción de este servicio según 
	 * Cups
	 */
	private String descripcionCups;
	
	/**
	 * Código / Acronimo / Nombre de
	 * este servicio según ISS
	 */
	private String codigoISS;
	
	/**
	 * Descripción de este servicio según 
	 * ISS
	 */
	private String descripcionISS;
	
	/**
	 * Código / Acronimo / Nombre de
	 * este servicio según Soat
	 */
	private String codigoSoat;
	
	/**
	 * Descripción de este servicio según 
	 * Soat
	 */
	private String descripcionSoat;
	
	/**
	 * Unidades Soat
	 */
	private String unidadesSoat;
	
	
	/**
	 * Grupos del servicio
	 */
	private int grupoServicio;
	
	
	/**
	 * Entero que indica el número de servicios
	 * a manejar en la 
	 */
	private int numeroServicios;
	
	/**
	 * Atributo que me dice en que indice voy 
	 * (navegación de modificar)
	 */
	private int indiceNavegacion;
	
	/**
	 * Atributo que me dice hasta que indice debo ir 
	 * (navegación de modificar)
	 */
	private int siguienteIndiceNavegacion;

	/**
	 * Atributo que me dice el número de elementos 
	 * que debo mostrar (navegación de modificar)
	 */
	private int numeroElementosMostrar;
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * Unidades uvr
	 */
	private String unidadesUvr;
	
	/**
	 * Variable que indica si el servicio requiere diagnostico
	 */
	private String requiereDiagnostico;

	/**
	 * En el caso que el estado sea finalizar,
	 * hay que distinguir entre que se debe hacer.
	 * En esta clase esta por uniformidad y seguridad
	 * (cualquiera puede poner el parámetro en el path)
	 * Valores validos: insertar
	 * 
	 */
	private String accionAFinalizar="";

	/**
	 * Contiene el log con info original para
	 * almacenar esta info en el log tipo Archivo
	 */
	private String  logInfoOriginalServicio="";
	
	/**
	 * Contiene la posicion del registro que es seleccionado
	 * paa ver su detalle
	 */
	private int pos;
	
	
	
	private String realizaInstitucion;
	private String requiereInterpretacion;
	private String costo;
	
	private HashMap tarifariosMap;
	
	private int codigoTarifario;
	private String nombreTarifario;
	private String codigoPropietario;
	private String descripcionTarifario;
	
	/**
	 * Codigo y Descripcion con la informacion para los servicios portatiles
	 * Adicion señalada en el documento Anexo 591
	 */
	private String desServicioPortatil;
	
	private int codigoServicioPortatil;
	
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado 
	 */
	
	//Cambios or Anexo 868
	
	private String atencionOdontologica;
	
	private int convencion;
	
	private String minutosDuracion;
	
	private String archivoImagen;
	
	
	public void reset ()
	{
		accionAFinalizar="";
		especialidad=-1;
		sexo=-1;
		tipoServicio="";
		naturalezaServicio="";
		esPos=true;
		this.posSubsidiado=ConstantesBD.acronimoNo;
		formulario= new HashMap<String, Object>();
		this.arregloFormularios = new ArrayList<DtoPlantillaServDiag>();
		this.codigoFormulario = ConstantesBD.codigoNuncaValido;
		nivel="";
		codigoCups="";
		descripcionCups="";
		codigoISS="";
		descripcionISS="";
		codigoSoat="";
		descripcionSoat="";
		unidadesSoat="0.0";
		unidadesUvr="0.0";
		this.requiereDiagnostico = "";
		this.grupoServicio = -1;
		numeroServicios=0;
		indiceNavegacion=0;
		numeroElementosMostrar=3;
		siguienteIndiceNavegacion=0;
		this.pos = 0;
		
		this.requiereInterpretacion=ConstantesBD.acronimoNo;	
		this.realizaInstitucion="";
		this.costo=ConstantesBD.codigoNuncaValidoDouble+"";
		this.tarifariosMap= new HashMap();
		this.tarifariosMap.put("numRegistros","0");
		this.desServicioPortatil = "";
		this.codigoServicioPortatil = ConstantesBD.codigoNuncaValido;
		valores.clear();
		
		this.atencionOdontologica=ConstantesBD.acronimoNo;
		this.convencion=ConstantesBD.codigoNuncaValido;
		this.minutosDuracion="";
		this.archivoImagen="";

	}
	
	public void resetOdontologia()
	{
		this.convencion=ConstantesBD.codigoNuncaValido;
		this.minutosDuracion="";
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
		//System.out.print("\n estado --> "+this.estado+"  naturaleza --> "+this.naturalezaServicio+" accion finalizar --> "+this.accionAFinalizar);
		
		int i;
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);

		if (this.estado.equals("salir"))
		{
			if (this.accionAFinalizar.equals("insertar"))
			{
				if (this.especialidad==-1)
				{
					errors.add("especialidadInvalida", new ActionMessage("errors.seleccionInvalida", "especialidad"));
				}
				if (this.naturalezaServicio==null||this.naturalezaServicio.equals(""))
				{
					errors.add("naturalezaServicioInvalida", new ActionMessage("errors.seleccionInvalida", "naturaleza de servicio"));
				}
				if (this.tipoServicio==null||this.tipoServicio.equals(""))
				{
					errors.add("tipoServicioInvalido", new ActionMessage("errors.seleccionInvalida", "tipo de servicio"));
				}
				
				if (this.grupoServicio==-1)
				{
					errors.add("grupoServicioInvalido", new ActionMessage("errors.seleccionInvalida", "grupo del servicio"));
				}
				
				//Se valida el campo requiere diagnostico segun el tipo de servicio
				if(this.tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")||this.tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")||this.tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||this.tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+""))
				{
					if(this.requiereDiagnostico.equals(""))
						errors.add("requiereDiagnosticoRequerido", new ActionMessage("errors.required", "El campo requiere diagnóstico"));
				}
				else
					this.requiereDiagnostico = "";
				
				if (this.codigoCups==null||this.codigoCups.equals("") || !Utilidades.validarEspacios(this.codigoCups))
				{
				    errors.add("errors.required", new ActionMessage("errors.required", "El Codigo CUPS"));	
				}
				
				if (this.descripcionCups==null || this.descripcionCups.equals("") || !Utilidades.validarEspacios(this.descripcionCups))
				{
					errors.add("errors.required", new ActionMessage("errors.required", "La descripción CUPS"));
				}
				
				if(this.descripcionISS==null || this.descripcionISS.equals("") || !Utilidades.validarEspacios(this.descripcionISS))
				{
				    this.descripcionISS="";
				}
				
				if(this.codigoISS==null || this.codigoISS.equals("") || !Utilidades.validarEspacios(this.codigoISS))
				{
				    this.codigoISS="";
				}
				
				if(this.descripcionSoat==null || this.descripcionSoat.equals("") || !Utilidades.validarEspacios(this.descripcionSoat))
				{
				    this.descripcionSoat="";
				}
				
				if(this.codigoSoat==null || this.codigoSoat.equals("") || !Utilidades.validarEspacios(this.codigoSoat))
				{
				    this.codigoSoat="";
				}
				
				/*if (this.descripcionISS!=null&&!this.descripcionISS.equals("") )
				{
					if (this.codigoISS==null||this.codigoISS.equals(""))
					{
						this.codigoISS="";
						//errors.add("descripcionSinCodigoISS", new ActionMessage("errors.descripcionSinCodigo", "ISS"));
					}
				}*/
				/*if (this.descripcionSoat!=null&&!this.descripcionSoat.equals(""))
				{
					if (this.codigoSoat==null||this.codigoSoat.equals(""))
					{
						this.codigoSoat="";
					}
				}*/
				if ( (this.codigoCups==null||this.codigoCups.equals("") || !Utilidades.validarEspacios(this.codigoCups)) &&  (this.codigoISS==null||this.codigoISS.equals("") || !Utilidades.validarEspacios(this.codigoISS)) &&  (this.codigoSoat==null || this.codigoSoat.equals("") || !Utilidades.validarEspacios(this.codigoSoat))  )
				{
					//Si se presenta este caso el soat lo vamos a
					//guardar vacío, si se quiere validar que se ponga
					//al menos un código se descomenta la linea 
					//de abajo (2 líneas abajo)
					this.codigoCups="";
					//errors.add("error.servicios.ningunCodigo", new ActionMessage("error.servicios.ningunCodigo"));
				}
				if (this.codigoCups!=null && !this.codigoCups.equals("") && Utilidades.validarEspacios(this.codigoCups))
				{
					if (this.codigoCups.length()>12)
					{
						errors.add("tamanioCodigoCupsInvalido", new ActionMessage("errors.maxlength", "El código CUPS", "12"));
					}
				}
				if (this.codigoISS!=null && !this.codigoISS.equals("") && Utilidades.validarEspacios(this.codigoISS))
				{
					if (this.codigoISS.length()>12)
					{
						errors.add("tamanioCodigoISSInvalido", new ActionMessage("errors.maxlength", "El código ISS", "12"));
					}
				}
				if (  (this.codigoSoat!=null && !this.codigoSoat.equals("") && Utilidades.validarEspacios(this.codigoSoat))    ||   (this.descripcionSoat!=null && !this.descripcionSoat.equals("") && Utilidades.validarEspacios(this.descripcionSoat))  )
				{
					if (this.codigoSoat.length()>12)
					{
						errors.add("tamanioCodigoSOATInvalido", new ActionMessage("errors.maxlength", "El código SOAT", "12"));
					}
				}

				if (this.unidadesSoat==null||this.unidadesSoat.equals(""))
				{
					this.unidadesSoat="-1.0";
				}
				else 
				{
					try
					{
						if(Double.parseDouble(this.unidadesSoat)<0)
						{
							errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades SOAT ", "0"));
						}
					}
					catch (Exception e)
					{
						errors.add("error.servicios.unidadesSoatNoFloat", new ActionMessage("error.servicios.unidadesSoatNoFloat"));
					}
				}
				
				if (this.unidadesUvr==null || this.unidadesUvr.equals(""))
				{
					//Vamos a dejarlo en un valor  negativo y vamos a revisar este hecho en la inserción
					this.unidadesUvr="-1.0";
				}
				else
				{
					try
					{
						if (Double.parseDouble(this.unidadesUvr)<0)
						{
							errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades Uvr ", "0"));
						}
					}
					catch (Exception e)
					{
						errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades Uvr ", "0"));
					}
				}
				
				if(this.nivel==null || this.nivel.trim().equals(""))
				{
					errors.add("errors.required", new ActionMessage("errors.required", "El Nivel de Atención"));
				}
				
				//Se agregó el siguiente if para saber si se debe o no realizar la validacion 
				//de minutos  mayor a 0 que aplica solo para atención odontológica.
				if (this.atencionOdontologica.equals(ConstantesBD.acronimoSi)){
					if (Utilidades.convertirAEntero(this.minutosDuracion)<=0)
					{
						errors.add("errors.MayorQue", new ActionMessage("errors.MayorQue", "Los minutos de duración "," 0 "));
					}
				}
				
			}
			else if (this.accionAFinalizar.equals("modificar"))
			{
				String parametro;
				
				//1. Se cambiaron los checkbox por la presencia de varias
				//páginas
				
				i = this.pos;
				
					
				parametro=(String)valores.get("descripcionCups_" + i);
				if (parametro==null||parametro.equals(""))
				{
					errors.add("errors.required", new ActionMessage("errors.required", "La descripción CUPS del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i)));
				}
				else
				{
					parametro=(String)valores.get("nombreCups_" + i);
					if (parametro==null||parametro.equals(""))
					{
						//errors.add("descripcionSinCodigoCups", new ActionMessage("errors.descripcionSinCodigo", "CUPS"));
						//yaErrorCups=true;
						valores.put("nombreCups_" + i, "");
					}
				}
					
				parametro=(String)valores.get("descripcionISS_" + i);
				if (parametro!=null&&!parametro.equals(""))
				{
					parametro=(String)valores.get("nombreISS_" + i);
					if (parametro==null||parametro.equals(""))
					{
						//errors.add("descripcionSinCodigoISS", new ActionMessage("errors.descripcionSinCodigo", "ISS"));
						//yaErrorISS=true;
						valores.put("nombreISS_" + i, "");
					}
				}
					

				//Debe existir al menos uno de los parametros
					
				parametro=(String)valores.get("nombreCups_" + i);
				if (parametro==null||parametro.equals(""))
				{
					//El cups esta vacío
					parametro=(String)valores.get("nombreISS_" + i);
					if (parametro==null||parametro.equals(""))
					{
						//El cups y el ISS estan vacios
						parametro=(String)valores.get("nombreSoat_" + i);
						if (parametro==null||parametro.equals(""))
						{
							//Si se presenta este caso el soat lo vamos a
							//guardar vacío, si se quiere validar que se ponga
							//al menos un código se descomenta la linea 
							//de abajo (2 líneas abajo)
							valores.put("nombreCups_" + i, "");
							//errors.add("error.servicios.ningunCodigo", new ActionMessage("error.servicios.ningunCodigo"));
						}
					}
					
				}
				else
				{
					//En este caso el cups no es vacío, revisamos que tenga
					//longitud de 12
					if (parametro.length()>12)
					{
						errors.add("tamanioCodigoCupsInvalido", new ActionMessage("errors.maxlength", "El código CUPS de Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "12"));
					}
				}
					
				parametro=(String)valores.get("nombreISS_" + i);
				if (parametro!=null&&!parametro.equals(""))
				{
					if (parametro.length()>12)
					{
						errors.add("tamanioCodigoISSInvalido", new ActionMessage("errors.maxlength", "El código ISS del ServicIo No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "12"));
					}
				}					
				
				parametro=(String)valores.get("unidadesUvr_" + i);
				if (parametro==null||parametro.equals(""))
				{
					valores.put("unidadesUvr_" + i, "-1");
				}
				else
				{
					try
					{
						if (Double.parseDouble(parametro)<0)
						{
							errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades UVR del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "0"));
						}
					}
					catch (Exception e)
					{
						errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades UVR  del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "0"));
					}
				}
					
				parametro=(String)valores.get("nombreSoat_" + i);
				if (parametro!=null&&!parametro.equals(""))
				{
					if (parametro.length()>12)
					{
						errors.add("tamanioCodigoSOATInvalido", new ActionMessage("errors.maxlength", "El código SOAT del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "12"));
					}
				}
				
				parametro=valores.get("unidadesSoat_" + i)+"";
				if (parametro==null||parametro.equals(""))
				{
					valores.put("unidadesSoat_" + i, "");
				}
				else
				{
					try
					{
						if (Double.parseDouble(parametro)<0)
						{
							errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades SOAT del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "0"));
						}
					}
					catch (NumberFormatException e1)
					{
						errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades SOAT  del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "0"));
					}
				}
				
				
				String tipoServicio = this.getValores("tipoServicio_"+i).toString();
				
				//Se valida el campo requiere diagnostico segun el tipo de servicio
				if(tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")||tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")||tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+""))
				{
					if(this.requiereDiagnostico.equals(""))
						errors.add("requiereDiagnosticoRequerido", new ActionMessage("errors.required", "El campo requiere diagnóstico"));
				}
				else
					this.requiereDiagnostico = "";
					
				/*parametro=(String)valores.get("nombreSoat_" + i);
				String parametro2=(String)valores.get("descripcionSoat_" + i);
				String parametro3=(String)valores.get("unidadesSoat_" + i);
				if (   (parametro!=null&&!parametro.equals("")) || (parametro2!=null&&!parametro2.equals("")) || (parametro3!=null&&!parametro3.equals("")) )
				{
					if (parametro.length()>8)
					{
						errors.add("tamanioCodigoSOATInvalido", new ActionMessage("errors.maxlength", "El código SOAT del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "8"));
					}
					parametro=(String)valores.get("unidadesSoat_" + i);
					if (parametro==null||parametro.equals(""))
					{
						valores.put("unidadesSoat_" + i, "-1");
					}
					else
					{
						try
						{
							if (Double.parseDouble(parametro)<0)
							{
								errors.add("errors.floatMayorOIgualQue", new ActionMessage("errors.floatMayorOIgualQue", "Las unidades SOAT del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i), "0"));
							}
						}
						catch (Exception e)
						{
							errors.add("error.servicios.unidadesSoatNoFloat", new ActionMessage("error.servicios.unidadesSoatNoFloat" +" del Servicio No. "+valores.get("especialidad_"+i)+"-"+valores.get("codigo_"+i)));
						}
					}
				}*/					
				//Se agregó el siguiente if para saber si se debe o no realizar la validacion 
				//de minutos  mayor a 0 que aplica solo para atención odontológica.
				if (this.atencionOdontologica.equals(ConstantesBD.acronimoSi)){
					if (Utilidades.convertirAEntero(this.valores.get("minutosduracion_"+i)+"")<=0)
						{
							errors.add("errors.MayorQue", new ActionMessage("errors.MayorQue", "Los minutos de duración "," 0 "));
						}
				}
			}
			
		}
	//	System.out.print("\n naturaleza del servicio --> "+this.naturalezaServicio);
		return errors;
	}	

	/**
	 * @return
	 */
	public String getAccionAFinalizar()
	{
		return accionAFinalizar;
	}

	/**
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param string
	 */
	public void setAccionAFinalizar(String string)
	{
		accionAFinalizar = string;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string)
	{
		estado = string;
	}

	public String getUnidadesUvr() {
		return unidadesUvr;
	}
	public void setUnidadesUvr(String unidadesUvr) {
		this.unidadesUvr = unidadesUvr;
	}
	/**
	 * @return
	 */
	public String getCodigoCups()
	{
		return codigoCups;
	}

	/**
	 * @return
	 */
	public String getCodigoISS()
	{
		return codigoISS;
	}

	/**
	 * @return
	 */
	public String getCodigoSoat()
	{
		return codigoSoat;
	}

	/**
	 * @return
	 */
	public String getDescripcionCups()
	{
		return descripcionCups;
	}

	/**
	 * @return
	 */
	public String getDescripcionISS()
	{
		return descripcionISS;
	}

	/**
	 * @return
	 */
	public String getDescripcionSoat()
	{
		return descripcionSoat;
	}

	/**
	 * @return
	 */
	public int getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * @return
	 */
	public boolean isEsPos()
	{
		return esPos;
	}

	

	

	/**
	 * @return
	 */
	public String getNaturalezaServicio()
	{
		return naturalezaServicio;
	}

	/**
	 * @return
	 */
	public int getSexo()
	{
		return sexo;
	}

	/**
	 * @return
	 */
	public String getTipoServicio()
	{
		return tipoServicio;
	}

	/**
	 * @param string
	 */
	public void setCodigoCups(String string)
	{
		codigoCups = string;
	}

	/**
	 * @param string
	 */
	public void setCodigoISS(String string)
	{
		codigoISS = string;
	}

	/**
	 * @param string
	 */
	public void setCodigoSoat(String string)
	{
		codigoSoat = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionCups(String string)
	{
		descripcionCups = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionISS(String string)
	{
		descripcionISS = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionSoat(String string)
	{
		descripcionSoat = string;
	}

	/**
	 * @param i
	 */
	public void setEspecialidad(int i)
	{
		especialidad = i;
	}

	/**
	 * @param b
	 */
	public void setEsPos(boolean b)
	{
		esPos = b;
	}


	

	/**
	 * @param string
	 */
	public void setNaturalezaServicio(String string)
	{
		naturalezaServicio = string;
	}

	/**
	 * @param i
	 */
	public void setSexo(int i)
	{
		sexo = i;
	}

	/**
	 * @param string
	 */
	public void setTipoServicio(String string)
	{
		tipoServicio = string;
	}

	/**
	 * @return
	 */
	public int getNumeroServicios()
	{
		return numeroServicios;
	}

	/**
	 * @param i
	 */
	public void setNumeroServicios(int i)
	{
		numeroServicios = i;
	}

	/**
	 * Método que guarda un valor en el mapa
	 * 
	 * @param key llave con la que quedará el
	 * valor a guardar
	 * @param value valor a guardar en el mapa
	 */
	public void setValores(String key, Object value) 
	{
		valores.put(key, value);
	}

	/**
	 * Método que recupera un valor del mapa
	 * 
	 * @param key llave con la que esta el valor
	 * buscado en el mapa
	 * @return
	 */
	public Object getValores(String key) 
	{
		return valores.get(key);
	}
	/**
	 * @return
	 */
	public int getIndiceNavegacion()
	{
		return indiceNavegacion;
	}

	/**
	 * @return
	 */
	public int getNumeroElementosMostrar()
	{
		return numeroElementosMostrar;
	}

	/**
	 * @param i
	 */
	public void setIndiceNavegacion(int i)
	{
		indiceNavegacion = i;
	}

	/**
	 * @param i
	 */
	public void setNumeroElementosMostrar(int i)
	{
		numeroElementosMostrar = i;
	}

	/**
	 * @return
	 */
	public int getSiguienteIndiceNavegacion()
	{
		return siguienteIndiceNavegacion;
	}

	/**
	 * @param i
	 */
	public void setSiguienteIndiceNavegacion(int i)
	{
		siguienteIndiceNavegacion = i;
	}

	/**
	 * @return
	 */
	public String getUnidadesSoat()
	{
		return unidadesSoat;
	}

	/**
	 * @param string
	 */
	public void setUnidadesSoat(String string)
	{
		unidadesSoat = string;
	}

	/**
	 * @return 
	 * Retorna logInfoOriginalServicio.
	 */
	public String getLogInfoOriginalServicio ()
	{
		return logInfoOriginalServicio;
	}
	/**
	 * logInfoOriginalServicio
	 * @param logInfoOriginalServicio.
	 */
	public void setLogInfoOriginalServicio (String logInfoOriginalServicio)
	{
		this.logInfoOriginalServicio = logInfoOriginalServicio;
	}
	
	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}
	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
	/**
	 * @return Returns the grupoServicio.
	 */
	public int getGrupoServicio() {
		return grupoServicio;
	}
	/**
	 * @param grupoServicio The grupoServicio to set.
	 */
	public void setGrupoServicio(int grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	/**
	 * @return Returns the nivel.
	 */
	public String getNivel() {
		return nivel;
	}

	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(String nivel) {
		this.nivel = nivel;
	}
	
		
	/**
	 * 
	 * @return
	 */
	public String getRealizaInstitucion() {
		return realizaInstitucion;
	}
	
	/**
	 * 
	 * @param realizaInstitucion
	 */
	public void setRealizaInstitucion(String realizaInstitucion) {
		this.realizaInstitucion = realizaInstitucion;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRequiereInterpretacion() {
		return requiereInterpretacion;
	}
	
	/**
	 * 
	 * @param requiereInterpretacion
	 */
	public void setRequiereInterpretacion(String requiereInterpretacion) {
		this.requiereInterpretacion = requiereInterpretacion;
	}

	public HashMap getTarifariosMap() {
		return tarifariosMap;
	}

	public void setTarifariosMap(HashMap tarifariosMap) {
		this.tarifariosMap = tarifariosMap;
	}
	public Object getTarifariosMap(String key) 
	{
		return tarifariosMap.get(key);
	}

	public void setTarifariosMap(String key,Object value)
	{
		this.tarifariosMap.put(key, value);
	}

	public String getCosto() 
	{
		if(UtilidadTexto.isEmpty(costo))
			return "0";
		return UtilidadTexto.formatearValores(costo,"#.#####");
	}

	public void setCosto(String costo) {
		this.costo = costo;
	}

	public HashMap getValores() {
		return valores;
	}

	public void setValores(HashMap valores) {
		this.valores = valores;
	}

	/**
	 * @return the requiereDiagnostico
	 */
	public String getRequiereDiagnostico() {
		return requiereDiagnostico;
	}

	/**
	 * @param requiereDiagnostico the requiereDiagnostico to set
	 */
	public void setRequiereDiagnostico(String requiereDiagnostico) {
		this.requiereDiagnostico = requiereDiagnostico;
	}

	/**
	 * @return
	 */
	public String getDesServicioPortatil() {
		return desServicioPortatil;
	}

	/**
	 * @param desServicioPortatil
	 */
	public void setDesServicioPortatil(String desServicioPortatil) {
		this.desServicioPortatil = desServicioPortatil;
	}

	/**
	 * @return
	 */
	public int getCodigoServicioPortatil() {
		return codigoServicioPortatil;
	}

	/**
	 * @param codigoServicioPortatil
	 */
	public void setCodigoServicioPortatil(int codigoServicioPortatil) {
		this.codigoServicioPortatil = codigoServicioPortatil;
	}

	public String getPosSubsidiado() {
		return posSubsidiado;
	}

	public void setPosSubsidiado(String posSubsidiado) {
		this.posSubsidiado = posSubsidiado;
	}

	/**
	 * @return the formulario
	 */
	public HashMap<String, Object> getFormulario() {
		return formulario;
	}

	/**
	 * @param formulario the formulario to set
	 */
	public void setFormulario(HashMap<String, Object> formulario) {
		this.formulario = formulario;
	}
	
	/**
	 * @return the formulario
	 */
	public Object getFormulario(String key) {
		return formulario.get(key);
	}

	/**
	 * @param formulario the formulario to set
	 */
	public void setFormulario(String key, Object obj) {
		this.formulario.put(key, obj);
	}
	
	/**
	 * Método para obtener el número de formularios
	 * @return
	 */
	public int getNumFormularios()
	{
		return Utilidades.convertirAEntero(this.formulario.get("numRegistros")+"", true);
	}
	
	/**
	 * Método para asignar el número de formularios
	 * @param numFormularios
	 */
	public void setNumFormularios(int numFormularios)
	{
		this.formulario.put("numRegistros", numFormularios);
	}

	/**
	 * @return the arregloFormularios
	 */
	public ArrayList<DtoPlantillaServDiag> getArregloFormularios() {
		return arregloFormularios;
	}

	/**
	 * @param arregloFormularios the arregloFormularios to set
	 */
	public void setArregloFormularios(
			ArrayList<DtoPlantillaServDiag> arregloFormularios) {
		this.arregloFormularios = arregloFormularios;
	}

	/**
	 * @return the codigoFormulario
	 */
	public int getCodigoFormulario() {
		return codigoFormulario;
	}

	/**
	 * @param codigoFormulario the codigoFormulario to set
	 */
	public void setCodigoFormulario(int codigoFormulario) {
		this.codigoFormulario = codigoFormulario;
	}

	public String getAtencionOdontologica() {
		return atencionOdontologica;
	}

	public void setAtencionOdontologica(String atencionOdontologica) {
		this.atencionOdontologica = atencionOdontologica;
	}	
	
	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	public String getMinutosDuracion() {
		return minutosDuracion;
	}

	public void setMinutosDuracion(String minutosDuracion) {
		this.minutosDuracion = minutosDuracion;
	}

	public String getArchivoImagen() {
		return archivoImagen;
	}

	public void setArchivoImagen(String archivoImagen) {
		this.archivoImagen = archivoImagen;
	}
	
	
}