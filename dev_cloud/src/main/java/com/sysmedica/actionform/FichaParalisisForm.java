package com.sysmedica.actionform;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.ValoresPorDefecto;

public class FichaParalisisForm extends ValidatorForm {

	private transient Logger logger=Logger.getLogger(FichaParalisisForm.class);
	
	private boolean activa;
	private String urlFichasAnteriores;
	
	private int codigoInstitucion;
	
	private boolean hayServicios;
    private boolean hayLaboratorios;
    
	private boolean notificar;
    private String loginUsuario;
    private int codigoFichaParalisis;
    private int codigoPaciente;
    private String codigoDiagnostico;
    private int codigoNotificacion;
    private int numeroSolicitud;
    private int estadoFicha;
    
    private int codigoConvenio;
    
    private String valorDivDx;
    
    private int codigoEnfNotificable;
    
    private boolean fichamodulo;
    private String esPrimeraVez; 
    private boolean vieneDeFichasAnteriores;
    
    //*************************************
    // DATOS CARA A :
        
    // Informacion general
    private String sire;
    private String municipioNotifica;
    private String departamentoNotifica;
    private String lugarNotifica;
    private int institucionAtendio;
    private String fechaNotificacion;
    private int semanaEpidemiologica;
    private int anyoSemanaEpi;
    
    // Identificacion del paciente
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String departamentoNacimiento;
    private String ciudadNacimiento;
    private String departamentoResidencia;
    private String ciudadResidencia;
    private String direccionPaciente;
    private String telefonoPaciente;
    private String fechaNacimiento;
    private String edad;
    private String genero;
    private String estadoCivil;
    private String documento;
    private String barrioResidencia;
    private String zonaDomicilio;
    private String ocupacion;
    private String aseguradora;
    private String regimenSalud;
    private String etnia;
    private int desplazado;
    private String tipoId;
    private String paisExpedicion;
    private String paisNacimiento;
    private String paisResidencia;
        
    
    // Notificacion
    private String fechaConsultaGeneral;
    private String codigoMunProcedencia;
    private String codigoDepProcedencia;
    private String lugarProcedencia;
    private String fechaInicioSint;
    private int tipoCaso;
    private boolean hospitalizado;
    private String fechaHospitalizacion;
    private boolean estaVivo;
    private String fechaDefuncion;
    private String nombreProfesional;
    
    //**************************************
    // DATOS CARA B :    
    private String nombreMadre;
    private String nombrePadre;
    private String fechaInicioInvestigacion;
    private String numeroDosis;
    private String fechaUltimaDosis;
    private int tieneCarnet;
    private int fiebre;
    private int respiratorios;
    private int digestivos;
    private String instalacion;
    private int dolorMuscular;
    private int signosMeningeos1;
    private int fiebreInicioParalisis;
    private int progresion;
    private String fechaInicioParalisis;
    private int musculosRespiratorios;
    private int signosMeningeos2;
    private int babinsky;
    private int brudzinsky;
    private int paresCraneanos;
    private int liquidocefalo;
    private String fechaTomaLiquido;
    private int celulas;
    private int globulosRojos;
    private int leucocitos;
    private int linfocitos;
    private int proteinas;
    private int glucosa;
    private int electromiografia;
    private String fechaTomaElectro;
    private int velocidadConduccion;
    private int resultadoConduccion;
    private String fechaTomaVelocidad;
    private String impresionDiagnostica;
    private int muestraMateriaFecal;
    private String fechaTomaFecal;
    private String fechaEnvioFecal;
    private String fechaRecepcionFecal;
    private String fechaResultadoFecal;
    private int virusAislado;
    private String fechaVacunacionBloqueo;
    private String fechaCulminacionVacunacion;
    private String municipiosVacunados;
    private int codigoExtremidad1;
    private int codigoExtremidad2;
    private int codigoExtremidad3;
    private int codigoExtremidad4;
    private int codigoGrupoEdad1;
    private int codigoGrupoEdad2;
    private int codigoGrupoEdad3;
    
    private int sumaGrupo1;
    private int sumaGrupo2;
    private int sumaGrupo3;
    
    private int sumaPoblacion;
    private int sumaRecien;
    private int sumaVop1;
    private int sumaVop2;
    private int sumaVop3;
    private int sumaAdicional;
    private int sumaTotal;
    
    private String telefonoContacto;
    
    private HashMap datosExtremidades;
    private HashMap datosGrupoEdad;
    
    private String pais;
    private int areaProcedencia;
    private String grupoPoblacional;
    
    //  Variables para las valoraciones
    private String valorDiagnostico;
    private String idDiagnostico;
    private String propiedadDiagnostico;
    private String idDiv;
    private String idCheckBox;
    private String idHiddenCheckBox;
    private String propiedadHiddenCheckBox;
    private int tipoDiagnostico;
    private int numero;
    private String idNumero;
    private String diagnosticosSeleccionados;
    private String idDiagSeleccionados;
    private String idValorFicha;
    private boolean epidemiologia;
    
    public void reset() {
    	
    	sire = "";
        nombreMadre = "";
        nombrePadre = "";
        fechaInicioInvestigacion = "";
        numeroDosis = "";
        fechaUltimaDosis = "";
        tieneCarnet = 0;
        fiebre = 0;
        respiratorios = 0;
        digestivos = 0;
        instalacion = "";
        dolorMuscular = 0;
        signosMeningeos1 = 0;
        fiebreInicioParalisis = 0;
        progresion = 0;
        fechaInicioParalisis = "";
        musculosRespiratorios = 0;
        signosMeningeos2 = 0;
        babinsky = 0;
        brudzinsky = 0;
        paresCraneanos = 0;
        liquidocefalo = 0;
        fechaTomaLiquido= "";
        celulas = 0;
        globulosRojos = 0;
        leucocitos = 0;
        proteinas = 0;
        glucosa = 0;
        electromiografia = 0;
        fechaTomaElectro = "";
        velocidadConduccion = 0;
        resultadoConduccion = 0;
        fechaTomaVelocidad = "";
        impresionDiagnostica = "";
        muestraMateriaFecal = 0;
        fechaTomaFecal = "";
        fechaEnvioFecal = "";
        fechaRecepcionFecal = "";
        fechaResultadoFecal = "";
        virusAislado = 0;
        fechaVacunacionBloqueo = "";
        fechaCulminacionVacunacion = "";
        municipiosVacunados = "";
        codigoExtremidad1 = 0;
        codigoExtremidad2 = 0;
        codigoExtremidad3 = 0;
        codigoExtremidad4 = 0;
        codigoGrupoEdad1 = 0;
        codigoGrupoEdad2 = 0;
        codigoGrupoEdad3 = 0;
        
        datosExtremidades = new HashMap();
        datosGrupoEdad = new HashMap();
        
        codigoMunProcedencia = "";
        codigoDepProcedencia = "";
        
        estaVivo = false;
        
        String ciudad = ValoresPorDefecto.getCiudadNacimiento(codigoInstitucion);
        String lugar = ciudad.split("-")[1]+"-"+ciudad.split("-")[0];
        
        lugarNotifica = lugar;
        lugarProcedencia = lugar;
        
        fechaInicioSint = "";
        tipoCaso = 0;
        
        fechaConsultaGeneral = "";
        
        fechaInicioSint = "";
        tipoCaso = 0;
        hospitalizado = false;
        fechaHospitalizacion = "";
        estaVivo = false;
        fechaDefuncion = "";
        nombreProfesional = "";
        
        sumaGrupo1 = 0;
        sumaGrupo2 = 0;
        sumaGrupo3 = 0;
        
        sumaPoblacion = 0;
        sumaRecien = 0;
        sumaVop1 = 0;
        sumaVop2 = 0;
        sumaVop3 = 0;
        sumaAdicional = 0;
        sumaTotal = 0;
        
        telefonoContacto = "";
        
        pais = "COLOMBIA";
        areaProcedencia = 0;
        grupoPoblacional = "";
    }
    
    
    /**
     * Metodo para validar los atributos provenientes del formulario
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        
        ActionErrors errores = new ActionErrors();
        
        if (estado.equals("empezar")) {
            
			reset();
		}
        
        if (estado.equals("actualizar")) {
        	
        	String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
        	String fechaInicioInvestigacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioInvestigacion);
        	String fechaInicioParalisisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioParalisis);
        	String fechaTomaElectroTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaElectro);
        	String fechaTomaVelocidadTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaVelocidad);
        	String fechaTomaFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaFecal);
        	String fechaEnvioFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioFecal);
        	String fechaRecepcionFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaRecepcionFecal);
        	String fechaResultadoFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaResultadoFecal);
        	String fechaVacunacionBloqueoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunacionBloqueo);
        	String fechaCulminacionVacunacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaCulminacionVacunacion);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
            /*
            if (fechaInicioSint.trim().length()>0) {
            	
            	if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            */
        	
        	if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }

        	try {
	            if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	        	
	        	if (fechaUltimaDosis.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
	        			
	        			errores.add("Campo Fecha de Ultima Dosis de VOP no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de VOP"));
	        		}
	        		else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de VOP no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de VOP", "actual"));
					}
	        	}
	        	
	        	if (fechaInicioInvestigacion.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaInicioInvestigacion)) {
	        			
	        			errores.add("Campo Fecha de Inicio de Investigacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Investigacion"));
	        		}
	        		else if (fechaInicioInvestigacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Investigacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Investigacion", "actual"));
					}
	        	}
	        	
	        	try {
	        		int numdosis = Integer.parseInt(numeroDosis);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("Campo No. Dosis Recibidas (VOP) no válido",new ActionMessage("error.epidemiologia.numerodosis"));
	        	}
	        	
	        	try {
	        		int dias = Integer.parseInt(instalacion);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("Campo Días de Instalación no válido",new ActionMessage("error.epidemiologia.diasinstalacion"));
	        	}
	        	
	        	if (fechaInicioParalisis.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaInicioParalisis)) {
	        			
	        			errores.add("Campo Fecha de Inicio de Paralisis no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Paralisis"));
	        		}
	        		else if (fechaInicioParalisisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Paralisis no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Paralisis", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaElectro.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaElectro)) {
	        			
	        			errores.add("Campo Fecha de Toma de Electromiografia no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Electromiografia"));
	        		}
	        		else if (fechaTomaElectroTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Electromiografia no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Electromiografia", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaVelocidad.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaVelocidad)) {
	        			
	        			errores.add("Campo Fecha de Toma de Velocidad no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Velocidad"));
	        		}
	        		else if (fechaTomaVelocidadTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Velocidad no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Velocidad", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaFecal)) {
	        			
	        			errores.add("Campo Fecha de Toma de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Muestra Fecal"));
	        		}
	        		else if (fechaTomaFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaEnvioFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaEnvioFecal)) {
	        			
	        			errores.add("Campo Fecha de Envio de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Envio de Muestra Fecal"));
	        		}
	        		else if (fechaEnvioFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Envio de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envio de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaRecepcionFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaRecepcionFecal)) {
	        			
	        			errores.add("Campo Fecha de Recepcion de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion de Muestra Fecal"));
	        		}
	        		else if (fechaRecepcionFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Recepcion de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaResultadoFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaResultadoFecal)) {
	        			
	        			errores.add("Campo Fecha de Resultado de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado de Muestra Fecal"));
	        		}
	        		else if (fechaResultadoFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Resultado de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Resultado de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	
	        	for (int i=0;i<3;i++) {
        			
        			int j = i+1;
        			
        			try {
        				String pobmeta = datosGrupoEdad.get("poblacionmeta_"+Integer.toString(j)).toString();
        				
        				if (pobmeta.trim().length()==0) {
        					
        					pobmeta = "0";
        				}
        				
        				int poblacionmeta = Integer.parseInt(pobmeta);
        			}
        			catch (NumberFormatException nfe) {
        				errores.add("Campo población meta no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Población Meta "+Integer.toString(j)));
        			}
        			
        			if (i==0) {
	        			try {
	        				String recnac = datosGrupoEdad.get("reciennacido_"+Integer.toString(j)).toString();
	        				
	        				if (recnac.trim().length()==0) {
	        					recnac = "0";
	        				}
	        				int reciennacido = Integer.parseInt(recnac);
	        			}
	        			catch (NumberFormatException nfe) {
	        				errores.add("Campo recien nacido no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Recien Nacido "));
	        			}
        			}
        			
        			try {
        				String vp1 = datosGrupoEdad.get("vop1_"+Integer.toString(j)).toString();
        				
        				if (vp1.trim().length()==0) {
        					vp1 = "0";
        				}
        				
        				int vop1 = Integer.parseInt(vp1);
        			}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop1 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP1 "+Integer.toString(j)));
        			}
    				
    				try {
    					String vp2 = datosGrupoEdad.get("vop2_"+Integer.toString(j)).toString();
    					
    					if (vp2.trim().length()==0) {
    						vp2 = "0";
    					}
    					
    					int vop2 = Integer.parseInt(vp2);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop2 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP2 "+Integer.toString(j)));
        			}
    				
    				try {
    					String vp3 = datosGrupoEdad.get("vop3_"+Integer.toString(j)).toString();
    					
    					if (vp3.trim().length()==0) {
    						vp3 = "0";
    					}
    					
    					int vop3 = Integer.parseInt(vp3);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop3 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP3 "+Integer.toString(j)));
        			}
    				
    				try {
    					String adic = datosGrupoEdad.get("adicional_"+Integer.toString(j)).toString();
    					
    					if (adic.trim().length()==0) {
    						adic="0";
    					}
    					
    					int adicional = Integer.parseInt(adic);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo adicional (Vacunación de Bloqueo) no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Adicional "+Integer.toString(j)));
        			}
        		}
        	
	        	
	        	
	        	if (fechaVacunacionBloqueo.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaVacunacionBloqueo)) {
	        			
	        			errores.add("Campo Fecha de Vacunacion de Bloqueo no valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion de Bloqueo"));
	        		}
	        	}
	        	
	        	if (fechaCulminacionVacunacion.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaCulminacionVacunacion)) {
	        			
	        			errores.add("Campo Fecha de Culminacion de Vacunacion de Bloqueo no valido", new ActionMessage("errors.formatoFechaInvalido","de Culminacion de Vacunacion de Bloqueo"));
	        		}
	        		else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaVacunacionBloqueo,fechaCulminacionVacunacion)) {
						
						errores.add("Campo Fecha de Culminacion de Vacunacion de Bloqueo no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion de Bloqueo", "de Culminacion de Vacunacion"));
					}
	        	}
        	}
        	catch (NullPointerException npe) {}
        	
        }
        
        if (estado.equals("validar")) {
        	
        	String fechaUltimaDosisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaUltimaDosis);
        	String fechaInicioInvestigacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioInvestigacion);
        	String fechaInicioParalisisTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioParalisis);
        	String fechaTomaElectroTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaElectro);
        	String fechaTomaVelocidadTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaVelocidad);
        	String fechaTomaFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaTomaFecal);
        	String fechaEnvioFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaEnvioFecal);
        	String fechaRecepcionFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaRecepcionFecal);
        	String fechaResultadoFecalTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaResultadoFecal);
        	String fechaVacunacionBloqueoTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaVacunacionBloqueo);
        	String fechaCulminacionVacunacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaCulminacionVacunacion);
            String fechaInicioSintomasTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaInicioSint);
            String fechaHospitalizacionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaHospitalizacion);
            String fechaDefuncionTransformada=UtilidadFecha.conversionFormatoFechaABD(fechaDefuncion);
        	
        	String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
        	
        	if (fechaInicioSint.trim().length()>0) {
	            if (!UtilidadFecha.validarFecha(fechaInicioSint)) {
	                
	                errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Sintomas"));
	            }
	            else if (fechaInicioSintomasTransformada.compareTo(fechaActual)>0) {
					
					errores.add("Campo Fecha de Inicio de Sintomas no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Sintomas", "actual"));
				}
            }
            else {
            	errores.add("El campo Fecha de Inicio de Sintomas es Requerido", new ActionMessage("errors.required","El campo fecha de inicio de Sintomas"));
            }
            
            try {
	            if (fechaHospitalizacion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaHospitalizacion)) {
		                
		                errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Hospitalizacion"));
		            }
		            else if (fechaHospitalizacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Hospitalizacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Hospitalizacion", "actual"));
					}
	            }
	            
	            if (fechaDefuncion.trim().length()>0) {
	            	
	            	if (!UtilidadFecha.validarFecha(fechaDefuncion)) {
		                
		                errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.formatoFechaInvalido","de Defuncion"));
		            }
		            else if (fechaDefuncionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Defuncion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Defuncion", "actual"));
					}
	            }
	        	
	        	if (fechaUltimaDosis.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaUltimaDosis)) {
	        			
	        			errores.add("Campo Fecha de Ultima Dosis de VOP no valido", new ActionMessage("errors.formatoFechaInvalido","de Ultima Dosis de VOP"));
	        		}
	        		else if (fechaUltimaDosisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Ultima Dosis de VOP no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Ultima Dosis de VOP", "actual"));
					}
	        	}
	        	
	        	if (fechaInicioInvestigacion.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaInicioInvestigacion)) {
	        			
	        			errores.add("Campo Fecha de Inicio de Investigacion no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Investigacion"));
	        		}
	        		else if (fechaInicioInvestigacionTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Investigacion no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Investigacion", "actual"));
					}
	        	}
	        	
	        	try {
	        		int numdosis = Integer.parseInt(numeroDosis);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("Campo No. Dosis Recibidas (VOP) no válido",new ActionMessage("error.epidemiologia.numerodosis"));
	        	}
	        	
	        	try {
	        		int dias = Integer.parseInt(instalacion);
	        	}
	        	catch (NumberFormatException nfe) {
	        		
	        		errores.add("Campo Días de Instalación no válido",new ActionMessage("error.epidemiologia.diasinstalacion"));
	        	}
	        	
	        	if (fechaInicioParalisis.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaInicioParalisis)) {
	        			
	        			errores.add("Campo Fecha de Inicio de Paralisis no valido", new ActionMessage("errors.formatoFechaInvalido","de Inicio de Paralisis"));
	        		}
	        		else if (fechaInicioParalisisTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Inicio de Paralisis no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Inicio de Paralisis", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaElectro.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaElectro)) {
	        			
	        			errores.add("Campo Fecha de Toma de Electromiografia no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Electromiografia"));
	        		}
	        		else if (fechaTomaElectroTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Electromiografia no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Electromiografia", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaVelocidad.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaVelocidad)) {
	        			
	        			errores.add("Campo Fecha de Toma de Velocidad no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Velocidad"));
	        		}
	        		else if (fechaTomaVelocidadTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Velocidad no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Velocidad", "actual"));
					}
	        	}
	        	
	        	if (fechaTomaFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaTomaFecal)) {
	        			
	        			errores.add("Campo Fecha de Toma de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Toma de Muestra Fecal"));
	        		}
	        		else if (fechaTomaFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Toma de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Toma de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaEnvioFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaEnvioFecal)) {
	        			
	        			errores.add("Campo Fecha de Envio de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Envio de Muestra Fecal"));
	        		}
	        		else if (fechaEnvioFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Envio de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Envio de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaRecepcionFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaRecepcionFecal)) {
	        			
	        			errores.add("Campo Fecha de Recepcion de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Recepcion de Muestra Fecal"));
	        		}
	        		else if (fechaRecepcionFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Recepcion de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Recepcion de Muestra Fecal", "actual"));
					}
	        	}
	        	
	        	if (fechaResultadoFecal.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaResultadoFecal)) {
	        			
	        			errores.add("Campo Fecha de Resultado de Muestra Fecal no valido", new ActionMessage("errors.formatoFechaInvalido","de Resultado de Muestra Fecal"));
	        		}
	        		else if (fechaResultadoFecalTransformada.compareTo(fechaActual)>0) {
						
						errores.add("Campo Fecha de Resultado de Muestra Fecal no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Resultado de Muestra Fecal", "actual"));
					}
	        	}
	        	
        		for (int i=0;i<3;i++) {
        			
        			int j = i+1;
        			
        			try {
        				String pobmeta = datosGrupoEdad.get("poblacionmeta_"+Integer.toString(j)).toString();
        				
        				if (pobmeta.trim().length()==0) {
        					
        					pobmeta = "0";
        				}
        				
        				int poblacionmeta = Integer.parseInt(pobmeta);
        			}
        			catch (NumberFormatException nfe) {
        				errores.add("Campo población meta no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Población Meta "+Integer.toString(j)));
        			}
        			
        			if (i==0) {
	        			try {
	        				String recnac = datosGrupoEdad.get("reciennacido_"+Integer.toString(j)).toString();
	        				
	        				if (recnac.trim().length()==0) {
	        					recnac = "0";
	        				}
	        				int reciennacido = Integer.parseInt(recnac);
	        			}
	        			catch (NumberFormatException nfe) {
	        				errores.add("Campo recien nacido no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Recien Nacido "));
	        			}
        			}
        			
        			try {
        				String vp1 = datosGrupoEdad.get("vop1_"+Integer.toString(j)).toString();
        				
        				if (vp1.trim().length()==0) {
        					vp1 = "0";
        				}
        				
        				int vop1 = Integer.parseInt(vp1);
        			}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop1 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP1 "+Integer.toString(j)));
        			}
    				
    				try {
    					String vp2 = datosGrupoEdad.get("vop2_"+Integer.toString(j)).toString();
    					
    					if (vp2.trim().length()==0) {
    						vp2 = "0";
    					}
    					
    					int vop2 = Integer.parseInt(vp2);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop2 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP2 "+Integer.toString(j)));
        			}
    				
    				try {
    					String vp3 = datosGrupoEdad.get("vop3_"+Integer.toString(j)).toString();
    					
    					if (vp3.trim().length()==0) {
    						vp3 = "0";
    					}
    					
    					int vop3 = Integer.parseInt(vp3);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo vop3 no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","VOP3 "+Integer.toString(j)));
        			}
    				
    				try {
    					String adic = datosGrupoEdad.get("adicional_"+Integer.toString(j)).toString();
    					
    					if (adic.trim().length()==0) {
    						adic="0";
    					}
    					
    					int adicional = Integer.parseInt(adic);
    				}
    				catch (NumberFormatException nfe) {
        				errores.add("Campo adicional (Vacunación de Bloqueo) no valido", new ActionMessage("error.epidemiologia.valornumericogeneral","Adicional "+Integer.toString(j)));
        			}
        		}
	        	
	        	if (fechaVacunacionBloqueo.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaVacunacionBloqueo)) {
	        			
	        			errores.add("Campo Fecha de Vacunacion de Bloqueo no valido", new ActionMessage("errors.formatoFechaInvalido","de Vacunacion de Bloqueo"));
	        		}
	        	}
	        	
	        	if (fechaCulminacionVacunacion.trim().length()>0) {
	        		
	        		if (!UtilidadFecha.validarFecha(fechaCulminacionVacunacion)) {
	        			
	        			errores.add("Campo Fecha de Culminacion de Vacunacion de Bloqueo no valido", new ActionMessage("errors.formatoFechaInvalido","de Culminacion de Vacunacion de Bloqueo"));
	        		}
	        		else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaVacunacionBloqueo,fechaCulminacionVacunacion)) {
						
						errores.add("Campo Fecha de Culminacion de Vacunacion de Bloqueo no valido", new ActionMessage("errors.fechaPosteriorIgualActual","de Vacunacion de Bloqueo", "de Culminacion de Vacunacion"));
					}
	        	}
            }
            catch (NullPointerException npe) {}
        }
        
        return errores;
    }
    
    private String estado;
    
	public int getBabinsky() {
		return babinsky;
	}
	public void setBabinsky(int babinsky) {
		this.babinsky = babinsky;
	}
	public int getBrudzinsky() {
		return brudzinsky;
	}
	public void setBrudzinsky(int brudzinsky) {
		this.brudzinsky = brudzinsky;
	}
	public int getCelulas() {
		return celulas;
	}
	public void setCelulas(int celulas) {
		this.celulas = celulas;
	}
	public String getCiudadNacimiento() {
		return ciudadNacimiento;
	}
	public void setCiudadNacimiento(String ciudadNacimiento) {
		this.ciudadNacimiento = ciudadNacimiento;
	}
	public String getCiudadResidencia() {
		return ciudadResidencia;
	}
	public void setCiudadResidencia(String ciudadResidencia) {
		this.ciudadResidencia = ciudadResidencia;
	}
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}
	public int getCodigoExtremidad1() {
		return codigoExtremidad1;
	}
	public void setCodigoExtremidad1(int codigoExtremidad1) {
		this.codigoExtremidad1 = codigoExtremidad1;
	}
	public int getCodigoExtremidad2() {
		return codigoExtremidad2;
	}
	public void setCodigoExtremidad2(int codigoExtremidad2) {
		this.codigoExtremidad2 = codigoExtremidad2;
	}
	public int getCodigoExtremidad3() {
		return codigoExtremidad3;
	}
	public void setCodigoExtremidad3(int codigoExtremidad3) {
		this.codigoExtremidad3 = codigoExtremidad3;
	}
	public int getCodigoExtremidad4() {
		return codigoExtremidad4;
	}
	public void setCodigoExtremidad4(int codigoExtremidad4) {
		this.codigoExtremidad4 = codigoExtremidad4;
	}
	public int getCodigoFichaParalisis() {
		return codigoFichaParalisis;
	}
	public void setCodigoFichaParalisis(int codigoFichaParalisis) {
		this.codigoFichaParalisis = codigoFichaParalisis;
	}
	public int getCodigoGrupoEdad1() {
		return codigoGrupoEdad1;
	}
	public void setCodigoGrupoEdad1(int codigoGrupoEdad1) {
		this.codigoGrupoEdad1 = codigoGrupoEdad1;
	}
	public int getCodigoGrupoEdad2() {
		return codigoGrupoEdad2;
	}
	public void setCodigoGrupoEdad2(int codigoGrupoEdad2) {
		this.codigoGrupoEdad2 = codigoGrupoEdad2;
	}
	public int getCodigoGrupoEdad3() {
		return codigoGrupoEdad3;
	}
	public void setCodigoGrupoEdad3(int codigoGrupoEdad3) {
		this.codigoGrupoEdad3 = codigoGrupoEdad3;
	}
	public int getCodigoNotificacion() {
		return codigoNotificacion;
	}
	public void setCodigoNotificacion(int codigoNotificacion) {
		this.codigoNotificacion = codigoNotificacion;
	}
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	public HashMap getDatosExtremidades() {
		return datosExtremidades;
	}
	public void setDatosExtremidades(HashMap datosExtremidades) {
		this.datosExtremidades = datosExtremidades;
	}
	public HashMap getDatosGrupoEdad() {
		return datosGrupoEdad;
	}
	public void setDatosGrupoEdad(HashMap datosGrupoEdad) {
		this.datosGrupoEdad = datosGrupoEdad;
	}
	public String getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}
	public void setDepartamentoNacimiento(String departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}
	public String getDepartamentoResidencia() {
		return departamentoResidencia;
	}
	public void setDepartamentoResidencia(String departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}
	public int getDigestivos() {
		return digestivos;
	}
	public void setDigestivos(int digestivos) {
		this.digestivos = digestivos;
	}
	public String getDireccionPaciente() {
		return direccionPaciente;
	}
	public void setDireccionPaciente(String direccionPaciente) {
		this.direccionPaciente = direccionPaciente;
	}
	public String getDocumento() {
		return documento;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public int getDolorMuscular() {
		return dolorMuscular;
	}
	public void setDolorMuscular(int dolorMuscular) {
		this.dolorMuscular = dolorMuscular;
	}
	public String getEdad() {
		return edad;
	}
	public void setEdad(String edad) {
		this.edad = edad;
	}
	public int getElectromiografia() {
		return electromiografia;
	}
	public void setElectromiografia(int electromiografia) {
		this.electromiografia = electromiografia;
	}
	public String getEstadoCivil() {
		return estadoCivil;
	}
	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}
	public int getEstadoFicha() {
		return estadoFicha;
	}
	public void setEstadoFicha(int estadoFicha) {
		this.estadoFicha = estadoFicha;
	}
	public String getFechaCulminacionVacunacion() {
		return fechaCulminacionVacunacion;
	}
	public void setFechaCulminacionVacunacion(String fechaCulminacionVacunacion) {
		this.fechaCulminacionVacunacion = fechaCulminacionVacunacion;
	}
	public String getFechaEnvioFecal() {
		return fechaEnvioFecal;
	}
	public void setFechaEnvioFecal(String fechaEnvioFecal) {
		this.fechaEnvioFecal = fechaEnvioFecal;
	}
	public String getFechaInicioInvestigacion() {
		return fechaInicioInvestigacion;
	}
	public void setFechaInicioInvestigacion(String fechaInicioInvestigacion) {
		this.fechaInicioInvestigacion = fechaInicioInvestigacion;
	}
	public String getFechaInicioParalisis() {
		return fechaInicioParalisis;
	}
	public void setFechaInicioParalisis(String fechaInicioParalisis) {
		this.fechaInicioParalisis = fechaInicioParalisis;
	}
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	public String getFechaRecepcionFecal() {
		return fechaRecepcionFecal;
	}
	public void setFechaRecepcionFecal(String fechaRecepcionFecal) {
		this.fechaRecepcionFecal = fechaRecepcionFecal;
	}
	public String getFechaResultadoFecal() {
		return fechaResultadoFecal;
	}
	public void setFechaResultadoFecal(String fechaResultadoFecal) {
		this.fechaResultadoFecal = fechaResultadoFecal;
	}
	public String getFechaTomaElectro() {
		return fechaTomaElectro;
	}
	public void setFechaTomaElectro(String fechaTomaElectro) {
		this.fechaTomaElectro = fechaTomaElectro;
	}
	public String getFechaTomaFecal() {
		return fechaTomaFecal;
	}
	public void setFechaTomaFecal(String fechaTomaFecal) {
		this.fechaTomaFecal = fechaTomaFecal;
	}
	public String getFechaTomaLiquido() {
		return fechaTomaLiquido;
	}
	public void setFechaTomaLiquido(String fechaTomaLiquido) {
		this.fechaTomaLiquido = fechaTomaLiquido;
	}
	public String getFechaTomaVelocidad() {
		return fechaTomaVelocidad;
	}
	public void setFechaTomaVelocidad(String fechaTomaVelocidad) {
		this.fechaTomaVelocidad = fechaTomaVelocidad;
	}
	public String getFechaUltimaDosis() {
		return fechaUltimaDosis;
	}
	public void setFechaUltimaDosis(String fechaUltimaDosis) {
		this.fechaUltimaDosis = fechaUltimaDosis;
	}
	public String getFechaVacunacionBloqueo() {
		return fechaVacunacionBloqueo;
	}
	public void setFechaVacunacionBloqueo(String fechaVacunacionBloqueo) {
		this.fechaVacunacionBloqueo = fechaVacunacionBloqueo;
	}
	public int getFiebre() {
		return fiebre;
	}
	public void setFiebre(int fiebre) {
		this.fiebre = fiebre;
	}
	public int getFiebreInicioParalisis() {
		return fiebreInicioParalisis;
	}
	public void setFiebreInicioParalisis(int fiebreInicioParalisis) {
		this.fiebreInicioParalisis = fiebreInicioParalisis;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public int getGlobulosRojos() {
		return globulosRojos;
	}
	public void setGlobulosRojos(int globulosRojos) {
		this.globulosRojos = globulosRojos;
	}
	public int getGlucosa() {
		return glucosa;
	}
	public void setGlucosa(int glucosa) {
		this.glucosa = glucosa;
	}
	public String getImpresionDiagnostica() {
		return impresionDiagnostica;
	}
	public void setImpresionDiagnostica(String impresionDiagnostica) {
		this.impresionDiagnostica = impresionDiagnostica;
	}
	public String getInstalacion() {
		return instalacion;
	}
	public void setInstalacion(String instalacion) {
		this.instalacion = instalacion;
	}
	public int getLeucocitos() {
		return leucocitos;
	}
	public void setLeucocitos(int leucocitos) {
		this.leucocitos = leucocitos;
	}
	public int getLiquidocefalo() {
		return liquidocefalo;
	}
	public void setLiquidocefalo(int liquidocefalo) {
		this.liquidocefalo = liquidocefalo;
	}
	public String getLoginUsuario() {
		return loginUsuario;
	}
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	public int getMuestraMateriaFecal() {
		return muestraMateriaFecal;
	}
	public void setMuestraMateriaFecal(int muestraMateriaFecal) {
		this.muestraMateriaFecal = muestraMateriaFecal;
	}
	public String getMunicipiosVacunados() {
		return municipiosVacunados;
	}
	public void setMunicipiosVacunados(String municipiosVacunados) {
		this.municipiosVacunados = municipiosVacunados;
	}
	public int getMusculosRespiratorios() {
		return musculosRespiratorios;
	}
	public void setMusculosRespiratorios(int musculosRespiratorios) {
		this.musculosRespiratorios = musculosRespiratorios;
	}
	public String getNombreMadre() {
		return nombreMadre;
	}
	public void setNombreMadre(String nombreMadre) {
		this.nombreMadre = nombreMadre;
	}
	public String getNombrePadre() {
		return nombrePadre;
	}
	public void setNombrePadre(String nombrePadre) {
		this.nombrePadre = nombrePadre;
	}
	public boolean isNotificar() {
		return notificar;
	}
	public void setNotificar(boolean notificar) {
		this.notificar = notificar;
	}
	public String getNumeroDosis() {
		return numeroDosis;
	}
	public void setNumeroDosis(String numeroDosis) {
		this.numeroDosis = numeroDosis;
	}
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public int getParesCraneanos() {
		return paresCraneanos;
	}
	public void setParesCraneanos(int paresCraneanos) {
		this.paresCraneanos = paresCraneanos;
	}
	public String getPrimerApellido() {
		return primerApellido;
	}
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	public String getPrimerNombre() {
		return primerNombre;
	}
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	public int getProgresion() {
		return progresion;
	}
	public void setProgresion(int progresion) {
		this.progresion = progresion;
	}
	public int getProteinas() {
		return proteinas;
	}
	public void setProteinas(int proteinas) {
		this.proteinas = proteinas;
	}
	public int getRespiratorios() {
		return respiratorios;
	}
	public void setRespiratorios(int respiratorios) {
		this.respiratorios = respiratorios;
	}
	public int getResultadoConduccion() {
		return resultadoConduccion;
	}
	public void setResultadoConduccion(int resultadoConduccion) {
		this.resultadoConduccion = resultadoConduccion;
	}
	public String getSegundoApellido() {
		return segundoApellido;
	}
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	public String getSegundoNombre() {
		return segundoNombre;
	}
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	public int getSignosMeningeos1() {
		return signosMeningeos1;
	}
	public void setSignosMeningeos1(int signosMeningeos1) {
		this.signosMeningeos1 = signosMeningeos1;
	}
	public int getSignosMeningeos2() {
		return signosMeningeos2;
	}
	public void setSignosMeningeos2(int signosMeningeos2) {
		this.signosMeningeos2 = signosMeningeos2;
	}
	public String getSire() {
		return sire;
	}
	public void setSire(String sire) {
		this.sire = sire;
	}
	public String getTelefonoPaciente() {
		return telefonoPaciente;
	}
	public void setTelefonoPaciente(String telefonoPaciente) {
		this.telefonoPaciente = telefonoPaciente;
	}
	public int getTieneCarnet() {
		return tieneCarnet;
	}
	public void setTieneCarnet(int tieneCarnet) {
		this.tieneCarnet = tieneCarnet;
	}
	public int getVelocidadConduccion() {
		return velocidadConduccion;
	}
	public void setVelocidadConduccion(int velocidadConduccion) {
		this.velocidadConduccion = velocidadConduccion;
	}
	public int getVirusAislado() {
		return virusAislado;
	}
	public void setVirusAislado(int virusAislado) {
		this.virusAislado = virusAislado;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}


	public int getLinfocitos() {
		return linfocitos;
	}


	public void setLinfocitos(int linfocitos) {
		this.linfocitos = linfocitos;
	}


	public int getSumaAdicional() {
		return sumaAdicional;
	}


	public void setSumaAdicional(int sumaAdicional) {
		this.sumaAdicional = sumaAdicional;
	}


	public int getSumaGrupo1() {
		return sumaGrupo1;
	}


	public void setSumaGrupo1(int sumaGrupo1) {
		this.sumaGrupo1 = sumaGrupo1;
	}


	public int getSumaGrupo2() {
		return sumaGrupo2;
	}


	public void setSumaGrupo2(int sumaGrupo2) {
		this.sumaGrupo2 = sumaGrupo2;
	}


	public int getSumaGrupo3() {
		return sumaGrupo3;
	}


	public void setSumaGrupo3(int sumaGrupo3) {
		this.sumaGrupo3 = sumaGrupo3;
	}


	public int getSumaPoblacion() {
		return sumaPoblacion;
	}


	public void setSumaPoblacion(int sumaPoblacion) {
		this.sumaPoblacion = sumaPoblacion;
	}


	public int getSumaVop1() {
		return sumaVop1;
	}


	public void setSumaVop1(int sumaVop1) {
		this.sumaVop1 = sumaVop1;
	}


	public int getSumaVop2() {
		return sumaVop2;
	}


	public void setSumaVop2(int sumaVop2) {
		this.sumaVop2 = sumaVop2;
	}


	public int getSumaVop3() {
		return sumaVop3;
	}


	public void setSumaVop3(int sumaVop3) {
		this.sumaVop3 = sumaVop3;
	}


	public int getSumaRecien() {
		return sumaRecien;
	}


	public void setSumaRecien(int sumaRecien) {
		this.sumaRecien = sumaRecien;
	}


	public int getSumaTotal() {
		return sumaTotal;
	}


	public void setSumaTotal(int sumaTotal) {
		this.sumaTotal = sumaTotal;
	}


	public String getAseguradora() {
		return aseguradora;
	}


	public void setAseguradora(String aseguradora) {
		this.aseguradora = aseguradora;
	}


	public String getBarrioResidencia() {
		return barrioResidencia;
	}


	public void setBarrioResidencia(String barrioResidencia) {
		this.barrioResidencia = barrioResidencia;
	}


	public String getCodigoDepProcedencia() {
		return codigoDepProcedencia;
	}


	public void setCodigoDepProcedencia(String codigoDepProcedencia) {
		this.codigoDepProcedencia = codigoDepProcedencia;
	}


	public String getCodigoMunProcedencia() {
		return codigoMunProcedencia;
	}


	public void setCodigoMunProcedencia(String codigoMunProcedencia) {
		this.codigoMunProcedencia = codigoMunProcedencia;
	}


	public String getDepartamentoNotifica() {
		return departamentoNotifica;
	}


	public void setDepartamentoNotifica(String departamentoNotifica) {
		this.departamentoNotifica = departamentoNotifica;
	}


	public int getDesplazado() {
		return desplazado;
	}


	public void setDesplazado(int desplazado) {
		this.desplazado = desplazado;
	}


	public boolean isEstaVivo() {
		return estaVivo;
	}


	public void setEstaVivo(boolean estaVivo) {
		this.estaVivo = estaVivo;
	}


	public String getEtnia() {
		return etnia;
	}


	public void setEtnia(String etnia) {
		this.etnia = etnia;
	}


	public String getFechaConsultaGeneral() {
		return fechaConsultaGeneral;
	}


	public void setFechaConsultaGeneral(String fechaConsultaGeneral) {
		this.fechaConsultaGeneral = fechaConsultaGeneral;
	}


	public String getFechaDefuncion() {
		return fechaDefuncion;
	}


	public void setFechaDefuncion(String fechaDefuncion) {
		this.fechaDefuncion = fechaDefuncion;
	}


	public String getFechaHospitalizacion() {
		return fechaHospitalizacion;
	}


	public void setFechaHospitalizacion(String fechaHospitalizacion) {
		this.fechaHospitalizacion = fechaHospitalizacion;
	}


	public String getFechaInicioSint() {
		return fechaInicioSint;
	}


	public void setFechaInicioSint(String fechaInicioSint) {
		this.fechaInicioSint = fechaInicioSint;
	}


	public String getFechaNotificacion() {
		return fechaNotificacion;
	}


	public void setFechaNotificacion(String fechaNotificacion) {
		this.fechaNotificacion = fechaNotificacion;
	}


	public boolean isHospitalizado() {
		return hospitalizado;
	}


	public void setHospitalizado(boolean hospitalizado) {
		this.hospitalizado = hospitalizado;
	}


	public int getInstitucionAtendio() {
		return institucionAtendio;
	}


	public void setInstitucionAtendio(int institucionAtendio) {
		this.institucionAtendio = institucionAtendio;
	}


	public Logger getLogger() {
		return logger;
	}


	public void setLogger(Logger logger) {
		this.logger = logger;
	}


	public String getLugarNotifica() {
		return lugarNotifica;
	}


	public void setLugarNotifica(String lugarNotifica) {
		this.lugarNotifica = lugarNotifica;
	}


	public String getLugarProcedencia() {
		return lugarProcedencia;
	}


	public void setLugarProcedencia(String lugarProcedencia) {
		this.lugarProcedencia = lugarProcedencia;
	}


	public String getMunicipioNotifica() {
		return municipioNotifica;
	}


	public void setMunicipioNotifica(String municipioNotifica) {
		this.municipioNotifica = municipioNotifica;
	}


	public String getNombreProfesional() {
		return nombreProfesional;
	}


	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}


	public String getOcupacion() {
		return ocupacion;
	}


	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}


	public String getRegimenSalud() {
		return regimenSalud;
	}


	public void setRegimenSalud(String regimenSalud) {
		this.regimenSalud = regimenSalud;
	}


	public int getTipoCaso() {
		return tipoCaso;
	}


	public void setTipoCaso(int tipoCaso) {
		this.tipoCaso = tipoCaso;
	}


	public String getTipoId() {
		return tipoId;
	}


	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}


	public String getZonaDomicilio() {
		return zonaDomicilio;
	}


	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}


	public int getAnyoSemanaEpi() {
		return anyoSemanaEpi;
	}


	public void setAnyoSemanaEpi(int anyoSemanaEpi) {
		this.anyoSemanaEpi = anyoSemanaEpi;
	}


	public int getSemanaEpidemiologica() {
		return semanaEpidemiologica;
	}


	public void setSemanaEpidemiologica(int semanaEpidemiologica) {
		this.semanaEpidemiologica = semanaEpidemiologica;
	}


	public int getCodigoConvenio() {
		return codigoConvenio;
	}


	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}


	public String getValorDivDx() {
		return valorDivDx;
	}


	public void setValorDivDx(String valorDivDx) {
		this.valorDivDx = valorDivDx;
	}


	public int getCodigoEnfNotificable() {
		return codigoEnfNotificable;
	}


	public void setCodigoEnfNotificable(int codigoEnfNotificable) {
		this.codigoEnfNotificable = codigoEnfNotificable;
	}


	public String getEsPrimeraVez() {
		return esPrimeraVez;
	}


	public void setEsPrimeraVez(String esPrimeraVez) {
		this.esPrimeraVez = esPrimeraVez;
	}


	public boolean isFichamodulo() {
		return fichamodulo;
	}


	public void setFichamodulo(boolean fichamodulo) {
		this.fichamodulo = fichamodulo;
	}


	public String getTelefonoContacto() {
		return telefonoContacto;
	}


	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}


	public boolean isVieneDeFichasAnteriores() {
		return vieneDeFichasAnteriores;
	}


	public void setVieneDeFichasAnteriores(boolean vieneDeFichasAnteriores) {
		this.vieneDeFichasAnteriores = vieneDeFichasAnteriores;
	}


	public String getDiagnosticosSeleccionados() {
		return diagnosticosSeleccionados;
	}


	public void setDiagnosticosSeleccionados(String diagnosticosSeleccionados) {
		this.diagnosticosSeleccionados = diagnosticosSeleccionados;
	}


	public boolean isEpidemiologia() {
		return epidemiologia;
	}


	public void setEpidemiologia(boolean epidemiologia) {
		this.epidemiologia = epidemiologia;
	}


	public String getIdCheckBox() {
		return idCheckBox;
	}


	public void setIdCheckBox(String idCheckBox) {
		this.idCheckBox = idCheckBox;
	}


	public String getIdDiagnostico() {
		return idDiagnostico;
	}


	public void setIdDiagnostico(String idDiagnostico) {
		this.idDiagnostico = idDiagnostico;
	}


	public String getIdDiagSeleccionados() {
		return idDiagSeleccionados;
	}


	public void setIdDiagSeleccionados(String idDiagSeleccionados) {
		this.idDiagSeleccionados = idDiagSeleccionados;
	}


	public String getIdDiv() {
		return idDiv;
	}


	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}


	public String getIdHiddenCheckBox() {
		return idHiddenCheckBox;
	}


	public void setIdHiddenCheckBox(String idHiddenCheckBox) {
		this.idHiddenCheckBox = idHiddenCheckBox;
	}


	public String getIdNumero() {
		return idNumero;
	}


	public void setIdNumero(String idNumero) {
		this.idNumero = idNumero;
	}


	public String getIdValorFicha() {
		return idValorFicha;
	}


	public void setIdValorFicha(String idValorFicha) {
		this.idValorFicha = idValorFicha;
	}


	public int getNumero() {
		return numero;
	}


	public void setNumero(int numero) {
		this.numero = numero;
	}


	public String getPropiedadDiagnostico() {
		return propiedadDiagnostico;
	}


	public void setPropiedadDiagnostico(String propiedadDiagnostico) {
		this.propiedadDiagnostico = propiedadDiagnostico;
	}


	public String getPropiedadHiddenCheckBox() {
		return propiedadHiddenCheckBox;
	}


	public void setPropiedadHiddenCheckBox(String propiedadHiddenCheckBox) {
		this.propiedadHiddenCheckBox = propiedadHiddenCheckBox;
	}


	public int getTipoDiagnostico() {
		return tipoDiagnostico;
	}


	public void setTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico = tipoDiagnostico;
	}


	public String getValorDiagnostico() {
		return valorDiagnostico;
	}


	public void setValorDiagnostico(String valorDiagnostico) {
		this.valorDiagnostico = valorDiagnostico;
	}


	public boolean isHayLaboratorios() {
		return hayLaboratorios;
	}


	public void setHayLaboratorios(boolean hayLaboratorios) {
		this.hayLaboratorios = hayLaboratorios;
	}


	public boolean isHayServicios() {
		return hayServicios;
	}


	public void setHayServicios(boolean hayServicios) {
		this.hayServicios = hayServicios;
	}


	public boolean isActiva() {
		return activa;
	}


	public void setActiva(boolean activa) {
		this.activa = activa;
	}


	public String getUrlFichasAnteriores() {
		return urlFichasAnteriores;
	}


	public void setUrlFichasAnteriores(String urlFichasAnteriores) {
		this.urlFichasAnteriores = urlFichasAnteriores;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public int getAreaProcedencia() {
		return areaProcedencia;
	}


	public void setAreaProcedencia(int areaProcedencia) {
		this.areaProcedencia = areaProcedencia;
	}


	public String getGrupoPoblacional() {
		return grupoPoblacional;
	}


	public void setGrupoPoblacional(String grupoPoblacional) {
		this.grupoPoblacional = grupoPoblacional;
	}


	public String getPais() {
		return pais;
	}


	public void setPais(String pais) {
		this.pais = pais;
	}


	public String getPaisExpedicion() {
		return paisExpedicion;
	}


	public void setPaisExpedicion(String paisExpedicion) {
		this.paisExpedicion = paisExpedicion;
	}


	public String getPaisNacimiento() {
		return paisNacimiento;
	}


	public void setPaisNacimiento(String paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}


	public String getPaisResidencia() {
		return paisResidencia;
	}


	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
}
